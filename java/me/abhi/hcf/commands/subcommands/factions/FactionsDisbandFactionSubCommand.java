package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionMember;
import me.abhi.hcf.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsDisbandFactionSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsDisbandFactionSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("hcf.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " disbandfaction <faction>");
            return true;
        }
        String name = args[1];
        if (!this.plugin.getManagerHandler().getFactionsManager().factionExists(name)) {
            sender.sendMessage(Messages.FACTION_DOESNT_EXIST.getMessage());
            return true;
        }
        Faction faction = this.plugin.getManagerHandler().getFactionsManager().getFaction(name);
        if (!faction.isSystem()) {
            for (int i = 0; i < faction.getAllies().size(); i++) {
                Faction allies = faction.getAllies().get(i);
                allies.getAllies().remove(faction);
                faction.getAllies().remove(allies);
            }
            for (FactionMember factionMembers : faction.getMembers()) {
                Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
                if (members != null) {
                    PlayerData memberData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(members);
                    memberData.setFaction(null);
                }
            }
        }
        this.plugin.getManagerHandler().getFactionsManager().removeFaction(faction);
        sender.sendMessage(Messages.DISBANDED_FACTION.getMessage().replace("%faction%", faction.getName()));
        return true;
    }
}
