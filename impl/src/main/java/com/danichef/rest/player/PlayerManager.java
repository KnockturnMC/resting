package com.danichef.rest.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerManager {

    /**
     * Check if players can rest at the current time and location or not.0
     *
     * @param player the player to check for.
     * @return if player can player rest
     */
    boolean canRest(@NotNull Player player);
}
