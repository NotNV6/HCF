package me.abhi.hcf.listener;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.util.ScoreHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private HCF plugin;

    public QuitListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        for (String cooldown : playerData.getCooldownMap().keySet()) {
            playerData.getCooldown(cooldown).pause();
        }
        ScoreHelper.removeScore(player);
        playerData.save();
    }
}
