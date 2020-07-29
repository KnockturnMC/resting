package com.danichef.rest.listeners;

import com.danichef.rest.player.seats.PlayerSit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class PlayerDismountListener implements Listener {

    private final PlayerSit playerSit;

    public PlayerDismountListener (final PlayerSit playerSit) {
        this.playerSit = playerSit;
    }

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if (!playerSit.isSeat(event.getDismounted())) return;

        event.getDismounted().remove();
    }
}
