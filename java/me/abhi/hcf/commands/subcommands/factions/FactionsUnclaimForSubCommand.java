package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FactionsUnclaimForSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsUnclaimForSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("hcf.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " unclaimfor <faction>");
            return true;
        }
        String name = args[1];
        if (!this.plugin.getManagerHandler().getFactionsManager().factionExists(name)) {
            sender.sendMessage(Messages.NOT_IN_FACTION.getMessage());
            return true;
        }
        Faction faction = this.plugin.getManagerHandler().getFactionsManager().getFaction(name);
        if (faction.getClaim() == null) {
            sender.sendMessage(Messages.NO_CLAIM.getMessage());
            return true;
        }
        faction.setClaim(null);
        if (!faction.isSystem()) {
            faction.setBalance(faction.getBalance() + faction.getClaimCost());
            faction.setClaimCost(0);
        }
        sender.sendMessage(Messages.LAND_UNCLAIMED.getMessage().replace("%price%", String.valueOf(faction.getClaimCost())));
        return true;
    }
}
