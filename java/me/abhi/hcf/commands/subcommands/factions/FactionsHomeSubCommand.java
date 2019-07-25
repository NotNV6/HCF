package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.cooldown.Cooldown;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsHomeSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsHomeSubCommand(HCF plugin) {
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
        if (faction.getHome() == null) {
            sender.sendMessage(Messages.NO_FACTION_HOME.getMessage());
            return true;
        }
        if (playerData.hasCooldown("combat")) {
            sender.sendMessage(Messages.IN_COMBAT.getMessage());
            return true;
        }
        if (playerData.hasCooldown("home")) {
            sender.sendMessage(Messages.ALREADY_HAVE_TIMER.getMessage());
            return true;
        }
        if (playerData.getClaimFac() != null && !playerData.getClaim().equalsIgnoreCase(faction.getName()) && !playerData.getClaimFac().isSafe()) {
            new Cooldown(this.plugin, playerData, "home", 30);
        } else {
            new Cooldown(this.plugin, playerData, "home", 10);
        }
        sender.sendMessage(Messages.TELEPORT_TIMER_STARTED.getMessage());
        return true;
    }
}
