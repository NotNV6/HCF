package me.abhi.hcf.data;

import lombok.Getter;
import lombok.Setter;
import me.abhi.hcf.HCF;
import me.abhi.hcf.chatmode.ChatMode;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionInvite;
import me.abhi.hcf.cooldown.Cooldown;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.*;

@Getter
@Setter
public class PlayerData {

    private UUID uuid;
    private Faction faction;
    private Map<String, Cooldown> cooldownMap;
    private List<FactionInvite> factionInviteList;
    private Location claimOne;
    private Location claimTwo;
    private int balance;
    private Faction claimFor;
    private Location from;
    private Location to;
    private long lastCombat;
    private ChatMode chatMode;
    private String claim;
    private boolean vanished;
    private boolean staffMode;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.cooldownMap = new HashMap();
        this.factionInviteList = new ArrayList();
        this.chatMode = ChatMode.GLOBAL;
        this.claim = "";
        this.balance = HCF.getInstance().getBalancesFile().getInt(uuid.toString());
    }

    public boolean hasFaction() {
        return faction != null;
    }

    public void addCooldown(Cooldown cooldown) {
        cooldownMap.put(cooldown.getId(), cooldown);
    }

    public boolean hasCooldown(String id) {
        return cooldownMap.containsKey(id);
    }

    public Cooldown getCooldown(String id) {
        return cooldownMap.get(id);
    }

    public void removeCooldown(String id) {
        cooldownMap.remove(id);
    }

    public boolean hasInvite(Faction faction) {
        for (FactionInvite factionInvite : factionInviteList) {
            if (factionInvite.getFaction() == faction && System.currentTimeMillis() - factionInvite.getTimestamp() <= 60000) {
                return true;
            }
        }
        return false;
    }

    public FactionInvite getFactionInvite(Faction faction) {
        for (FactionInvite factionInvite : factionInviteList) {
            if (factionInvite.getFaction() == faction && System.currentTimeMillis() - factionInvite.getTimestamp() <= 60000) {
                return factionInvite;
            }
        }
        return null;
    }

    public Faction getClaimFac() {
        return HCF.getInstance().getManagerHandler().getFactionsManager().getFaction(ChatColor.stripColor(claim));
    }

    public void save() {
        HCF.getInstance().getBalancesFile().set(uuid.toString(), balance);
        HCF.getInstance().saveBalancesFile();
    }
}
