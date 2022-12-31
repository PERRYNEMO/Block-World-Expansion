package be.perrynemo.addblock.listener

import be.perrynemo.addblock.config.ConfigManager
import be.perrynemo.addblock.config.NoteBlockData
import net.perrynemo.psapi.perryspigotapi.util.PMessage
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCreativeEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.CreativeCategory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta


class MiddelClickListener: Listener {

    @EventHandler
    fun detectclickmolet(event:InventoryCreativeEvent) {
        if (event.cursor.type != Material.NOTE_BLOCK) return
        if (event.whoClicked !is Player) return
        val player = event.whoClicked
        val block = player.getTargetBlock(null, 5)
        if (block.type != Material.NOTE_BLOCK) return
        val noteBlock:NoteBlock = block.blockData as NoteBlock
        val noteBlockData = NoteBlockData(noteBlock.instrument, 0, noteBlock.isPowered, 0)

        val pairs = ConfigManager.getBlock(noteBlockData, noteBlock.note) ?: return
        val itemStack: ItemStack = ItemStack(Material.STICK, 1)
        if (!ConfigManager.contains(pairs.first)) return
        event.isCancelled = true
        val itemMeta: ItemMeta = itemStack.itemMeta!!
        itemMeta.setCustomModelData(pairs.second.customModeldata)
        itemMeta.setDisplayName("${ChatColor.GRAY}${pairs.first}")
        itemStack.itemMeta = itemMeta
        if (!player.inventory.contains(itemStack)) player.inventory.addItem(itemStack)
        if (player.inventory.first(itemStack)>8) return
        player.inventory.heldItemSlot = player.inventory.first(itemStack)
    }



}