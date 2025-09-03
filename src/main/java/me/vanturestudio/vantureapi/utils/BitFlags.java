package me.vanturestudio.vantureapi.utils;

import me.vanturestudio.vantureapi.output.OutputStream;

import java.util.EnumSet;

public class BitFlags {

	private final EnumSet<OutputStream.FormatFlag> flags;

	// Constructor that converts an integer to a set of flags
	public BitFlags(int flagsValue) {
		flags = EnumSet.noneOf(OutputStream.FormatFlag.class);
		for (OutputStream.FormatFlag flag : OutputStream.FormatFlag.values()) {
			if ((flagsValue & flag.getFlag()) != 0) {
				flags.add(flag);
			}
		}
	}

	// Check if the flag is present
	public boolean has(OutputStream.FormatFlag flag) {
		return flags.contains(flag);
	}

	// Convert a bit flag integer to an EnumSet of flags
	public static EnumSet<OutputStream.FormatFlag> fromInt(int flagsValue) {
		EnumSet<OutputStream.FormatFlag> flagSet = EnumSet.noneOf(OutputStream.FormatFlag.class);
		for (OutputStream.FormatFlag flag : OutputStream.FormatFlag.values()) {
			if ((flagsValue & flag.getFlag()) != 0) {
				flagSet.add(flag);
			}
		}
		return flagSet;
	}

	// Add a flag to the BitFlags
	public BitFlags add(OutputStream.FormatFlag flag) {
		flags.add(flag);
		return this;
	}

	// Remove a flag from the BitFlags
	public BitFlags remove(OutputStream.FormatFlag flag) {
		flags.remove(flag);
		return this;
	}

	// Get the integer representation of the current flags
	public int toInt() {
		int value = 0;
		for (OutputStream.FormatFlag flag : flags) {
			value |= flag.getFlag();
		}
		return value;
	}
}