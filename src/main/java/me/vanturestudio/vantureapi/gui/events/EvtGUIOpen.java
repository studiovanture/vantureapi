package me.vanturestudio.vantureapi.gui.events;

import me.vanturestudio.vantureapi.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EvtGUIOpen extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}

	private boolean cancelled;

	private final GUI gui;
	private final Player viewer;

	public EvtGUIOpen(GUI gui, Player viewer) {
		this.cancelled = false;
		this.gui = gui;
		this.viewer = viewer;
	}

	public GUI getGUI() {
		return gui;
	}

	public Player getViewer() {
		return viewer;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
}
