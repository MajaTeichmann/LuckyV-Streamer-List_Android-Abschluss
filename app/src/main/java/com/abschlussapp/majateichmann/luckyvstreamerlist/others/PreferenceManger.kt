package com.abschlussapp.majateichmann.luckyvstreamerlist.others

import android.content.Context

class PreferenceManager {
    companion object {
        private const val PREFERENCE_NAME = "app_preferences"
        private const val KEY_LANGUAGE = "language"

        fun saveLanguagePreference(context: Context, language: String) {
            val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(KEY_LANGUAGE, language)
            editor.apply()
        }

        fun getLanguagePreference(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_LANGUAGE, "de") ?: "de"
        }
    }
}