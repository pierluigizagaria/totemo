package com.pierluigizagaria.totemo.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    @JvmStatic
    @TypeConverter
    fun fromJson(value: String?): Array<String> {
        val type = object : TypeToken<Array<String?>?>() {}.type
        return Gson().fromJson(value, type)
    }

    @JvmStatic
    @TypeConverter
    fun toJson(list: Array<String?>?): String {
        return Gson().toJson(list)
    }
}