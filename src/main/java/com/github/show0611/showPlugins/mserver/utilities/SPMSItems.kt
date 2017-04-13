package com.github.show0611.showPlugins.mserver.utilities

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import kotlin.properties.Delegates

/**
 * Created by show0611 on 17/04/12.
 */
object SPMSItems {
    var itemStack: ItemStack by Delegates.notNull()
    var im: ItemMeta by Delegates.notNull()

    fun Cursor(): ItemStack {
        itemStack = ItemStack(Material.STICK)
        im = itemStack.itemMeta
        im.displayName = "§bCursor"
        im.lore = listOf("#SPItems", "§2Select§a:§r null")
        im.addEnchant(Enchantment.DURABILITY, 0, true)
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        itemStack.itemMeta = im

        return itemStack
    }

    fun EnhancedPickaxe(): ItemStack {
        itemStack = ItemStack(Material.DIAMOND_PICKAXE)
        im = itemStack.itemMeta
        im.displayName = "§bEnhanced Pickaxe"
        im.lore = listOf("#SPItems", "§2Area§a:§r 3x3")
        im.addEnchant(Enchantment.DURABILITY, 0, true)
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        itemStack.itemMeta = im

        return itemStack
    }

    fun checkItemStack(is1: ItemStack, is2: ItemStack): Boolean {
        val im1 = is1.itemMeta
        val im2 = is2.itemMeta
        return when {
            (im1.isUnbreakable && im2.isUnbreakable) &&
                    (im1.displayName.equals(im2.displayName)) &&
                    (im1.enchants.isEmpty() == im2.enchants.isEmpty() && im1.enchants.equals(im2.enchants)) &&
                    (im1.itemFlags.isEmpty() == im2.itemFlags.isEmpty() && im1.itemFlags.equals(im2.itemFlags)) &&
                    (im1.lore.isEmpty() == im2.lore.isEmpty() && im1.lore.equals(im2.lore)) -> true
            else -> false
        }
    }
}