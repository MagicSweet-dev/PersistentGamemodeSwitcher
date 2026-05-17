package com.magicsweet.fabric.persistentgamemodeswitcher.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.magicsweet.fabric.persistentgamemodeswitcher.PersistentGamemodeSwitcher;
import com.magicsweet.fabric.persistentgamemodeswitcher.client.KeyboardHotkey;
import com.magicsweet.fabric.persistentgamemodeswitcher.client.PersistentGamemodeSwitcherClient;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
	 * @reason Sending a command instead of ServerboundChangeGameModePacket.
	 */
	@Redirect(
		method = "switchToHoveredGameMode(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"
		)
	)
	private static void sendCommandInsteadOfPacket(ClientPacketListener instance, Packet<?> packet) {
		if (packet instanceof ServerboundChangeGameModePacket(GameType mode)) {
			PersistentGamemodeSwitcherClient.sendCommand("gamemode " + mode.getName());
		}
	}

	/**
	 * @author MagicSweet
	 * @reason Checking if custom button is pressed (or not), in such a case - close the menu.
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

	@Unique
    private boolean checkButtons() {
		var key = KeyBindingHelper.getBoundKeyOf(KeyboardHotkey.gmSwitcherKeybind);
		return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getValue());
	}
}
