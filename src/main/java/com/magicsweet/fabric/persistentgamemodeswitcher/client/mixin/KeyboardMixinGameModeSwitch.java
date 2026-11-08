package com.magicsweet.fabric.persistentgamemodeswitcher.client.mixin;

import com.magicsweet.fabric.persistentgamemodeswitcher.client.KeyboardHotkeys;
import com.magicsweet.fabric.persistentgamemodeswitcher.client.PersistentGamemodeSwitcherClient;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if >= 1.21.11 {
import net.minecraft.client.Minecraft;
//?}
//? if >= 1.21.9 {
import net.minecraft.client.input.KeyEvent;
//?}
@Mixin(KeyboardHandler.class)
public class KeyboardMixinGameModeSwitch {

	@Inject(
		at = @At("HEAD"),
		//? if >= 1.21.11 {
		method = "handleDebugKeys(Lnet/minecraft/client/input/KeyEvent;)Z",
		//?} else {
		/*method = "handleDebugKeys",
		*///?}
		cancellable = true
	)
	public void behaviour(
		//? if >= 1.21.9 {
		KeyEvent event,
		//?} else {
		/*int keyCode,
		*///?}
		CallbackInfoReturnable<Boolean> cir
	) {
		//? if >= 1.21.9 {
		var key = event.key();
		//?} else {
		/*@SuppressWarnings("UnnecessaryLocalVariable")
		var key = keyCode;
		*///?}

		if (
			//? if >= 1.21.11 {
			Minecraft.getInstance().options.keyDebugSwitchGameMode.matches(event)
			//?} else
			//key == 293
		) {
			if (KeyboardHotkeys.gmSwitcherKeybind.isDefault()) {
				PersistentGamemodeSwitcherClient.openGamemodeSwitcher();
			}
			cir.setReturnValue(true);
		} else if (key == 78) {
			if (KeyboardHotkeys.gmCycleKeybind.isDefault()) {
				PersistentGamemodeSwitcherClient.cycleGamemodes();
			}
			cir.setReturnValue(true);
		}
	}
}
