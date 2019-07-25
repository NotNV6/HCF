package me.abhi.hcf.commands;

import me.abhi.hcf.HCF;
import me.abhi.hcf.commands.subcommands.factions.*;
import me.abhi.hcf.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsCommand implements CommandExecutor {

    private HCF plugin;
    private FactionsCreateSubCommand factionsCreateSubCommand;
    private FactionsInviteSubCommand factionsInviteSubCommand;
    private FactionsJoinSubCommand factionsJoinSubCommand;
    private FactionsLeaveSubCommand factionsLeaveSubCommand;
    private FactionsKickSubCommand factionsKickSubCommand;
    private FactionsDisbandSubCommand factionsDisbandSubCommand;
    private FactionsUninviteSubCommand factionsUninviteSubCommand;
    private FactionsClaimSubCommand factionsClaimSubCommand;
    private FactionsUnclaimSubCommand factionsUnclaimSubCommand;
    private FactionsAllySubCommand factionsAllySubCommand;
    private FactionsUnallySubCommand factionsUnallySubCommand;
    private FactionsCreateSystemSubCommand factionsCreateSystemSubCommand;
    private FactionsClaimForSubCommand factionsClaimForSubCommand;
    private FactionsToggleSafeSubCommand factionsToggleSafeSubCommand;
    private FactionsUnclaimForSubCommand factionsUnclaimForSubCommand;
    private FactionsDisbandFactionSubCommand factionsDisbandFactionSubCommand;
    private FactionsPromoteSubCommand factionsPromoteSubCommand;
    private FactionsDemoteSubCommand factionsDemoteSubCommand;
    private FactionsNewLeaderSubCommand factionsNewLeaderSubCommand;
    private FactionsChatSubCommand factionsChatSubCommand;
    private FactionsSetHomeSubCommand factionsSetHomeSubCommand;
    private FactionsSetBalanceSubCommand factionsSetBalanceSubCommand;
    private FactionsHomeSubCommand factionsHomeSubCommand;
    private FactionsShowSubCommand factionsShowSubCommand;
    private FactionsDespositSubCommand factionsDespositSubCommand;
    private FactionsWithdrawSubCommand factionsWithdrawSubCommand;

    public FactionsCommand(HCF plugin) {
        this.plugin = plugin;
        factionsCreateSubCommand = new FactionsCreateSubCommand(this.plugin);
        factionsInviteSubCommand = new FactionsInviteSubCommand(this.plugin);
        factionsJoinSubCommand = new FactionsJoinSubCommand(this.plugin);
        factionsLeaveSubCommand = new FactionsLeaveSubCommand(this.plugin);
        factionsKickSubCommand = new FactionsKickSubCommand(this.plugin);
        factionsDisbandSubCommand = new FactionsDisbandSubCommand(this.plugin);
        factionsUninviteSubCommand = new FactionsUninviteSubCommand(this.plugin);
        factionsClaimSubCommand = new FactionsClaimSubCommand(this.plugin);
        factionsUnclaimSubCommand = new FactionsUnclaimSubCommand(this.plugin);
        factionsAllySubCommand = new FactionsAllySubCommand(this.plugin);
        factionsUnallySubCommand = new FactionsUnallySubCommand(this.plugin);
        factionsCreateSystemSubCommand = new FactionsCreateSystemSubCommand(this.plugin);
        factionsClaimForSubCommand = new FactionsClaimForSubCommand(this.plugin);
        factionsToggleSafeSubCommand = new FactionsToggleSafeSubCommand(this.plugin);
        factionsUnclaimForSubCommand = new FactionsUnclaimForSubCommand(this.plugin);
        factionsDisbandFactionSubCommand = new FactionsDisbandFactionSubCommand(this.plugin);
        factionsPromoteSubCommand = new FactionsPromoteSubCommand(this.plugin);
        factionsDemoteSubCommand = new FactionsDemoteSubCommand(this.plugin);
        factionsNewLeaderSubCommand = new FactionsNewLeaderSubCommand(this.plugin);
        factionsChatSubCommand = new FactionsChatSubCommand(this.plugin);
        factionsSetHomeSubCommand = new FactionsSetHomeSubCommand(this.plugin);
        factionsSetBalanceSubCommand = new FactionsSetBalanceSubCommand(this.plugin);
        factionsHomeSubCommand = new FactionsHomeSubCommand(this.plugin);
        factionsShowSubCommand = new FactionsShowSubCommand(this.plugin);
        factionsDespositSubCommand = new FactionsDespositSubCommand(this.plugin);
        factionsWithdrawSubCommand = new FactionsWithdrawSubCommand(this.plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(Messages.FACTION_HELP.getMessage());
            return true;
        }
        if (args[0].equalsIgnoreCase("create")) {
            factionsCreateSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("invite")) {
            factionsInviteSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("accept")) {
            factionsJoinSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("leave")) {
            factionsLeaveSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("kick")) {
            factionsKickSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("disband")) {
            factionsDisbandSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("uninvite") || args[0].equalsIgnoreCase("cancelinvite")) {
            factionsUninviteSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("claim")) {
            factionsClaimSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("unclaim")) {
            factionsUnclaimSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("ally")) {
            factionsAllySubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("unally")) {
            factionsUnallySubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("createsystem")) {
            factionsCreateSystemSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("claimfor")) {
            factionsClaimForSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("togglesafe")) {
            factionsToggleSafeSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("unclaimfor")) {
            factionsUnclaimForSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("disbandfaction")) {
            factionsDisbandFactionSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("promote")) {
            factionsPromoteSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("demote")) {
            factionsDemoteSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("newleader")) {
            factionsNewLeaderSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("chat")) {
            factionsChatSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("sethome") || args[0].equalsIgnoreCase("sethq")) {
            factionsSetHomeSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("setbalance")) {
            factionsSetBalanceSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("hq")) {
            factionsHomeSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("who")) {
            factionsShowSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("deposit") || args[0].equalsIgnoreCase("d")) {
            factionsDespositSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("withdraw") || args[0].equalsIgnoreCase("w")) {
            factionsWithdrawSubCommand.onCommand(sender, cmd, commandLabel, args);
            return true;
        }
        return true;
    }
}
