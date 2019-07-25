package me.abhi.hcf.util;

import me.abhi.hcf.faction.Faction;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public static String formattedDtr(Faction faction) {
        double maxDtr = faction.getMembers().size() < 5 ? (faction.getMembers().size() + 0.1) : 5.1;
        double dtr = faction.getDtr();
        if (dtr <= 0.1) {
            return ChatColor.RED + String.valueOf(dtr);
        }
        return ChatColor.GREEN + String.valueOf(faction.getDtr());
    }

    public static List<String> stringsToList(String... strings) {
        List<String> list = new ArrayList();
        for (String string : strings) {
            list.add(string);
        }
        return list;
    }
}
