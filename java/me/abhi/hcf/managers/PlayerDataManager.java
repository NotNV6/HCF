package me.abhi.hcf.managers;

import lombok.Getter;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.manager.Manager;
import me.abhi.hcf.manager.ManagerHandler;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerDataManager extends Manager {

    private Map<UUID, PlayerData> playerDataMap;

    public PlayerDataManager(ManagerHandler managerHandler) {
        super(managerHandler);
        playerDataMap = new HashMap();
    }

    public void addPlayer(Player player) {
        playerDataMap.put(player.getUniqueId(), new PlayerData(player.getUniqueId()));
    }

    public void removePlayer(Player player) {
        playerDataMap.remove(player.getUniqueId());
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public boolean hasPlayerData(Player player) {
        return playerDataMap.containsKey(player.getUniqueId());
    }

}
