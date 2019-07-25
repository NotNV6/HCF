package me.abhi.hcf.managers;

import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.manager.Manager;
import me.abhi.hcf.manager.ManagerHandler;
import me.abhi.hcf.cuboid.Cuboid;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class ClaimManager extends Manager {

    public ClaimManager(ManagerHandler managerHandler) {
        super(managerHandler);
    }

    public int getCost(Location location1, Location location2) {
        Location loc1 = location1;
        loc1.setY(0);
        Location loc2 = location2;
        loc2.setY(0);
        return (int) Math.round(loc1.distance(loc2) * 7);
    }

    public Faction getClaim(Location location) {
        for (Faction faction : this.managerHandler.getFactionsManager().getFactionList()) {
            if (faction.getClaim() != null && faction.getClaim().contains(location)) {
                return faction;
            }
        }
        return null;
    }

    public boolean isClaimed(Location location) {
        for (Faction faction : this.managerHandler.getFactionsManager().getFactionList()) {
            if (faction.getClaim() != null && faction.getClaim().contains(location)) {
                return true;
            }
        }
        return false;
    }

    public boolean claimableDistance(final Location location, final int distance) {
        for (Faction faction : managerHandler.getFactionsManager().getFactionList()) {
            if (faction.getClaim() != null) {
                for (Block block : faction.getClaim().getBlocks((int) location.getY())) {
                    if (block.getLocation().distance(location) <= distance) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isClaimable(Cuboid cuboid) {
        for (Faction faction : this.managerHandler.getFactionsManager().getFactionList()) {
            if (faction.getClaim() != null) {
                for (Block block : faction.getClaim().getBlocks()) {
                    if (cuboid.contains(block.getLocation()))
                        return false;
                }
            }
        }
        return true;
    }

    public boolean isSafe(Location location) {
        for (Faction faction : this.managerHandler.getFactionsManager().getFactionList()) {
            if (faction.getClaim() != null) {
                if (faction.isSafe() && faction.getClaim().contains(location)) {
                    return true;
                }
            }
        }
        return false;
    }
}
