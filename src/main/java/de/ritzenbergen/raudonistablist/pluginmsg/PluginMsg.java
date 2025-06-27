package de.ritzenbergen.raudonistablist.pluginmsg;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class PluginMsg implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("my:tab_update")) return;

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            String action = in.readUTF();
            String name = in.readUTF();
            String server = in.readUTF();

            switch (action) {
                case "join":
                    break;
                case "switch":

                    break;
                case "quit":

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
