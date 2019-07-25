package me.abhi.hcf.runnable;

import me.abhi.hcf.HCF;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardRunnable extends BukkitRunnable {

    private HCF plugin;

    public ScoreboardRunnable(HCF plugin) {
        this.plugin = plugin;
    }

    public void run() {
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            this.plugin.getManagerHandler().getScoreboardManager().updatePlayer(player);
            this.plugin.getManagerHandler().getScoreboardManager().updateNames(player);
        }
    }
}
