package com.danichef.rest.player.corpses;

import org.bukkit.entity.Player;

public interface PlayerCorpses {

    /**
     * @param player to put to sleep
     */
    void putToSleep(Player player);

    /**
     * @param player to put to sleep
     * @param sendTo the players that will recieve the sleep packets
     */
    void putToSleep(Player player, Player... sendTo);

    /**
     * @param player to wake up
     */
    void wakeUp(Player player);

    /**
     * @param player to put to sleep
     * @param sendTo the players that will recieve the wake-up packets
     */
    void wakeUp(Player player, Player... sendTo);

    /**
     * Check if player is lying
     *
     * @param player
     * @return if player is lying
     */
    boolean isLying(Player player);
}
