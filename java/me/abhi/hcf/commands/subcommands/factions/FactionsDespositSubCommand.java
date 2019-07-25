package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.util.Messages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsDespositSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsDespositSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " deposit <amount>");
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (!playerData.hasFaction()) {
            sender.sendMessage(Messages.NOT_IN_FACTION.getMessage());
            return true;
        }
        int amount = 0;
        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("a")) {
            amount = playerData.getBalance();
        } else if (!NumberUtils.isDigits(args[1])) {
            sender.sendMessage(Messages.INVALID_AMOUNT.getMessage());
        } else {
            amount = Integer.parseInt(args[1]);
        }
        if (amount <= 0) {
            sender.sendMessage(Messages.AMOUNT_CANT_BE_ZERO.getMessage());
            return true;
        }
        if (amount > playerData.getBalance()) {
            sender.sendMessage(Messages.INSUFFICIENT_FUNDS.getMessage());
            return true;
        }
        playerData.setBalance(playerData.getBalance() - amount);
        Faction faction = playerData.getFaction();
        faction.setBalance(faction.getBalance() + amount);
        sender.sendMessage(Messages.DEPOSITED_AMOUNT.getMessage().replace("%amount%", String.valueOf(amount)));
        return true;
    }
}
