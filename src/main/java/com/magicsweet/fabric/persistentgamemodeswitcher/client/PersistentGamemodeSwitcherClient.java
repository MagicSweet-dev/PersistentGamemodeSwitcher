package com.magicsweet.fabric.persistentgamemodeswitcher.client;

import com.google.common.base.MoreObjects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.Nullable;


@Environment(EnvType.CLIENT)
public class PersistentGamemodeSwitcherClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		KeyboardHotkeys.initialize();
	}

	public static void openGamemodeSwitcher() {
		setScreen(new GameModeSwitcherScreen());
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

	public static void setScreen(@Nullable Screen screen) {
		//? if <= 26.1
		//Minecraft.getInstance().setScreen(screen);
		//? if >= 26.2
		Minecraft.getInstance().gui.setScreen(screen);
	}
}
