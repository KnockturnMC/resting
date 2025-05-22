package com.danichef.rest.player.corpses;

import com.danichef.rest.corpses.PlayerCorpses;
import com.danichef.rest.events.PlayerCorpseEvent;
import com.danichef.rest.utils.ReflectionUtil;
import com.danichef.rest.utils.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class PlayerCorpsesImpl implements PlayerCorpses {

    private static final EntityDataAccessor<Optional<BlockPos>> BED_POSITION_ACCESSOR;
    private static final EntityDataAccessor<Pose> POSE_ACCESSOR;

    static {
        try {
            BED_POSITION_ACCESSOR = (EntityDataAccessor<Optional<BlockPos>>) ReflectionUtil.getter(
              LivingEntity.class.getDeclaredField("SLEEPING_POS_ID")
            ).invoke();

            POSE_ACCESSOR = (EntityDataAccessor<Pose>) ReflectionUtil.getter(
              Entity.class.getDeclaredField("DATA_POSE")
            ).invoke();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to find entity data accessors required for PlayerCorpsesImpl", e);
        }
    }

    private final List<UUID> sleepingPlayers = new ArrayList<>();

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
        final PlayerCorpseEvent corpseEvent = new PlayerCorpseEvent(player, true);
        Bukkit.getPluginManager().callEvent(corpseEvent);
        if (corpseEvent.isCancelled()) return;

        try {
            List<Player> send = Arrays.asList(sendTo);
            Location playerLocation = player.getLocation().clone();
            Location hiddenBedBlock = findNextHiddenBlock(playerLocation);

            final BlockPos block = CraftLocation.toBlockPosition(hiddenBedBlock);
            final ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(
              ((CraftPlayer) player).getHandle().getId(),
              List.of(
                SynchedEntityData.DataValue.create(BED_POSITION_ACCESSOR, Optional.of(block)),
                SynchedEntityData.DataValue.create(POSE_ACCESSOR, Pose.SLEEPING)
              )
            );

            send.forEach(all -> {
                all.sendBlockChange(hiddenBedBlock, Material.RED_BED.createBlockData(b -> {
                    final Bed bed = (Bed) b;
                    bed.setFacing(Util.yawToFace(player));
                }));
                ((CraftPlayer) all).getHandle().connection.send(packet);
            });

            player.teleport(playerLocation);
            sleepingPlayers.add(player.getUniqueId());
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
        final PlayerCorpseEvent corpseEvent = new PlayerCorpseEvent(player, false);
        Bukkit.getPluginManager().callEvent(corpseEvent);
        if (corpseEvent.isCancelled()) return;

        try {
            List<Player> send = Arrays.asList(sendTo);
            Location playerLocation = player.getLocation().clone();
            Location hiddenBedBlock = findNextHiddenBlock(playerLocation);

            final ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(
              ((CraftPlayer) player).getHandle().getId(),
              List.of(
                SynchedEntityData.DataValue.create(BED_POSITION_ACCESSOR, Optional.empty()),
                SynchedEntityData.DataValue.create(POSE_ACCESSOR, Pose.STANDING)
              )
            );

            send.forEach(all -> ((CraftPlayer) all).getHandle().connection.send(packet));

            hiddenBedBlock.getBlock().getState().update(); //Update fake block
            sleepingPlayers.remove(player.getUniqueId());
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
        return this.sleepingPlayers.contains(player.getUniqueId());
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
