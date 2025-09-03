package me.vanturestudio.vantureapi.item;

import me.vanturestudio.vantureapi.classes.arrays.Enumerator;
import me.vanturestudio.vantureapi.item.runnables.ItemClickEvent;
import me.vanturestudio.vantureapi.output.OutputStream;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Item {

	/*
	 *
	 * FIELDS
	 *
	 * */
	Material material;
	String display;
	int amount;

	List<String> lore;
	List<ItemFlag> itemFlags;
	boolean unbreakable;
	boolean stealable;
	boolean showEnchants;
	ItemClickEvent clickEvent;

	public Item(@NotNull Material material) {
		this.material = material;
		this.display = material.name();
		this.amount = 1;
		this.lore = new ArrayList<>();

		this.unbreakable = false;
		this.stealable = false;
		this.showEnchants = true;
	}

	@Contract(value = " -> new", pure = true)
	public static @NotNull Item item() {
		return new Item(Material.GRASS_BLOCK);
	}

	@Contract(value = "_ -> new", pure = true)
	public static @NotNull Item item(Material material) {
		return new Item(material);
	}

	@Contract(value = "_ -> new", pure = true)
	public static @NotNull Item from(@NotNull ItemStack itemStack) {
		Item item = Item.item(itemStack.getType())
				.display(itemStack.getItemMeta().getDisplayName())
				.amount(itemStack.getAmount());
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			item.unbreakable(meta.isUnbreakable())
					.itemFlags(new ArrayList<>(meta.getItemFlags()))
					.showEnchants(!meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS));
		}
		return item;
	}

	public ItemStack create() {
		return create(null);
	}

	public ItemStack create(Player owner) {
		ItemStack stack = new ItemStack(this.material, this.amount);
		ItemMeta meta = stack.getItemMeta();

		meta.setDisplayName(OutputStream.format(this.display));

		if (this.lore != null && !this.lore.isEmpty()) {
			List<String> finalLore = new ArrayList<>();
			for (String l : this.lore) {
				if (l == null) continue;
				finalLore.add(OutputStream.format(l));
			}
			meta.setLore(finalLore);
		}

		meta.setUnbreakable(this.unbreakable);
		meta.setLocalizedName(this.display);
		if (itemFlags != null && !itemFlags.isEmpty())
			itemFlags.forEach(meta::addItemFlags);
		if (!showEnchants)
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		stack.setItemMeta(meta);
		return stack;
	}

	/*
	 *
	 * SETTERS
	 *
	 * */

	public Item material(Material material) {
		this.material = material;
		return this;
	}

	public Item display(String display) {
		this.display = display;
		return this;
	}

	public Item dynamicDisplayLore(String dynamic) {
		List<String> lore = new ArrayList<>();
		String[] strings = dynamic.split("%nl");
		for (int i = 0; i < strings.length; i++) {
			if (i == 0) {
				this.display(strings[i]);
				continue;
			}
			lore.add(strings[i]);
		}
		this.lore(lore);
		return this;
	}

	public Item amount(int amount) {
		this.amount = amount;
		return this;
	}

	public Item showEnchants(boolean show) {
		this.showEnchants = show;
		return this;
	}

	public Item lore(String... lore) {
		this.lore = Arrays.stream(lore).collect(Collectors.toList());
		return this;
	}

	public Item lore(List<String> lore) {
		this.lore = lore;
		return this;
	}

	public Item lore(@NotNull Enumerator<String> lore) {
		this.lore = lore.values();
		return this;
	}

	public Item itemFlags(List<ItemFlag> itemFlags) {
		this.itemFlags = itemFlags;
		return this;
	}

	public Item unbreakable(boolean unbreakable) {
		this.unbreakable = unbreakable;
		return this;
	}

	public Item stealable(boolean stealable) {
		this.stealable = stealable;
		return this;
	}

	public Item onClick(ItemClickEvent clickEvent) {
		this.clickEvent = clickEvent;
		return this;
	}

	/*
	 *
	 * GETTERS
	 *
	 * */

	public Material getMaterial() {
		return material;
	}

	public ItemClickEvent getClickEvent() {
		return clickEvent;
	}

	public boolean isUnbreakable() {
		return unbreakable;
	}

	public List<String> getLore() {
		return lore;
	}

	public List<ItemFlag> getItemFlags() {
		return itemFlags;
	}

	public int getAmount() {
		return amount;
	}

	public String getDisplay() {
		return display;
	}

	public boolean isStealable() {
		return stealable;
	}

	public boolean isShowEnchants() {
		return showEnchants;
	}

}
