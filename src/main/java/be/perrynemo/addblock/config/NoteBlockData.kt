package be.perrynemo.addblock.config

import net.perrynemo.psapi.perryspigotapi.util.PMessage
import org.bukkit.Instrument
import org.bukkit.Note

data class NoteBlockData(val instrument: Instrument, val note: Int, val powered: Boolean, val customModeldata:Int) {

    companion object {

        fun getList(): MutableList<NoteBlockData> {
            val possibilities: MutableList<NoteBlockData> = mutableListOf()
            Instrument.values().forEach { instrument ->
                if (instrument != Instrument.PIANO) {
                    (0..24).forEach { i ->
                        possibilities.add(NoteBlockData(instrument, i, true, possibilities.size+1))
                        possibilities.add(NoteBlockData(instrument, i, false, possibilities.size+1))
                    }
                }
            }
            return possibilities
        }
    }

    fun equal(noteBlock: NoteBlockData, notes:Note): Boolean {
        if (noteBlock.instrument != instrument) return false
        if (noteBlock.powered != powered) return false
 //       PMessage.infoMessage(Note(this.note).toString())
//        PMessage.infoMessage(notes.toString())
        if (Note(this.note) != notes) return false
        return true
    }
}
