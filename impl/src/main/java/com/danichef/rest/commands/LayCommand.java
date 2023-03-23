package com.danichef.rest.commands;

import com.danichef.rest.corpses.PlayerCorpses;
import com.danichef.rest.player.PlayerManager;
import com.danichef.rest.utils.MessagesUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LayCommand implements CommandExecutor {

    private final PlayerCorpses playerCorpses;
    private final PlayerManager playerManager;

    public LayCommand(final PlayerCorpses playerCorpses, final PlayerManager playerManager) {
        this.playerCorpses = playerCorpses;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This plugin is for players only!");
            return false;
        }

        Player player = (Player) sender;

        if (playerCorpses.isLying(player)) {
            playerCorpses.wakeUp(player);
        } else {
            if (!playerManager.canRest(player)) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessagesUtil.NO_GROUND));
                return false;
            }
            playerCorpses.putToSleep(player);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessagesUtil.LAYING));
            player.sendMessage(MessagesUtil.STAND_UP);
        }
        return true;
    }
}
