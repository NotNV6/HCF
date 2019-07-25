package me.abhi.hcf.listener;

import me.abhi.hcf.HCF;
import me.abhi.hcf.cooldown.Cooldown;
import me.abhi.hcf.cooldown.ServerCooldown;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.events.CooldownEndEvent;
import me.abhi.hcf.events.ServerCooldownEndEvent;
import me.abhi.hcf.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CooldownListener implements Listener {

    private HCF plugin;

    public CooldownListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCooldownEnd(CooldownEndEvent event) {
        Cooldown cooldown = event.getCooldown();
        if (cooldown.getPlayerData() != null && this.plugin.getServer().getPlayer(cooldown.getPlayerData().getUuid()) != null) {
            Player player = this.plugin.getServer().getPlayer(cooldown.getPlayerData().getUuid());
            PlayerData playerData = cooldown.getPlayerData();
            if (cooldown.getId().equalsIgnoreCase("enderpearl")) {
                player.sendMessage(Messages.MAY_PEARL_AGAIN.getMessage());
                return;
            }
            if (cooldown.getId().equalsIgnoreCase("combat")) {
                player.sendMessage(Messages.NO_LONGER_IN_COMBAT.getMessage());
                return;
            }
            if (cooldown.getId().equalsIgnoreCase("home")) {
                if (playerData.hasFaction() && playerData.getFaction().getHome() != null) {
                    player.teleport(playerData.getFaction().getHome());
                } else {
                    player.sendMessage(ChatColor.RED + "Error.");
                }
                return;
            }
            if (cooldown.getId().equalsIgnoreCase("logout")) {
                player.kickPlayer(ChatColor.RED + "You have successfully logged out.");
                return;
            }
        }
    }

    @EventHandler
    public void onServerCooldownEnd(ServerCooldownEndEvent event) {
       ServerCooldown serverCooldown = event.getCooldown();
       if (serverCooldown.getId().equalsIgnoreCase("sotw")) {
           this.plugin.getServer().broadcastMessage(Messages.SOTW_ENDED.getMessage());
       }
    }
}
