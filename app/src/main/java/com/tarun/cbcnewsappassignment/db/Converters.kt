package com.tarun.cbcnewsappassignment.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.tarun.cbcnewsappassignment.model.TypeAttributes

/**
 * TypeConverter class required to save custom data classes into the [ArticleDatabase].
 */
object Converters {
    @JvmStatic
    @TypeConverter
    fun fromJsonToTypeAttributes(typeAttributesJson: String?): TypeAttributes? {
        return typeAttributesJson?.let {
            Gson().fromJson(typeAttributesJson, TypeAttributes::class.java)
        }
    }

    @JvmStatic
    @TypeConverter
    fun typeAttributesToJson(typeAttributes: TypeAttributes?) = Gson().toJson(typeAttributes)
}