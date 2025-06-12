package com.danichef.rest.commands;

import com.danichef.rest.player.PlayerManager;
import com.danichef.rest.seats.PlayerSit;
import com.danichef.rest.utils.MessagesUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SitCommand implements CommandExecutor {

    private final PlayerSit playerSit;
    private final PlayerManager playerManager;

    public SitCommand(final PlayerSit playerSit, final PlayerManager playerManager) {
        this.playerSit = playerSit;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This plugin is for players only!");
            return false;
        }

        Player player = (Player) sender;

        if (!playerManager.canRest(player)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessagesUtil.NO_GROUND));
            return false;
        }

        boolean isSitting = playerSit.sit(player);
        if (!isSitting) return false;
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessagesUtil.SITTING));

        return true;
    }
}
