package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FactionsToggleSafeSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsToggleSafeSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("hcf.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " togglesafe <faction>");
            return true;
        }
        String name = args[1];
        if (!this.plugin.getManagerHandler().getFactionsManager().factionExists(name)) {
            sender.sendMessage(Messages.FACTION_DOESNT_EXIST.getMessage());
            return true;
        }
        Faction faction = this.plugin.getManagerHandler().getFactionsManager().getFaction(name);
        if (!faction.isSystem()) {
            sender.sendMessage(Messages.NOT_SYSTEM_FACTION.getMessage());
            return true;
        }
        faction.setSafe(faction.isSafe() ? false : true);
        sender.sendMessage(Messages.FACTION_UPDATED.getMessage());
        return true;
    }
}
