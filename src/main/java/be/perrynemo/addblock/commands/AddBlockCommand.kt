package be.perrynemo.addblock.commands

import be.perrynemo.addblock.config.ConfigManager
import be.perrynemo.addblock.util.Generator
import net.perrynemo.psapi.perryspigotapi.Command.Command
import net.perrynemo.psapi.perryspigotapi.Command.CommandInfo
import net.perrynemo.psapi.perryspigotapi.Command.PSubCommand
import net.perrynemo.psapi.perryspigotapi.util.PMessage
import org.bukkit.ChatColor
import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.Note
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

import org.bukkit.plugin.java.JavaPlugin

@CommandInfo(name = "addblock", permissions = "block.add", requirePlayer = false)
class AddBlockCommand(main: JavaPlugin) : Command(main) {


    override fun getSynthax(): String {
        return "/addblock"
    }

    override fun tabComplete(sender: CommandSender, args: Array<out String>): MutableList<String> {
        val completers:ArrayList<String> = ArrayList()
        when (args.size) {
            1 -> {
                completers.addAll(arrayListOf(
                    "generate", "reload", "get"
                ))
            }
            2 -> {
                if (args[0] == "get") completers.addAll(ConfigManager.getBlockList().keys)
            }
        }

        return completers
    }

    override fun oneArg(arg: String): PSubCommand {
        if (arg == "generate") return PSubCommand{generateTexture()}
        if (arg == "reload") return PSubCommand { reload(it.sender)}
        return super.oneArg(arg)
    }

    override fun twoArg(args: Array<out String>): PSubCommand {
        if (args[0] == "get") return PSubCommand { getBlock(it.args[1], it.sender as Player) }
        return super.twoArg(args)
    }
    fun generateTexture() {
        Generator.generateStick(main.dataFolder.absolutePath + "/models/item/stick.json")
        Generator.generateBlockstate(main.dataFolder.absolutePath + "/blockstates/note_block.json")
        Generator.generateBlockJson(main.dataFolder.absolutePath+"/models/block")
    }
    fun reload(sender: CommandSender) {
        main.reloadConfig()
        sender.sendMessage("${ChatColor.GREEN}+addBlock vient de reload")
    }

    fun getBlock(arg:String, player:Player) {
        val itemStack:ItemStack = ItemStack(Material.STICK, 1)
        if (!ConfigManager.contains(arg)) {
            return
        }
        val itemMeta: ItemMeta = itemStack.itemMeta!!
        itemMeta.setCustomModelData(ConfigManager.getBlockList()[arg]?.customModeldata)
        itemMeta.setDisplayName("${ChatColor.GRAY}$arg")
        itemStack.itemMeta = itemMeta
        player.inventory.addItem(itemStack)
    }
}