package de.ritzenbergen.raudonistablist;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import de.ritzenbergen.raudonistablist.pluginmsg.PluginMsg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.bukkit.Bukkit.createCommandSender;
import static org.bukkit.Bukkit.getMessenger;


public final class RaudonisTablist extends JavaPlugin implements Listener {

    public static final Logger LOGGER = LoggerFactory.getLogger("RaudonistTablist");
    private static ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        LOGGER.info("Starting RaudonisTablist...");
        protocolManager = ProtocolLibrary.getProtocolManager();
        getMessenger().registerIncomingPluginChannel(this, "rn:updatetablist", new PluginMsg());
        getMessenger().registerOutgoingPluginChannel(this,"rn:updatetablist");
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        LOGGER.info("Started RaudonisTablist!");

    }

    @Override
    public void onDisable() {
        LOGGER.info("Stopped RaudonisTablist");
    }


    @EventHandler
    private void joinEvent(PlayerJoinEvent event) {
        sendPlayerPacket("TestFakePlayer");
    }
    public void sendPlayerPacket(String playername){
        LOGGER.info(playername);
        UUID uuid = UUID.randomUUID();
        WrappedGameProfile profile = new WrappedGameProfile(uuid, playername);

        PlayerInfoData infoData = new PlayerInfoData(
                profile,
                0,
                EnumWrappers.NativeGameMode.SURVIVAL,
                WrappedChatComponent.fromText(playername)
        );

        PacketContainer packet = protocolManager.createPacket(com.comphenix.protocol.PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoActions().write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER));

        List<PlayerInfoData> dataList = new ArrayList<>();
        dataList.add(infoData);
        packet.getPlayerInfoDataLists().write(0, dataList);


        try {
            LOGGER.info(packet.toString());
            for(Player player : Bukkit.getOnlinePlayers()){
                protocolManager.sendServerPacket(player, packet);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendPlayerDisconnectPacket(String playername){
        /*
        Is this right???
         */
        PacketContainer disconnectPacket = protocolManager.createPacket(PacketType.Play.Server.KICK_DISCONNECT);
        Player player = Bukkit.getPlayer(playername);
        protocolManager.sendServerPacket(player, disconnectPacket);
    }
}
