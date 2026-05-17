package com.magicsweet.fabric.persistentgamemodeswitcher.client;

import com.google.common.base.MoreObjects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.world.level.GameType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Environment(EnvType.CLIENT)
public class PersistentGamemodeSwitcherClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("persistentgamemodeswitchcer");

	@Override
	public void onInitializeClient() {
		new KeyboardHotkey();
	}

	public static void openGamemodeSwitcher() {
		Minecraft.getInstance().setScreen(new GameModeSwitcherScreen());
	}

	public static void cycleGamemodes() {
		if (Minecraft.getInstance().player == null) return;
		if (Minecraft.getInstance().gameMode == null) return;

		if (!Minecraft.getInstance().player.isSpectator()) {
			sendCommand("gamemode spectator");
		} else {
			GameType gamemode = Minecraft.getInstance().gameMode.getPreviousPlayerMode();
			sendCommand("gamemode " + MoreObjects.firstNonNull(gamemode, GameType.CREATIVE).getName());
		}

	}

	public static void sendCommand(String command) {
		Minecraft.getInstance().player.connection.sendCommand(command);
	}
}
