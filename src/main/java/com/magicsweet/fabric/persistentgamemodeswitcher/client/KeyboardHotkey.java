package com.magicsweet.fabric.persistentgamemodeswitcher.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class KeyboardHotkey {
	public static KeyBinding gmSwitcherKeybind;
	
	public KeyboardHotkey() {
		gmSwitcherKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.persistentgamemodeswitcher.activate", // The translation key of the keybinding's name
			InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
			GLFW.GLFW_KEY_UNKNOWN, // The keycode of the key
			"key.category.persistentgamemodeswitcher.activate" // The translation key of the keybinding's category.
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (gmSwitcherKeybind.wasPressed()) {
				PersistentGamemodeSwitcherClient.openGamemodeSwitcher();
			}
		});
	}
	
}
