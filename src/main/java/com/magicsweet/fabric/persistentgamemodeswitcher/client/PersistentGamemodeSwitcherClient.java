package com.magicsweet.fabric.persistentgamemodeswitcher.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PersistentGamemodeSwitcherClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		new KeyboardHotkey();
	}
	
	public static void openGamemodeSwitcher() {
		MinecraftClient.getInstance().openScreen(new GameModeSelectionScreen());
	}
}
