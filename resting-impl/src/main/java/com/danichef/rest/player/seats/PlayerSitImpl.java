package com.danichef.rest.player.seats;

import com.danichef.rest.events.PlayerSitEvent;
import com.danichef.rest.seats.PlayerSit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class PlayerSitImpl implements PlayerSit {

    private final NamespacedKey key;

    public PlayerSitImpl(final NamespacedKey key) {
        this.key = key;
    }

    /**
     * Makes player sit
     *
     * @param player to sit
     */
    @Override
    public boolean sit(@NotNull Player player) {
        final PlayerSitEvent sitEvent = new PlayerSitEvent(player, true);
        Bukkit.getPluginManager().callEvent(sitEvent);
        if (sitEvent.isCancelled()) {
            return false;
        };

        final Location location = player.getLocation();

        final ArmorStand seat = location.getWorld().spawn(
          location.clone().subtract(0.0D, 1.7D, 0.0D),
          ArmorStand.class
        );
        seat.setGravity(false);
        seat.setVisible(false);
        seat.getPersistentDataContainer().set(key, PersistentDataType.STRING, "");

        if (!seat.addPassenger(player)) {
            seat.remove(); // Failed to add the player as a passanger, removing the armor stand
            return false;
        }
    }

    /**
     * Makes player stand up
     *
     * @param player to stand up
     */
    @Override
    public void standUp(@NotNull final Player player) {
        if (player.getVehicle() == null) return;
        if (!isSeat(player.getVehicle())) return;

        final PlayerSitEvent sitEvent = new PlayerSitEvent(player, false);
        Bukkit.getPluginManager().callEvent(sitEvent);
        if (sitEvent.isCancelled()) return;

        final Location location = player.getLocation().clone().add(0, 1, 0);
        player.getVehicle().remove();
        player.teleport(location);
    }

    /**
     * Returns whether the given entity is a seat or not
     *
     * @param entity to check
     * @return whether the given entity is a seat or not
     */
    @Override
    public boolean isSeat(@NotNull Entity entity) {
        return entity.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }
}
