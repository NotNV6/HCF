package me.abhi.hcf.listener;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.cooldown.Cooldown;
import me.abhi.hcf.util.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private HCF plugin;

    public DamageListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
            if (playerData.getClaimFac() != null && playerData.getClaimFac().isSafe()) {
                event.setCancelled(true);
                return;
            }
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                PlayerData damagerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(damager);
                if (damagerData.hasCooldown("pvp-timer")) {
                    event.setCancelled(true);
                    damager.sendMessage(Messages.PVP_TIMER_ACTIVE.getMessage());
                    return;
                }
                if (playerData.hasCooldown("pvp-timer")) {
                    event.setCancelled(true);
                    damager.sendMessage(Messages.PLAYER_HAS_PVP_TIMER.getMessage());
                    return;
                }
                if (damagerData.hasFaction() && damagerData.getFaction().isFactionMember(player.getUniqueId()) || (damagerData.hasFaction() && damagerData.getFaction().getAllies().contains(playerData.getFaction()))) {
                    event.setCancelled(true);
                    damager.sendMessage(Messages.CANT_HURT_PERSON.getMessage().replace("%player%", player.getName()));
                    return;
                }
                if (damagerData.hasCooldown("combat")) {
                    damagerData.getCooldown("combat").setTime(30);
                } else {
                    new Cooldown(this.plugin, damagerData, "combat", 30);
                    damager.sendMessage(Messages.COMBAT_TAGGED.getMessage());
                }
                damagerData.setLastCombat(System.currentTimeMillis());
                if (playerData.hasCooldown("combat")) {
                    playerData.getCooldown("combat").setTime(30);
                } else {
                    new Cooldown(this.plugin, playerData, "combat", 30);
                    player.sendMessage(Messages.COMBAT_TAGGED.getMessage());
                }
                playerData.setLastCombat(System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (this.plugin.getManagerHandler().getServerManager().hasCooldown("sotw")) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
