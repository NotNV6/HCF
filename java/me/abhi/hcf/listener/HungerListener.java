package me.abhi.hcf.listener;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerListener implements Listener {

    private HCF plugin;

    public HungerListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
            if (playerData.getClaimFac() != null && playerData.getClaimFac().isSafe()) {
                event.setFoodLevel(20);
            }
        }
    }
}
