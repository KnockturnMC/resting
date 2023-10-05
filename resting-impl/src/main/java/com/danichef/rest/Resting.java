package com.danichef.rest;

import com.danichef.rest.commands.AdminCommand;
import com.danichef.rest.commands.LayCommand;
import com.danichef.rest.commands.SitCommand;
import com.danichef.rest.corpses.PlayerCorpses;
import com.danichef.rest.listeners.PlayerDismountListener;
import com.danichef.rest.listeners.PlayerQuitListener;
import com.danichef.rest.player.PlayerManager;
import com.danichef.rest.player.PlayerManagerImpl;
import com.danichef.rest.player.corpses.PlayerCorpsesImpl;
import com.danichef.rest.player.seats.PlayerSitImpl;
import com.danichef.rest.seats.PlayerSit;
import com.danichef.rest.utils.MessagesUtil;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Resting extends JavaPlugin implements RestingAPI {

    @Getter
    private PlayerSit playerSit;
    @Getter
    private PlayerCorpses playerCorpses;
    @Getter
    private PlayerManager playerManager;

    @Override
    public void onLoad() {
        getServer().getServicesManager().register(RestingAPI.class, this, this, ServicePriority.Normal);
    }

    @Override
    public void onEnable() {
        playerSit = new PlayerSitImpl(new NamespacedKey(this, MessagesUtil.AS_KEY));
        playerCorpses = new PlayerCorpsesImpl();
        playerManager = new PlayerManagerImpl(playerCorpses);

        getServer().getPluginManager().registerEvents(new PlayerQuitListener(playerCorpses), this);
        getServer().getPluginManager().registerEvents(new PlayerDismountListener(playerSit), this);

        getCommand("sit").setExecutor(new SitCommand(playerSit, playerManager));
        getCommand("lay").setExecutor(new LayCommand(playerCorpses, playerManager));
        getCommand("rest").setExecutor(new AdminCommand(playerSit, playerCorpses));
    }

    @Override
    public void onDisable() {
    }

    @Override
    public @NotNull PlayerCorpses corpseManager() {
        return this.playerCorpses;
    }

    @Override
    public @NotNull PlayerSit sitManager() {
        return this.playerSit;
    }
}
