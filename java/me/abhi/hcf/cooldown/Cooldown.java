package me.abhi.hcf.cooldown;

import lombok.Getter;
import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.events.CooldownEndEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;

@Getter
public class Cooldown {

    private HCF plugin;
    private PlayerData playerData;
    private String id;
    private int time;
    private BukkitTask bukkitTask;
    @Getter
    private boolean paused;

    public Cooldown(HCF plugin, PlayerData playerData, String id, int time) {
        this.plugin = plugin;
        this.playerData = playerData;
        this.id = id;
        this.time = time;
        playerData.addCooldown(this);
        start();
    }

    private void start() {
        paused = false;
        bukkitTask = new BukkitRunnable() {
            public void run() {
                if (time <= 0) {
                    stop();
                    new BukkitRunnable() {
                        public void run() {
                            plugin.getServer().getPluginManager().callEvent(new CooldownEndEvent(Cooldown.this));
                        }
                    }.runTask(plugin);
                    return;
                }
                time--;
            }
        }.runTaskTimerAsynchronously(this.plugin, 0L, 20L);
    }

    public void pause() {
        bukkitTask.cancel();
        paused = true;
    }

    public void unpause() {
        start();
        paused = false;
    }

    public void stop() {
        bukkitTask.cancel();
        playerData.removeCooldown(id);
        paused = true;
    }

    public String getTime() {
        int seconds = this.time % 60;
        int minutes = this.time / 60 % 60;
        int hours = (this.time / 60) / 60;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String strHours = decimalFormat.format(hours);
        String strMinutes = decimalFormat.format(minutes);
        String strSeconds = decimalFormat.format(seconds);
        if (hours <= 0) {
            return strMinutes + ":" + strSeconds;
        }
        return strHours + ":" + strMinutes + ":" + strSeconds;
    }

    public int getSeconds() {
        return time % 60;
    }

    public int getMinutes() {
        return time / 60 % 60;
    }

    public int getHours() {
        return (time / 60) / 60;
    }

    public void setTime(int time) {
        bukkitTask.cancel();
        this.time = time;
        start();
    }
}
