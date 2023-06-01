package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui


//TODO: ZUSATZ

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.abschlussapp.majateichmann.luckyvstreamerlist.R

class SettingsFragment : Fragment() {
    private lateinit var btnLightMode: Button
    private lateinit var btnDarkMode: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        return view
    }
}
