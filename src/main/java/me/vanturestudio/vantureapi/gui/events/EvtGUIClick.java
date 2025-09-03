package me.vanturestudio.vantureapi.gui.events;

import me.vanturestudio.vantureapi.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class EvtGUIClick extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;

	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}

	private final GUI gui;
	private final Player whoClicked;
	private final InventoryClickEvent inventoryClickEvent;

	public EvtGUIClick(GUI gui, @NotNull InventoryClickEvent e) {
		this.gui = gui;
		this.inventoryClickEvent = e;
		this.whoClicked = (Player) e.getWhoClicked();
	}

	public GUI getGUI() {
		return gui;
	}

	public Player getWhoClicked() {
		return whoClicked;
	}

	public InventoryClickEvent getInventoryClickEvent() {
		return inventoryClickEvent;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
