package com.magicsweet.fabric.persistentgamemodeswitcher.client;

import com.google.common.base.MoreObjects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.world.GameMode;


@Environment(EnvType.CLIENT)
public class PersistentGamemodeSwitcherClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		new KeyboardHotkey();
	}
	
	public static void openGamemodeSwitcher() {
		MinecraftClient.getInstance().setScreen(new GameModeSelectionScreen());
	}
	
	public static void cycleGamemodes() {
		if (MinecraftClient.getInstance().player == null) return;
		if (MinecraftClient.getInstance().interactionManager == null) return;
		
		if (!MinecraftClient.getInstance().player.isSpectator()) {
			MinecraftClient.getInstance().player.networkHandler.sendCommand("gamemode spectator");
		} else {
			ClientPlayerEntity entity = MinecraftClient.getInstance().player;
			GameMode gamemode = MinecraftClient.getInstance().interactionManager.getPreviousGameMode();
			entity.networkHandler.sendCommand("gamemode " + MoreObjects.firstNonNull(gamemode, GameMode.CREATIVE).getName());
		}
		
	}
}
