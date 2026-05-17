package com.magicsweet.fabric.persistentgamemodeswitcher.client.mixin;

import com.magicsweet.fabric.persistentgamemodeswitcher.client.KeyboardHotkey;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(GameModeSwitcherScreen.class)
public abstract class GameModeSelectionScreenMixin {

	@Shadow protected abstract void switchToHoveredGameMode();

	@Redirect(
		method = "switchToHoveredGameMode(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;hasPermissions(I)Z"
		)
	)
	private static boolean returnFakePermissionCheck(LocalPlayer clientPlayerEntity, int permissionLevel) {
		return true;
	}

	/**
	 * @author MagicSweet
	 * @reason Necessary custom behaviour
	 */
	@Inject(at = @At("HEAD"), method = "checkToClose", cancellable = true)
	private void checkForClose(CallbackInfoReturnable<Boolean> cir) {
		if (!KeyboardHotkey.gmSwitcherKeybind.isDefault()) {
			if (!checkButtons()) {
				this.switchToHoveredGameMode();
				Minecraft.getInstance().setScreen(null);
				cir.setReturnValue(true);
			} else cir.setReturnValue(false);
		}
	}

	private boolean checkButtons() {
		var key = KeyBindingHelper.getBoundKeyOf(KeyboardHotkey.gmSwitcherKeybind);
		return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getValue());
	}
}
