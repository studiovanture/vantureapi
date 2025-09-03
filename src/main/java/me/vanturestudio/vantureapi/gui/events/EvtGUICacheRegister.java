package me.vanturestudio.vantureapi.gui.events;

import me.vanturestudio.vantureapi.gui.GUI;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EvtGUICacheRegister extends Event {

	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}

	private final GUI gui;

	public EvtGUICacheRegister(GUI gui) {
		this.gui = gui;
	}

	public GUI getGUI() {
		return gui;
	}
}
