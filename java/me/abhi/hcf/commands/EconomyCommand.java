package me.abhi.hcf.commands;

import me.abhi.hcf.HCF;
import me.abhi.hcf.commands.subcommands.economy.EconomyGiveSubCommand;
import me.abhi.hcf.commands.subcommands.economy.EconomySetSubCommand;
import me.abhi.hcf.commands.subcommands.economy.EconomyTakeSubCommand;
import me.abhi.hcf.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EconomyCommand implements CommandExecutor {

    private HCF plugin;
    private EconomySetSubCommand economySetSubCommand;
    private EconomyGiveSubCommand economyGiveSubCommand;
    private EconomyTakeSubCommand economyTakeSubCommand;

    public EconomyCommand(HCF plugin) {
        this.plugin = plugin;
        economySetSubCommand = new EconomySetSubCommand(this.plugin);
        economyGiveSubCommand = new EconomyGiveSubCommand(this.plugin);
        economyTakeSubCommand = new EconomyTakeSubCommand(this.plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("hcf.command.economy")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <set:give:take> <player> <amount>");
            return true;
        }
        if (args[0].equalsIgnoreCase("set")) {
            economySetSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            economyGiveSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("take")) {
            economyTakeSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        return true;
    }
}
