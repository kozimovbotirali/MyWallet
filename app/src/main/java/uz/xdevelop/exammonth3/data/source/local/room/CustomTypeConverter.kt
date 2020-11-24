package uz.xdevelop.exammonth3.data.source.local.room

import com.google.gson.Gson
import java.lang.reflect.Type

object CustomTypeConverter {
    private val gson = Gson()

    private inline fun <reified T> parseArray(json: String, typeToken: Type): T {
        return gson.fromJson(json, typeToken)
    }
/*
    @JvmStatic
    @TypeConverter
    fun fromListData(data: ArrayList<ListData>?): String = gson.toJson(data)

    @JvmStatic
    @TypeConverter
    fun toListData(data: String): ArrayList<ListData>? {
        val type = object : TypeToken<ArrayList<ListData>?>() {}.type
        return parseArray(
            data,
            type
        )
    }

    @JvmStatic
    @TypeConverter
    fun fromListTags(data: ArrayList<String>?): String = gson.toJson(data)

    @JvmStatic
    @TypeConverter
    fun toListTags(data: String): ArrayList<String>? {
        val type = object : TypeToken<ArrayList<String>?>() {}.type
        return parseArray(
            data,
            type
        )
    }*/
}