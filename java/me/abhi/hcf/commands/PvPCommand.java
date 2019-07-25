package me.abhi.hcf.commands;

import me.abhi.hcf.HCF;
import me.abhi.hcf.commands.subcommands.pvp.PvPEnableSubCommand;
import me.abhi.hcf.commands.subcommands.pvp.PvPTimeSubCommand;
import me.abhi.hcf.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvPCommand implements CommandExecutor {

    private HCF plugin;
    private PvPEnableSubCommand pvpEnableSubCommand;
    private PvPTimeSubCommand pvpTimeSubCommand;

    public PvPCommand(HCF plugin) {
        this.plugin = plugin;
        pvpEnableSubCommand = new PvPEnableSubCommand(this.plugin);
        pvpTimeSubCommand = new PvPTimeSubCommand(this.plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(Messages.PVP_HELP.getMessage());
            return true;
        }
        if (args[0].equalsIgnoreCase("enable")) {
            pvpEnableSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("time")) {
            pvpTimeSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        return true;
    }
}
