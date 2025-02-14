package com.cettourdev.challenge.data.roomdb

import androidx.room.TypeConverter
import com.cettourdev.challenge.model.ItemAttribute
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * clase que convierte la lista de atributos de un ítem a formato JSON y viceversa
 */

class Converter {
    private val gson = Gson()

    @TypeConverter
    fun fromItemAttributeList(attributes: List<ItemAttribute>?): String {
        return gson.toJson(attributes)
    }

    @TypeConverter
    fun toItemAttributeList(attributesString: String?): List<ItemAttribute> {
        if (attributesString.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<ItemAttribute>>() {}.type
        return gson.fromJson(attributesString, type)
    }
}
