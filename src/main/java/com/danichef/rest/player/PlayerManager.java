package com.danichef.rest.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerManager {

    /**
     * Check if players can rest or not
     *
     * @param player
     * @return if player can player rest
     */
    boolean canRest(@NotNull Player player);
}
