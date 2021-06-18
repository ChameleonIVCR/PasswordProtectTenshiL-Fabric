package com.chame.passwordtenshi.mixin;


import com.chame.passwordtenshi.listeners.OnFirstTick;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.function.BooleanSupplier;

/**
 * 
 */

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    //Runs after the first server tick event is fired. It casts itself to the OnFirstTick listener.
    @Inject(at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V") }, method = {
                    "tick" })
    private void onStartTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        //ugly cast here
        OnFirstTick.setMinecraftServer((MinecraftServer) (Object) this);
    }
}