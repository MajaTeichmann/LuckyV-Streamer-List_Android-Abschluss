package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.FragmentFavoritesBinding
import com.abschlussapp.majateichmann.luckyvstreamerlist.live.LiveAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter.FavoritesLiveAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter.FavoritesOfflineAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer

private var dropdownPosition: Int = 0

class FavoritesFragment : Fragment() {

    private lateinit var recyclerViewLive: RecyclerView
    private lateinit var recyclerViewOffline: RecyclerView
    private lateinit var favoritesLiveAdapter: FavoritesLiveAdapter
    private lateinit var favoritesOfflineAdapter: FavoritesOfflineAdapter

    // Deklariere eine Variable, um den aktuellen Scroll-Zustand zu speichern
    private var scrollPositionLive: Int = 0
    private var scrollPositionOffline: Int = 0

    /** ViewModel, in dem die Logik stattfindet */
    private val viewModel: MainViewModel by activityViewModels()

    /** Das binding für das HomeFragment wird deklariert */
    private lateinit var binding: FragmentFavoritesBinding

    /** Liste der Streamer initialisieren */
    private lateinit var dataset: List<Streamer>

    /** Lifecycle Funktion onCreateView
     * Hier wird das binding initialisiert und das Layout gebaut */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        /** ViewModel initialisieren */
        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        val view = binding.root

        /** RecyclerView initialisieren*/
        recyclerViewLive = view.findViewById(R.id.rv_streamer_online)
        recyclerViewLive.layoutManager = GridLayoutManager(context, 3)

        recyclerViewOffline = view.findViewById(R.id.rv_Streamer_offline)
        recyclerViewOffline.layoutManager = GridLayoutManager(context, 3)

        viewModel.streamersOnline.observe(viewLifecycleOwner) { streamers ->
            dataset = streamers

            /** favoritesAdapter mit gefilteter Liste initialisieren */
            favoritesLiveAdapter = FavoritesLiveAdapter(viewModel)
            recyclerViewLive.adapter = favoritesLiveAdapter

            favoritesOfflineAdapter = FavoritesOfflineAdapter(viewModel)
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
        gridLayoutManagerLive.isAutoMeasureEnabled = true
        favoritesLive.layoutManager = gridLayoutManagerLive

        val gridLayoutManagerOffline = GridLayoutManager(requireContext(), 3)
        gridLayoutManagerOffline.isAutoMeasureEnabled = true
        favoritesOffline.layoutManager = gridLayoutManagerOffline

        /** favoritesAdapter mit gefilteter Liste initialisieren */
        favoritesLiveAdapter = FavoritesLiveAdapter(viewModel)
        recyclerViewLive.adapter = favoritesLiveAdapter

        /** favoritesAdapter mit gefilteter Liste initialisieren */
        favoritesOfflineAdapter = FavoritesOfflineAdapter(viewModel)
        recyclerViewOffline.adapter = favoritesOfflineAdapter

        /** Die Variable streamer wird beobachtet und bei einer Änderung wird der LiveAdapter der
         * Recyclerview neu gesetzt */
        viewModel.streamersOnline.observe(
            viewLifecycleOwner
        ) { streamers ->
            dataset = streamers
            favoritesLiveAdapter.submitList(dataset.filter { it.live && it.favorisiert })
        }

        viewModel.streamersOffline.observe(
            viewLifecycleOwner
        ) { streamers ->
            dataset = streamers
            favoritesOfflineAdapter.submitList(dataset.filter { !it.live && it.favorisiert })
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
        favoritesOffline.setHasFixedSize(true)
    }

    /** Die Funktion onSaveInstanceState wird überschrieben um den aktuellen Scroll-Zustand
     * der RecyclerViews zu speichern. */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        scrollPositionLive = (binding.rvStreamerOnline.layoutManager as LinearLayoutManager)
            .findFirstVisibleItemPosition()
        scrollPositionOffline = (binding.rvStreamerOffline.layoutManager as LinearLayoutManager)
            .findFirstVisibleItemPosition()
    }

    /** Die Funktion onViewStateRestored wird überschrieben um den vorherigen Scroll-Zustand
     * der RecyclerViews wiederherzustellen. */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            scrollPositionLive = savedInstanceState.getInt("scrollPositionLive")
            scrollPositionOffline = savedInstanceState.getInt("scrollPositionOffline")
        }
    }
}