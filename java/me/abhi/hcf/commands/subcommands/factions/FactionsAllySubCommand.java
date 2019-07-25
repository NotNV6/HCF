package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionAllyInvite;
import me.abhi.hcf.faction.FactionMember;
import me.abhi.hcf.faction.FactionRole;
import me.abhi.hcf.util.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsAllySubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsAllySubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " ally <faction>");
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
        Faction potentialAlly = this.plugin.getManagerHandler().getFactionsManager().getFaction(name);
        if (faction.getAllies().contains(potentialAlly)) {
            sender.sendMessage(Messages.ALREADY_ALLIED.getMessage().replace("%faction%", potentialAlly.getName()));
            return true;
        }
        if (faction.hasFactionAllyInvite(potentialAlly)) {
            faction.removeFactionAllyInvite(faction.getFactionAllyInvite(potentialAlly));
            faction.getAllies().add(potentialAlly);
            potentialAlly.getAllies().add(faction);
            for (FactionMember factionMembers : faction.getMembers()) {
                Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
                members.sendMessage(Messages.ALLIED.getMessage().replace("%faction%", potentialAlly.getName()));
            }
            for (FactionMember factionMembers : potentialAlly.getMembers()) {
                Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
                members.sendMessage(Messages.ALLIED.getMessage().replace("%faction%", faction.getName()));
            }
            return true;
        }
        for (FactionMember factionMembers : potentialAlly.getMembers()) {
            if (factionMembers.getFactionRole().equals(FactionRole.LEADER)) {
                Player leader = this.plugin.getServer().getPlayer(factionMembers.getUuid());
                if (leader == null) {
                    sender.sendMessage(Messages.LEADER_NOT_ONLINE.getMessage());
                    return true;
                }
            }
        }
        potentialAlly.addFactionAllyInvite(new FactionAllyInvite(faction));
        for (FactionMember factionMembers : faction.getMembers()) {
            Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
            members.sendMessage(Messages.SENT_ALLY_INVITE.getMessage().replace("%faction%", potentialAlly.getName()));
        }
        potentialAlly.getMembers().stream().forEach(o -> System.out.println(o.toString()));
        for (FactionMember factionMembers : potentialAlly.getMembers()) {
            Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
            TextComponent textComponent = new TextComponent(Messages.ALLY_INVITE.getMessage().replace("%faction%", faction.getName()));
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to accept invitation.").create()));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f ally " + faction.getName()));
            members.spigot().sendMessage(textComponent);
        }
        return true;
    }
}
