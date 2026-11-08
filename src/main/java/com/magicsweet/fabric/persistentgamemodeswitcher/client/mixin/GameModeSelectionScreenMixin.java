package com.magicsweet.fabric.persistentgamemodeswitcher.client.mixin;

import com.magicsweet.fabric.persistentgamemodeswitcher.client.KeyboardHotkeys;
import com.magicsweet.fabric.persistentgamemodeswitcher.client.PersistentGamemodeSwitcherClient;
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
//? if >= 1.21.11 {
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.client.input.KeyEvent;
//?} else {
/*import com.mojang.blaze3d.platform.InputConstants;
*///?}
import net.minecraft.client.Minecraft;

@Mixin(GameModeSwitcherScreen.class)
public abstract class GameModeSelectionScreenMixin {

	@Shadow protected abstract void switchToHoveredGameMode();

	@Redirect(
		method = "switchToHoveredGameMode(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon;)V",
		at = @At(
			value = "INVOKE",
			//? if >= 1.21.11 {
			target = "Lnet/minecraft/server/permissions/PermissionCheck;check(Lnet/minecraft/server/permissions/PermissionSet;)Z"
			//?} else {
			/*target = "Lnet/minecraft/client/player/LocalPlayer;hasPermissions(I)Z"
			*///?}
		)
	)
	private static boolean returnFakePermissionCheck(
		//? if >= 1.21.11 {
		PermissionCheck instance, PermissionSet permissionSet
		//?} else {
		/*LocalPlayer instance, int i
		*///?}
	) {
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
	@Inject(
		at = @At("HEAD"),
		//? if >= 1.21.11 {
		 method = "keyReleased",
		//?} else {
		/*method = "checkToClose",
		*///?}
		cancellable = true
	)
	private void checkForClose(
		//? if >= 1.21.11 {
		final KeyEvent event,
		//?}
		CallbackInfoReturnable<Boolean> cir
	) {
		if (!KeyboardHotkeys.gmSwitcherKeybind.isDefault()) {
			//? if >= 1.21.11 {
			if (event.key() == 292) {
			//?} else {
			/*if (InputConstants.isKeyDown(
				Minecraft.getInstance().getWindow()
				//? if <= 1.21.6
				//.getWindow()
				, 292)) {
			*///?}
				cir.setReturnValue(true);
				return;
			}
			if (!checkButtons()) {
				this.switchToHoveredGameMode();
				//$ gui_set_screen null
				Minecraft.getInstance().gui.setScreen(null);
				cir.setReturnValue(true);
			} else cir.setReturnValue(false);
		}
	}

	@Unique
    private boolean checkButtons() {
		return KeyboardHotkeys.isDown(KeyboardHotkeys.gmSwitcherKeybind);
	}
}
