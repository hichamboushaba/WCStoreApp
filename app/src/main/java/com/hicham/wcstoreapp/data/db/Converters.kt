package com.hicham.wcstoreapp.data.db

import androidx.room.TypeConverter
import java.math.BigDecimal

class Converters {
    @TypeConverter
    fun listToString(value: List<String>) = value.joinToString(";")

    @TypeConverter
    fun stringToList(value: String) = value.split(";")

    @TypeConverter
    fun bigDecimalToString(value: BigDecimal?) = value?.toPlainString()

    @TypeConverter
    fun stringToBigDecimal(value: String?) = value?.let { BigDecimal(it) }
}
