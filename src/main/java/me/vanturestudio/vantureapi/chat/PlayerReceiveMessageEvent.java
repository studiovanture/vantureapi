package me.vanturestudio.vantureapi.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerReceiveMessageEvent extends PlayerEvent implements Cancellable {

	/*
	* Event Handlers
	* */
	private static final HandlerList handlers = new HandlerList();
	public static HandlerList getHandlerList() {return handlers;}
	public HandlerList getHandlers() {return handlers;}


	private final Message message;

	public PlayerReceiveMessageEvent(Player receiver, Message message) {
		super(receiver);
		this.message = message;

		this.cancelled = false;
	}

	public Message getMessage() {
		return message;
	}

	/*
	* Cancellable
	* */
	private boolean cancelled;

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
}
