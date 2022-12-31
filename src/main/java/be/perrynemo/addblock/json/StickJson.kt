package be.perrynemo.addblock.json

import be.perrynemo.addblock.config.ConfigManager

data class StickJson(val parent: String,val textures: Texture,val overrides: ArrayList<Model>) {
    companion object {
        fun getStick(): StickJson {
            val models:ArrayList<Model> = ArrayList()
            ConfigManager.getBlockList().forEach { (name, _) ->
                models.add(Model(CustomModelDataObject(models.size+1), "block/$name"))
            }
            return StickJson("item/handheld", Texture("item/stick"),models)
        }
    }
}


data class Model(val predicate: CustomModelDataObject, val model: String)

data class CustomModelDataObject(val custom_model_data: Int)

data class Texture(val layer0:String)