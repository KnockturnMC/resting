package com.danichef.rest.commands;


import com.danichef.rest.corpses.PlayerCorpses;
import com.danichef.rest.seats.PlayerSit;
import com.danichef.rest.utils.MessagesUtil;
import com.danichef.rest.utils.PermissionsUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {

    private final PlayerSit playerSit;
    private final PlayerCorpses playerCorpses;

    public AdminCommand(final PlayerSit playerSit, final PlayerCorpses playerCorpses) {
        this.playerSit = playerSit;
        this.playerCorpses = playerCorpses;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This plugin is for players only!");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(PermissionsUtil.ADMIN)) {
            player.sendMessage(MessagesUtil.NO_PERMISSIONS);
            return false;
        }

        if ((args.length == 1) && (args[0].equalsIgnoreCase("cleanse"))) {
            player.getWorld().getEntitiesByClass(ArmorStand.class).stream()
              .filter(playerSit::isSeat)
              .forEach(Entity::remove);

        } else if ((args.length == 2) && (Bukkit.getPlayer(args[1]) != null)) {
            Player target = Bukkit.getPlayer(args[1]);
            if (args[0].equalsIgnoreCase("sit")) {
                boolean isSitting = playerSit.sit(target);
                if (!isSitting) return false;
                target.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessagesUtil.SITTING));

            } else if (args[0].equalsIgnoreCase("lay")) {
                if (playerCorpses.isLying(target)) {
                    playerCorpses.wakeUp(target);
                } else {
                    playerCorpses.putToSleep(target);
                    target.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessagesUtil.LAYING));
                    target.sendMessage(MessagesUtil.STAND_UP);
                }

            } else if (args[0].equalsIgnoreCase("stand")) {
                if ((target.getVehicle() != null) && (playerSit.isSeat(target.getVehicle()))) {
                    target.getVehicle().remove();
                }

            } else {
                player.sendMessage(MessagesUtil.COMMAND);
                return false;
            }

        } else {
            player.sendMessage(MessagesUtil.COMMAND);
            return false;
        }
        return true;
    }
}
