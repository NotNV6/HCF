package me.abhi.hcf.listener;

import me.abhi.hcf.HCF;
import me.abhi.hcf.cooldown.Cooldown;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.util.Messages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private HCF plugin;

    public MoveListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        Location from = event.getFrom();
        Location to = event.getTo();
        playerData.setFrom(from);
        playerData.setTo(to);
        if (playerData.hasCooldown("combat") && this.plugin.getManagerHandler().getClaimManager().isSafe(to)) {
            event.setTo(from);
        }
        if ((int) from.getX() != (int) to.getX() || (int) from.getY() != (int) to.getY() || (int) from.getZ() != (int) to.getZ()) {
            if (playerData.hasCooldown("home") || playerData.hasCooldown("logout")) {
                if (playerData.hasCooldown("home")) {
                    playerData.getCooldown("home").stop();
                }
                if (playerData.hasCooldown("logout")) {
                    playerData.getCooldown("logout").stop();
                }
                player.sendMessage(Messages.TIMER_CANCELLED.getMessage());
            }
        }
        if (playerData.getClaimFac() != null && playerData.getClaimFac().isSafe() && playerData.hasCooldown("pvp-timer")) {
            Cooldown pvpTimer = playerData.getCooldown("pvp-timer");
            if (!pvpTimer.isPaused()) {
                pvpTimer.pause();
            }
        } else if ((playerData.getClaimFac() == null || (playerData.getClaimFac() != null && !playerData.getClaimFac().isSafe())) && playerData.hasCooldown("pvp-timer")) {
            Cooldown pvpTimer = playerData.getCooldown("pvp-timer");
            if (pvpTimer.isPaused()) {
                pvpTimer.unpause();
            }
        }
    }
}
