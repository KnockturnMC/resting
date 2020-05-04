package com.danichef.rest.listeners;

import com.danichef.rest.utils.MessagesUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class PlayerDismountListener implements Listener {

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if ((event.getDismounted().getCustomName() == null)
                || (!event.getDismounted().getCustomName().equals(MessagesUtil.AS_NAME))) return;

        event.getDismounted().remove();
    }
}
