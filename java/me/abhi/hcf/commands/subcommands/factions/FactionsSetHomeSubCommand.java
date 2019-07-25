package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionMember;
import me.abhi.hcf.faction.FactionRole;
import me.abhi.hcf.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsSetHomeSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsSetHomeSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] ars) {
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (!playerData.hasFaction()) {
            sender.sendMessage(Messages.NOT_IN_FACTION.getMessage());
            return true;
        }
        Faction faction = playerData.getFaction();
        FactionMember factionMember = faction.getFactionMember(player.getUniqueId());
        if (!factionMember.getFactionRole().equals(FactionRole.LEADER) && !factionMember.getFactionRole().equals(FactionRole.CAPTAIN)) {
            sender.sendMessage(Messages.ALREADY_CAPTAIN.getMessage());
            return true;
        }
        if (faction.getClaim() == null || !faction.getClaim().contains(player.getLocation())) {
            sender.sendMessage(Messages.HOME_MUST_BE_IN_CLAIM.getMessage());
            return true;
        }
        faction.setHome(player.getLocation());
        sender.sendMessage(Messages.FACTION_HOME_SET.getMessage());
        return true;
    }
}
