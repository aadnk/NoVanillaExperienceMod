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
