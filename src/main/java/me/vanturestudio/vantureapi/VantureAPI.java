package me.vanturestudio.vantureapi;

import me.vanturestudio.vantureapi.chat.PlayerChatEvent;
import me.vanturestudio.vantureapi.gui.GUI;
import org.bukkit.plugin.java.JavaPlugin;

public final class VantureAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new GUI(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatEvent(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
