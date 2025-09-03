package me.vanturestudio.vantureapi.gui.runnables;

import me.vanturestudio.vantureapi.gui.GUI;
import me.vanturestudio.vantureapi.item.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class GUIClickEvent extends BukkitRunnable {
	
	public InventoryClickEvent event;
	public Player player;
	public Inventory inventory;
	public int slot;
	
	public boolean isShiftClick;
	public boolean isLeftClick;
	public boolean isRightClick;
	GUI gui;
	Item item;
	
	public GUIClickEvent setItem(@NotNull Item item) {
		this.item = item;
		return this;
	}
	
	public GUIClickEvent setGUI(@NotNull GUI gui) {
		this.gui = gui;
		return this;
	}
	
	// Getter for accessing the event in your custom logic
	public InventoryClickEvent getEvent() {
		return this.event;
	}

	// Method to set the event dynamically
	public GUIClickEvent setEvent(@NotNull InventoryClickEvent e) {
		this.event = e;
		this.player = (Player) e.getWhoClicked();
		this.slot = e.getSlot();
		this.inventory = e.getInventory();
		
		this.isShiftClick = e.isShiftClick();
		this.isLeftClick = e.isLeftClick();
		this.isRightClick = e.isRightClick();
		
		return this;
	}
	
	/**
	 * When an object implementing interface {@code Runnable} is used
	 * to create a thread, starting the thread causes the object's
	 * {@code run} method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method {@code run} is that it may
	 * take any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	@Override
	public void run() {
	
	}
}
