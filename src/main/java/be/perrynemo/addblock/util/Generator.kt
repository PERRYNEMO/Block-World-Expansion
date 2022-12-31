package be.perrynemo.addblock.util

import be.perrynemo.addblock.json.BlockJson
import be.perrynemo.addblock.json.BlockStateJson
import be.perrynemo.addblock.json.StickJson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.perrynemo.psapi.perryspigotapi.util.PMessage
import java.io.FileWriter
import java.nio.file.Files
import kotlin.io.path.Path

object Generator {

    val gson:Gson = GsonBuilder().disableHtmlEscaping().create();

    fun generateFile(filepath:String) {
        val itemfolder = Path("$filepath/models/item/")
        val blockfolder =Path("$filepath/models/block/")
        val blockstatefolder = Path("$filepath/blockstates/")

        Files.createDirectories(itemfolder)
        Files.createDirectories(blockfolder)
        Files.createDirectories(blockstatefolder)
    }

    fun generateStick(filepath:String){
        val writer = FileWriter(filepath);
        gson.toJson(StickJson.getStick(), writer)
        writer.close()

    }
    fun generateBlockstate(filepath: String) {
        val writer = FileWriter(filepath);
        gson.toJson(BlockStateJson.getBlockState(), writer)
        writer.close()
    }

    fun generateBlockJson(filepath: String) {
        BlockJson.getBlockjson().forEach { (name, block) ->
            val writer = FileWriter("$filepath/$name.json");
            gson.toJson(block, writer)
            writer.close()
        }
    }

}