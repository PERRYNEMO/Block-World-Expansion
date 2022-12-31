package be.perrynemo.addblock. listener

import be.perrynemo.addblock.config.ConfigManager
import org.bukkit.FluidCollisionMode
import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.Note
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*



inline fun NoteBlock.edit(body: NoteBlock.() -> Unit) = apply(body)



class BlockListener:Listener {

    private val players:ArrayList<String> = ArrayList()


    @EventHandler
    fun placeBlock(event: PlayerInteractEvent) {
        if (!event.isRightClickWithMainHand() || event.clickedBlock?.isNoteBlock()!!) return
        val name = event.item?.customModelData()?.let { ConfigManager.getBlock(it)?.first } ?: ""
        if (!ConfigManager.contains(name)) return
        updateBlockNote(event.player, name, event.blockFace)
        event.player.addToPlayersList()
    }

    @EventHandler
    fun cancelClickOnNoteBlock(event: PlayerInteractEvent) {
        if (!event.isRightClickWithMainHand() || !event.clickedBlock?.isNoteBlock()!!) return
        event.isCancelled = true
        if (!event.item?.type?.isBlock!! && (event.item?.type ?: Material.AIR) != Material.STICK) return
        if ((event.item?.type ?: Material.AIR) != Material.STICK) {
            updateBlock(event.material, event.player, event.blockFace)
            return
        }
        val name = event.item?.customModelData()?.let { ConfigManager.getBlock(it)?.first } ?: ""
        if (!ConfigManager.contains(name)) return
        if (event.player.isInPlayersList()) {
            event.player.removeFromPlayersList()
            return
        }
        updateBlockNote(event.player, name, event.blockFace)
    }

    private fun PlayerInteractEvent.isRightClickWithMainHand(): Boolean {
        return this.action == Action.RIGHT_CLICK_BLOCK && this.hand == EquipmentSlot.HAND
    }

    private fun Block.isNoteBlock(): Boolean = this.type == Material.NOTE_BLOCK


    private fun ItemStack.customModelData(): Int? = this.itemMeta?.customModelData


    private fun Player.isInPlayersList(): Boolean = players.contains(this.name)


    private fun Player.addToPlayersList() = players.add(this.name)


    private fun Player.removeFromPlayersList() = players.remove(this.name)



    private fun updateBlock(mat: Material, player: Player, blockFace: BlockFace) {
        val block = player.rayTraceBlocks(5.0, FluidCollisionMode.NEVER)?.hitBlock?.getRelative(blockFace) ?: return
        block.type = mat
    }

    private fun updateBlockNote(player: Player, name: String, blockFace: BlockFace) {
        // Get the block that the player is looking at within a 5 block range
        // and get the block next to it in the direction specified by blockFace
        val block = player.rayTraceBlocks(5.0, FluidCollisionMode.NEVER)?.hitBlock?.getRelative(blockFace) ?: return
        // If the block is not air, return from the function
        if (block.type != Material.AIR) return

        // Set the block type to NOTE_BLOCK
        block.type = Material.NOTE_BLOCK
        // Get the NoteBlock data for the block
        val noteBlockData = block.blockData as? NoteBlock ?: return

        // Get the block configuration for the block with the given name from ConfigManager
        with(ConfigManager.getBlockList()[name]) {
            // Edit the NoteBlock data using the configuration values
            noteBlockData.edit {
                this@edit.instrument = this@with?.instrument ?: Instrument.BANJO
                this@edit.note = this@with?.let { Note(it.note) }!!
                this@edit.isPowered = this@with.powered
            }
        }

        // Set the block's data to the modified NoteBlock data
        block.blockData = noteBlockData
    }



    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        var offset = 1
        var aboveBlock = event.block.location.add(0.0, offset.toDouble(), 0.0).block

        // Cancel the event and update the state of any note blocks above the current block
        while (aboveBlock.type == Material.NOTE_BLOCK) {
            event.isCancelled = true
            aboveBlock.state.update(true, true)
            offset++
            aboveBlock = event.block.location.add(0.0, offset.toDouble(), 0.0).block
        }

        // Cancel the event if the current block is a note block
        if (event.block.type == Material.NOTE_BLOCK) {
            event.isCancelled = true
        }

        // Return if the current block is a sign
        if (event.block.type.toString().lowercase(Locale.getDefault()).contains("sign")) {
            return
        }

        // Update the state of the current block
        event.block.state.update(true, false)
    }


}