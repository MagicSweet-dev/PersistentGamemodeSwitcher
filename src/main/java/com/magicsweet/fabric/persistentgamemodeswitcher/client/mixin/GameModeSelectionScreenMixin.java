package com.magicsweet.fabric.persistentgamemodeswitcher.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.magicsweet.fabric.persistentgamemodeswitcher.client.KeyboardHotkeys;
import com.magicsweet.fabric.persistentgamemodeswitcher.client.PersistentGamemodeSwitcherClient;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.PermissionSet;
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
			target = "Lnet/minecraft/server/permissions/PermissionCheck;check(Lnet/minecraft/server/permissions/PermissionSet;)Z"
		)
	)
	private static boolean returnFakePermissionCheck(PermissionCheck instance, PermissionSet permissionSet) {
		return true;
	}

	/**
	 * @author MagicSweet
	 * @reason Sending a command instead of ServerboundChangeGameModePacket.
	 */
	@WrapOperation(
		method = "switchToHoveredGameMode(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"
		)
	)
	private static void sendCommandInsteadOfPacket(ClientPacketListener instance, Packet<?> packet, Operation<Void> original) {
		if (packet instanceof ServerboundChangeGameModePacket(GameType mode)) {
			PersistentGamemodeSwitcherClient.sendCommand("gamemode " + mode.getName());
		} else {
			original.call(instance, packet);
		}
	}

	/**
	 * @author MagicSweet
	 * @reason Checking if custom button is pressed (or not), in such a case - close the menu.
	 */
	@Inject(at = @At("HEAD"), method = "keyReleased", cancellable = true)
	private void checkForClose(final KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
		if (!KeyboardHotkeys.gmSwitcherKeybind.isDefault()) {
			if (event.key() == 292) {
				cir.setReturnValue(true);
				return;
			}
			if (!checkButtons()) {
				this.switchToHoveredGameMode();
				Minecraft.getInstance().gui.setScreen(null);
				cir.setReturnValue(true);
			} else cir.setReturnValue(false);
		}
	}

	@Unique
	private boolean checkButtons() {
		var key = KeyMappingHelper.getBoundKeyOf(KeyboardHotkeys.gmSwitcherKeybind);
		return InputConstants.isKeyDown(key.getValue());
	}
}
