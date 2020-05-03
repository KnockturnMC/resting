package com.danichef.rest.player.seats;

import com.danichef.rest.utils.MessagesUtil;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class PlayerSitImpl implements PlayerSit {

    /**
     * Makes player sit
     * @param player
     */
    @Override
    public void sit(Player player) {
        Location location = player.getLocation();

        ArmorStand seat = location.getWorld().spawn(location.clone().subtract(0.0D, 1.7D, 0.0D), ArmorStand.class);
        seat.setGravity(false);
        seat.setVisible(false);
        seat.setCustomNameVisible(false);
        seat.setCustomName(MessagesUtil.AS_NAME);
        seat.addPassenger(player);
    }

    /**
     * Makes player stand up
     * @param player
     */
    @Override
    public void standUp(Player player)  {
        if (player.getVehicle() == null) return;
        if (!player.getVehicle().getCustomName().equals(MessagesUtil.AS_NAME)) return;

        Location location = player.getLocation().clone().add(0, 1, 0);
        player.getVehicle().remove();
        player.teleport(location);
    }
}
