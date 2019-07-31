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
        Arrays.stream(this.plugin.getServer().getOnlinePlayers()).forEach(player -> {
            plugin.getManagerHandler().getScoreboardManager().updatePlayer(player);
            plugin.getManagerHandler().getScoreboardManager().updateNames(player);
        });
    }
}
