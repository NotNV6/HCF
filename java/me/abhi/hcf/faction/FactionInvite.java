package me.abhi.hcf.faction;

import lombok.Getter;
import lombok.Setter;

@Getter
public class FactionInvite {

    private Faction faction;
    private long timestamp;
    @Setter
    private boolean cancelled;

    public FactionInvite(Faction faction) {
        this.faction = faction;
        this.timestamp = System.currentTimeMillis();
    }
}
