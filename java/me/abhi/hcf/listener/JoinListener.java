package me.abhi.hcf.listener;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.runnable.CombatRunnable;
import me.abhi.hcf.util.ScoreHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private HCF plugin;

    public JoinListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ScoreHelper.createScore(player);
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (player.hasPermission("hcf.admin") && !playerData.isStaffMode()) {
            playerData.setStaffMode(true);
        }
        this.plugin.getManagerHandler().getFactionsManager().findFaction(player);
        new CombatRunnable(this.plugin, player).runTaskTimerAsynchronously(this.plugin, 0L, 0L);
        this.plugin.getServer().dispatchCommand(player, "f show");
    }
}
