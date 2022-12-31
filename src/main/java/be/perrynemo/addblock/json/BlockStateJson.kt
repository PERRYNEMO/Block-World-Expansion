package be.perrynemo.addblock.json

import be.perrynemo.addblock.config.ConfigManager
import be.perrynemo.addblock.config.NoteBlockData
import com.google.gson.internal.LinkedTreeMap


data class BlockStateJson(val variants: Map<String, Variant>) {

    companion object {
        fun getBlockState() : BlockStateJson{
            val map:LinkedTreeMap<String,Variant> = LinkedTreeMap()
            ConfigManager.getBlockList().forEach { (name, noteBlockdata) ->
                map[getVariantString(noteBlockdata)] = Variant("block/$name")
            }
            return BlockStateJson(map)
        }

        fun getVariantString(noteBlockData: NoteBlockData): String {
            val sb = StringBuilder()

            sb.append("instrument=").append(noteBlockData.instrument.name.toLowerCase().replace("bass_drum", "basedrum")
                .replace("sticks","hat").replace("bass_guitar","bass").replace("snare_drum","snare")).append(",")
            sb.append("note=").append(noteBlockData.note).append(",")
            sb.append("powered=".toString()).append(noteBlockData.powered.toString())

            return sb.toString()
        }
    }
}


data class Variant(val model: String)
