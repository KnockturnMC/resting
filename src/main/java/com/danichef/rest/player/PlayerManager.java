package com.danichef.rest.player;

import org.bukkit.entity.Player;

public interface PlayerManager {

    /**
     * Check if players can rest or not
     *
     * @param player
     * @return if player can player rest
     */
    boolean canRest(Player player);
}
