package com.mcmiddleearth.connect.bungee.tabList.playerItem;

import com.mcmiddleearth.connect.bungee.ConnectBungeePlugin;
import com.mcmiddleearth.connect.bungee.tabList.TabViewManager;
import com.mcmiddleearth.connect.log.Log;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.protocol.packet.PlayerListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PlayerItemUpdater {

    ScheduledTask updateTask;

    public PlayerItemUpdater() {
        updateTask = ProxyServer.getInstance().getScheduler().schedule(ConnectBungeePlugin.getInstance(), () -> {
//PlayerItemManager.showItems();
            for (Map.Entry<String, ServerInfo> server : ProxyServer.getInstance().getServers().entrySet()) {

                List<PlayerListItem.Item> removal = new ArrayList<>();
                Map<UUID,TabViewPlayerItem> itemMap = PlayerItemManager.getPlayerItems(server.getKey());
                if(itemMap!=null) {
                    for (TabViewPlayerItem item : itemMap.values()) {
                        ProxiedPlayer search = ProxyServer.getInstance().getPlayer(item.getUuid());

                        if (search == null || !search.getServer().getInfo().getName().equals(server.getValue().getName())) {
                            Logger.getGlobal().info("PIUPdate remove: " + item.getUsername());
                            PlayerListItem.Item removalItem = new PlayerListItem.Item();
                            removalItem.setUuid(item.getUuid());
                            Log.warn("PIUpdate", "__________remove " + item.getUsername());
                            Log.info("PIUpdate", "uuid: " + item.getUuid());
                            removal.add(removalItem);
                        }
                    }
                }
                PlayerListItem packet = new PlayerListItem();
                packet.setItems(removal.toArray(new PlayerListItem.Item[0]));
                TabViewManager.handleRemovePlayerPacket(null, packet);

                if (!server.getValue().getPlayers().isEmpty()) {
                    ProxiedPlayer sender = server.getValue().getPlayers().stream().findFirst().orElse(null);
                    PlayerListItem.Item[] items = new PlayerListItem.Item[server.getValue().getPlayers().size()];
                    int i = 0;
                    for (ProxiedPlayer player : server.getValue().getPlayers()) {
                        items[i] = new PlayerListItem.Item();
                        items[i].setUuid(player.getUniqueId());
                        items[i].setProperties(new String[0][0]);
                        items[i].setPing(player.getPing());
                        items[i].setUsername(player.getName());
                        //items[i].setDisplayName(player.getDisplayName());
                        Log.warn("PIUpdate","___________add "+player.getName());
                        Log.info("PIUpdate",""+player.getUniqueId());
                        Log.info("PIUpdate",""+player.getPing());
                        Log.info("PIUpdate",""+player.getDisplayName());
                        Log.info("PIUpdate",""+items[i].getProperties().length);
                        i++;
                    }
                    packet = new PlayerListItem();
                    packet.setItems(items);
                    TabViewManager.handleAddPlayerPacket(sender, packet);
                }
                //TabViewManager.updateViews();
            }
        }, 10, ConnectBungeePlugin.getConfig().getInt("TabListUpdateSeconds",2), TimeUnit.SECONDS);
    }

    public void disable() {
        updateTask.cancel();
    }
}
