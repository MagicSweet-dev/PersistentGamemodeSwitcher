package com.magicsweet.fabric.persistentgamemodeswitcher.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.resources.Identifier;

public class KeyboardHotkeys {
	public static final KeyMapping gmSwitcherKeybind;
	public static final KeyMapping gmCycleKeybind;

	static {
		var registered = register(
			"persistentgamemodeswitcher:activate",
			Map.of(
				Map.entry("key.persistentgamemodeswitcher.activate", InputConstants.KEY_F4), unused -> {
					PersistentGamemodeSwitcherClient.openGamemodeSwitcher();
				},
				Map.entry("key.persistentgamemodeswitcher.activate.cycle", InputConstants.UNKNOWN.getValue()), unused -> {
					PersistentGamemodeSwitcherClient.cycleGamemodes();
				}
			)
		);

		gmSwitcherKeybind = registered.get(0);
		gmCycleKeybind = registered.get(1);
	}

	private static List<KeyMapping> register(
		@SuppressWarnings("SameParameterValue") String categoryIdentifier,
		Map<Map.Entry<String, Integer>, Consumer<Minecraft>> keys
	) {
		//? if <= 1.21.6 {
		/*@SuppressWarnings("UnnecessaryLocalVariable")
		var category = categoryIdentifier;
		*///?} else {
		var category = KeyMapping.Category.register(Identifier.parse(categoryIdentifier));
		//?}
		var list = new ArrayList<KeyMapping>();
		for (var key : keys.entrySet()) {
			var keyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				key.getKey().getKey(),
				InputConstants.Type.KEYBOARD,
				key.getKey().getValue(),
				category
			));
			ClientTickEvents.END_CLIENT_TICK.register(client -> {
				if (keyMapping.consumeClick()) {
					key.getValue().accept(client);
				}
			});
			list.add(keyMapping);
		}
		return list;
	}

	public static boolean isDown(KeyMapping keyMapping) {
		var key = KeyMappingHelper.getBoundKeyOf(keyMapping);
		return InputConstants.isKeyDown(
			//$ gui_get_window
			// nothing
			key.getValue()
		);
	}

	public static void initialize() {

	}
}
