package be.perrynemo.addblock.config

import net.perrynemo.psapi.perryspigotapi.util.PMessage
import org.bukkit.Note

object ConfigManager {


    private val blocklist: LinkedHashMap<String, NoteBlockData> = linkedMapOf()
    private val dataList: MutableList<NoteBlockData> = NoteBlockData.getList()

    fun getBlockList(): MutableMap<String, NoteBlockData> {
        return blocklist.toMutableMap()
    }

    fun addBlock(name: String) {
        blocklist.getOrPut(name) { dataList[blocklist.size] }
    }
    fun removeBlock(name: String) {
        if (contains(name)) blocklist.remove(name)
    }
    fun addAll(list: ArrayList<String>) {
        list.forEach() { addBlock(it) }
    }
    fun contains(name: String): Boolean {
        return blocklist.containsKey(name)
    }
    fun getBlock(data: NoteBlockData, note:Note) : Pair<String, NoteBlockData>? {
        blocklist.forEach { (t, u) ->
            if (u.equal(data,note)) return Pair(t,u)
        }
        return null
    }
    fun getBlock(cutsomModelData:Int) : Pair<String, NoteBlockData>? {
        blocklist.forEach { (t, u) ->
            if (u.customModeldata == cutsomModelData) return Pair(t,u)
        }
        return null
    }


}