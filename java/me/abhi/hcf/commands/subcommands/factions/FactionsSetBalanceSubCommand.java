package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.util.Messages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FactionsSetBalanceSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsSetBalanceSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("hcf.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " setbalance <faction> <amount>");
            return true;
        }
        String name = args[1];
        if (!this.plugin.getManagerHandler().getFactionsManager().factionExists(name)) {
            sender.sendMessage(Messages.FACTION_DOESNT_EXIST.getMessage());
            return true;
        }
        Faction faction = this.plugin.getManagerHandler().getFactionsManager().getFaction(name);
        if (faction.isSystem()) {
            sender.sendMessage(Messages.SYSTEM_FACTION.getMessage());
            return true;
        }
        if (!NumberUtils.isDigits(args[2])) {
            sender.sendMessage(Messages.INVALID_AMOUNT.getMessage());
            return true;
        }
        int amount = Integer.parseInt(args[2]);
        faction.setBalance(amount);
        sender.sendMessage(Messages.FACTION_BALANCE_UPDATED.getMessage());
        return true;
    }
}
