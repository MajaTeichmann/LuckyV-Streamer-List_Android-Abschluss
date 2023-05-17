package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter.FavoritesAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer


// TODO: BONUS THEMA

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter

    // Hier wird das ViewModel, in dem die Logik stattfindet, geholt
    private lateinit var viewModel: MainViewModel

    // Hier wird die Liste der Streamer initialisiert
    private lateinit var streamerList: List<Streamer>



    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        // ViewModel initialisieren
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        // RecyclerView initialisieren
        recyclerView = view.findViewById(R.id.rv_favorites)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        // Adapter initialisieren und RecyclerView setzen
        favoritesAdapter = FavoritesAdapter(emptyList(), ::updateStreamer)
        recyclerView.adapter = favoritesAdapter

        // Daten aus dem ViewModel beobachten und bei Änderungen den Adapter aktualisieren
        viewModel.streamersOnline.observe(viewLifecycleOwner) { streamers ->
            favoritesAdapter.itemList = streamers.filter { it.favorisiert }
            favoritesAdapter.notifyDataSetChanged()
        }

        return view
    }

    private fun updateStreamer(streamer: Streamer) {
        viewModel.updateStreamer(streamer)
    }

}
