package com.danichef.rest.player;

import com.danichef.rest.player.corpses.PlayerCorpses;
import com.danichef.rest.player.seats.PlayerSit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerManagerImpl implements PlayerManager {

    private final PlayerCorpses playerCorpses;

    public PlayerManagerImpl(final PlayerCorpses playerCorpses) {
        this.playerCorpses = playerCorpses;
    }

    /**
     * Check if players can rest or not
     *
     * @param player
     * @return if player can player rest
     */
    @Override
    public boolean canRest(@NotNull Player player) {
        if (player.isOnGround() && !player.isSleeping() && !playerCorpses.isLying(player) && player.getVehicle() == null) return true;
        return false;
    }

}
