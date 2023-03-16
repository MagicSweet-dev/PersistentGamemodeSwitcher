package com.magicsweet.fabric.persistentgamemodeswitcher.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyboardHotkey {
	public static final KeyBinding gmSwitcherKeybind;
	public static final KeyBinding gmCycleKeybind;
	
	
	static {
		gmSwitcherKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.persistentgamemodeswitcher.activate",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_F4,
			"key.category.persistentgamemodeswitcher.activate"
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (gmSwitcherKeybind.wasPressed()) {
				PersistentGamemodeSwitcherClient.openGamemodeSwitcher();
			}
		});
		
		gmCycleKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.persistentgamemodeswitcher.activate.cycle",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_UNKNOWN,
			"key.category.persistentgamemodeswitcher.activate"
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (gmCycleKeybind.wasPressed()) {
				PersistentGamemodeSwitcherClient.cycleGamemodes();
			}
		});
	}
	
}
