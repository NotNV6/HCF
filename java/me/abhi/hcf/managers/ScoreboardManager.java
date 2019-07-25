package me.abhi.hcf.managers;

import me.abhi.hcf.chatmode.ChatMode;
import me.abhi.hcf.cooldown.Cooldown;
import me.abhi.hcf.cooldown.ServerCooldown;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.manager.Manager;
import me.abhi.hcf.manager.ManagerHandler;
import me.abhi.hcf.util.ScoreHelper;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardManager extends Manager {

    public ScoreboardManager(ManagerHandler managerHandler) {
        super(managerHandler);
    }

    public void updatePlayer(Player player) {
        try {
            if (managerHandler.getPlayerDataManager().hasPlayerData(player) && ScoreHelper.hasScore(player)) {
                PlayerData playerData = managerHandler.getPlayerDataManager().getPlayerData(player);
                ScoreHelper scoreHelper = ScoreHelper.getByPlayer(player);
                List<String> slots = new ArrayList();
                scoreHelper.setTitle("&6&lHCFactions &c[Map 1]");
                slots.add("&7&m--------------------");
                if (playerData.isStaffMode()) {
                    slots.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Staff Mode:");
                    slots.add(ChatColor.GOLD + " » " + ChatColor.YELLOW + "Visibility: " + (playerData.isVanished() ? ChatColor.GREEN + "Vanished" : ChatColor.RED + "Visible"));
                    slots.add(ChatColor.GOLD + " » " + ChatColor.YELLOW + "Gamemode: " + (player.getGameMode().equals(GameMode.CREATIVE) ? ChatColor.GREEN + "Creative" : ChatColor.RED + "Survival"));
                    slots.add(ChatColor.GOLD + " » " + ChatColor.YELLOW + "Staff Chat: " + (playerData.getChatMode().equals(ChatMode.STAFF) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
                    if (playerData.getCooldownMap().size() > 0 || this.managerHandler.getServerManager().getCooldownMap().size() > 0) {
                        slots.add("&7&m--------------------");
                    }
                }
                if (this.managerHandler.getServerManager().hasCooldown("sotw")) {
                    ServerCooldown serverCooldown = this.managerHandler.getServerManager().getCooldown("sotw");
                    slots.add("&a&lSOTW&7: &c" + serverCooldown.getTime());
                }
                if (playerData.hasCooldown("pvp-timer")) {
                    Cooldown pvpTimer = playerData.getCooldown("pvp-timer");
                    slots.add("&a&lPvPTimer&7: &c" + pvpTimer.getTime());
                }
                if (playerData.hasCooldown("combat")) {
                    Cooldown combat = playerData.getCooldown("combat");
                    slots.add("&4&lCombat&7: &c" + combat.getTime());
                }
                if (playerData.hasCooldown("enderpearl")) {
                    Cooldown enderpearl = playerData.getCooldown("enderpearl");
                    slots.add("&e&lEnderpearl&7: &c" + enderpearl.getTime());
                }
                if (playerData.hasCooldown("home")) {
                    Cooldown home = playerData.getCooldown("home");
                    slots.add("&d&lHome&7: &c" + home.getTime());
                }
                if (playerData.hasCooldown("golden-apple")) {
                    Cooldown goldenApple = playerData.getCooldown("golden-apple");
                    slots.add("&5&lGapple&7: &c" + goldenApple.getTime());
                }
                if (playerData.hasCooldown("god-apple")) {
                    Cooldown goldenApple = playerData.getCooldown("god-apple");
                    slots.add("&6&lGopple&7: &c" + goldenApple.getTime());
                }
                if (playerData.hasCooldown("logout")) {
                    Cooldown logout = playerData.getCooldown("logout");
                    slots.add("&4&lLogout&7: &c" + logout.getTime());
                }
                slots.add("&7&m--------------------");
                if (slots.size() == 2) {
                    for (int i = 1; i <= 15; i++) {
                        scoreHelper.removeSlot(i);
                    }
                    return;
                }
                scoreHelper.setSlotsFromList(slots);
            }
        } catch (Exception ex) {
            // Remove random error in console
        }
    }

    public void updateNames(Player player) {
        if (managerHandler.getPlayerDataManager().hasPlayerData(player) && ScoreHelper.hasScore(player)) {
            PlayerData playerData = managerHandler.getPlayerDataManager().getPlayerData(player);
            ScoreHelper scoreHelper = ScoreHelper.getByPlayer(player);
            for (Player players : managerHandler.getPlugin().getServer().getOnlinePlayers()) {
                String correlation = "enemy";
                if (playerData.hasFaction()) {
                    if (playerData.getFaction().isFactionMember(players.getUniqueId())) {
                        correlation = "faction";
                    }
                    for (Faction ally : playerData.getFaction().getAllies()) {
                        if (ally.isFactionMember(players.getUniqueId())) {
                            correlation = "ally";
                        }
                    }
                }
                if (correlation.equalsIgnoreCase("enemy") && !scoreHelper.getEnemy().getEntries().contains(players.getName())) {
                    scoreHelper.getEnemy().addEntry(players.getName());
                    if (scoreHelper.getAlly().getEntries().contains(players.getName())) {
                        scoreHelper.getAlly().getEntries().remove(players.getName());
                    }
                    if (scoreHelper.getFaction().getEntries().contains(players.getName())) {
                        scoreHelper.getFaction().getEntries().remove(players.getName());
                    }
                } else if (correlation.equalsIgnoreCase("faction") && !scoreHelper.getFaction().getEntries().contains(players.getName())) {
                    scoreHelper.getFaction().addEntry(players.getName());
                    if (scoreHelper.getAlly().getEntries().contains(players.getName())) {
                        scoreHelper.getAlly().getEntries().remove(players.getName());
                    }
                    if (scoreHelper.getEnemy().getEntries().contains(players.getName())) {
                        scoreHelper.getEnemy().getEntries().remove(players.getName());
                    }
                } else if (correlation.equalsIgnoreCase("ally") && !scoreHelper.getAlly().getEntries().contains(players.getName())) {
                    scoreHelper.getAlly().addEntry(players.getName());
                    if (scoreHelper.getFaction().getEntries().contains(players.getName())) {
                        scoreHelper.getFaction().getEntries().remove(players.getName());
                    }
                    if (scoreHelper.getEnemy().getEntries().contains(players.getName())) {
                        scoreHelper.getEnemy().getEntries().remove(players.getName());
                    }
                }
            }
        }
    }
}
