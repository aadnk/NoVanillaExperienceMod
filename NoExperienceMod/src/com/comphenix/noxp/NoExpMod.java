/*
 *  NoExperienceMod - Plugin that disables the new 1.3 block and smelting experience.
 *  Copyright (C) 2012 Kristian S. Stangeland
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the 
 *  GNU General Public License as published by the Free Software Foundation; either version 2 of 
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program; 
 *  if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 *  02111-1307 USA
 */

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
