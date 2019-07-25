package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FactionsCreateSystemSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsCreateSystemSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("hcf.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " createsystem <name>");
            return true;
        }
        String name = args[1];
        if (this.plugin.getManagerHandler().getFactionsManager().factionExists(name)) {
            sender.sendMessage(Messages.FACTION_ALREADY_EXISTS.getMessage());
            return true;
        }
        Faction faction = new Faction(name, true);
        this.plugin.getManagerHandler().getFactionsManager().addFaction(faction);
        sender.sendMessage(Messages.SYSTEM_FACTION_CREATED.getMessage().replace("%faction%", name));
        return true;
    }
}
