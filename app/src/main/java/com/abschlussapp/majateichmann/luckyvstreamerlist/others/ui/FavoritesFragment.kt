package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.FragmentFavoritesBinding
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter.FavoritesLiveAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter.FavoritesOfflineAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer

class FavoritesFragment : Fragment() {

    private lateinit var recyclerViewLive: RecyclerView
    private lateinit var recyclerViewOffline: RecyclerView
    private lateinit var favoritesLiveAdapter: FavoritesLiveAdapter
    private lateinit var favoritesOfflineAdapter: FavoritesOfflineAdapter

    /** Liste der Streamer initialisieren */
    private lateinit var dataset: List<Streamer>

    /** Das binding für das HomeFragment wird deklariert */
    private lateinit var binding: FragmentFavoritesBinding

    /** ViewModel, in dem die Logik stattfindet */
    private val viewModel: MainViewModel by activityViewModels()

    /** Lifecycle Funktion onCreateView
     * Hier wird das binding initialisiert und das Layout gebaut */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root

        /** ViewModel initialisieren */
        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        /** RecyclerView initialisieren*/
        recyclerViewLive = view.findViewById(R.id.rv_streamer_online)
        recyclerViewLive.layoutManager = GridLayoutManager(context, 3)

        recyclerViewOffline = view.findViewById(R.id.rv_Streamer_offline)
        recyclerViewOffline.layoutManager = GridLayoutManager(context, 3)

        viewModel.streamersOnline.observe(viewLifecycleOwner) { streamers ->
            dataset = streamers

            /** streamersList filtern */
            val filteredListLive =
                dataset.filter { (it.live && it.favorisiert) }

            val filteredListOffline =
                dataset.filter { (!it.live && it.favorisiert) }

            /** favoritesAdapter mit gefilteter Liste initialisieren */
            favoritesLiveAdapter = FavoritesLiveAdapter(filteredListLive, viewModel)
            recyclerViewLive.adapter = favoritesLiveAdapter

            favoritesOfflineAdapter = FavoritesOfflineAdapter(filteredListOffline, viewModel)
            recyclerViewOffline.adapter = favoritesOfflineAdapter
        }

        return view
    }

    /** Lifecycle Funktion onViewCreated
     * Hier werden die Elemente eingerichtet und z.B. onClickListener gesetzt */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoritesLive = binding.rvStreamerOnline
        val favoritesOffline = binding.rvStreamerOffline

        /** GridLayoutManger für die RecyclerViews erstellen */
        val gridLayoutManagerLive = GridLayoutManager(requireContext(), 3)
        favoritesLive.layoutManager = gridLayoutManagerLive

        val gridLayoutManagerOffline = GridLayoutManager(requireContext(), 3)
        favoritesOffline.layoutManager = gridLayoutManagerOffline

        /** Die Variable streamer wird beobachtet und bei einer Änderung wird der LiveAdapter der
         * Recyclerview neu gesetzt */
        viewModel.streamersOnline.observe(
            viewLifecycleOwner
        ) { streamers ->
            dataset = streamers

            val filteredListLive = dataset.filter { it.live && it.favorisiert }

            /** favoritesAdapter mit gefilteter Liste initialisieren */
            favoritesLiveAdapter = FavoritesLiveAdapter(filteredListLive, viewModel)
            recyclerViewLive.adapter = favoritesLiveAdapter
        }

        viewModel.streamersOffline.observe(
            viewLifecycleOwner
        ) { streamers ->
            dataset = streamers
            val filteredListOffline = dataset.filter { !it.live && it.favorisiert }

            /** favoritesAdapter mit gefilteter Liste initialisieren */
            favoritesOfflineAdapter = FavoritesOfflineAdapter(filteredListOffline, viewModel)
            recyclerViewOffline.adapter = favoritesOfflineAdapter
        }

        fun updateRecyclerViews(isLive: Boolean) {
            if (isLive) {
                binding.rvStreamerOnline.visibility = View.VISIBLE
                binding.rvStreamerOffline.visibility = View.GONE
                binding.btnStreamersLive.isEnabled = false
                binding.btnStreamersOffline.isEnabled = true
            } else {
                binding.rvStreamerOnline.visibility = View.GONE
                binding.rvStreamerOffline.visibility = View.VISIBLE
                binding.btnStreamersLive.isEnabled = true
                binding.btnStreamersOffline.isEnabled = false
            }
        }

        /** Standardansicht setzen (LiveStreamer anzeigen, Button Streamers online deaktiviert) */
        updateRecyclerViews(true)

        binding.btnStreamersLive.setOnClickListener {
            updateRecyclerViews(true)
        }

        binding.btnStreamersOffline.setOnClickListener {
            updateRecyclerViews(false)
        }

        /** Verbesserte Performance bei fixer Listengröße */
        favoritesLive.setHasFixedSize(true)
    }
}