package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter.FavoritesAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer


// TODO: MUSS NOCH ERSTELLT WERDEN (BONUS THEMA)

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter

    // Hier wird das ViewModel, in dem die Logik stattfindet, geholt
    private lateinit var viewModel: MainViewModel

    // Hier wird die Liste der Streamer initialisiert
    private lateinit var streamerList: List<Streamer>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.rv_favorites)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        // ViewModel initialisieren
        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        viewModel.streamersOnline.observe(viewLifecycleOwner) { streamers ->
            streamerList = streamers
            // Filter streamersList
            val filteredList = streamerList.filter { it.live && it.favorisiert }

            // Initialize the favoritesAdapter with the filtered list
            favoritesAdapter = FavoritesAdapter(filteredList, ::updateStreamer)
            recyclerView.adapter = favoritesAdapter
        }
        return view
    }

    private fun updateStreamer(streamer: Streamer) {
        val updatedStreamer = streamer.copy(favorisiert = false)

        val position = streamerList.indexOf(streamer)
        if (position != -1) {
            streamerList = streamerList.toMutableList().apply { set(position, updatedStreamer) }
            favoritesAdapter.notifyItemChanged(position)
        }
    }
}