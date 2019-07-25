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

public class FactionsLeaveSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsLeaveSubCommand(HCF plugin) {
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
        if (factionMember.getFactionRole().equals(FactionRole.LEADER)) {
            sender.sendMessage(Messages.LEADER_CANT_LEAVE.getMessage());
            return true;
        }
        if (playerData.hasCooldown("combat")) {
            sender.sendMessage(Messages.IN_COMBAT.getMessage());
            return true;
        }
        playerData.setFaction(null);
        faction.removeFactionMember(factionMember);
        for (FactionMember factionMembers : faction.getMembers()) {
            Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
            if (members != null) {
                members.sendMessage(Messages.PLAYER_LEFT_FACTION.getMessage().replace("%player%", player.getName()));
            }
            sender.sendMessage(Messages.LEFT_FACTION.getMessage());
            return true;
        }
        return true;
    }
}
