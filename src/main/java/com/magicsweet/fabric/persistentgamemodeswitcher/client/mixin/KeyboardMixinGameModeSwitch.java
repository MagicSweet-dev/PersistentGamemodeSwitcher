package com.magicsweet.fabric.persistentgamemodeswitcher.client.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public class KeyboardMixinGameModeSwitch {
	
	@Inject(at = @At("HEAD"), method = "processF3(I)Z", cancellable = true)
	public void behaviour(int key, CallbackInfoReturnable<Boolean> cir) {
		if (key == 293) {
			MinecraftClient.getInstance().openScreen(new GameModeSelectionScreen());
			cir.setReturnValue(true);
		}
	}
}
