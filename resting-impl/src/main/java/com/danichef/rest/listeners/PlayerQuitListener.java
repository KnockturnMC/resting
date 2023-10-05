package com.danichef.rest.listeners;

import com.danichef.rest.corpses.PlayerCorpses;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final PlayerCorpses playerCorpses;

    public PlayerQuitListener(final PlayerCorpses playerCorpses) {
        this.playerCorpses = playerCorpses;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!playerCorpses.isLying(event.getPlayer())) return;

        playerCorpses.wakeUp(event.getPlayer());
    }
}
