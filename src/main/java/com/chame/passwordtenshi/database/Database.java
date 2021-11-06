package com.chame.passwordtenshi.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.jdbcx.JdbcConnectionPool;

public class Database{

    private final JdbcConnectionPool credentials;
    private Connection connection;
    private ResultSet resultset;
    private Statement statement;

    public Database (String hostip, String user, String pass) {
        this.credentials = JdbcConnectionPool.create("jdbc:h2:file:"+hostip+";IFEXISTS=FALSE", user, pass);
    }

    public boolean check() {
        final StringBuilder tablereq
                = new StringBuilder("CREATE TABLE IF NOT EXISTS pptenshi (");
        tablereq.append("`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
        tablereq.append("`uuid` VARCHAR(128) NOT NULL,");
        tablereq.append("`password` VARCHAR(512) NOT NULL,");
        tablereq.append("`autologin` BIT NOT NULL DEFAULT 0,");
        // 50 length to support IPV6 with network adapter codes too,
        // even though the theorical max is 45.
        tablereq.append("`ip` VARCHAR(50) NOT NULL)");

        try {
            connection = credentials.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(tablereq.toString());
            closeSQL();
            return true;
        } catch(SQLException ex){
            closeSQL();
            return false;
        }
    }

    public boolean addUser(String playeruuid, String password, String ip){
        boolean result = false;
        final String insertquery = "INSERT INTO pptenshi (uuid, password, ip) VALUES ('"+playeruuid+"', '"+password+"', '"+ip+"');";

        try {
            connection = credentials.getConnection();
            statement = connection.createStatement();
            int response = statement.executeUpdate(insertquery);
            if (response > 0){
                result = true;
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }

        closeSQL();
        return result;
    }

    public Boolean changeAutoLogin(String playeruuid, Boolean value){
        Boolean result = false;
        String updateValue = value ? "1" : "0";

        final String updateQuery = "UPDATE pptenshi SET autologin='"+updateValue+"' WHERE uuid='"+playeruuid+"'";

        try {
            connection = credentials.getConnection();
            statement = connection.createStatement();
            int response = statement.executeUpdate(updateQuery);
            if (response > 0){
                result = true;
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }

        closeSQL();
        return result;
    }

    public boolean removeUser(String playeruuid){
        boolean result = false;
        final String delquery = "DELETE FROM pptenshi WHERE uuid = "+"'"+playeruuid+"'";

        try {
            connection = credentials.getConnection();
            statement = connection.createStatement();
            int response = statement.executeUpdate(delquery);
            if (response > 0){
                result = true;
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        closeSQL();
        return result;
    }

    public String[] fetchUser(String playeruuid){
        final String getQuery = "SELECT password,autologin,ip FROM pptenshi WHERE uuid = '"+playeruuid+"'";

        try {
            connection = credentials.getConnection();
            statement = connection.createStatement();
            resultset = statement.executeQuery(getQuery);

            if (!this.resultset.next()) {
                closeSQL();
                return null;
            }

            final String retrievedpass = resultset.getString("password");
            final String autologin = resultset.getBoolean("autologin") ? "true" : "false";
            final String ip = resultset.getString("ip");

            closeSQL();
            return new String[]{retrievedpass, autologin, ip};

        } catch(SQLException ex){
            ex.printStackTrace();
        }

        closeSQL();
        return null;
    }

    private void closeSQL(){
        try {
            if(connection != null){
                try{
                    connection.close();
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
            if(statement != null){
                try{
                    statement.close();
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
            if(resultset != null){
                try{
                    resultset.close();
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}