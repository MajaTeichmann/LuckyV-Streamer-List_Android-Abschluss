package com.abschlussapp.majateichmann.luckyvstreamerlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.ActivityMainBinding
import com.abschlussapp.majateichmann.luckyvstreamerlist.ui.StartFragment

/**
 * Main Activity, dient als Einstiegspunkt für die App
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //damit Kopfbinde im StartFragemnt nicht zu sehen ist
    private lateinit var mainLayout: ConstraintLayout

    private lateinit var navController: NavController

    fun showMainLayout() {
        binding.clAppHeader.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        mainLayout = binding.clAppHeader

        // Zeige das ConstraintLayout standardmäßig an
        binding.clAppHeader.visibility = View.VISIBLE
    }

    fun ausblenden() {
        binding.tvHeader.visibility = View.INVISIBLE
        binding.ivLuckyvLogo.alpha = 0F
        binding.clNavBar.visibility = View.INVISIBLE
    }

    fun einblenden() {
        binding.tvHeader.visibility = View.VISIBLE
        binding.ivLuckyvLogo.alpha = 1F
        binding.clNavBar.visibility = View.VISIBLE
    }
}