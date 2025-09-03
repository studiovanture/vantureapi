package me.vanturestudio.vantureapi.chat;

import me.vanturestudio.vantureapi.classes.arrays.Dictionary;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlayerChatEvent implements Listener {

	private final JavaPlugin PLUGIN;

	public PlayerChatEvent(JavaPlugin plugin) {
		this.PLUGIN = plugin;
	}

	public static Dictionary<Player, PlayerChat> LOGGED_CHATS = new Dictionary<>();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {

		event.setCancelled(true);

		Player player = event.getPlayer();
		String msg = event.getMessage();

		for (Player recipient : event.getRecipients()) {
			PlayerReceiveMessageEvent receiveMessageEvent = new PlayerReceiveMessageEvent(recipient, new Message(UUID.randomUUID(), event.getMessage(), event.getPlayer()));
			if (receiveMessageEvent.isCancelled()) continue;

			if (recipient == player) {
				PlayerChat chat = LOGGED_CHATS.getOrDefaultAndPut(player, new PlayerChat(PLUGIN, player));

				event.setCancelled(true);

				TextComponent text = new TextComponent(msg);
				text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Edit message")}));
				text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/e " + UUID.randomUUID() + " " + msg));
				chat.println(UUID.randomUUID(), text);
			}else {
				PlayerChat chat = LOGGED_CHATS.getOrDefaultAndPut(recipient, new PlayerChat(PLUGIN, recipient));
				chat.println(new Message(UUID.randomUUID(), msg, player));
			}
		}

	}
}
