package me.vanturestudio.vantureapi.gui;

import me.vanturestudio.vantureapi.classes.arrays.Dictionary;
import me.vanturestudio.vantureapi.gui.events.EvtGUICacheRegister;
import me.vanturestudio.vantureapi.gui.events.EvtGUIClick;
import me.vanturestudio.vantureapi.gui.events.EvtGUIOpen;
import me.vanturestudio.vantureapi.gui.runnables.GUIClickEvent;
import me.vanturestudio.vantureapi.gui.utils.GUIUtils;
import me.vanturestudio.vantureapi.item.Item;
import me.vanturestudio.vantureapi.output.OutputStream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class GUI implements Listener {
	
	
	/*
	*
	 * GUI Initialisations
	*
	* */
	@Contract(value = " -> new", pure = true)
	public static @NotNull GUI gui() {
		return new GUI();
	}
	
	/*
	*
	* AzGui.gui()
	*
	* */
	
	Player owner;
	
	String title;
	int size;
	InventoryType type;
	
	Dictionary<Integer, Item> contents;
	Dictionary<Integer, GUIClickEvent> clickEvents;
	
	public GUI() {
		this.title = "Inventory";
		this.size = 27;
		this.type = InventoryType.CHEST;
		this.contents = new Dictionary<>();
		this.clickEvents = new Dictionary<>();
	}
	
	public GUI owner(Player owner) {
		this.owner = owner;
		return this;
	}
	
	public GUI title(String title) {
		this.title = title;
		return this;
	}
	
	public GUI size(int size) {
		this.size = GUIUtils.guiSize(size);
		return this;
	}
	
	public GUI type(InventoryType type) {
		this.type = type;
		return this;
	}

	public GUI setItem(int @NotNull [] slots, ItemStack itemStack) {for (int slot: slots) {setItem(slot, itemStack);}return this;}
	public GUI setItem(int x, int y, ItemStack itemStack) {return setItem(GUIUtils.point(x, y), itemStack);}
	public GUI setItem(int slot, ItemStack itemStack) {return setItem(slot, itemStack, true);}
	public GUI setItem(int slot, ItemStack itemStack, boolean replace) {
		if (contents.containsKey(slot)) {
			if (replace)
				contents.put(slot, Item.from(itemStack));
			else return this;
		}else{
			contents.put(slot, Item.from(itemStack));
		}
		return this;
	}
	
	
	public GUI setItem(int @NotNull [] slots, Item item) {for (int slot: slots) {setItem(slot, item);}return this;}
	public GUI setItem(int x, int y, Item item) {return setItem(GUIUtils.point(x, y), item);}
	public GUI setItem(int slot, Item item) {
		return setItem(slot, item, true);
	}
	public GUI setItem(int slot, Item item, boolean replace) {
		if (contents.containsKey(slot)) {
			if (replace)
				contents.put(slot, item);
			else return this;
		}else{
			contents.put(slot, item);
		}
		return this;
	}

	public GUI open(Player @NotNull ... players) {
		for (Player player: players) {
			EvtGUIOpen eventGUICreate = new EvtGUIOpen(this, player);

			if (!eventGUICreate.isCancelled())
				player.openInventory(create());
		}
		return this;
	}
	public GUI open() {
		if (this.owner != null) {
			EvtGUIOpen eventGUICreate = new EvtGUIOpen(this, this.owner);

			if (!eventGUICreate.isCancelled())
				this.owner.openInventory(create());
		}
		return this;
	}
	
	private @NotNull Inventory create() {

		Inventory inventory;
		if (this.type == InventoryType.CHEST)
			inventory = Bukkit.createInventory(null, this.size, OutputStream.format(title));
		else inventory = Bukkit.createInventory(null, this.type, OutputStream.format(title));

		for (int itemSlot: contents.keySet()) {
			if (contents.get(itemSlot) == null) continue;
			inventory.setItem(itemSlot, contents.get(itemSlot).create());
		}

		new EvtGUICacheRegister(this);
		GUICache.registerGUI(inventory, this);
		return inventory;
	}
	/*
	*
	* Event Listeners
	*
	* */
	
	@EventHandler
	public void onInventoryClick(@NotNull InventoryClickEvent e){
		if(e.getClickedInventory() == null) return;
		if(GUICache.getGUI(e.getClickedInventory()) == null) return;
		GUI gui = GUICache.getGUI(e.getClickedInventory());
		EvtGUIClick evtGUIClick = new EvtGUIClick(gui, e);

		if (evtGUIClick.isCancelled()) return;
		
		if (gui.contents.get(e.getRawSlot()) != null) {
			Item clickedItem = gui.contents.get(e.getRawSlot());
			e.setCancelled(!clickedItem.isStealable());
			if (clickedItem.getClickEvent() != null) clickedItem.getClickEvent().setEvent(e).setGui(gui).setItem(clickedItem).run();
		}
		if(!gui.clickEvents.isEmpty()){
			for(int slot: gui.clickEvents.keySet()){
				if (slot == e.getSlot()) {
					if (contents.get(e.getSlot()) != null) {
						e.setCancelled(contents.get(e.getSlot()).isStealable());      // Should the user take the item
						gui.clickEvents.get(slot).setItem(contents.get(e.getSlot())); // Pass the item to the runnable
					}
					gui.clickEvents.get(slot).setEvent(e);  // Pass the event to the runnable
					gui.clickEvents.get(slot).setGUI(this); // Pass the GUI to the runnable
					gui.clickEvents.get(slot).run();        // Execute the runnable
				}
			}
		}
	}
	
	
	/*
	*
	* GETTERS
	*
	* */
	
	public Player getOwner() {
		return owner;
	}
	
	public Dictionary<Integer, GUIClickEvent> getClickEvents() {
		return clickEvents;
	}
	
	public Dictionary<Integer, Item> getContents() {
		return contents;
	}
	
	public InventoryType getType() {
		return type;
	}
	
	public int getSize() {
		return size;
	}
	
	public String getTitle() {
		return title;
	}


}
