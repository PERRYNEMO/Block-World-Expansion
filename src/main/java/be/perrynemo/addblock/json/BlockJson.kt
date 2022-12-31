package be.perrynemo.addblock.json

import be.perrynemo.addblock.config.ConfigManager
import com.google.gson.internal.LinkedTreeMap

data class BlockJson(val parent: String, val textures: LinkedHashMap<String, String>) {

    companion object {

        fun getBlockjson(): Map<String, BlockJson> {
            return ConfigManager.getBlockList().map { (name, noteBlockData) ->
                val textures = mapOf(
                    "down" to "block/$name",
                    "east" to "block/$name",
                    "north" to "block/$name",
                    "south" to "block/$name",
                    "up" to "block/$name",
                    "west" to "block/$name",
                    "particle" to "block/$name",
                )
                name to BlockJson("block/cube_all", textures as LinkedHashMap<String, String>)
            }.toMap()
        }
    }
}