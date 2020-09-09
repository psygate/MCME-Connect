/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.connect.bungee.tabList;

import java.util.Set;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.packet.PlayerListHeaderFooter;

/**
 *
 * @author Eriol_Eandur
 */
public interface ITabView {


    void handleAddPlayer(ProxiedPlayer vanillaRecipient, Set<TabViewPlayerItem> items);
    
    void handleUpdateGamemode(ProxiedPlayer vanillaRecipient, Set<TabViewPlayerItem> items);
    
    void handleUpdateLatency(ProxiedPlayer vanillaRecipient, Set<TabViewPlayerItem> items);
    
    void handleUpdateDisplayName(ProxiedPlayer vanillaRecipient, Set<TabViewPlayerItem> items);

    void handleRemovePlayer(ProxiedPlayer vanillaRecipient, Set<TabViewPlayerItem> items);

    void handleVanishPlayer(TabViewPlayerItem items);

    void handleUnvanishPlayer(TabViewPlayerItem items);

    void addViewer(ProxiedPlayer player);
    
    void removeViewer(ProxiedPlayer player);
    
    boolean isViewer(ProxiedPlayer player);

    void handleHeaderFooter(ProxiedPlayer vanillaRecipient, PlayerListHeaderFooter packet);

}
