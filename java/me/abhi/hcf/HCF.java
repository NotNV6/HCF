package me.abhi.hcf;

import lombok.Getter;
import me.abhi.hcf.commands.*;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.listener.*;
import me.abhi.hcf.manager.ManagerHandler;
import me.abhi.hcf.runnable.ClaimRunnable;
import me.abhi.hcf.runnable.CombatRunnable;
import me.abhi.hcf.runnable.ScoreboardRunnable;
import me.abhi.hcf.util.Logger;
import me.abhi.hcf.util.ScoreHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

@Getter
public class HCF extends JavaPlugin {

    @Getter
    private static HCF instance;
    private ManagerHandler managerHandler;
    private FileConfiguration factionsFile;
    private FileConfiguration balancesFile;

    public void onEnable() {
        instance = this;
        createFiles();
        registerManagers();
        registerListeners();
        registerCommands();
        registerPlayers();
        registerRunnables();
    }

    public void onDisable() {
        saveFactions();
        savePlayerData();
    }

    private void createFiles() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        try {
            File fFile = new File(getDataFolder(), "factions.yml");
            if (!fFile.exists()) 
                fFile.createNewFile();
            
            
            File bFile = new File(getDataFolder(), "balances.yml");
            if (!bFile.exists()) 
                bFile.createNewFile();
            
            balancesFile = YamlConfiguration.loadConfiguration(bFile);
        } catch (Exception ex) {
            Logger.log("Error loading config. Please report the following error:");
            ex.printStackTrace();
        }
    }

    private void registerManagers() {
        managerHandler = new ManagerHandler(this);
    }

    private void registerListeners() {
        Arrays.asList(
            new LoginListener(this),
            new JoinListener(this),
            new QuitListener(this),
            new ChatListener(this),
            new CooldownListener(this),
            new DamageListener(this),
            new InteractListener(this),
            new MoveListener(this),
            new ShutdownListener(this),
            new RespawnListener(this),
            new ConsumeListener(this),
            new HungerListener(this)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

    }

    private void registerCommands() {
        getCommand("factions").setExecutor(new FactionsCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("pvp").setExecutor(new PvPCommand(this));
        getCommand("economy").setExecutor(new EconomyCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("logout").setExecutor(new LogoutCommand(this));
        getCommand("sotw").setExecutor(new SOTWCommand(this));
        getCommand("modmode").setExecutor(new ModModeCommand(this));
    }

    private void registerPlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            managerHandler.getPlayerDataManager().addPlayer(player);
            ScoreHelper.createScore(player);
            managerHandler.getFactionsManager().findFaction(player);
            new CombatRunnable(this, player).runTaskTimerASynchronously(this);
            if (player.hasPermission("hcf.admin")) {
                PlayerData playerData = managerHandler.getPlayerDataManager().getPlayerData(player);
                playerData.setStaffMode(true);
            }
        });
    }

    private void registerRunnables() {
        new ScoreboardRunnable(this).runTaskTimerASynchronously(this);
        new ClaimRunnable(this).runTaskTimerASynchronously(this);
    }

    private void saveFactions() {
        managerHandler.getFactionsManager().saveFactions();
    }

    private void savePlayerData() {
        managerHandler.getPlayerDataManager().getPlayerDataMap().keySet().forEach(uuid -> 
            managerHandler.getPlayerDataManager().getPlayerDataMap().get(uuid).save()
        );
    }

    public void saveFactionsFile() {
        try {
            File fFile = new File(getDataFolder(), "factions.yml");
            factionsFile.save(fFile);
        } catch (Exception ex) {
            Logger.log("Error saving factions.yml. Please report the following error:");
            ex.printStackTrace();
        }
    }

    public void saveBalancesFile() {
        try {
            File bFile = new File(getDataFolder(), "balances.yml");
            balancesFile.save(bFile);
        } catch (Exception ex) {
            Logger.log("Error saving balances.yml. Please report the following error:");
            ex.printStackTrace();
        }
    }
}
