package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionInvite;
import me.abhi.hcf.faction.FactionMember;
import me.abhi.hcf.faction.FactionRole;
import me.abhi.hcf.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsUninviteSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsUninviteSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " uninvite <player>");
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (!playerData.hasFaction()) {
            sender.sendMessage(Messages.NOT_IN_FACTION.getMessage());
            return true;
        }
        Faction faction = playerData.getFaction();
        Player target = this.plugin.getServer().getPlayer(args[1]);
        if (target == null || target == player) {
            sender.sendMessage(Messages.COULD_NOT_FIND_PLAYER.getMessage());
            return true;
        }
        FactionMember factionMember = faction.getFactionMember(player.getUniqueId());
        if (!factionMember.getFactionRole().equals(FactionRole.LEADER) && !factionMember.getFactionRole().equals(FactionRole.CAPTAIN)) {
            sender.sendMessage(Messages.MUST_BE_CAPTAIN.getMessage());
            return true;
        }
        PlayerData targetData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(target);
        if (!targetData.hasInvite(faction)) {
            sender.sendMessage(Messages.PLAYER_DOESNT_HAVE_INVITE.getMessage());
            return true;
        }
        FactionInvite factionInvite = targetData.getFactionInvite(faction);
        targetData.getFactionInviteList().remove(factionInvite);
        for (FactionMember factionMembers : faction.getMembers()) {
            Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
            if (members != null) {
                members.sendMessage(Messages.INVITE_CANCELLED.getMessage().replace("%player%", target.getName()));
            }
        }
        return true;
    }
}
