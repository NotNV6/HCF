package me.abhi.hcf.listener;

import me.abhi.hcf.HCF;
import me.abhi.hcf.cooldown.Cooldown;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.util.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class ConsumeListener implements Listener {

    private HCF plugin;

    public ConsumeListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            if (event.getItem().getDurability() == 1) {
                if (playerData.hasCooldown("god-apple")) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.GOLDEN_APPLE_COOLDOWN.getMessage());
                    return;
                }
                new Cooldown(this.plugin, playerData, "god-apple", 3601);
            } else {
                if (playerData.hasCooldown("golden-apple")) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.GOLDEN_APPLE_COOLDOWN.getMessage());
                    return;
                }
                new Cooldown(this.plugin, playerData, "golden-apple", 6);
            }
        }
    }
}
