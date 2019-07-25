package me.abhi.hcf.listener;

import me.abhi.hcf.HCF;
import me.abhi.hcf.chatmode.ChatMode;
import me.abhi.hcf.data.PlayerData;
import me.abhi.hcf.faction.Faction;
import me.abhi.hcf.faction.FactionMember;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private HCF plugin;

    public ChatListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.plugin.getManagerHandler().getPlayerDataManager().getPlayerData(player);
        event.setCancelled(true);
        if (!playerData.getChatMode().equals(ChatMode.GLOBAL) && !playerData.hasFaction()) {
            playerData.setChatMode(ChatMode.GLOBAL);
        }
        if (playerData.getChatMode().equals(ChatMode.GLOBAL)) {
            for (Player players : this.plugin.getServer().getOnlinePlayers()) {
                if (playerData.hasFaction()) {
                    players.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[&e%faction%&6] &7%player%&8: &f%message%").replace("%faction%", playerData.getFaction().getName()).replace("%player%", player.getName()).replace("%message%", event.getMessage().replace("%", "%%")));
                } else {
                    players.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7%player%&8: &f%message%").replace("%player%", player.getName()).replace("%message%", event.getMessage().replace("%", "%%")));
                }
            }
            return;
        }
        if (playerData.getChatMode().equals(ChatMode.FACTION)) {
            for (FactionMember factionMembers : playerData.getFaction().getMembers()) {
                Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
                if (members != null) {
                    members.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3(Faction) %player%: &e%message%").replace("%player%", player.getName()).replace("%message%", event.getMessage().replace("%", "%%")));
                }
            }
            return;
        }
        if (playerData.getChatMode().equals(ChatMode.ALLY)) {
            for (FactionMember factionMembers : playerData.getFaction().getMembers()) {
                Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
                if (members != null) {
                    members.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2(Ally) %player%: &e%message%").replace("%player%", player.getName()).replace("%message%", event.getMessage().replace("%", "%%")));
                }
            }
            for (Faction ally : playerData.getFaction().getAllies()) {
                for (FactionMember factionMembers : ally.getMembers()) {
                    Player members = this.plugin.getServer().getPlayer(factionMembers.getUuid());
                    if (members != null) {
                        members.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2(Ally) %player%: &e%message%").replace("%player%", player.getName()).replace("%message%", event.getMessage().replace("%", "%%")));
                    }
                }
            }
            return;
        }
    }
}
