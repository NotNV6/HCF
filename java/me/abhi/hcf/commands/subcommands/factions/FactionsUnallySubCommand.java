package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionMember;
import me.abhi.hcf.faction.FactionRole;
import me.abhi.hcf.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsUnallySubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsUnallySubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " unally <faction>");
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (!playerData.hasFaction()) {
            sender.sendMessage(Messages.NOT_IN_FACTION.getMessage());
            return true;
        }
        Faction faction = playerData.getFaction();
        FactionMember factionMember = faction.getFactionMember(player.getUniqueId());
        if (!factionMember.getFactionRole().equals(FactionRole.LEADER)) {
            sender.sendMessage(Messages.MUST_BE_LEADER.getMessage());
            return true;
        }
        String name = args[1];
        if (!this.plugin.getManagerHandler().getFactionsManager().factionExists(name)) {
            sender.sendMessage(Messages.FACTION_DOESNT_EXIST.getMessage());
            return true;
        }
        Faction ally = this.plugin.getManagerHandler().getFactionsManager().getFaction(name);
        if (!faction.getAllies().contains(ally)) {
            sender.sendMessage(Messages.NOT_ALLIED.getMessage());
            return true;
        }
        ally.getAllies().remove(faction);
        faction.getAllies().remove(ally);
        for (FactionMember factionMembers : faction.getMembers()) {
            Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
            if (members != null) {
                members.sendMessage(Messages.NO_LONGER_ALLIED.getMessage().replace("%faction%", ally.getName()));
            }
        }
        for (FactionMember factionMembers : ally.getMembers()) {
            Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
            if (members != null) {
                members.sendMessage(Messages.NO_LONGER_ALLIED.getMessage().replace("%faction%", faction.getName()));
            }
        }
        return true;
    }
}
