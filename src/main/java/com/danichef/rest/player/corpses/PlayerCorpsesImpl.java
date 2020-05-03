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

import java.util.*;

public class PlayerCorpsesImpl implements PlayerCorpses {

    @Getter
    private List<Player> sleepingPlayers = new ArrayList<>();

    private static final Object ENTITY_LIVING_BED_POSITION_DATA_WATCHER_OBJECT = ReflectionUtil.getDeclaredField("bq",
            ReflectionUtil.getNmsClass("EntityLiving"), null);
    private static final Class<?> CLASS_DATA_WATCHER_ITEM = ReflectionUtil.getNmsClass("DataWatcher$Item"),
            CLASS_DATA_WATCHER_OBJECT = ReflectionUtil.getNmsClass("DataWatcherObject"),
            CLASS_PACKET_PLAY_OUT_ENTITY_META_DATA = ReflectionUtil.getNmsClass("PacketPlayOutEntityMetadata");

    /**
     * @param player to put to sleep
     */
    @Override
    public void putToSleep(Player player) {
        putToSleep(player, player.getWorld().getPlayers().toArray(new Player[0]));
    }

    /**
     * @param player to put to sleep
     * @param sendTo the players that will recieve the sleep packets
     */
    @Override
    public void putToSleep(Player player, Player... sendTo) {
        try {
            List<Player> send = Arrays.asList(sendTo);
            Location playerLocation = player.getLocation().clone();
            Location hiddenBedBlock = findNextHiddenBlock(playerLocation);

            Optional<Object> block = Optional.of(ReflectionUtil.getBlockPosition(hiddenBedBlock)); //The BlockPosition Instance
            Object item = CLASS_DATA_WATCHER_ITEM.getDeclaredConstructor(CLASS_DATA_WATCHER_OBJECT, Object.class)
                    .newInstance(ENTITY_LIVING_BED_POSITION_DATA_WATCHER_OBJECT, block);

            Object packet = new ReflectedObject(CLASS_PACKET_PLAY_OUT_ENTITY_META_DATA.getDeclaredConstructor().newInstance())
                    .with("a", ReflectionUtil.getID(player))
                    .with("b", Collections.singletonList(item))
                    .get();

            send.forEach(all -> {
                all.sendBlockChange(hiddenBedBlock, Material.RED_BED.createBlockData(b -> {
                    final Bed bed = (Bed) b;
                    ((Bed) b).setFacing(Util.yawToFace(player));
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
    public void wakeUp(Player player) {
        wakeUp(player, player.getWorld().getPlayers().toArray(new Player[0]));
    }

    /**
     * @param player to put to sleep
     * @param sendTo the players that will recieve the wake-up packets
     */
    @Override
    public void wakeUp(Player player, Player... sendTo) {
        try {
            List<Player> send = Arrays.asList(sendTo);
            Location playerLocation = player.getLocation().clone();
            Location hiddenBedBlock = findNextHiddenBlock(playerLocation);

            Object item = CLASS_DATA_WATCHER_ITEM.getDeclaredConstructor(CLASS_DATA_WATCHER_OBJECT, Object.class)
                    .newInstance(ENTITY_LIVING_BED_POSITION_DATA_WATCHER_OBJECT, Optional.empty());

            Object packet = new ReflectedObject(CLASS_PACKET_PLAY_OUT_ENTITY_META_DATA.getDeclaredConstructor().newInstance())
                    .with("a", ReflectionUtil.getID(player))
                    .with("b", Collections.singletonList(item))
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
    public boolean isLying(Player player) {
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