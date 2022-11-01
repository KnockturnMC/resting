package com.danichef.rest.player.corpses;

import com.danichef.rest.utils.ReflectedObject;
import com.danichef.rest.utils.ReflectionUtil;
import com.danichef.rest.utils.Util;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerCorpsesImpl implements PlayerCorpses {

    @Getter
    private final List<Player> sleepingPlayers = new ArrayList<>();

    private static final Object ENTITY_LIVING_BED_POSITION_DATA_WATCHER_OBJECT = ReflectionUtil.getDeclaredField("bO",
            ReflectionUtil.getNmsClassEntLiv(), null);

    private static final Object ENTITY_POSE_DATA_WATCHER_OBJECT = ReflectionUtil.getDeclaredField("ad",
            ReflectionUtil.getNmsClassEnt(), null);

    private static final Class<?> CLASS_DATA_WATCHER_ITEM = ReflectionUtil.getNmsClassWatchItem(),
            CLASS_DATA_WATCHER_OBJECT = ReflectionUtil.getNmsClassWatchObject(),
            CLASS_PACKET_PLAY_OUT_ENTITY_META_DATA = ReflectionUtil.getNmsClassPacketEnt();

    private static final Object POSE_SLEEPING = ReflectionUtil.getNmsClassPose().getEnumConstants()[2];
    private static final Object POSE_STANDING = ReflectionUtil.getNmsClassPose().getEnumConstants()[0];

    /**
     * @param player to put to sleep
     */
    @Override
    public void putToSleep(@NotNull Player player) {
        putToSleep(player, player.getWorld().getPlayers().toArray(new Player[0]));
    }

    /**
     * @param player to put to sleep
     * @param sendTo the players that will recieve the sleep packets
     */
    @Override
    public void putToSleep(@NotNull Player player, @NotNull Player... sendTo) {
        try {
            List<Player> send = Arrays.asList(sendTo);
            Location playerLocation = player.getLocation().clone();
            Location hiddenBedBlock = findNextHiddenBlock(playerLocation);

            Object nms = ReflectionUtil.getNMSEntity(player);
            Object watcher = ReflectionUtil.getDeclaredField("Y", ReflectionUtil.getNmsClassEnt(), nms);

            Optional<Object> block = Optional.of(ReflectionUtil.getBlockPosition(hiddenBedBlock)); //The BlockPosition Instance
            Object bedPositionData = CLASS_DATA_WATCHER_ITEM.getDeclaredConstructor(CLASS_DATA_WATCHER_OBJECT, Object.class)
                    .newInstance(ENTITY_LIVING_BED_POSITION_DATA_WATCHER_OBJECT, block);
            Object poseData = CLASS_DATA_WATCHER_ITEM.getDeclaredConstructor(CLASS_DATA_WATCHER_OBJECT, Object.class)
                    .newInstance(ENTITY_POSE_DATA_WATCHER_OBJECT, POSE_SLEEPING);

            Object packet = new ReflectedObject(CLASS_PACKET_PLAY_OUT_ENTITY_META_DATA.getDeclaredConstructor(int.class, ReflectionUtil.getNmsClassWatch(), boolean.class)
                    .newInstance(ReflectionUtil.getID(player), watcher, false))
                    .with("a", ReflectionUtil.getID(player))
                    .with("b", Stream.of(bedPositionData, poseData).collect(Collectors.toList()))
                    .get();

            send.forEach(all -> {
                all.sendBlockChange(hiddenBedBlock, Material.RED_BED.createBlockData(b -> {
                    final Bed bed = (Bed) b;
                    bed.setFacing(Util.yawToFace(player));
                }));
                ReflectionUtil.sendPacket(all, packet);
            });
            player.teleport(playerLocation);
            sleepingPlayers.add(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param player to wake up
     */
    @Override
    public void wakeUp(@NotNull Player player) {
        wakeUp(player, player.getWorld().getPlayers().toArray(new Player[0]));
    }

    /**
     * @param player to put to sleep
     * @param sendTo the players that will recieve the wake-up packets
     */
    @Override
    public void wakeUp(@NotNull Player player, @NotNull Player... sendTo) {
        try {
            List<Player> send = Arrays.asList(sendTo);
            Location playerLocation = player.getLocation().clone();
            Location hiddenBedBlock = findNextHiddenBlock(playerLocation);

            Object nms = ReflectionUtil.getNMSEntity(player);
            Object watcher = ReflectionUtil.getDeclaredField("Y", ReflectionUtil.getNmsClassEnt(), nms);

            Object bedPositionData = CLASS_DATA_WATCHER_ITEM.getDeclaredConstructor(CLASS_DATA_WATCHER_OBJECT, Object.class)
                    .newInstance(ENTITY_LIVING_BED_POSITION_DATA_WATCHER_OBJECT, Optional.empty());
            Object poseData = CLASS_DATA_WATCHER_ITEM.getDeclaredConstructor(CLASS_DATA_WATCHER_OBJECT, Object.class)
                    .newInstance(ENTITY_POSE_DATA_WATCHER_OBJECT, POSE_STANDING);

            Object packet = new ReflectedObject(CLASS_PACKET_PLAY_OUT_ENTITY_META_DATA.getDeclaredConstructor(int.class, ReflectionUtil.getNmsClassWatch(), boolean.class)
                    .newInstance(ReflectionUtil.getID(player), watcher, false))
                    .with("a", ReflectionUtil.getID(player))
                    .with("b", Stream.of(bedPositionData, poseData).collect(Collectors.toList()))
                    .get();

            hiddenBedBlock.getBlock().getState().update(); //Update fake block
            send.forEach(all -> {
                ReflectionUtil.sendPacket(all, packet); //Send animation packet
            });
            sleepingPlayers.remove(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if player is lying
     *
     * @param player
     * @return if player is lying
     */
    @Override
    public boolean isLying(@NotNull Player player) {
        return getSleepingPlayers().contains(player);
    }

    /**
     * @param loc from where to search
     * @return the first block that is completly hidden from other players
     */
    private Location findNextHiddenBlock(Location loc) {
        Location block = loc.clone().add(0, -1, 0);

        while (!isHidden(block) && block.getBlockY() > 0) {
            block = block.getBlock().getRelative(BlockFace.DOWN).getLocation();
        }

        return block;
    }

    /**
     * @param location that has to be check if completly hidden
     * @return if the location is hidden
     */
    private boolean isHidden(Location location) {
        BlockFace[] faces = new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

        for (BlockFace face : faces) {
            if (!location.getBlock().getRelative(face).getType().isOccluding()) return false;
        }

        return true;
    }

}