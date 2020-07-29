package com.danichef.rest.player.seats;

import com.danichef.rest.Rest;
import com.danichef.rest.utils.MessagesUtil;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class PlayerSitImpl implements PlayerSit {

    private final NamespacedKey key;

    public PlayerSitImpl(final NamespacedKey key) { this.key = key; }

    /**
     * Makes player sit
     * @param player to sit
     */
    @Override
    public void sit(@NotNull Player player) {
        Location location = player.getLocation();

        ArmorStand seat = location.getWorld().spawn(location.clone().subtract(0.0D, 1.7D, 0.0D), ArmorStand.class);
        seat.setGravity(false);
        seat.setVisible(false);
        seat.getPersistentDataContainer().set(key, PersistentDataType.STRING, "");
        seat.addPassenger(player);
    }

    /**
     * Makes player stand up
     * @param player to stand up
     */
    @Override
    public void standUp(@NotNull Player player)  {
        if (player.getVehicle() == null) return;
        if (!isSeat(player.getVehicle())) return;

        Location location = player.getLocation().clone().add(0, 1, 0);
        player.getVehicle().remove();
        player.teleport(location);
    }

    /**
     * Returns whether the given entity is a seat or not
     * @param entity to check
     * @return whether the given entity is a seat or not
     */
    @Override
    public boolean isSeat(@NotNull Entity entity) {
        return (entity.getPersistentDataContainer().has(key, PersistentDataType.STRING));
    }
}
