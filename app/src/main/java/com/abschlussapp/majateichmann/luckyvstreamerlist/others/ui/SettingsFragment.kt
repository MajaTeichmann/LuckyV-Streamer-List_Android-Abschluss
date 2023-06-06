package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.ActivityMainBinding
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.FragmentSettingsBinding
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.PreferenceManager
import java.util.Locale

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    private lateinit var btnLightMode: Button
    private lateinit var btnDarkMode: Button

    private lateinit var btnGerman: Button
    private lateinit var btnEnglish: Button

    private lateinit var tvHeaderSettingsDe: String
    private lateinit var tvHeaderSettingsEn: String

    //settings_de_language
    private lateinit var tvLanguageDe: String

    //settings_en_language
    private lateinit var tvLanguageEn: String

    //settings_de_language_deutsch
    private lateinit var tvDeutsch: String

    //settings_de_language_englisch
    private lateinit var tvEnglisch: String

    //settings_en_language_german
    private lateinit var tvGerman: String

    //settings_en_language_english
    private lateinit var tvEnglish: String

    //de_settings_mode
    private lateinit var tvModeDe: String

    //en_settings_mode
    private lateinit var tvModeEn: String

    //de_settings_mode_light
    private lateinit var tvModeHell: String

    //en_settings_mode_light
    private lateinit var tvModeLight: String

    //de_settings_mode_dark
    private lateinit var tvModeDunkel: String

    //en_settings_mode_dark
    private lateinit var tvModeDark: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            view.context.setTheme(R.style.Theme_LuckyVStreamerList_Night)
        } else {
            view.context.setTheme(R.style.Theme_LuckyVStreamerList_Day)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLightMode = view.findViewById(R.id.btn_lightmode)
        btnDarkMode = view.findViewById(R.id.btn_darkmode)

        tvHeaderSettingsDe = resources.getString(R.string.de_header_settings)
        tvLanguageDe = resources.getString(R.string.settings_de_language)
        tvDeutsch = resources.getString(R.string.settings_de_language_deutsch)
        tvEnglisch = resources.getString(R.string.settings_de_language_englisch)
        tvModeDe = resources.getString(R.string.de_settings_mode)
        tvModeHell = resources.getString(R.string.de_settings_mode_light)
        tvModeDunkel = resources.getString(R.string.de_settings_mode_dark)

        tvHeaderSettingsEn = resources.getString(R.string.en_header_settings)
        tvLanguageEn = resources.getString(R.string.settings_en_language)
        tvGerman = resources.getString(R.string.settings_en_language_german)
        tvEnglish = resources.getString(R.string.settings_en_language_english)
        tvModeEn = resources.getString(R.string.en_settings_mode)
        tvModeLight = resources.getString(R.string.en_settings_mode_light)
        tvModeDark = resources.getString(R.string.en_settings_mode_dark)

        // Button click listeners
        btnLightMode.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        btnDarkMode.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // Dark Mode aktiviert
            binding.tvHeader.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.night_txt_luckyv_white)
            )
            binding.tvDisplayModus.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.night_txt_luckyv_white)
            )
            binding.tvLanguage.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.night_txt_luckyv_white)
            )
        } else {
            // Light Mode aktiviert
            binding.tvHeader.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.day_txt_luckyv_black)
            )
            binding.tvDisplayModus.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.day_txt_luckyv_black)
            )
            binding.tvLanguage.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.day_txt_luckyv_black)
            )
        }

        btnGerman = view.findViewById(R.id.btn_german)
        btnEnglish = view.findViewById(R.id.btn_english)

        btnGerman.setOnClickListener {
            binding.tvHeader.text = tvHeaderSettingsDe
            binding.tvDisplayModus.text = tvModeDe
            binding.btnLightmode.text = tvModeHell
            binding.btnDarkmode.text = tvModeDunkel
            binding.tvLanguage.text = tvLanguageDe
            binding.btnGerman.text = tvDeutsch
            binding.btnEnglish.text = tvEnglisch

            saveLanguagePreference("de")
            updateAppLanguage("de")
        }

        btnEnglish.setOnClickListener {
            binding.tvHeader.text = tvHeaderSettingsEn
            binding.tvDisplayModus.text = tvModeEn
            binding.btnLightmode.text = tvModeLight
            binding.btnDarkmode.text = tvModeDark
            binding.tvLanguage.text = tvLanguageEn
            binding.btnGerman.text = tvGerman
            binding.btnEnglish.text = tvEnglish


            saveLanguagePreference("en")
            updateAppLanguage("en")
        }
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

        // Aktualisiere die Textfelder in SettingsFragment (dieses Fragment)
        updateLanguage()
    }


    private fun updateLanguage() {
        // Aktualisiere die Textfelder in diesem Fragment (SettingsFragment)
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