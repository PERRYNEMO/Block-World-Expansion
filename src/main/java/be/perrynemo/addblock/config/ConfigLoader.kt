package be.perrynemo.addblock.config

import be.perrynemo.addblock.Main
import net.perrynemo.psapi.perryspigotapi.loader.PerryLoader
import org.bukkit.configuration.file.FileConfiguration

class ConfigLoader() : PerryLoader(Main.instance) {

    private lateinit var config:FileConfiguration

    override fun getName(): String {
        return "config"
    }

    override fun loadDataFromMain() {
        config = main.config
    }

    override fun loadDataFromConfig() {
        ConfigManager.addAll(config.getStringList("blocks") as ArrayList<String>);
    }

    override fun generateResponce() {

    }

    companion object {
        val instance: ConfigLoader by lazy { ConfigLoader() }
    }
}