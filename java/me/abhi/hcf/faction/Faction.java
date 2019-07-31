package me.abhi.hcf.faction;

import lombok.Getter;
import lombok.Setter;
import me.abhi.hcf.cuboid.Cuboid;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Faction {

    private String name;
    private List<FactionMember> members;
    private List<Faction> allies;
    private int balance;
    private Cuboid claim;
    private int claimCost;
    private List<FactionAllyInvite> factionAllyInvitesList;
    private boolean system;
    private boolean safe;
    private Location home;
    private double dtr;

    public Faction(String name) {
        this.name = name;
        this.members = new ArrayList();
        this.allies = new ArrayList();
        this.factionAllyInvitesList = new ArrayList();
    }

    public Faction(String name, boolean system) {
        this.name = name;
        this.system = system;
    }

    public FactionMember getFactionMember(UUID uuid) {
        return members.stream().filter(factionMember -> factionMember.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public boolean isFactionMember(UUID uuid) {
        return members.stream().filter(factionMember -> factionMember.getUuid().equals(uuid)).findFirst().orElse(null) != null;
    }

    public void addFactionMember(FactionMember factionMember) {
        members.add(factionMember);
    }

    public void removeFactionMember(FactionMember factionMember) {
        members.remove(factionMember);
    }

    public FactionAllyInvite getFactionAllyInvite(Faction faction) {
         return factionAllyInvites.stream().filter(invite -> invite.getFaction().equals(faction) && System.currentTimeMills() - invite.getTimestamp() <= 60000).findFirst().orElse(null);
    }

    public boolean hasFactionAllyInvite(Faction faction) {
        return getFactionAllyInvite(faction) != null;
    }

    public void addFactionAllyInvite(FactionAllyInvite factionAllyInvite) {
        factionAllyInvitesList.add(factionAllyInvite);
    }

    public void removeFactionAllyInvite(FactionAllyInvite factionAllyInvite) {
        factionAllyInvitesList.remove(factionAllyInvite);
    }

}
