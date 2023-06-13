package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.abschlussapp.majateichmann.luckyvstreamerlist.LanguageChangeListener
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.FragmentSettingsBinding
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.PreferenceManager
import java.util.Locale

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var btnLightMode: Button
    private lateinit var btnDarkMode: Button

    private lateinit var btnGerman: Button
    private lateinit var btnEnglish: Button

    private lateinit var tvHeaderSettings: TextView
    private lateinit var tvLanguage: TextView
    private lateinit var tvMode: TextView

    private lateinit var tvModeLight: String
    private lateinit var tvModeDark: String
    private lateinit var tvLanguageGerman: String
    private lateinit var tvLanguageEnglish: String

    private lateinit var btnLuckyV: Button
    private lateinit var btnStreamerList: Button
    private lateinit var tvQuellen: TextView

    var languageChangeListener: LanguageChangeListener? = null

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
        btnGerman = view.findViewById(R.id.btn_german)
        btnEnglish = view.findViewById(R.id.btn_english)

        tvHeaderSettings = view.findViewById(R.id.tv_header)
        tvLanguage = view.findViewById(R.id.tv_language)
        tvMode = view.findViewById(R.id.tv_displayModus)

        tvModeLight = resources.getString(R.string.de_settings_mode_light)
        tvModeDark = resources.getString(R.string.de_settings_mode_dark)
        tvLanguageGerman = resources.getString(R.string.settings_de_language_deutsch)
        tvLanguageEnglish = resources.getString(R.string.settings_de_language_englisch)

        btnLuckyV = view.findViewById(R.id.btn_luckyv)
        btnStreamerList = view.findViewById(R.id.btn_streamerList)
        tvQuellen = view.findViewById(R.id.tv_quellen)

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
            binding.tvQuellen.setTextColor(
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
            binding.tvQuellen.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.day_txt_luckyv_black)
            )
        }

        btnGerman.setOnClickListener {
            updateLanguage("de")
        }

        btnEnglish.setOnClickListener {
            updateLanguage("en")
        }

        updateLanguage(PreferenceManager.getLanguagePreference(requireContext()))
    }

    private fun updateLanguage(language: String) {
        val isEnglish = language == "en"

        tvHeaderSettings.text =
            getString(if (isEnglish) R.string.en_header_settings else R.string.de_header_settings)
        tvLanguage.text =
            getString(if (isEnglish) R.string.settings_en_language else R.string.settings_de_language)
        tvMode.text =
            getString(if (isEnglish) R.string.en_settings_mode else R.string.de_settings_mode)
        btnLightMode.text =
            getString(if (isEnglish) R.string.en_settings_mode_light else R.string.de_settings_mode_light)
        btnDarkMode.text =
            getString(if (isEnglish) R.string.en_settings_mode_dark else R.string.de_settings_mode_dark)
        btnGerman.text =
            getString(if (isEnglish) R.string.settings_en_language_german else R.string.settings_de_language_deutsch)
        btnEnglish.text =
            getString(if (isEnglish) R.string.settings_en_language_english else R.string.settings_de_language_englisch)

        tvQuellen.text =
            getString(if (isEnglish) R.string.en_settings_quellen else R.string.de_settings_quellen)


        // Update the language preference
        PreferenceManager.setLanguagePreference(requireContext(), language)

        // Sprach-LiveData mit postValue aktualisieren
        viewModel.setLanguage(language)


        // Spracheinstellung der App aktualisieren

        //neues Locale-Objekt mit der angegebenen Sprache erstellen & als Standardsprache setzen
        val locale = Locale(language)
        Locale.setDefault(locale)

        //neue Configuration-Instanz erstellen & mit dem neuen Locale konfigurieren
        val config = Configuration()
        config.setLocale(locale)

        //Configuration auf App anwenden
        resources.updateConfiguration(config, resources.displayMetrics)

        // HomeFragment über Änderung der Sprache infomieren
        languageChangeListener?.onLanguageChanged()

        // setOnClickListener
        btnLuckyV.setOnClickListener {
            val url = "https://www.luckyv.de/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        btnStreamerList.setOnClickListener {
            val url = "https://luckyv-streamer.frozenpenguin.media/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}