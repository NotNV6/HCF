package me.abhi.hcf.commands.subcommands.economy;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.util.Messages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyTakeSubCommand implements CommandExecutor {

    private HCF plugin;

    public EconomyTakeSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player target = this.plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Messages.COULD_NOT_FIND_PLAYER.getMessage());
            return true;
        }
        if (!NumberUtils.isDigits(args[2])) {
            sender.sendMessage(Messages.INVALID_AMOUNT.getMessage());
            return true;
        }
        int amount = Integer.parseInt(args[2]);
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(target);
        playerData.setBalance(playerData.getBalance() - amount);
        sender.sendMessage(Messages.UPDATED_PLAYER_BALANCE.getMessage().replace("%player%", target.getName()).replace("%balance%", String.valueOf(playerData.getBalance())));
        target.sendMessage(Messages.BALANCE_UPDATED.getMessage().replace("%balance%", String.valueOf(playerData.getBalance())));
        return true;
    }
}
