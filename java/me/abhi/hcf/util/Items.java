package me.abhi.hcf.util;

import lombok.Getter;
import me.abhi.hcf.HCF;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum Items {

    CLAIM_WAND(new ItemBuilder(Material.GOLD_HOE).setName(ChatColor.GOLD + "Claim Wand").setLore(StringUtil.stringsToList("&7&m------------------------", "&eLeft Click Block - Select First Point", "&eRight Click Block - Select Second Point", "&eShift/Left Click Air - Claim Land", "&eRight Click Air - Clear Claim", "&7&m------------------------")).toItemStack());

    private ItemStack itemStack;

    private Items(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
