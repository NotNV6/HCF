package me.abhi.hcf.commands.subcommands.pvp;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.cooldown.Cooldown;
import me.abhi.hcf.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvPTimeSubCommand implements CommandExecutor {

    private HCF plugin;

    public PvPTimeSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (!playerData.hasCooldown("pvp-timer")) {
            sender.sendMessage(Messages.NO_PVP_TIMER.getMessage());
            return true;
        }
        Cooldown pvpTimer = playerData.getCooldown("pvp-timer");
        sender.sendMessage(Messages.PVP_TIME.getMessage().replace("%time%", pvpTimer.getTime()));
        return true;
    }
}
