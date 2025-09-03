package me.vanturestudio.vantureapi.commands;

import me.vanturestudio.vantureapi.classes.arrays.Enumerator;
import me.vanturestudio.vantureapi.commands.utils.CommandListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public abstract class ImplCommand implements CommandExecutor {
	
	CommandSender sender;
	Command command;
	String label;
	Enumerator<String> args;
	
	public ImplCommand() {}
	
	public abstract boolean execute(CommandSender sender, Command command, String label, Enumerator<String> args);
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		this.sender = sender;
		this.command = command;
		this.label = label;
		this.args = Enumerator.fromArray(args);
		if (sender instanceof Player player)
			CommandListener.onCommand(player, command);
		return execute(sender, command, label, this.args);
	}
	
	public boolean subcommand(Class<? extends ImplSubCommand> subcommand) {
		try {
			return subcommandthrows(subcommand);
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ignored) {}
		return false;
	}
	
	public boolean subcommandthrows(Class<? extends ImplSubCommand> subcommand) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		if (sender == null ||
				command == null ||
				label == null ||
				args == null) throw new IllegalArgumentException("Sender, command, label, and or args are null.");
		return subcommand.getDeclaredConstructor().newInstance().execute(sender, command, label, args);
	}
}
