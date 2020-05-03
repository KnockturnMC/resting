package com.danichef.rest.player.seats;

import org.bukkit.entity.Player;

public interface PlayerSit {
    void sit(Player player);

    void standUp(Player player);
}
