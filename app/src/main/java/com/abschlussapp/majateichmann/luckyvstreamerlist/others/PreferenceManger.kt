package com.abschlussapp.majateichmann.luckyvstreamerlist.others

import android.content.Context
import android.preference.PreferenceManager

class PreferenceManager {
    companion object {
        private const val LANGUAGE_PREF_KEY = "language_pref"

        fun setLanguagePreference(context: Context, language: String) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putString(LANGUAGE_PREF_KEY, language)
            editor.apply()
        }

        fun getLanguagePreference(context: Context): String {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(LANGUAGE_PREF_KEY, "en") ?: "en"
        }
    }
}