package com.magicsweet.fabric.persistentgamemodeswitcher.client.mixin;

import com.magicsweet.fabric.persistentgamemodeswitcher.client.KeyboardHotkeys;
import com.magicsweet.fabric.persistentgamemodeswitcher.client.PersistentGamemodeSwitcherClient;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyboardHandler.class)
public class KeyboardMixinGameModeSwitch {

	@Inject(at = @At("HEAD"), method = "handleDebugKeys(Lnet/minecraft/client/input/KeyEvent;)Z", cancellable = true)
	public void behaviour(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
		if (Minecraft.getInstance().options.keyDebugSwitchGameMode.matches(event)) {
			if (KeyboardHotkeys.gmSwitcherKeybind.isDefault()) {
				PersistentGamemodeSwitcherClient.openGamemodeSwitcher();
			}
			cir.setReturnValue(true);
		} else if (event.key() == 78) {
			if (KeyboardHotkeys.gmCycleKeybind.isDefault()) {
				PersistentGamemodeSwitcherClient.cycleGamemodes();
			}
			cir.setReturnValue(true);
		}
	}
}
