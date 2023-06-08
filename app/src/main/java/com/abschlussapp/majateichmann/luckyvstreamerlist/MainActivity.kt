package com.abschlussapp.majateichmann.luckyvstreamerlist


import android.content.ContentProvider
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.ActivityMainBinding
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.AppRepository
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.local.getDatabase
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.remote.StreamerApi
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui.HomeFragment
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui.MainViewModel
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.util.Locale

/** Main Activity, dient als Einstiegspunkt für die App */
class MainActivity : AppCompatActivity(), LanguageChangeListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel
    private lateinit var repository: AppRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        theme = theme
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Erstellen des SettingsFragments und HomeFragments
        val settingsFragment = SettingsFragment()

        // Füge den LanguageChangeListener zum HomeFragment hinzu
        val homeFragment = HomeFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.nav_host_fragment, homeFragment)
//            .commit()

        // Setzen des SettingsFragment als LanguageChangeListener für das HomeFragment
        settingsFragment.languageChangeListener = homeFragment

        val isNightMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            val textColor = ContextCompat.getColor(this, R.color.night_txt_luckyv_white)
            binding.tvHeader.setTextColor(textColor)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            val textColor = ContextCompat.getColor(this, R.color.day_txt_luckyv_black)
            binding.tvHeader.setTextColor(textColor)
        }

        /** Repository-Objekt erstellen und Context übergeben */
        repository = AppRepository(StreamerApi, getDatabase(applicationContext), applicationContext)

        /** ViewModel initialisieren */
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val navBar: BottomNavigationView = findViewById(R.id.navBar)
        navBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    /** Aktion für "Home" ausführen */
                    if (navController.currentDestination?.id != R.id.homeFragment) {
                        navController.navigate(R.id.homeFragment)
                    }
                    true
                }

                R.id.navigation_favorites -> {
                    /** Aktion für "Favorites" ausführen */
                    if (navController.currentDestination?.id != R.id.favoritesFragment) {
                        navController.navigate(R.id.favoritesFragment)
                    }
                    true
                }

                R.id.navigation_settings -> {
                    /** Aktion für "Settings" ausführen */
                    if (navController.currentDestination?.id != R.id.settingsFragment) {
                        navController.navigate(R.id.settingsFragment)
                    }
                    true
                }

                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.startFragment) {
                navBar.visibility = View.GONE
            } else {
                navBar.visibility = View.VISIBLE
            }
        }

        /** Zeige das ConstraintLayout standardmäßig an */
        binding.clAppHeader.visibility = View.VISIBLE

        /** Mittels Coroutine Streamerdaten aus der API laden */
        lifecycleScope.launch {
            repository.getStreamer()
        }
    }

    override fun onLanguageChanged() {
        val currentLanguage = Locale.getDefault().language

        when (currentLanguage) {
            "de" -> {
                val homeFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? HomeFragment
                homeFragment?.updateTextViewsForGerman()
            }
            "en" -> {
                val homeFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? HomeFragment
                homeFragment?.updateTextViewsForEnglish()
            }
            // Weitere Sprachen hinzufügen...
        }
    }

    /** Funktionen zum ein- und ausblenden der TopApp Bar */
    fun ausblenden() {
        binding.tvHeader.visibility = View.INVISIBLE
        binding.ivLuckyvLogo.alpha = 0F
    }

    fun einblenden() {
        binding.tvHeader.visibility = View.VISIBLE
        binding.ivLuckyvLogo.alpha = 1F
    }
}