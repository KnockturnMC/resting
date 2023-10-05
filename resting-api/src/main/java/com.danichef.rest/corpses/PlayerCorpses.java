package com.danichef.rest.corpses;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerCorpses {

    /**
     * @param player to put to sleep
     */
    void putToSleep(@NotNull Player player);

    /**
     * @param player to put to sleep
     * @param sendTo the players that will recieve the sleep packets
     */
    void putToSleep(@NotNull Player player, @NotNull Player... sendTo);

    /**
     * @param player to wake up
     */
    void wakeUp(@NotNull Player player);

    /**
     * @param player to put to sleep
     * @param sendTo the players that will recieve the wake-up packets
     */
    void wakeUp(@NotNull Player player, @NotNull Player... sendTo);

    /**
     * Check if player is lying
     *
     * @param player
     * @return if player is lying
     */
    boolean isLying(@NotNull Player player);
}
