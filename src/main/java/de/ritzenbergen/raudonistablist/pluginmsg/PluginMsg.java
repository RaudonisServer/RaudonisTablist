package de.ritzenbergen.raudonistablist.pluginmsg;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import static de.ritzenbergen.raudonistablist.RaudonisTablist.log;

public class PluginMsg implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("rn:updatetablist")) return;

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            String action = in.readUTF();
            String name = in.readUTF();
            String server = in.readUTF();

            switch (action) {
                case "switch":
                    log("Tablist-Änderung: switch "+name+" "+server);
                    break;
                case "quit":
                    log("Tablist-Änderung: quit "+name);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
