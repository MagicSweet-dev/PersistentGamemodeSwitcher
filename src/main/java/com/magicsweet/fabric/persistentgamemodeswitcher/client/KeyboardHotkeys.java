package com.magicsweet.fabric.persistentgamemodeswitcher.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

//? if > 1.21.6 {
import com.magicsweet.fabric.persistentgamemodeswitcher.util.IdentifierUtil;
//?}

//? if <= 1.21.11 {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
*///?} else
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

public class KeyboardHotkeys {
	public static final KeyMapping gmSwitcherKeybind;
	public static final KeyMapping gmCycleKeybind;

	static {
		var registered = register(
			"persistentgamemodeswitcher:activate",
			Map.of(
				Map.entry("key.persistentgamemodeswitcher.activate.cycle", InputConstants.KEY_F4), _ -> {
					PersistentGamemodeSwitcherClient.openGamemodeSwitcher();
				},
				Map.entry("key.persistentgamemodeswitcher.activate.cycle", InputConstants.UNKNOWN.getValue()), _ -> {
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
		var category = KeyMapping.Category.register(IdentifierUtil.parseIdentifier(categoryIdentifier));
		//?}
		var list = new ArrayList<KeyMapping>();
		for (var key : keys.entrySet()) {
			var keyMapping =
				//? if <= 1.21.11 {
				/*KeyBindingHelper.registerKeyBinding
				*///? } else
				 KeyMappingHelper.registerKeyMapping
					(new KeyMapping(
				key.getKey().getKey(),
				//? if >= 26.3 {
				InputConstants.Type.KEYBOARD,
				//?}
				//? if <= 26.2 {
				 /*InputConstants.Type.KEYSYM,
				*///?}
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
		var key =
		//? if <= 1.21.11 {
		/*KeyBindingHelper.
		*///? } else
		 KeyMappingHelper.
		getBoundKeyOf(keyMapping);
		return InputConstants.isKeyDown(
			//? if <= 26.2 {
			/*Minecraft.getInstance().getWindow()
			//? if <= 1.21.6
			//.getWindow()
			*///?}
			//? if <= 26.2 >=1.21.6 {
			/*,
			*///?}
			key.getValue()
		);
	}

	public static void initialize() {

	}
}
