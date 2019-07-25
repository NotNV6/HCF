package me.abhi.hcf.cooldown;

import lombok.Getter;
import me.abhi.hcf.HCF;
import me.abhi.hcf.events.ServerCooldownEndEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;

@Getter
public class ServerCooldown {

    private HCF plugin;
    private String id;
    private int time;
    private BukkitTask bukkitTask;
    @Getter
    private boolean paused;

    public ServerCooldown(HCF plugin, String id, int time) {
        this.plugin = plugin;
        this.id = id;
        this.time = time;
        this.plugin.getManagerHandler().getServerManager().addCooldown(this);
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
                            plugin.getServer().getPluginManager().callEvent(new ServerCooldownEndEvent(ServerCooldown.this));
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
        this.plugin.getManagerHandler().getServerManager().removeCooldown(this.id);
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
