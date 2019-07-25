package me.abhi.hcf.commands.subcommands.factions;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.util.Items;
import me.abhi.hcf.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsClaimForSubCommand implements CommandExecutor {

    private HCF plugin;

    public FactionsClaimForSubCommand(HCF plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("hcf.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " claimfor <name>");
            return true;
        }
        String name = args[1];
        if (!this.plugin.getManagerHandler().getFactionsManager().factionExists(name)) {
            sender.sendMessage(Messages.FACTION_DOESNT_EXIST.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        Faction faction = this.plugin.getManagerHandler().getFactionsManager().getFaction(name);
        if (player.getInventory().contains(Items.CLAIM_WAND.getItemStack())) {
            player.getInventory().remove(Items.CLAIM_WAND.getItemStack());
            playerData.setClaimFor(null);
            player.updateInventory();
            sender.sendMessage(Messages.CLAIM_WAND_REVOKED.getMessage());
            return true;
        }
        player.getInventory().addItem(Items.CLAIM_WAND.getItemStack());
        sender.sendMessage(Messages.CLAIM_WAND_GRANTED.getMessage());
        playerData.setClaimFor(faction);
        return true;
    }
}
