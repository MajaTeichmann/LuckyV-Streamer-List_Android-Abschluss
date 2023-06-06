package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.PreferenceManager
import java.util.Locale

class SettingsFragment : Fragment() {
    private lateinit var btnLightMode: Button
    private lateinit var btnDarkMode: Button

    private lateinit var btnGerman: Button
    private lateinit var btnEnglish: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        btnLightMode = view.findViewById(R.id.btn_lightmode)
        btnDarkMode = view.findViewById(R.id.btn_darkmode)

        // Button click listeners
        btnLightMode.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        btnDarkMode.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        btnGerman = view.findViewById(R.id.btn_german)
        btnEnglish = view.findViewById(R.id.btn_english)

        btnGerman.setOnClickListener {
            saveLanguagePreference("de")
            updateAppLanguage("de")
        }

        btnEnglish.setOnClickListener {
            saveLanguagePreference("en")
            updateAppLanguage("en")
        }

        return view
    }

    private fun saveLanguagePreference(language: String) {
        PreferenceManager.saveLanguagePreference(requireContext(), language)
    }

    private fun updateAppLanguage(language: String) {
        // Aktualisiere die Sprache der Textfelder in der gesamten App basierend auf der Spracheinstellung
        val newLocale = Locale(language)
        Locale.setDefault(newLocale)

        val configuration = Configuration()
        configuration.setLocale(newLocale)

        requireContext().resources.updateConfiguration(
            configuration,
            requireContext().resources.displayMetrics
        )

        // Aktualisiere die Textfelder in HomeFragment
        val homeFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.homeFragment) as HomeFragment?
        homeFragment?.updateLanguage()

        // Aktualisiere die Textfelder in com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui.SettingsFragment (dieses Fragment)
        updateLanguage()
    }


    private fun updateLanguage() {
        // Aktualisiere die Textfelder in diesem Fragment (com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui.SettingsFragment)
        val language = PreferenceManager.getLanguagePreference(requireContext())

        val headerTextView = requireView().findViewById<TextView>(R.id.tv_header)
        val displayModusTextView = requireView().findViewById<TextView>(R.id.tv_displayModus)
        val languageTextView = requireView().findViewById<TextView>(R.id.tv_language)

        if (language == "en") {
            headerTextView.text = getString(R.string.en_header_settings)
            displayModusTextView.text = getString(R.string.en_settings_mode)
            languageTextView.text = getString(R.string.settings_en_language)
        } else {
            headerTextView.text = getString(R.string.de_header_settings)
            displayModusTextView.text = getString(R.string.de_settings_mode)
            languageTextView.text = getString(R.string.settings_de_language)
        }
    }
}