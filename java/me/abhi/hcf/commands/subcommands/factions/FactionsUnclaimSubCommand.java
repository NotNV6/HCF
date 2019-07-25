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

public class FactionsUnclaimSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsUnclaimSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (!playerData.hasFaction()) {
            sender.sendMessage(Messages.NOT_IN_FACTION.getMessage());
            return true;
        }
        Faction faction = playerData.getFaction();
        FactionMember factionMember = faction.getFactionMember(player.getUniqueId());
        if (!factionMember.getFactionRole().equals(FactionRole.LEADER) && !factionMember.getFactionRole().equals(FactionRole.CAPTAIN)) {
            sender.sendMessage(Messages.MUST_BE_CAPTAIN.getMessage());
            return true;
        }
        if (faction.getClaim() == null) {
            sender.sendMessage(Messages.NO_CLAIM.getMessage());
            return true;
        }
        faction.setBalance(faction.getBalance() + faction.getClaimCost());
        faction.setClaim(null);
        sender.sendMessage(Messages.LAND_UNCLAIMED.getMessage().replace("%price%", String.valueOf(faction.getClaimCost())));
        faction.setClaimCost(0);
        return true;
    }
}
