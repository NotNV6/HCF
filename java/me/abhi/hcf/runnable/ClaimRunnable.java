package me.abhi.hcf.runnable;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ClaimRunnable extends BukkitRunnable {
    private HCF plugin;

    public ClaimRunnable(HCF plugin) {
        this.plugin = plugin;
    }

    public void run() {
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
            if (playerData.getFrom() == null || playerData.getTo() == null) {
                continue;
            }
            Location from = playerData.getFrom();
            Location to = playerData.getTo();
            String place = ChatColor.DARK_GREEN + "Wilderness";
            if (this.plugin.getManagerHandler().getClaimManager().isClaimed(to) && this.plugin.getManagerHandler().getClaimManager().getClaim(to).getName().equalsIgnoreCase("warzone")) {
                place = ChatColor.DARK_RED + "WarZone";
            } else if (this.plugin.getManagerHandler().getClaimManager().isClaimed(to) && !this.plugin.getManagerHandler().getClaimManager().getClaim(to).getName().equalsIgnoreCase("warzone")) {
                place = (this.plugin.getManagerHandler().getClaimManager().getClaim(to).isSafe() ? ChatColor.GREEN : ChatColor.RED) + this.plugin.getManagerHandler().getClaimManager().getClaim(to).getName();
            }
            if ((playerData.getClaim().equalsIgnoreCase(ChatColor.DARK_GREEN + "Wilderness") || playerData.getClaimFac() != null) && !playerData.getClaim().equalsIgnoreCase(place)) {
                player.sendMessage(Messages.SWITCHING_CLAIMS.getMessage().replace("%entering%", place).replace("%leaving%", playerData.getClaim()));
            }
            playerData.setClaim(place);
        }
    }
}
