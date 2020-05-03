package com.danichef.rest.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ReflectionUtil {

    private static final String version;

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        version = packageName.substring(packageName.lastIndexOf(".") + 1);
    }

    public static Class<?> getNmsClass(@NotNull String className) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Could not find NMS class: %s", className), e);
        }
    }

    public static Class<?> getCraftBukkitClass(@NotNull String className) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Could not find CB class: %s", className), e);
        }
    }

    public static Object getDeclaredField(String fieldName, Class<?> fieldClazz, Object clazzInstance) {
        try {
            Field declaredField = fieldClazz.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return declaredField.get(clazzInstance);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(String.format("Could not access declared field %s in class %s", fieldName
                    , fieldClazz.getName()), e);
        }
    }

    public static Object getBlockPosition(Location loc) {
        try {
            return getNmsClass("BlockPosition")
                    .getConstructor(int.class, int.class, int.class)
                    .newInstance(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(String.format("Could not create block position object for %s",
                    loc.toString()), e);
        }
    }

    public static int getID(Entity entity) {
        try {
            final Object nmsEntity = getNMSEntity(entity);
            return (int) getNmsClass("Entity").getMethod("getId").invoke(nmsEntity);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Could not fetch entity id from server", e);
        }
    }

    public static void sendPacket(Player p, Object packet) {
        try {
            Object nmsPlayer = getNMSEntity(p);

            Field fieldCon = Objects.requireNonNull(nmsPlayer).getClass().getField("playerConnection");
            fieldCon.setAccessible(true);
            Object nmsCon = fieldCon.get(nmsPlayer);

            Method sendPacket = nmsCon.getClass().getMethod("sendPacket", getNmsClass("Packet"));
            sendPacket.invoke(nmsCon, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getNMSEntity(Entity entity) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return getCraftBukkitClass("entity.CraftEntity").getMethod("getHandle").invoke(entity);
    }

}
