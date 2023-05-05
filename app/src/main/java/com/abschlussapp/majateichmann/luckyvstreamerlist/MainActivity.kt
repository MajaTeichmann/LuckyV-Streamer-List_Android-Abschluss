package com.abschlussapp.majateichmann.luckyvstreamerlist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set the visibility of the app header based on the apiLoading variable
        if(apiLoading) {
            binding.clAppHeader.visibility = View.GONE
        } else {
            binding.clAppHeader.visibility = View.VISIBLE
        }

//        setupActionBarWithNavController(navController)
    }
    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        //todo: binding = null
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.navController
////        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//
//        //Wenn API noch nicht geladen: Header nicht sichtbar
////    if(apiLoading) {
////        binding?.clAppHeader?.visibility = View.GONE
////    }else
////    {
////        //Header wird angezeigt, wenn Api fertig geladen
////        binding?.clAppHeader?.visibility = View.VISIBLE
////    }
//
//        if(apiLoading) {
//            binding?.clAppHeader?.visibility = View.GONE
//        }else
//        {
//            //Header wird angezeigt, wenn Api fertig geladen
//            binding?.clAppHeader?.visibility = View.VISIBLE
//        }
//}
    fun onApiLoading(){
//            binding = ActivityMainBinding.inflate(layoutInflater)
        //ApiLoading wird auf false gesetzt, wenn api fertig geladen
        apiLoading = false
        //Header wird angezeigt, wenn Api fertig geladen
//        binding.clAppHeader.visibility = View.VISIBLE

        //todo: Navigiere zu HomeFragment
//        findNavController(View).navigate(R.id.action_startFragment_to_homeFragment)

    }
}