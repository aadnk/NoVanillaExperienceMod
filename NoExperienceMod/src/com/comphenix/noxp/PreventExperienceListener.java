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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.comphenix.noxp.events.FurnaceExpEvent;

public class PreventExperienceListener implements Listener {
	
	private Logger logger;
	private boolean enabled;
	
	public PreventExperienceListener(Logger logger) {
		this.logger = logger;
		this.enabled = true;
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreakEvent(BlockBreakEvent event) {
		
		// Guard
		if (!enabled) {
			return;
		}
		
		// Remove the default dropped experience
		try {
			event.setExpToDrop(0);
		} catch (NoSuchMethodError e) {
			logger.severe("Cannot prevent experience drop. Incompatible class - are you using CraftBukkit #2326 or later?");
			setEnabled(false);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onFurnanceExpEvent(FurnaceExpEvent event) {
		
		// Disable the default dropped experience
		event.setExpToDropPerItem(0.0f);
	}

	/**
	 * Retrieves whether or not this listener is enabled.
	 * <p>
	 * It may be automatically disabled if it detects CraftBukkit version incompatibility.
	 * @return TRUE if it is enabled, FALSE otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets whether or not this listener is enabled.
	 * <p>
	 * It may be automatically disabled if it detects CraftBukkit version incompatibility.
	 * 
	 * @param enabled - TRUE if it should be enabled, FALSE otherwise.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Logger getLogger() {
		return logger;
	}
}
