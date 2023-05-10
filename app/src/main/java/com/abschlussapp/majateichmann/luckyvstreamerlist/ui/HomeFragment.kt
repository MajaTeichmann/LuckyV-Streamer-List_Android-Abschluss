package com.abschlussapp.majateichmann.luckyvstreamerlist.ui

import LiveAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.abschlussapp.majateichmann.luckyvstreamerlist.MainActivity
import com.abschlussapp.majateichmann.luckyvstreamerlist.adapter.OfflineAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.AppRepository
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.datamodels.StreamerList
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.local.getDatabase
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.remote.StreamerApi
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    // Hier wird das ViewModel, in dem die Logik stattfindet, geholt
    private val viewModel: MainViewModel by activityViewModels()

    // Das binding für das QuizFragment wird deklariert
    private lateinit var binding: FragmentHomeBinding

    /**
     * Lifecycle Funktion onCreateView
     * Hier wird das binding initialisiert und das Layout gebaut
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Lifecycle Funktion onViewCreated
     * Hier werden die Elemente eingerichtet und z.B. onClickListener gesetzt
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val streamerListLive = binding.rvStreamerOnline
        val streamerListOffline = binding.rvStreamerOffline

        // Bei einem Klick auf btnRefresh sollen die Informationen erneut abgerufen werden
        binding.btnRefresh.setOnClickListener {
            viewModel.loadData()
        }
        // Die Variable streamer wird beobachtet und bei einer Änderung wird der LiveAdapter der
        // Recyclerview neu gesetzt.
        viewModel.streamersOnline.observe(
            viewLifecycleOwner
        ) {
            binding.tvNumberPlayersOnline.text = it.size.toString()
            streamerListLive.adapter = LiveAdapter(it)
        }

        viewModel.streamersOffline.observe(
            viewLifecycleOwner
        ) {
            streamerListOffline.adapter = OfflineAdapter(it)
        }

        // Verbesserte Performance bei fixer Listengröße
        streamerListLive.setHasFixedSize(true)
        streamerListOffline.setHasFixedSize(true)
    }
}