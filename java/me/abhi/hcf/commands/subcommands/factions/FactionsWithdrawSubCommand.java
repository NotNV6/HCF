package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionMember;
import me.abhi.hcf.faction.FactionRole;
import me.abhi.hcf.util.Messages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsWithdrawSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsWithdrawSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " withdraw <amount>");
            return true;
        }
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
        int amount = 0;
        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("a")) {
            amount = faction.getBalance();
        } else if (!NumberUtils.isDigits(args[1])) {
            sender.sendMessage(Messages.INVALID_AMOUNT.getMessage());
        } else {
            amount = Integer.parseInt(args[1]);
        }
        if (amount <= 0) {
            sender.sendMessage(Messages.AMOUNT_CANT_BE_ZERO.getMessage());
            return true;
        }
        if (amount > faction.getBalance()) {
            sender.sendMessage(Messages.INSUFFICIENT_FUNDS.getMessage());
            return true;
        }
        playerData.setBalance(playerData.getBalance() + amount);
        faction.setBalance(faction.getBalance() - amount);
        sender.sendMessage(Messages.WITHDREW_AMOUNT.getMessage().replace("%amount%", String.valueOf(amount)));
        return true;
    }
}
