package de.ritzenbergen.raudonistablist;

import de.ritzenbergen.raudonistablist.pluginmsg.PluginMsg;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import static org.bukkit.Bukkit.getMessenger;


public final class RaudonisTablist extends JavaPlugin {
    public static final String PREFIX="§cRaudonisTablist §b>> §r";
    @Override
    public void onEnable() {

        getMessenger().registerIncomingPluginChannel(this, "rn:updatetablist", new PluginMsg());
        log("Plugin gestartet!");
    }

    @Override
    public void onDisable() {


        log("Plugin gestoppt!");
    }

    public static void log(String text){
        Bukkit.getConsoleSender().sendMessage(PREFIX+text);
    }
}
