package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionMember;
import me.abhi.hcf.faction.FactionRole;
import me.abhi.hcf.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsKickSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsKickSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " kick <player>");
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
        if (!factionMember.getFactionRole().equals(FactionRole.LEADER) && !factionMember.getFactionRole().equals(FactionRole.CAPTAIN)) {
            sender.sendMessage(Messages.MUST_BE_CAPTAIN.getMessage());
            return true;
        }
        OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(args[1]);
        if (offlinePlayer == null || offlinePlayer.getUniqueId().equals(player.getUniqueId())) {
            sender.sendMessage(Messages.COULD_NOT_FIND_PLAYER.getMessage());
            return true;
        }
        if (!faction.isFactionMember(offlinePlayer.getUniqueId())) {
            sender.sendMessage(Messages.PLAYER_NOT_IN_YOUR_FACTION.getMessage());
            return true;
        }
        FactionMember kickedMember = faction.getFactionMember(offlinePlayer.getUniqueId());
        if (!factionMember.getFactionRole().equals(FactionRole.LEADER) && !kickedMember.getFactionRole().equals(FactionRole.MEMBER)) {
            sender.sendMessage(Messages.CANNOT_KICK_PLAYER.getMessage());
            return true;
        }
        faction.removeFactionMember(kickedMember);
        for (FactionMember factionMembers : faction.getMembers()) {
            Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
            if (members != null) {
                members.sendMessage(Messages.PLAYER_LEFT_FACTION.getMessage().replace("%player%", offlinePlayer.getName()));
            }
            if (offlinePlayer.isOnline()) {
                Player target = this.plugin.getServer().getPlayer(offlinePlayer.getUniqueId());
                PlayerData targetData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(target);
                targetData.setFaction(null);
                target.sendMessage(Messages.LEFT_FACTION.getMessage());
                return true;
            }
        }
        return true;
    }
}
