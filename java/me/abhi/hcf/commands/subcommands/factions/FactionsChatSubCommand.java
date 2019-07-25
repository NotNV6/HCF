package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.chatmode.ChatMode;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsChatSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsChatSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (!playerData.hasFaction()) {
            playerData.setChatMode(ChatMode.GLOBAL);
            sender.sendMessage(Messages.NOT_IN_FACTION.getMessage());
            return true;
        }
        switch (playerData.getChatMode()) {
            case FACTION: {
                playerData.setChatMode(ChatMode.GLOBAL);
                sender.sendMessage(Messages.SWITCHED_TO_GLOBAL_CHAT.getMessage());
                break;
            }
            case ALLY: {
                playerData.setChatMode(ChatMode.FACTION);
                sender.sendMessage(Messages.SWITCHED_TO_FACTION_CHAT.getMessage());
                break;
            }
            case GLOBAL: {
                playerData.setChatMode(ChatMode.ALLY);
                sender.sendMessage(Messages.SWITCHED_TO_ALLY_CHAT.getMessage());
                break;
            }
        }
        return true;
    }
}
