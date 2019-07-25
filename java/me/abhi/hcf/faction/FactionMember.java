package me.abhi.hcf.faction;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FactionMember {

    private UUID uuid;
    private FactionRole factionRole;

    public FactionMember(UUID uuid, FactionRole factionRole) {
        this.uuid = uuid;
        this.factionRole = factionRole;
    }

}
