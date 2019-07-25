package me.abhi.hcf.commands;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModModeCommand implements CommandExecutor {

    private HCF plugin;

    public ModModeCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!sender.hasPermission("hcf.command.modmode")) {
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (playerData.isStaffMode()) {
            playerData.setStaffMode(false);
        } else {
            playerData.setStaffMode(true);
        }
        sender.sendMessage(Messages.MOD_MODE_STATUS.getMessage().replace("%status%", (playerData.isStaffMode() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled")));
        return true;
    }
}
