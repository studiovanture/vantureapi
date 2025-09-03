package me.vanturestudio.vantureapi.chat;

import me.vanturestudio.vantureapi.output.OutputStream;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerChat {

	private final JavaPlugin PLUGIN;
	private final Player PLAYER;

	private final List<Message> SAVED_CHAT;

	private final int CHAT_LENGTH = 1000;


	private boolean reverseIndex;

	public PlayerChat(JavaPlugin plugin, Player player) {
		this.PLUGIN = plugin;
		this.PLAYER = player;
		this.SAVED_CHAT = new ArrayList<>();
		this.reverseIndex = true;
	}

	public void clear() {
		this.clear(CHAT_LENGTH, true);
	}
	public void clear(int length) {
		this.clear(CHAT_LENGTH, true);
	}

	public void clear(int length, boolean removeHistory) {
		boolean swapCode = false;
		for (int i = 0; i < length; i++) {
			if (swapCode)
				println(Message.of("&0"), false);
			else
				println(Message.of("&1"), false);
			swapCode = !swapCode;
		}
		if (removeHistory)
			this.SAVED_CHAT.clear();
	}

	public void println(String line) { println(Message.of(line)); }
	public void println(Message line) {
		println(line, true);
	}

	public void println(String line, boolean saveInput) { println(Message.of(line), saveInput); }
	public void println(Message line, boolean saveInput) {
		this.PLAYER.sendMessage(OutputStream.format(line.getContent()));
		if (saveInput)
			this.SAVED_CHAT.add(this.SAVED_CHAT.size(), line);
	}
	public void println(UUID id, BaseComponent text, BaseComponent... other) {
		this.PLAYER.spigot().sendMessage(text);
		this.SAVED_CHAT.add(this.SAVED_CHAT.size(), new Message(id, text.toPlainText(), this.PLAYER));
		for (BaseComponent component: other) {
			this.PLAYER.spigot().sendMessage(component);
			this.SAVED_CHAT.add(this.SAVED_CHAT.size(), new Message(id, component.toPlainText(), this.PLAYER));
		}
	}

	public void editln(@Range(from = 0, to = CHAT_LENGTH-1) int index, String newline) throws IndexOutOfBoundsException { editln(index, Message.of(newline)); }
	public void editln(@Range(from = 0, to = CHAT_LENGTH-1) int index, Message newline) throws IndexOutOfBoundsException {
		if (reverseIndex)
			index = reverseIndex(index);
		if (index >= CHAT_LENGTH || index < 0) throw new IndexOutOfBoundsException(String.format("PlayerChat.editln(index: %s, newline: \"%s\"); Index %s out of bounds for length %s", index, newline, index, this.SAVED_CHAT.size()));
		this.SAVED_CHAT.set(index, newline);
		this.update();
	}

	public void removeln(@Range(from = 0, to = CHAT_LENGTH-1) int index) {
		if (reverseIndex)
			index = reverseIndex(index);
		this.SAVED_CHAT.remove(index);
		this.update();
	}

	public void update() {
		clear(CHAT_LENGTH - this.SAVED_CHAT.size(), false);
		for (Message line: this.SAVED_CHAT) {
			println(line.getContent(), false);
		}
	}

	public void reverseIndex(boolean reverseIndex) {
		this.reverseIndex = reverseIndex;
	}

	private int reverseIndex(int index) {
		return this.SAVED_CHAT.size() - index - 1;
	}

	public void editid(UUID id, String newline) {
		for (int i = 0; i < SAVED_CHAT.size(); i++) {
			if (SAVED_CHAT.get(i).getID() == id) {
				editln(i, newline);
			}
		}
	}
}
