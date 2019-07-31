package me.abhi.hcf.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {

    public static void log(String message) { Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l[HCF] &f" + msg)); }
}
