package com.danichef.rest.utils;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Util {

    public static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

    /**
     * Gets the horizontal Block Face from a player
     *
     * @param player
     * @return The Block Face of the angle
     */
    public static BlockFace yawToFace(Player player) {
        return yawToFace(player.getLocation().getYaw());
    }

    /**
     * Gets the horizontal Block Face from a given yaw angle
     *
     * @param yaw angle
     * @return The Block Face of the angle
     */
    public static BlockFace yawToFace(float yaw) {
            return axis[Math.round(yaw / 90f) & 0x3];
    }

}
