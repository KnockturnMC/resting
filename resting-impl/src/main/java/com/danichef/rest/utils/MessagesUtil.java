package com.danichef.rest.utils;

import org.bukkit.ChatColor;

public class MessagesUtil {

    public static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "SimpleRest" + ChatColor.DARK_GRAY + "] ";

    public static final String AS_KEY = "seat";

    public static final String NO_GROUND = ChatColor.AQUA + "Must be on the floor to rest!";
    public static final String SITTING = ChatColor.AQUA + "You are now sitting";
    public static final String LAYING = ChatColor.AQUA + "You are now laying";
    public static final String STAND_UP = PREFIX + ChatColor.AQUA + "Do /lay or /unlay to stand up!";

    public static final String COMMAND = PREFIX + ChatColor.AQUA + "/rest (cleanse/sit/lay/stand) [Player]";
    public static final String NO_PERMISSIONS = PREFIX + ChatColor.AQUA + "Woah! Stop there big fellah! Not enough permissions.";
    public static final String SIT_FAILED = PREFIX + ChatColor.RED + "Failed to sit.";

}
