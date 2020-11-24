package uz.xdevelop.exammonth3.data.source.local.storage

import android.content.Context

class LocalStorage private constructor(context: Context) {
    companion object {
        lateinit var instance: LocalStorage; private set

        fun init(context: Context) {
            instance =
                LocalStorage(
                    context
                )
        }
    }

    private val pref = context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE)
//    private val pref = SecurePreferences(context, "55555", "LocalStorage")

    var isFirst: Boolean by BooleanPreferenceDefTrue(pref)
    var remember: Boolean by BooleanPreference(pref)
    var token: String by StringPreference(pref)

    fun clear() {
        pref.edit().clear().apply()
    }
}