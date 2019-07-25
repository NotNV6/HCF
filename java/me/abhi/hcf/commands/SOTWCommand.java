package me.abhi.hcf.commands;

import me.abhi.hcf.HCF;
import me.abhi.hcf.cooldown.ServerCooldown;
import me.abhi.hcf.util.Messages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SOTWCommand implements CommandExecutor {

    private HCF plugin;

    public SOTWCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("hcf.command.sotw")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <time>");
            return true;
        }
        if (!NumberUtils.isDigits(args[0])) {
            sender.sendMessage(Messages.INVALID_AMOUNT.getMessage());
            return true;
        }
        int time = Integer.parseInt(args[0]);
        if (this.plugin.getManagerHandler().getServerManager().hasCooldown("sotw")) {
            this.plugin.getManagerHandler().getServerManager().getCooldown("sotw").setTime(time);
        } else {
            new ServerCooldown(this.plugin, "sotw", time);
        }
        ServerCooldown sotw = this.plugin.getManagerHandler().getServerManager().getCooldown("sotw");
        this.plugin.getServer().broadcastMessage(Messages.SOTW_STARTED.getMessage().replace("%time%", sotw.getTime()));
        return true;
    }
}
