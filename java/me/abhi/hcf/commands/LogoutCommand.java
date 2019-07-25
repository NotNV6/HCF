package me.abhi.hcf.commands;

import me.abhi.hcf.HCF;
import me.abhi.hcf.cooldown.Cooldown;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LogoutCommand implements CommandExecutor {

    private HCF plugin;

    public LogoutCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (playerData.hasCooldown("combat")) {
            sender.sendMessage(Messages.IN_COMBAT.getMessage());
            return true;
        }
        if (playerData.hasCooldown("logout")) {
            sender.sendMessage(Messages.ALREADY_HAVE_TIMER.getMessage());
            return true;
        }
        new Cooldown(this.plugin, playerData, "logout", 30);
        return true;
    }
}
