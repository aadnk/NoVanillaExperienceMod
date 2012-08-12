package com.comphenix.noxp;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NoExpMod extends JavaPlugin {

	private Logger logger;
	
	// Listeners
	private PreventExperienceListener blockListener;
	
	// HACK!
	private MinecraftInjector injector;
	
	@Override
	public void onLoad() {
		
		// Load logger
		logger = getLogger();
	}
	
	@Override
	public void onEnable() {

		PluginManager manager = getServer().getPluginManager();
		
		// Initialize listeners
		blockListener = new PreventExperienceListener(logger);
		
		// Register listeners
		manager.registerEvents(blockListener, this);
		
		// Do our evil black magic
		injector = new MinecraftInjector(logger, manager);
		injector.injectMap();
	}
	
	@Override
	public void onDisable() {

		// Try to clean up after ourselves ...
		if (injector != null) {
			injector.uninjectMap();
		}
	}
}
