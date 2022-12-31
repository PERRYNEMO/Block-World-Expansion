package be.perrynemo.addblock

import be.perrynemo.addblock.commands.AddBlockCommand
import be.perrynemo.addblock.config.ConfigLoader
import be.perrynemo.addblock.listener.BlockListener
import be.perrynemo.addblock.listener.MiddelClickListener
import be.perrynemo.addblock.util.Generator
import net.perrynemo.psapi.perryspigotapi.loader.CommandLoader
import net.perrynemo.psapi.perryspigotapi.loader.EventLoader
import net.perrynemo.psapi.perryspigotapi.util.PMessage
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
            private set
    }

    override fun onEnable() {
        instance = this
        PMessage.getInstance(this)
        reloadConfig()
        val commandLoader = CommandLoader.getInstance(this)
        commandLoader.addCommand(AddBlockCommand::class.java)
        commandLoader.load()
        val eventLoader = EventLoader.getEventLoader(this)
        eventLoader.addClass(BlockListener::class.java)
        eventLoader.addClass(MiddelClickListener::class.java)
        eventLoader.load()

    }
    override fun onDisable() {
        // Plugin shutdown logic
    }

    override fun reloadConfig() {
        Generator.generateFile(dataFolder.absolutePath)
        super.reloadConfig();

        saveDefaultConfig();

        config.options().copyDefaults(true);

        saveConfig();
        val configLoader = ConfigLoader.instance
        configLoader.load()
    }



}