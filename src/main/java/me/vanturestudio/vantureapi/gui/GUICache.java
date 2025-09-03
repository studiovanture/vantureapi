package me.vanturestudio.vantureapi.gui;

import me.vanturestudio.vantureapi.classes.arrays.Dictionary;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GUICache {
	
	private static final Dictionary<Inventory, GUI> inventories = new Dictionary<>();
	
	public static void registerGUI(Inventory inventory, GUI gui) {
		inventories.put(inventory, gui);
	}
	
	public static void updateGUI(Inventory inventory, GUI gui) {
		inventories.remove(inventory);
		inventories.put(inventory, gui);
	}
	
	@Contract(pure = true)
	public static Dictionary<Inventory, GUI> getInventories() {
		return inventories;
	}
	
	public static GUI getGUI(Inventory inventory) {
		return inventories.get(inventory);
	}
	
	public static @NotNull List<Inventory> getInventory(GUI gui) {
		List<Inventory> invs = new ArrayList<>();
		for (Inventory inv : inventories.keySet()) {
			if (inventories.get(inv) == gui) invs.add(inv);
		}
		return invs;
	}
	
	public static void removeInventory(Inventory inventory) {
		inventories.remove(inventory);
	}
}
