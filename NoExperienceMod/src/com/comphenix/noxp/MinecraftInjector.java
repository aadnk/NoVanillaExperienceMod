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

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import net.minecraft.server.RecipesFurnace;
import com.comphenix.noxp.events.InjectedHashMap;

// The BIG BAD hack of the day.
public class MinecraftInjector {

	private InjectedHashMap injectedMap;
	private Map<Integer, Float> originalMap;
	private Logger logger;
	
	// Where our injected hash map is stored
	private Field mapField;
	
	// Handles events
	private PluginManager pluginManager;
	
	public MinecraftInjector(Logger logger, PluginManager pluginManager) {
		this.logger = logger;
		this.pluginManager = pluginManager;
	}

	public InjectedHashMap getInjectedMap() {
		return injectedMap;
	}

	@SuppressWarnings("unchecked")
	public void injectMap() {
		try {
			
			RecipesFurnace manager = RecipesFurnace.getInstance();
			mapField = retrieveMap(manager);
			
			if (mapField != null) { 
				originalMap = (Map<Integer, Float>) FieldUtils.readField(mapField, manager, true);
				
				// Don't do this twice
				if (!(originalMap instanceof InjectedHashMap)) {
					injectedMap = new InjectedHashMap(originalMap, pluginManager);
					
					// Let the magic happen
					FieldUtils.writeField(mapField, manager, injectedMap, true);
				} else {
					injectedMap = (InjectedHashMap) originalMap;
				}
			}
			
		} catch (Exception e) {
			logger.severe("Unable to inject into RecipesFurnace.");
		}
	}
	
	public void uninjectMap() {
		
		if (originalMap != null && mapField != null) {
			
			RecipesFurnace manager = RecipesFurnace.getInstance();
			
			// Try to revert our meddling
			try {
				FieldUtils.writeField(mapField, manager, originalMap, true);
			} catch (IllegalAccessException e) {
				logger.severe("Unable to revert injected furnance event. Illegal access.");
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	private Field retrieveMap(RecipesFurnace manager) throws IllegalAccessException {
		
		Field[] fields = manager.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			// Is this a map field?
			if (Map.class.isAssignableFrom(field.getType())) {
				
				// Retrieve its value
				Map map = (Map) FieldUtils.readField(field, manager, true);
				
				for (Object objectEntry : map.entrySet()) {
					Entry entry = (Entry) objectEntry;
					
					// We know our target hash map uses integers and floats
					if (entry.getKey() instanceof Integer &&
						entry.getValue() instanceof Float) {
						// This is the map we're looking for! Probably ...
						return field;
					} else {
						break;
					}
				}
			}
			
		}
		
		// No map found
		return null;
	}
}
