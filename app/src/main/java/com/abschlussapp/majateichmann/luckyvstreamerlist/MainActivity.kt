package com.abschlussapp.majateichmann.luckyvstreamerlist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.TAG


/**
 * Main Activity, dient als Einstiegspunkt für die App
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    //damit Kopfbinde im StartFragemnt nicht zu sehen ist
    private lateinit var mainLayout: ConstraintLayout

    fun changeHomeColor() {
        val btnHome: ImageButton = findViewById(R.id.btn_home)
        btnHome.setImageResource(R.drawable.baseline_home_black_20)

        val btnFavorites: ImageButton = findViewById(R.id.btn_favorites)
        btnFavorites.setImageResource(R.drawable.baseline_favorite_white_20)

        val btnSettings: ImageButton = findViewById(R.id.btn_settings)
        btnSettings.setImageResource(R.drawable.baseline_settings_white_20)
    }

    //Wenn FavoritesFragment sichtbar
    fun changeFavoritesColor() {
        val btnHome: ImageButton = findViewById(R.id.btn_home)
        btnHome.setImageResource(R.drawable.baseline_home_white_20)

        val btnFavorites: ImageButton = findViewById(R.id.btn_favorites)
        btnFavorites.setImageResource(R.drawable.baseline_favorite_black_20)

        val btnSettings: ImageButton = findViewById(R.id.btn_settings)
        btnSettings.setImageResource(R.drawable.baseline_settings_white_20)
    }

    //Wenn SettingsFragment sichtbar
    fun changeSettingsColor() {
        val btnHome: ImageButton = findViewById(R.id.btn_home)
        btnHome.setImageResource(R.drawable.baseline_home_white_20)

        val btnFavorites: ImageButton = findViewById(R.id.btn_favorites)
        btnFavorites.setImageResource(R.drawable.baseline_favorite_white_20)

        val btnSettings: ImageButton = findViewById(R.id.btn_settings)
        btnSettings.setImageResource(R.drawable.baseline_settings_black_20)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val btnHome = binding.btnHome
        val btnFavorites = binding.btnFavorites
        val btnSettings = binding.btnSettings

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    changeHomeColor()
                    btnHome.isEnabled = false
                    btnFavorites.isEnabled = true
                    btnSettings.isEnabled = true
                    btnFavorites.setOnClickListener {
                        try {
                            navController.navigate(R.id.action_homeFragment_to_favoritesFragment)
                        } catch (e: Exception) {
                            Log.e(TAG, "Navigation to FavoritesFragment failed: $e")
                        }
                    }

                    btnSettings.setOnClickListener {
                        try {
                            navController.navigate(R.id.action_homeFragment_to_settingsFragment)
                        } catch (e: Exception) {
                            Log.e(TAG, "Navigation to SettingsFragment failed: $e")
                        }
                    }
                }

                R.id.favoritesFragment -> {
                    changeFavoritesColor()
                    btnHome.isEnabled = true
                    btnFavorites.isEnabled = false
                    btnSettings.isEnabled = true
                    btnHome.setOnClickListener {
                        try {
                            navController.navigate(R.id.action_favoritesFragment_to_homeFragment)
                        } catch (e: Exception) {
                            Log.e(TAG, "Navigation to HomeFragment failed: $e")
                        }
                    }

                    btnSettings.setOnClickListener {
                        try {
                            navController.navigate(R.id.action_favoritesFragment_to_settingsFragment)
                        } catch (e: Exception) {
                            Log.e(TAG, "Navigation to SettingsFragment failed: $e")
                        }
                    }
                }

                R.id.settingsFragment -> {
                    changeSettingsColor()
                    btnHome.isEnabled = true
                    btnFavorites.isEnabled = true
                    btnSettings.isEnabled = false
                    btnHome.setOnClickListener {
                        try {
                            navController.navigate(R.id.action_settingsFragment_to_homeFragment)
                        } catch (e: Exception) {
                            Log.e(TAG, "Navigation to HomeFragment failed: $e")
                        }
                    }

                    btnFavorites.setOnClickListener {
                        try {
                            navController.navigate(R.id.action_settingsFragment_to_favoritesFragment)
                        } catch (e: Exception) {
                            Log.e(TAG, "Navigation to FavoritesFragment failed: $e")
                        }
                    }
                }
            }
        }
        // Zeige das ConstraintLayout standardmäßig an
        binding.clAppHeader.visibility = View.VISIBLE
    }


    fun ausblenden() {
        binding.tvHeader.visibility = View.INVISIBLE
        binding.ivLuckyvLogo.alpha = 0F

        binding.clNavBar.visibility = View.INVISIBLE
        binding.btnHome.visibility = View.INVISIBLE
        binding.btnFavorites.visibility = View.INVISIBLE
        binding.btnSettings.visibility = View.INVISIBLE
    }

    fun einblenden() {
        binding.tvHeader.visibility = View.VISIBLE
        binding.ivLuckyvLogo.alpha = 1F

        binding.clNavBar.visibility = View.VISIBLE
        binding.btnHome.visibility = View.VISIBLE
        binding.btnFavorites.visibility = View.VISIBLE
        binding.btnSettings.visibility = View.VISIBLE
    }
}