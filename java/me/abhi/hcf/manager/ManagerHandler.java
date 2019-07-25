package me.abhi.hcf.manager;

import lombok.Getter;
import me.abhi.hcf.HCF;
import me.abhi.hcf.managers.*;

@Getter
public class ManagerHandler {

    private HCF plugin;
    private ServerManager serverManager;
    private PlayerDataManager playerDataManager;
    private ScoreboardManager scoreboardManager;
    private FactionsManager factionsManager;
    private ClaimManager claimManager;

    public ManagerHandler(HCF plugin) {
        this.plugin = plugin;
        registerManagers();
    }

    private void registerManagers() {
        serverManager = new ServerManager(this);
        playerDataManager = new PlayerDataManager(this);
        scoreboardManager = new ScoreboardManager(this);
        factionsManager = new FactionsManager(this);
        claimManager = new ClaimManager(this);
    }
}
