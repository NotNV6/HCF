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

public class FactionsNewLeaderSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsNewLeaderSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " newleader <player>");
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
        OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(args[1]);
        if (!faction.isFactionMember(offlinePlayer.getUniqueId())) {
            sender.sendMessage(Messages.PLAYER_NOT_IN_YOUR_FACTION.getMessage());
            return true;
        }
        FactionMember targetMember = faction.getFactionMember(offlinePlayer.getUniqueId());
        targetMember.setFactionRole(FactionRole.LEADER);
        factionMember.setFactionRole(FactionRole.MEMBER);
        sender.sendMessage(Messages.PROMOTED_PLAYER.getMessage().replace("%player%", offlinePlayer.getName()).replace("%role%", "Leader"));
        if (offlinePlayer.isOnline()) {
            Player target = this.plugin.getServer().getPlayer(offlinePlayer.getUniqueId());
            target.sendMessage(Messages.PROMOTED.getMessage().replace("%role%", "Leader"));
        }
        return true;
    }
}
