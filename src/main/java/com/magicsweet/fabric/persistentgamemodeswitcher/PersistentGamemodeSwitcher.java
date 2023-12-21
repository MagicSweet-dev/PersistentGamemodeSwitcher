package com.magicsweet.fabric.persistentgamemodeswitcher;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistentGamemodeSwitcher implements DedicatedServerModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("persistentgamemodeswitchcer-server");

	@Override
	public void onInitializeServer() {
		LOGGER.warn("The PersistentGamemodeSwitcher mod is client-side. You don't have to install it on a server.");
		LOGGER.warn("The mod will not have any effect. You can delete it, if you want.");
	}
}
