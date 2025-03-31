package com.danichef.rest.listeners;

import com.danichef.rest.events.PlayerSitEvent;
import com.danichef.rest.seats.PlayerSit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;

public class PlayerDismountListener implements Listener {

    private final PlayerSit playerSit;

    public PlayerDismountListener(final PlayerSit playerSit) {
        this.playerSit = playerSit;
    }

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!playerSit.isSeat(event.getDismounted())) return;

        final PlayerSitEvent sitEvent = new PlayerSitEvent(((Player) event.getEntity()), false);
        Bukkit.getPluginManager().callEvent(sitEvent);
        if (sitEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        event.getEntity().eject();
        event.getDismounted().remove();
        event.getEntity().teleport(event.getEntity().getLocation().add(0, 1, 0));
    }
}
