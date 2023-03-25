package com.danichef.rest.seats;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerSit {

    /**
     * Makes player sit
     *
     * @param player to sit
     */
    void sit(@NotNull Player player);

    /**
     * Makes player stand up
     *
     * @param player to stand up
     */
    void standUp(@NotNull Player player);

    /**
     * Returns whether the given entity is a seat or not
     *
     * @param entity to check
     * @return whether the given entity is a seat or not
     */
    boolean isSeat(@NotNull Entity entity);
}
