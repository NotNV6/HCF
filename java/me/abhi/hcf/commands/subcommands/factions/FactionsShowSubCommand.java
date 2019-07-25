package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionMember;
import me.abhi.hcf.faction.FactionRole;
import me.abhi.hcf.util.Messages;
import me.abhi.hcf.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FactionsShowSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsShowSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            Player player = (Player) sender;
            PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
            if (!playerData.hasFaction()) {
                sender.sendMessage(Messages.NOT_IN_FACTION.getMessage());
                return true;
            }
            describedFaction(playerData.getFaction(), sender);
            return true;
        }
        String name = args[1];
        if (!this.plugin.getManagerHandler().getFactionsManager().factionExists(name)) {
            sender.sendMessage(Messages.FACTION_DOESNT_EXIST.getMessage());
            return true;
        }
        Faction faction = this.plugin.getManagerHandler().getFactionsManager().getFaction(name);
        describedFaction(faction, sender);
        return true;
    }

    private void describedFaction(Faction faction, CommandSender sender) {
        String home = (faction.getHome() != null ? (int) faction.getHome().getX() + ", " + (int) faction.getHome().getZ() : "None");
        String leader = "";
        int onlinePlayers = 0;
        List<String> captainNames = new ArrayList<String>();
        List<String> memberNames = new ArrayList<String>();
        if (!faction.isSystem()) {
            for (FactionMember factionMember : faction.getMembers()) {
                OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(factionMember.getUuid());
                String name = (offlinePlayer.isOnline() ? ChatColor.GREEN : ChatColor.GRAY) + offlinePlayer.getName();
                if (offlinePlayer.isOnline()) {
                    onlinePlayers += 1;
                }
                if (factionMember.getFactionRole().equals(FactionRole.CAPTAIN)) {
                    captainNames.add(name);
                } else if (factionMember.getFactionRole().equals(FactionRole.MEMBER)) {
                    memberNames.add(name);
                } else {
                    leader = name;
                }
            }
        }
        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------------");
        if (!faction.isSystem()) {
            sender.sendMessage(ChatColor.BLUE + faction.getName() + ChatColor.GRAY + " [" + onlinePlayers + "/" + faction.getMembers().size() + "]" + ChatColor.DARK_AQUA + " - " + ChatColor.YELLOW + "Home: " + ChatColor.WHITE + home);
            sender.sendMessage(ChatColor.YELLOW + "Leader: " + leader);
            if (captainNames.size() != 0) {
                sender.sendMessage(ChatColor.YELLOW + "Captains: " + Strings.join(captainNames, ", "));
            }
            if (memberNames.size() != 0) {
                sender.sendMessage(ChatColor.YELLOW + "Members: " + Strings.join(memberNames, ", "));
            }
            sender.sendMessage(ChatColor.YELLOW + "Balance: " + ChatColor.BLUE + "$" + faction.getBalance());
            sender.sendMessage(ChatColor.YELLOW + "Deaths Until Raidable: " + StringUtil.formattedDtr(faction) + ChatColor.GRAY + "/" + ChatColor.GREEN + (faction.getMembers().size() < 5 ? (faction.getMembers().size() + 0.1) : 5.1));
        } else {
            sender.sendMessage(ChatColor.BLUE + faction.getName() + ChatColor.DARK_AQUA + " - " + ChatColor.YELLOW + "Home: " + ChatColor.WHITE + home);
            sender.sendMessage(ChatColor.GRAY + "This faction is managed by the server.");
        }
        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------------");
    }
}
