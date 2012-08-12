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

package com.comphenix.noxp.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import org.bukkit.inventory.FurnaceRecipe;

public class FurnaceExpEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	// The experience that will be dropped from the furnace
	private float expToDropPerItem;
	private FurnaceRecipe recipe;
	
	public FurnaceExpEvent(float expToDropPerItem, FurnaceRecipe recipe) {
		this.expToDropPerItem = expToDropPerItem;
		this.recipe = recipe;
	}

	public float getExpToDropPerItem() {
		return expToDropPerItem;
	}

	public void setExpToDropPerItem(float expToDropPerItem) {
		this.expToDropPerItem = expToDropPerItem;
	}

	public FurnaceRecipe getRecipe() {
		return recipe;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
