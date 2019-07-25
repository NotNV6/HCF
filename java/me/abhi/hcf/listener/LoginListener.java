package me.abhi.hcf.listener;

import me.abhi.hcf.HCF;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginListener implements Listener {

    private HCF plugin;

    public LoginListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (!this.plugin.getManagerHandler().getPlayerDataManager().hasPlayerData(player)) {
            this.plugin.getManagerHandler().getPlayerDataManager().addPlayer(player);
        }
    }
}
