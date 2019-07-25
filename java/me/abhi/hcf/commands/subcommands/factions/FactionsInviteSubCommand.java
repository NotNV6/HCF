package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionInvite;
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

public class FactionsInviteSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsInviteSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " invite <player>");
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
        Player target = this.plugin.getServer().getPlayer(args[1]);
        if (target == null || target == player) {
            sender.sendMessage(Messages.COULD_NOT_FIND_PLAYER.getMessage());
            return true;
        }
        PlayerData targetData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(target);
        if (targetData.getFaction() != null && targetData.getFaction().equals(faction)) {
            sender.sendMessage(Messages.PLAYER_IN_YOUR_FACTION.getMessage());
            return true;
        }
        if (targetData.hasInvite(faction)) {
            sender.sendMessage(Messages.PLAYER_ALREADY_HAS_INVITE.getMessage());
            return true;
        }
        FactionInvite factionInvite = new FactionInvite(faction);
        targetData.getFactionInviteList().add(factionInvite);
        for (FactionMember factionMembers : faction.getMembers()) {
            Player member = this.plugin.getServer().getPlayer(factionMembers.getUuid());
            if (member != null) {
                member.sendMessage(Messages.PLAYER_INVITED_TO_FACTION.getMessage().replace("%player%", target.getName()));
            }
        }
        TextComponent textComponent = new TextComponent(Messages.INVITE_FROM_FACTION.getMessage().replace("%player%", sender.getName()).replace("%faction%", faction.getName()));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to accept invitation.").create()));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f join " + player.getName()));
        target.spigot().sendMessage(textComponent);
        return true;
    }

}
