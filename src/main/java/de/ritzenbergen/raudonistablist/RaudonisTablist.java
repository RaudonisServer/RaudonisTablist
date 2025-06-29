package de.ritzenbergen.raudonistablist;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.wrappers.*;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import de.ritzenbergen.raudonistablist.pluginmsg.PluginMsg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.Function;

import static org.bukkit.Bukkit.getMessenger;


public final class RaudonisTablist extends JavaPlugin implements Listener {

    public static final String PREFIX="§cRaudonisTablist §b>> §r";

    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        getMessenger().registerIncomingPluginChannel(this, "rn:updatetablist", new PluginMsg());
        getMessenger().registerOutgoingPluginChannel(this,"rn:updatetablist");
        log("Plugin gestartet!");
        Bukkit.getServer().getPluginManager().registerEvents(this,this);

    }

    @Override
    public void onDisable() {


        log("Plugin gestoppt!");
    }

    public static void log(String text){
        Bukkit.getConsoleSender().sendMessage(PREFIX+text);
    }

    @EventHandler
    private void joinEvent(PlayerJoinEvent event) {
        new BukkitRunnable(){
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    log(player.getName());
                    sendPlayerPacket("TestFakePlayer", player);
                }
            }
        }.runTaskLater(this,20);
    }
    public void sendPlayerPacket(String playername, Player player){
        ProtocolManager manager=ProtocolLibrary.getProtocolManager();

        WrappedGameProfile profile= new WrappedGameProfile(UUID.fromString("12345678-1234-5678-1234-567812345678"),playername);

        PlayerInfoData infoData=new PlayerInfoData(profile, 0, NativeGameMode.fromBukkit(Bukkit.getServer().getDefaultGameMode()),null);

        PacketContainer packet=manager.createPacket(PacketType.Play.Server.PLAYER_INFO);

        packet.getPlayerInfoActions().write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER));

        log(packet.getPlayerInfoDataLists().getField(0).getType().toString());

        packet.getPlayerInfoDataLists().write(1, List.of(infoData));

        for(FieldAccessor accessor : packet.getPlayerInfoDataLists().getFields()){
            log(accessor.getField().toString());
        }

        try {
            log(packet.toString());
            manager.sendServerPacket(player, packet);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void sendPlayerDisconnectPacket(String playername){

    }
}
