package com.magicsweet.fabric.persistentgamemodeswitcher.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class KeyboardHotkeys {
	public static final KeyMapping gmSwitcherKeybind;
	public static final KeyMapping gmCycleKeybind;
	public static final KeyMapping.Category category;

	static {
		category = KeyMapping.Category.register(Identifier.parse("persistentgamemodeswitcher:activate"));
		gmSwitcherKeybind = KeyMappingHelper.registerKeyMapping(new KeyMapping(
			"key.persistentgamemodeswitcher.activate",
			InputConstants.Type.KEYBOARD,
			InputConstants.KEY_F4,
			category
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (gmSwitcherKeybind.consumeClick()) {
				PersistentGamemodeSwitcherClient.openGamemodeSwitcher();
			}
		});

		gmCycleKeybind = KeyMappingHelper.registerKeyMapping(new KeyMapping(
			"key.persistentgamemodeswitcher.activate.cycle",
			InputConstants.Type.KEYBOARD,
			InputConstants.UNKNOWN.getValue(),
			category
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (gmCycleKeybind.consumeClick()) {
				PersistentGamemodeSwitcherClient.cycleGamemodes();
			}
		});
	}

	public static void initialize() {
		// dummy method so properties initialize
	}
}
