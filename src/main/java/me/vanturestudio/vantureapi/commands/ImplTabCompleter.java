package me.vanturestudio.vantureapi.commands;

import me.vanturestudio.vantureapi.classes.arrays.Enumerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public abstract class ImplTabCompleter implements TabCompleter {
	
	public Enumerator<String> tabs;

	private Enumerator<String> args;
	
	public ImplTabCompleter() {
		tabs = new Enumerator<>();
	}
	
	/**
	 Requests a list of possible completions for a command argument.
	 
	 @param sender  Source of the command.  For players tab-completing a
	 command inside a command block, this will be the player, not
	 the command block.
	 @param command Command which was executed
	 @param alias   The alias used
	 @param args    The arguments passed to the command, including final
	 partial argument to be completed and command label
	 
	 @return A List of possible completions for the final argument, or null
	 to default to the command executor
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		tabs = new Enumerator<>();
		this.args = Enumerator.fromArray(args);
		return execute(sender, command, alias, this.args).list();
	}

	public abstract Enumerator<String> execute(CommandSender sender, Command command, String alias, Enumerator<String> args);

	public void complete(@NotNull String complete) {
		String curr = this.args.array(String.class)[this.args.size() - 1];
		if (curr.isEmpty()) tabs.add(complete);
		else if(complete.toLowerCase(Locale.ENGLISH)
				.contains(curr.toLowerCase(Locale.ENGLISH)))
			tabs.add(complete);
	}

	public void complete(@NotNull String arg, @NotNull String complete) {
		if (arg.isEmpty()) tabs.add(complete);
		else if(complete.toLowerCase(Locale.ENGLISH)
				.contains(arg.toLowerCase(Locale.ENGLISH)))
			tabs.add(complete);
	}
	
	public void add(String complete) {
		tabs.add(complete);
	}
	
	public void remove(String complete) {
		tabs.remove(complete);
	}
}
