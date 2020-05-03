package com.danichef.rest.utils;

import org.bukkit.ChatColor;

public class MessagesUtil {

    public static final String PREFIX = ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "SimpleRest" + ChatColor.DARK_GRAY + "» ";

    public static final String AS_NAME = "Seat";

    public static final String NO_GROUND = ChatColor.AQUA + "Must be on the floor to rest!";
    public static final String SITTING = ChatColor.AQUA + "You are now sitting";
    public static final String LAYING = ChatColor.AQUA + "You are now laying";
    public static final String STAND_UP = PREFIX + ChatColor.AQUA + "Do /lay or /unlay to stand up!";

    public static final String COMMAND = PREFIX + ChatColor.AQUA + "/rest (cleanse/sit/lay/stand) [Player]";
    public static final String NO_PERMISSIONS = PREFIX + ChatColor.AQUA + "Woah! Stop there big fellah! Not enough permissions.";

}
