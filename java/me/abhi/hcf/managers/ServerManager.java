package me.abhi.hcf.managers;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import me.abhi.hcf.cooldown.Cooldown;
import me.abhi.hcf.cooldown.ServerCooldown;
import me.abhi.hcf.manager.Manager;
import me.abhi.hcf.manager.ManagerHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class ServerManager extends Manager {

    private ExecutorService executorService;
    private Map<String, ServerCooldown> cooldownMap;

    public ServerManager(ManagerHandler managerHandler) {
        super(managerHandler);
        executorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("HCF ForceField Thread").build());
        cooldownMap = new HashMap();
    }

    public void addCooldown(ServerCooldown cooldown) {
        cooldownMap.put(cooldown.getId(), cooldown);
    }

    public boolean hasCooldown(String id) {
        return cooldownMap.containsKey(id);
    }

    public ServerCooldown getCooldown(String id) {
        return cooldownMap.get(id);
    }

    public void removeCooldown(String id) {
        cooldownMap.remove(id);
    }
}
