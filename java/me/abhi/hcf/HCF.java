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
            if (!fFile.exists()) {
                fFile.createNewFile();
            }
            factionsFile = YamlConfiguration.loadConfiguration(fFile);
        } catch (Exception ex) {
            Logger.log("Error loading factions.yml. Please report the following error:");
            ex.printStackTrace();
        }
        try {
            File bFile = new File(getDataFolder(), "balances.yml");
            if (!bFile.exists()) {
                bFile.createNewFile();
            }
            balancesFile = YamlConfiguration.loadConfiguration(bFile);
        } catch (Exception ex) {
            Logger.log("Error loading balances.yml. Please report the following error:");
            ex.printStackTrace();
        }
    }

    private void registerManagers() {
        managerHandler = new ManagerHandler(this);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new LoginListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new CooldownListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractListener(this), this);
        getServer().getPluginManager().registerEvents(new MoveListener(this), this);
        getServer().getPluginManager().registerEvents(new ShutdownListener(this), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new ConsumeListener(this), this);
        getServer().getPluginManager().registerEvents(new HungerListener(this), this);
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
        for (Player player : getServer().getOnlinePlayers()) {
            managerHandler.getPlayerDataManager().addPlayer(player);
            ScoreHelper.createScore(player);
            managerHandler.getFactionsManager().findFaction(player);
            new CombatRunnable(this, player).runTaskTimerAsynchronously(this, 0L, 0L);
            if (player.hasPermission("hcf.admin")) {
                PlayerData playerData = managerHandler.getPlayerDataManager().getPlayerData(player);
                playerData.setStaffMode(true);
            }
        }
    }

    private void registerRunnables() {
        new ScoreboardRunnable(this).runTaskTimerAsynchronously(this, 0L, 0L);
        new ClaimRunnable(this).runTaskTimerAsynchronously(this, 0L, 0L);
    }

    private void saveFactions() {
        managerHandler.getFactionsManager().saveFactions();
    }

    private void savePlayerData() {
        for (UUID uuid : managerHandler.getPlayerDataManager().getPlayerDataMap().keySet()) {
            PlayerData playerData = managerHandler.getPlayerDataManager().getPlayerDataMap().get(uuid);
            playerData.save();
        }
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
