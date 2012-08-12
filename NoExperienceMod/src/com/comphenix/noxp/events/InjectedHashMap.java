package com.comphenix.noxp.events;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.craftbukkit.inventory.CraftFurnaceRecipe;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import net.minecraft.server.RecipesFurnace;

public class InjectedHashMap implements Map<Integer, Float> {

	// The actual map we're modifying
	private Map<Integer, Float> underlyingMap;
	
	// Modified values - DO NOT touch the underlying map
	private Map<Integer, Float> modifiedValues = new HashMap<Integer, Float>();

	// To invoke the event system
	private PluginManager pluginManager;
	
	public InjectedHashMap(Map<Integer, Float> underlyingMap, PluginManager pluginManager) {
		this.underlyingMap = underlyingMap;
		this.pluginManager = pluginManager;
	}

	// Called by Minecraft - inject our event code here
	@Override
	public boolean containsKey(Object key) {
		
		Integer id = (Integer) key;
		Float expPerItem = 0.0f;
		
		// Get the official value
		if (underlyingMap.containsKey(id)) {
			expPerItem = underlyingMap.get(id);
		}
		
		FurnaceRecipe recipe = getRecipe(id);
		FurnaceExpEvent furnaceExp = new FurnaceExpEvent(expPerItem, recipe);
		
		pluginManager.callEvent(furnaceExp);
		expPerItem = furnaceExp.getExpToDropPerItem();
		
		// Set it for the next time the get-method is called
		if (expPerItem > 0) {
			modifiedValues.put(id, expPerItem);
			return true;
		} else {
			return false;
		}
	}

	// Used for an event parameter
	private CraftFurnaceRecipe getRecipe(Integer id) {

		RecipesFurnace manager = RecipesFurnace.getInstance();
		CraftItemStack stack = new CraftItemStack(manager.getResult(id));

		// Create the recipe class
		if (stack.getTypeId() != 0) {
			return new CraftFurnaceRecipe(stack, new ItemStack(id, 1, (short) -1));
		} else {
			return null;
		}
	}
	
	@Override
	public Float get(Object key) {
		return modifiedValues.get(key);
	}
	
	@Override
	public boolean containsValue(Object value) {
		return modifiedValues.containsValue(value);
	}

	@Override
	public Set<Integer> keySet() {
		return modifiedValues.keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<Integer, Float>> entrySet() {
		return modifiedValues.entrySet();
	}

	@Override
	public boolean isEmpty() {
		return modifiedValues.isEmpty();
	}

	@Override
	public int size() {
		return modifiedValues.size();
	}

	@Override
	public Collection<Float> values() {
		return modifiedValues.values();
	}
	
	// While we override reading, writing is still done to the underlying map
	@Override
	public void clear() {
		underlyingMap.clear();
		modifiedValues.clear();
	}

	@Override
	public Float put(Integer key, Float value) {
		return underlyingMap.put(key, value);
	}

	@Override
	public void putAll(Map<? extends Integer, ? extends Float> m) {
		underlyingMap.putAll(m);
	}

	@Override
	public Float remove(Object key) {
		return underlyingMap.remove(key);
	}
}
