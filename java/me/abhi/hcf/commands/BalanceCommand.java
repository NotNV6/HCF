package me.abhi.hcf.commands;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    private HCF plugin;

    public BalanceCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length > 0 && sender.hasPermission("hcf.admin")) {
            Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Messages.COULD_NOT_FIND_PLAYER.getMessage());
                return true;
            }
            PlayerData targetData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(target);
            sender.sendMessage(Messages.PLAYERS_BALANCE.getMessage().replace("%player%", target.getName()).replace("%balance%", String.valueOf(targetData.getBalance())));
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        sender.sendMessage(Messages.YOUR_BALANCE.getMessage().replace("%balance%", String.valueOf(playerData.getBalance())));
        return true;
    }
}
