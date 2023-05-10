package com.abschlussapp.majateichmann.luckyvstreamerlist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.ActivityMainBinding

/**
 * Main Activity, dient als Einstiegspunkt für die App
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //apiLoading auf true setzen -> Progressbar noch sichtbar
    private var apiLoading: Boolean = true

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        //Wenn API noch nicht geladen: Header nicht sichtbar
        if (apiLoading) {
            binding.clAppHeader.visibility = View.GONE
        } else {
            //Header wird angezeigt, wenn Api fertig geladen
            binding.clAppHeader.visibility = View.VISIBLE
        }
    }
}