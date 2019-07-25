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

public class FactionsDisbandSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsDisbandSubCommand(HCF plugin) {
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
        if (!factionMember.getFactionRole().equals(FactionRole.LEADER)) {
            sender.sendMessage(Messages.MUST_BE_LEADER.getMessage());
            return true;
        }
        if (playerData.hasCooldown("combat")) {
            sender.sendMessage(Messages.IN_COMBAT.getMessage());
            return true;
        }
        for (int i = 0; i < faction.getAllies().size(); i++) {
            Faction allies = faction.getAllies().get(i);
            allies.getAllies().remove(faction);
            faction.getAllies().remove(allies);
        }
        for (FactionMember factionMembers : faction.getMembers()) {
            Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
            if (members != null) {
                PlayerData memberData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(members);
                memberData.setFaction(null);
            }
        }
        this.plugin.getManagerHandler().getFactionsManager().removeFaction(faction);
        this.plugin.getServer().broadcastMessage(Messages.FACTION_DISBANDED.getMessage().replace("%faction%", faction.getName()).replace("%player%", player.getName()));
        return true;
    }
}
