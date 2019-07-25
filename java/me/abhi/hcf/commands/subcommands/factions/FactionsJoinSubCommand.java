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

public class FactionsJoinSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsJoinSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " join <player>");
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (playerData.hasFaction()) {
            sender.sendMessage(Messages.ALREADY_IN_FACTION.getMessage());
            return true;
        }
        Player target = this.plugin.getServer().getPlayer(args[1]);
        if (target == null || target == player) {
            sender.sendMessage(Messages.COULD_NOT_FIND_PLAYER.getMessage());
            return true;
        }
        PlayerData targetData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(target);
        if (!targetData.hasFaction()) {
            sender.sendMessage(Messages.PLAYER_NOT_IN_YOUR_FACTION.getMessage());
            return true;
        }
        Faction faction = targetData.getFaction();
        if (!playerData.hasInvite(faction)) {
            sender.sendMessage(Messages.NO_INVITE_FOUND.getMessage());
            return true;
        }
        if (playerData.hasCooldown("combat")) {
            sender.sendMessage(Messages.IN_COMBAT.getMessage());
            return true;
        }
        faction.addFactionMember(new FactionMember(player.getUniqueId(), FactionRole.MEMBER));
        playerData.setFaction(faction);
        playerData.getFactionInviteList().remove(playerData.getFactionInvite(faction));
        for (FactionMember factionMembers : faction.getMembers()) {
            Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
            if (members != null) {
                members.sendMessage(Messages.PLAYER_JOINED_FACTION.getMessage().replace("%player%", sender.getName()));
            }
        }
        return true;
    }
}
