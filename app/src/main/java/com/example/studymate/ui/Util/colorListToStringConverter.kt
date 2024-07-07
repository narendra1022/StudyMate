package com.example.studymate.ui.Util

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

class colorListToStringConverter {

    @TypeConverter
    fun listToString(colorList: List<Int>): String {
        return colorList.joinToString(",") { it.toString() }
    }

    @TypeConverter
    fun stringToList(colorList: String): List<Int> {
        return colorList.split(",").map { it.toInt() }
    }
}