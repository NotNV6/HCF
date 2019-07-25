package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionMember;
import me.abhi.hcf.faction.FactionRole;
import me.abhi.hcf.util.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsCreateSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsCreateSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " create <name>");
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (playerData.hasFaction()) {
            sender.sendMessage(Messages.ALREADY_IN_FACTION.getMessage());
            return true;
        }
        String name = args[1];
        if (name.length() < 3 || name.length() > 10 || !StringUtils.isAlpha(name)) {
            sender.sendMessage(Messages.FACTION_NAME_REQUIREMENT.getMessage());
            return true;
        }
        if (this.plugin.getManagerHandler().getFactionsManager().factionExists(name)) {
            sender.sendMessage(Messages.FACTION_ALREADY_EXISTS.getMessage());
            return true;
        }
        Faction faction = new Faction(name);
        FactionMember factionMember = new FactionMember(playerData.getUuid(), FactionRole.LEADER);
        faction.addFactionMember(factionMember);
        playerData.setFaction(faction);
        this.plugin.getManagerHandler().getFactionsManager().addFaction(faction);
        this.plugin.getServer().broadcastMessage(Messages.FACTION_CREATED.getMessage().replace("%faction%", name).replace("%player%", sender.getName()));
        return true;
    }
}
