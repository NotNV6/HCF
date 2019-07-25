package me.abhi.hcf.listener;

import me.abhi.hcf.HCF;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.concurrent.TimeUnit;

public class ShutdownListener implements Listener {

    private HCF plugin;

    public ShutdownListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShutdown(PluginDisableEvent event) {
        if (event.getPlugin() != this.plugin) {
            return;
        }
        this.plugin.getManagerHandler().getServerManager().getExecutorService().shutdown();
        try {
            this.plugin.getManagerHandler().getServerManager().getExecutorService().awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ex) {
        }
    }
}
