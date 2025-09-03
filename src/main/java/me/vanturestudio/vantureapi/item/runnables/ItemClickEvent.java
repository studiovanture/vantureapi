package me.vanturestudio.vantureapi.item.runnables;

import me.vanturestudio.vantureapi.gui.GUI;
import me.vanturestudio.vantureapi.item.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class ItemClickEvent extends BukkitRunnable {
	
	public Player whoClicked;
	public InventoryClickEvent event;
	public GUI gui;
	public Item item;
	
	public ItemClickEvent setEvent(@NotNull InventoryClickEvent event) {
		this.event = event;
		this.whoClicked = (Player) event.getWhoClicked();
		return this;
	}
	
	public ItemClickEvent setGui(GUI gui) {
		this.gui = gui;
		return this;
	}
	
	public ItemClickEvent setItem(Item item) {
		this.item = item;
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
