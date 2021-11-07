package com.chame.passwordtenshi.mixin;

import com.chame.passwordtenshi.PasswordTenshi;

import net.minecraft.server.MinecraftServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "stop", at = @At("HEAD"))
    public void stop(boolean bool, CallbackInfo ci) {
        if (!bool) return;
        PasswordTenshi.getMainExecutor().shutdown();
    }
}
