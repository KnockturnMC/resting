package com.danichef.rest.player;

import com.danichef.rest.corpses.PlayerCorpses;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerManagerImpl implements PlayerManager {

    private final PlayerCorpses playerCorpses;

    public PlayerManagerImpl(final PlayerCorpses playerCorpses) {
        this.playerCorpses = playerCorpses;
    }

    @Override
    public boolean canRest(@NotNull Player player) {
        return player.isOnGround()
          && !player.isSleeping()
          && !playerCorpses.isLying(player)
          && player.getVehicle() == null;
    }

}
