package me.abhi.hcf.listener;

import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.cooldown.Cooldown;
import me.abhi.hcf.cuboid.Cuboid;
import me.abhi.hcf.util.Items;
import me.abhi.hcf.util.Messages;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    private HCF plugin;

    public InteractListener(HCF plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getGameMode() != GameMode.CREATIVE && player.getItemInHand().getType() == Material.ENDER_PEARL) {
                if (playerData.hasCooldown("enderpearl")) {
                    event.setCancelled(true);
                    player.updateInventory();
                    Cooldown cooldown = playerData.getCooldown("enderpearl");
                    player.sendMessage(Messages.ENDERPEARL_DELAY.getMessage().replace("%time%", String.valueOf(cooldown.getSeconds())));
                    return;
                }
                new Cooldown(plugin, playerData, "enderpearl", 16);
                return;
            }
        }
        //Claim Listener
        Faction faction = playerData.getClaimFor();
        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK: {
                if (player.getItemInHand().equals(Items.CLAIM_WAND.getItemStack())) {
                    Location location = event.getClickedBlock().getLocation();
                    event.setCancelled(true);
                    if (!faction.isSystem() && (plugin.getManagerHandler().getClaimManager().isClaimed(location) || !plugin.getManagerHandler().getClaimManager().claimableDistance(location, 2))) {
                        player.sendMessage(Messages.LAND_UNCLAIMED.getMessage());
                        return;
                    }
                    if (playerData.getClaimOne() == null || !playerData.getClaimOne().equals(location)) {
                        playerData.setClaimOne(location);
                        player.sendMessage(Messages.FIRST_POINT_SET.getMessage().replace("%x%", String.valueOf((int) location.getX())).replace("%z%", String.valueOf((int) location.getZ())));
                        if (playerData.getClaimTwo() != null) {
                            if (playerData.getClaimOne() != null) {
                                if (playerData.getClaimOne().distance(playerData.getClaimTwo()) < 5) {
                                    player.sendMessage(Messages.MINIMUM_CLAIM.getMessage());
                                    return;
                                }
                                int cost = plugin.getManagerHandler().getClaimManager().getCost(playerData.getClaimOne(), playerData.getClaimTwo());
                                if (!faction.isSystem()) {
                                    if (faction.getBalance() < cost) {
                                        player.sendMessage(Messages.CLAIM_COST_NO_FUNDS.getMessage().replace("%cost%", String.valueOf(cost)));
                                    } else {
                                        player.sendMessage(Messages.CLAIM_COST_FUNDS.getMessage().replace("%cost%", String.valueOf(cost)));
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            }
            case RIGHT_CLICK_BLOCK: {
                if (player.getItemInHand().equals(Items.CLAIM_WAND.getItemStack())) {
                    Location location = event.getClickedBlock().getLocation();
                    event.setCancelled(true);
                    if (!faction.isSystem() && (plugin.getManagerHandler().getClaimManager().isClaimed(location) || !plugin.getManagerHandler().getClaimManager().claimableDistance(location, 2))) {
                        player.sendMessage(Messages.LAND_UNCLAIMABLE.getMessage());
                        return;
                    }
                    if (playerData.getClaimTwo() == null || !playerData.getClaimTwo().equals(location)) {
                        playerData.setClaimTwo(location);
                        player.sendMessage(Messages.SECOND_POINT_SET.getMessage().replace("%x%", String.valueOf((int) location.getX())).replace("%z%", String.valueOf((int) location.getZ())));
                        if (playerData.getClaimOne() != null) {
                            if (playerData.getClaimOne().distance(playerData.getClaimTwo()) < 5) {
                                player.sendMessage(Messages.MINIMUM_CLAIM.getMessage());
                                return;
                            }
                            int cost = plugin.getManagerHandler().getClaimManager().getCost(playerData.getClaimOne(), location);
                            if (!faction.isSystem()) {
                                if (faction.getBalance() < cost) {
                                    player.sendMessage(Messages.CLAIM_COST_NO_FUNDS.getMessage().replace("%cost%", String.valueOf(cost)));
                                } else {
                                    player.sendMessage(Messages.CLAIM_COST_FUNDS.getMessage().replace("%cost%", String.valueOf(cost)));
                                }
                            }
                        }
                    }
                }
                break;
            }
            case RIGHT_CLICK_AIR: {
                if (player.getItemInHand().equals(Items.CLAIM_WAND.getItemStack())) {
                    event.setCancelled(true);
                    player.getInventory().remove(Items.CLAIM_WAND.getItemStack());
                    player.updateInventory();
                    playerData.setClaimOne(null);
                    playerData.setClaimTwo(null);
                    playerData.setClaimFor(null);
                    player.sendMessage(Messages.CLAIM_CLEARED.getMessage());
                }
                break;
            }
            case LEFT_CLICK_AIR: {
                if (player.getItemInHand().equals(Items.CLAIM_WAND.getItemStack()) && player.isSneaking()) {
                    event.setCancelled(true);
                    if (playerData.getClaimOne() == null || playerData.getClaimTwo() == null) {
                        player.sendMessage(Messages.INVALID_CLAIM.getMessage());
                        return;
                    }
                    if (!faction.isSystem()) {
                        int cost = plugin.getManagerHandler().getClaimManager().getCost(playerData.getClaimOne(), playerData.getClaimTwo());
                        if (faction.getBalance() < cost) {
                            player.sendMessage(Messages.INSUFFICIENT_FUNDS.getMessage());
                            return;
                        }
                        faction.setBalance(faction.getBalance() - cost);
                        faction.setClaimCost(cost);
                    }
                    player.getInventory().remove(Items.CLAIM_WAND.getItemStack());
                    player.updateInventory();
                    Cuboid cuboid = new Cuboid(new Location(playerData.getClaimOne().getWorld(), playerData.getClaimOne().getX(), 0, playerData.getClaimOne().getZ()), new Location(playerData.getClaimTwo().getWorld(), playerData.getClaimTwo().getX(), 256, playerData.getClaimTwo().getZ()));
                    faction.setClaim(cuboid);
                    playerData.setClaimOne(null);
                    playerData.setClaimTwo(null);
                    playerData.setClaimFor(null);
                    player.sendMessage(Messages.LAND_CLAIMED.getMessage());
                    return;
                }

            }
        }

    }
}
