package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
    private val viewModel: MainViewModel by activityViewModels()

    // Hier wird die Liste der Streamer initialisiert
    private lateinit var streamerList: List<Streamer>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        viewModel.streamersOnline.observe(viewLifecycleOwner) { streamers ->
            streamerList = streamers
            // Initialize the RecyclerView
            recyclerView = view.findViewById(R.id.rv_favorites)
            recyclerView.layoutManager = GridLayoutManager(context, 3)

            // Prüfen Sie, ob die streamerList bereits initialisiert wurde und filtern Sie sie
            if (::streamerList.isInitialized) {
                val filteredList = streamerList.filter { it.live && it.favorisiert }

                // Initialize the favoritesAdapter with the filtered list
                favoritesAdapter = FavoritesAdapter(filteredList)
                recyclerView.adapter = favoritesAdapter
            }
        }
        return view
    }
}
