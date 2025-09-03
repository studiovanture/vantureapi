package me.vanturestudio.vantureapi.gui.utils;

import org.jetbrains.annotations.Contract;

public class GUIUtils {
	
	/**
	 * Translate an (x and y) to a slot in an inventory
	 * <p>
	 *
	 * @param x The row of the slot  (1-6)
	 * @param y The placement in the row of the slot (1-9)
	 * @since 1.0.0
	 */
	@Contract(pure = true)
	public static int point(int x, int y) {
		return ((x - 1) * 9) + (y - 1);
	}
	
	public static int guiSize(int input) {
		int[] targets = {9, 18, 27, 36, 45, 54};
		
		if (input >= 1 && input <= 6) {
			return input * 9;
		}
		
		int closest = targets[0];
		int minDiff = Math.abs(input - closest);
		
		for (int i = 1; i < targets.length; i++) {
			int diff = Math.abs(input - targets[i]);
			if (diff < minDiff) {
				minDiff = diff;
				closest = targets[i];
			}
		}
		
		return closest;
	}
}
