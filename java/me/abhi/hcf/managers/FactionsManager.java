package me.abhi.hcf.managers;

import lombok.Getter;
import me.abhi.hcf.cuboid.Cuboid;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionMember;
import me.abhi.hcf.faction.FactionRole;
import me.abhi.hcf.manager.Manager;
import me.abhi.hcf.manager.ManagerHandler;
import me.abhi.hcf.util.LocationUtil;
import me.abhi.hcf.util.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class FactionsManager extends Manager {

    private List<Faction> factionList;

    public FactionsManager(ManagerHandler managerHandler) {
        super(managerHandler);
        factionList = new ArrayList();
        loadFactions();
        loadAllies();
        createWarzone();
    }

    private void loadFactions() {
        if (this.managerHandler.getPlugin().getFactionsFile().getConfigurationSection("factions") != null) {
            for (String factions : this.managerHandler.getPlugin().getFactionsFile().getConfigurationSection("factions").getKeys(false)) {
                Faction faction = new Faction(factions);
                String system = this.managerHandler.getPlugin().getFactionsFile().getString("factions." + factions + ".system");
                boolean safe = this.managerHandler.getPlugin().getFactionsFile().getBoolean("factions." + factions + ".safe");
                double dtr = this.managerHandler.getPlugin().getFactionsFile().getDouble("factions." + factions + ".dtr");
                if (!Boolean.parseBoolean(system)) {
                    if (this.managerHandler.getPlugin().getFactionsFile().getConfigurationSection("factions." + factions + ".members") != null) {
                        for (String member : this.managerHandler.getPlugin().getFactionsFile().getConfigurationSection("factions." + factions + ".members").getKeys(false)) {
                            UUID uuid = UUID.fromString(member);
                            FactionRole factionRole = FactionRole.valueOf(managerHandler.getPlugin().getFactionsFile().getString("factions." + factions + ".members." + member + ".role"));
                            FactionMember factionMember = new FactionMember(uuid, factionRole);
                            faction.addFactionMember(factionMember);
                        }
                    }
                    int balance = this.managerHandler.getPlugin().getFactionsFile().getInt("factions." + factions + ".balance");
                    faction.setBalance(balance);
                }
                if (this.managerHandler.getPlugin().getFactionsFile().getString("factions." + factions + ".claim.1") != null && this.managerHandler.getPlugin().getFactionsFile().getString("factions." + factions + ".claim.2") != null) {
                    Location claimOne = LocationUtil.getLocationFromString(this.managerHandler.getPlugin().getFactionsFile().getString("factions." + factions + ".claim.1"));
                    Location claimTwo = LocationUtil.getLocationFromString(this.managerHandler.getPlugin().getFactionsFile().getString("factions." + factions + ".claim.2"));
                    Cuboid claim = new Cuboid(claimOne, claimTwo);
                    faction.setClaim(claim);
                }
                if (this.managerHandler.getPlugin().getFactionsFile().getString("factions." + factions + ".claim.1") != null) {
                    Location home = LocationUtil.getLocationFromString(this.managerHandler.getPlugin().getFactionsFile().getString("factions." + factions + ".home"));
                    faction.setHome(home);
                }
                int claimCost = this.managerHandler.getPlugin().getFactionsFile().getInt("factions." + factions + ".claim.cost");
                faction.setClaimCost(claimCost);
                faction.setSafe(safe);
                faction.setSystem(Boolean.parseBoolean(system));
                faction.setDtr(dtr);
                addFaction(faction);
                Logger.log("Loaded faction: " + faction.getName());
            }
        }
    }

    private void loadAllies() {
        for (Faction faction : factionList) {
            for (String allies : this.managerHandler.getPlugin().getFactionsFile().getStringList("factions." + faction.getName() + ".allies")) {
                if (factionExists(allies) && !faction.getAllies().contains(getFaction(allies))) {
                    Faction ally = getFaction(allies);
                    faction.getAllies().add(ally);
                    ally.getAllies().add(faction);
                }
            }
        }
    }

    private void createWarzone() {
        int radius = 800;
        Cuboid cuboid = new Cuboid(new Location(this.managerHandler.getPlugin().getServer().getWorld(this.managerHandler.getPlugin().getConfig().getString("world")), radius, 0, radius), new Location(this.managerHandler.getPlugin().getServer().getWorld(this.managerHandler.getPlugin().getConfig().getString("world")), radius * -1, 256, radius * -1));
        Faction faction = new Faction("WarZone", true);
        faction.setClaim(cuboid);
        addFaction(faction);
    }

    public boolean factionExists(String name) {
        for (Faction faction : factionList) {
            if (faction.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public Faction getFaction(String name) {
        for (Faction faction : factionList) {
            if (faction.getName().equalsIgnoreCase(name)) {
                return faction;
            }
        }
        return null;
    }

    public void addFaction(Faction faction) {
        factionList.add(faction);
    }

    public void removeFaction(Faction faction) {
        factionList.remove(faction);
    }

    public void findFaction(Player player) {
        PlayerData playerData = this.managerHandler.getPlayerDataManager().getPlayerData(player);
        for (Faction faction : factionList) {
            if (!faction.isSystem()) {
                for (FactionMember factionMember : faction.getMembers()) {
                    if (factionMember.getUuid().equals(player.getUniqueId())) {
                        playerData.setFaction(faction);
                    }
                }
            }
        }
    }

    public void saveFactions() {
        if (this.managerHandler.getPlugin().getFactionsFile().getConfigurationSection("factions") != null) {
            for (String factions : this.managerHandler.getPlugin().getFactionsFile().getConfigurationSection("factions").getKeys(false)) {
                this.managerHandler.getPlugin().getFactionsFile().set("factions." + factions, null);
            }
        }
        for (Faction faction : factionList) {
            if (faction.getName().equalsIgnoreCase("warzone")) {
                continue;
            }
            if (!faction.isSystem()) {
                for (FactionMember factionMember : faction.getMembers()) {
                    this.managerHandler.getPlugin().getFactionsFile().set("factions." + faction.getName() + ".members." + factionMember.getUuid() + ".role", factionMember.getFactionRole().toString());
                }
                this.managerHandler.getPlugin().getFactionsFile().set("factions." + faction.getName() + ".balance", faction.getBalance());
            }
            if (faction.getClaim() != null) {
                this.managerHandler.getPlugin().getFactionsFile().set("factions." + faction.getName() + ".claim.1", LocationUtil.getStringFromLocation(faction.getClaim().getLocation1()));
                this.managerHandler.getPlugin().getFactionsFile().set("factions." + faction.getName() + ".claim.2", LocationUtil.getStringFromLocation(faction.getClaim().getLocation2()));
            }
            if (!faction.isSystem()) {
                this.managerHandler.getPlugin().getFactionsFile().set("factions." + faction.getName() + ".claim.cost", faction.getClaimCost());
                List<String> allies = new ArrayList();
                for (Faction ally : faction.getAllies()) {
                    allies.add(ally.getName());
                }
                this.managerHandler.getPlugin().getFactionsFile().set("factions." + faction.getName() + ".allies", allies);
            }
            if (faction.getHome() != null) {
                this.managerHandler.getPlugin().getFactionsFile().set("factions." + faction.getName() + ".home", LocationUtil.getStringFromLocation(faction.getHome()));
            }
            this.managerHandler.getPlugin().getFactionsFile().set("factions." + faction.getName() + ".dtr", faction.getDtr());
            this.managerHandler.getPlugin().getFactionsFile().set("factions." + faction.getName() + ".safe", faction.isSafe());
            this.managerHandler.getPlugin().getFactionsFile().set("factions." + faction.getName() + ".system", faction.isSystem());
        }
        this.managerHandler.getPlugin().saveFactionsFile();
    }
}
