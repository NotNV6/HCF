package me.abhi.hcf.runnable;

import com.google.common.collect.ImmutableList;
import me.abhi.hcf.HCF;
import me.abhi.hcf.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CombatRunnable extends BukkitRunnable {

    private HCF plugin;
    private List<BlockFace> ALL_DIRECTIONS = ImmutableList.of(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
    private Map<UUID, Set<Location>> previousUpdates;
    private Player player;

    public CombatRunnable(HCF plugin, Player player) {
        this.plugin = plugin;
        this.previousUpdates = new HashMap();
        this.player = player;
    }

    public void run() {
        if (!this.plugin.getManagerHandler().getPlayerDataManager().hasPlayerData(player)) {
            return;
        }
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        if (playerData.getFrom() == null || playerData.getTo() == null) {
            return;
        }
        Location t = playerData.getTo();
        Location f = playerData.getFrom();
        if (t.getBlockX() == f.getBlockX() && t.getBlockY() == f.getBlockY() && t.getBlockZ() == f.getBlockZ()) {
            return;
        }
        this.plugin.getManagerHandler().getServerManager().getExecutorService().submit(new Runnable() {
            public void run() {
                UUID uuid = player.getUniqueId();
                if (!player.isOnline()) {
                    previousUpdates.remove(uuid);
                    return;
                }
                Set<Location> changedBlocks = getChangedBlocks(player);
                Material forceFieldMaterial = Material.STAINED_GLASS;
                Set<Location> removeBlocks;
                if (previousUpdates.containsKey(uuid)) {
                    removeBlocks = previousUpdates.get(uuid);
                } else {
                    removeBlocks = new HashSet();
                }
                for (Location location : changedBlocks) {
                    if (plugin.getManagerHandler().getClaimManager().isSafe(location)) {
                        for (int i = 0; i <= 5; i++) {
                            player.sendBlockChange(location.getBlock().getLocation().add(0, i, 0), forceFieldMaterial, (byte) 14);
                        }
                    }
                    removeBlocks.remove(location);
                }

                for (Location location : removeBlocks) {
                    Block block = location.getBlock();
                    if (plugin.getManagerHandler().getClaimManager().isSafe(location)) {
                        for (int i = 0; i <= 5; i++) {
                            player.sendBlockChange(location.getBlock().getLocation().add(0, i, 0), block.getType(), block.getData());
                        }
                    }
                }
                previousUpdates.put(uuid, changedBlocks);
            }
        });
    }

    private Set<Location> getChangedBlocks(Player player) {
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        Set<Location> locations = new HashSet();
        if (System.currentTimeMillis() - playerData.getLastCombat() >= 30000) {
            return locations;
        }
        int r = 10;
        Location l = player.getLocation();
        Location loc1 = l.clone().add((double) r, 0.0, (double) r);
        Location loc2 = l.clone().subtract((double) r, 0.0, (double) r);
        int topBlockX = (loc1.getBlockX() < loc2.getBlockX()) ? loc2.getBlockX() : loc1.getBlockX();
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX()) ? loc2.getBlockX() : loc1.getBlockX();
        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ()) ? loc2.getBlockZ() : loc1.getBlockZ();
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ()) ? loc2.getBlockZ() : loc1.getBlockZ();
        for (int x = bottomBlockX; x <= topBlockX; ++x) {
            for (int z = bottomBlockZ; z <= topBlockZ; ++z) {
                Location location = new Location(l.getWorld(), (double) x, l.getY(), (double) z);
                if (plugin.getManagerHandler().getClaimManager().isSafe(location)) {
                    if (this.isPvpSurrounding(location)) {
                        for (int i = -r; i < r; ++i) {
                            Location loc3 = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
                            loc3.setY(loc3.getY() + i);
                            if (loc3.getBlock().getType().equals(Material.AIR)) {
                                locations.add(new Location(loc3.getWorld(), (double) loc3.getBlockX(), (double) loc3.getBlockY(), (double) loc3.getBlockZ()));
                            }
                        }
                    }
                }
            }
        }
        return locations;
    }


    private boolean isPvpSurrounding(Location loc) {
        for (BlockFace direction : ALL_DIRECTIONS) {
            if (!this.plugin.getManagerHandler().getClaimManager().isSafe(loc.getBlock().getRelative(direction).getLocation())) {
                return true;
            }
        }
        return false;
    }
}