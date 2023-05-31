//package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.abschlussapp.majateichmann.luckyvstreamerlist.R
//import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.FragmentFavoritesBinding
//import com.abschlussapp.majateichmann.luckyvstreamerlist.live.LiveAdapter
//import com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter.FavoritesLiveAdapter
//import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer
//
//TODO: LÖSCHEN
//class FavoritesFragmentOnline : Fragment() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var favoritesLiveAdapter: FavoritesLiveAdapter
//
//    // Hier wird die Liste der Streamer initialisiert
//    private lateinit var dataset: List<Streamer>
//
//    /** Das binding für das HomeFragment wird deklariert */
//    private lateinit var binding: FragmentFavoritesBinding
//
//    /** Hier wird das ViewModel, in dem die Logik stattfindet, geholt */
//    private val viewModel: MainViewModel by activityViewModels()
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
//
//        // Initialize the RecyclerView
//        recyclerView = view.findViewById(R.id.rv_streamer_online)
//        recyclerView.layoutManager = GridLayoutManager(context, 3)
//
//        // ViewModel initialisieren
//        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
//
//        viewModel.streamersOnline.observe(viewLifecycleOwner) { streamers ->
//            dataset = streamers
//            // Filter streamersList
//            val filteredList =
//                dataset.filter { (it.live && it.favorisiert) }
//
//            // Initialize the favoritesAdapter with the filtered list
//            favoritesLiveAdapter = FavoritesLiveAdapter(filteredList, viewModel)
//            recyclerView.adapter = favoritesLiveAdapter
//        }
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val favoritesLive = binding.rvStreamerOnline
//
//        /** GridLayoutManger für die RecyclerViews erstellen */
//        val gridLayoutManagerLive = GridLayoutManager(requireContext(), 3)
//        favoritesLive.layoutManager = gridLayoutManagerLive
//
//        /** Die Variable streamer wird beobachtet und bei einer Änderung wird der LiveAdapter der
//         * Recyclerview neu gesetzt */
//        viewModel.streamersOnline.observe(
//            viewLifecycleOwner
//        ) { streamers ->
//            dataset = streamers
//            val adapter = LiveAdapter(dataset, viewModel)
//            favoritesLive.adapter = adapter
//        }
//
//        fun updateRecyclerViews(isLive: Boolean) {
//            if (isLive) {
//                binding.rvStreamerOnline.visibility = View.VISIBLE
//                binding.rvStreamerOffline.visibility = View.GONE
//                binding.btnStreamersLive.isEnabled = false
//                binding.btnStreamersOffline.isEnabled = true
//            } else {
//                binding.rvStreamerOnline.visibility = View.GONE
//                binding.rvStreamerOffline.visibility = View.VISIBLE
//                binding.btnStreamersLive.isEnabled = true
//                binding.btnStreamersOffline.isEnabled = false
//            }
//        }
//
//        /** Standardansicht setzen (LiveStreamer anzeigen, Button Streamers online deaktiviert) */
//        updateRecyclerViews(true)
//
//        binding.btnStreamersLive.setOnClickListener {
//            updateRecyclerViews(true)
//        }
//
//        binding.btnStreamersOffline.setOnClickListener {
//            updateRecyclerViews(false)
//        }
//
//        /** Verbesserte Performance bei fixer Listengröße */
//        favoritesLive.setHasFixedSize(true)
//    }
//}