package me.abhi.hcf.faction;

import lombok.Getter;

@Getter
public class FactionAllyInvite {

    private Faction faction;
    private long timestamp;

    public FactionAllyInvite(Faction faction) {
        this.faction = faction;
        this.timestamp = System.currentTimeMillis();
    }

}
