package com.hicham.wcstoreapp.data.source.db

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun listToString(value: List<String>) = value.joinToString(";")

    @TypeConverter
    fun stringToList(value: String) = value.split(";")
}
