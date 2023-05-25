package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abschlussapp.majateichmann.luckyvstreamerlist.MainActivity
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.FragmentFavoritesBinding
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.FragmentHomeBinding
import com.abschlussapp.majateichmann.luckyvstreamerlist.live.LiveAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.offline.OfflineAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter.FavoritesAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter.FavoritesLiveAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter.FavoritesOfflineAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.local.StreamerDao

//TODO: Kommentare bearbeitet ❌

class FavoritesFragment : Fragment() {

    private lateinit var recyclerViewLive: RecyclerView
    private lateinit var recyclerViewOffline: RecyclerView
    private lateinit var favoritesLiveAdapter: FavoritesLiveAdapter
    private lateinit var favoritesOfflineAdapter: FavoritesOfflineAdapter

    // Hier wird die Liste der Streamer initialisiert
    private lateinit var dataset: List<Streamer>

    /** Das binding für das HomeFragment wird deklariert */
    private lateinit var binding: FragmentFavoritesBinding

    /** Hier wird das ViewModel, in dem die Logik stattfindet, geholt */
    private val viewModel: MainViewModel by activityViewModels()

    /** Lifecycle Funktion onCreateView
     * Hier wird das binding initialisiert und das Layout gebaut */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the binding using FragmentFavoritesBinding.inflate
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize the RecyclerView
        recyclerViewLive = view.findViewById(R.id.rv_streamer_online)
        recyclerViewLive.layoutManager = GridLayoutManager(context, 3)

        recyclerViewOffline = view.findViewById(R.id.rv_Streamer_offline)
        recyclerViewOffline.layoutManager = GridLayoutManager(context, 3)

        // ViewModel initialisieren
        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        viewModel.streamersOnline.observe(viewLifecycleOwner) { streamers ->
            dataset = streamers
            // Filter streamersList
            val filteredList =
                dataset.filter { (it.live && it.favorisiert) }

            // Initialize the favoritesAdapter with the filtered list
            favoritesLiveAdapter = FavoritesLiveAdapter(filteredList, viewModel)
            recyclerViewLive.adapter = favoritesLiveAdapter

            favoritesOfflineAdapter = FavoritesOfflineAdapter(filteredList, viewModel)
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
            val adapter = LiveAdapter(dataset, viewModel)
            favoritesLive.adapter = adapter
        }

        viewModel.streamersOffline.observe(
            viewLifecycleOwner
        ) { streamers ->
            dataset = streamers
            val adapter = OfflineAdapter(dataset, viewModel)
            favoritesOffline.adapter = adapter
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