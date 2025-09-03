package me.vanturestudio.vantureapi.commands;

import me.vanturestudio.vantureapi.classes.arrays.Enumerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class ImplSubCommand {

	public abstract boolean execute(CommandSender sender, Command command, String label, Enumerator<String> args);
}
