package com.abschlussapp.majateichmann.luckyvstreamerlist.Converter

import androidx.room.TypeConverter

class Converter {
    @TypeConverter
    fun fromNullableString(value: String?): String {
        return value ?: ""
    }

    fun toNullableString(value: String): String? {
        return if (value.isEmpty()) null else value
    }
}