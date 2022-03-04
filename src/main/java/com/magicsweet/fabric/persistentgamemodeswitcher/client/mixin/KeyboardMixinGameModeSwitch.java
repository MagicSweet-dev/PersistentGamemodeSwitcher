package com.magicsweet.fabric.persistentgamemodeswitcher.client.mixin;

import com.magicsweet.fabric.persistentgamemodeswitcher.client.KeyboardHotkey;
import com.magicsweet.fabric.persistentgamemodeswitcher.client.PersistentGamemodeSwitcherClient;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public class KeyboardMixinGameModeSwitch {
	
	@Inject(at = @At("HEAD"), method = "processF3(I)Z")
	public void behaviour(int key, CallbackInfoReturnable<Boolean> cir) {
		if (key == 293) {
			if (KeyboardHotkey.gmSwitcherKeybind.isDefault()) {
				PersistentGamemodeSwitcherClient.openGamemodeSwitcher();
			}
			cir.setReturnValue(true);
		} else if (key == 78) {
			if (KeyboardHotkey.gmCycleKeybind.isDefault()) {
				PersistentGamemodeSwitcherClient.cycleGamemodes();
			}
			cir.setReturnValue(true);
		}
	}
}
