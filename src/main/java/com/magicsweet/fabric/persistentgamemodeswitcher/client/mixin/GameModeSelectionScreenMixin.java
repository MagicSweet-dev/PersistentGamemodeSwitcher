package com.magicsweet.fabric.persistentgamemodeswitcher.client.mixin;

import com.magicsweet.fabric.persistentgamemodeswitcher.client.KeyboardHotkey;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(GameModeSelectionScreen.class)
public abstract class GameModeSelectionScreenMixin {
	
	@Shadow protected abstract void apply();
	
	@Redirect(method = "apply(Lnet/minecraft/client/MinecraftClient;Ljava/util/Optional;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasPermissionLevel(I)Z"))
	private static boolean returnFakePermissionCheck(ClientPlayerEntity clientPlayerEntity, int permissionLevel) {
		return true;
	}
	
	/**
	 * @author MagicSweet
	 * @reason Necessary custom behaviour
	 */
	@Overwrite
	private boolean checkForClose() {
		if (!checkButtons()) {
			this.apply();
			MinecraftClient.getInstance().openScreen(null);
			return true;
		} else {
			return false;
		}
	}
	
	private boolean checkButtons() {
		if (KeyboardHotkey.gmSwitcherKeybind.isDefault()) {
			return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 292);
		} else {
			var key = KeyBindingHelper.getBoundKeyOf(KeyboardHotkey.gmSwitcherKeybind);
			return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), key.getCode());
		}
	}
}
