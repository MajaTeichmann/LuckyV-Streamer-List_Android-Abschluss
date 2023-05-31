package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import com.abschlussapp.majateichmann.luckyvstreamerlist.live.LiveAdapter
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.abschlussapp.majateichmann.luckyvstreamerlist.MainActivity
import com.abschlussapp.majateichmann.luckyvstreamerlist.offline.OfflineAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.FragmentHomeBinding
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer

class HomeFragment : Fragment(), OfflineAdapter.ScrollToPositionCallback {

    /** Hier wird das ViewModel, in dem die Logik stattfindet, geholt */
    private val viewModel: MainViewModel by activityViewModels()

    /** Das binding für das HomeFragment wird deklariert */
    private lateinit var binding: FragmentHomeBinding

    private var datasetLive: List<Streamer> = emptyList()
    private var datasetOffline: List<Streamer> = emptyList()
    private var sortedStreamersLive: List<Streamer> = emptyList()
    private var sortedStreamersOffline: List<Streamer> = emptyList()

    /** Lifecycle Funktion onCreateView
     * Hier wird das binding initialisiert und das Layout gebaut */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /** Lifecycle Funktion onViewCreated
     * Hier werden die Elemente eingerichtet und z.B. onClickListener gesetzt */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val streamerListLive = binding.rvStreamerOnline
        val streamerListOffline = binding.rvStreamerOffline

        /** GridLayoutManger für die RecyclerViews erstellen */
        val gridLayoutManagerLive = GridLayoutManager(requireContext(), 3)
        val gridLayoutManagerOffline = GridLayoutManager(requireContext(), 3)
        gridLayoutManagerLive.isAutoMeasureEnabled = true
        gridLayoutManagerOffline.isAutoMeasureEnabled = true
        streamerListLive.layoutManager = gridLayoutManagerLive
        streamerListOffline.layoutManager = gridLayoutManagerOffline

        /** Bei einem Klick auf btnRefresh sollen die Informationen erneut abgerufen werden */
        binding.btnRefresh.setOnClickListener {
            viewModel.loadData()
        }

        /** Die Variable streamer wird beobachtet und bei einer Änderung wird der LiveAdapter der
         * Recyclerview neu gesetzt */
        viewModel.streamersOnline.observe(
            viewLifecycleOwner
        ) { streamers ->
            datasetLive = streamers
            binding.tvNumberPlayersOnline.text = streamers.size.toString()
            val adapter = LiveAdapter(datasetLive, viewModel)
            streamerListLive.adapter = adapter
        }

        /** Die Variable streamer wird beobachtet und bei einer Änderung wird der OfflineAdapter der
         * Recyclerview neu gesetzt */
        viewModel.streamersOffline.observe(
            viewLifecycleOwner
        ) { streamers ->
            datasetOffline = streamers

            val adapter = OfflineAdapter(datasetOffline, viewModel)
            adapter.scrollToPositionCallback = this
            streamerListOffline.adapter = adapter
        }

        fun updateRecyclerViews(isLive: Boolean, sortBy: String = "") {
            val streamerList =
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
        streamerListLive.setHasFixedSize(true)
        streamerListOffline.setHasFixedSize(true)

        /** Link in den Fließtext hinzufügen */
        val fullText = "Bei dieser App handelt es sich um die App-Version einer bereits " +
                "existierenden Internetseite, welche alle Spieler auflistet, die ihr LuckyV RP auf " +
                "Twitch streamen."
        val linkText = "Internetseite"
        val spannableString = SpannableString(fullText)

        /** Klickbarer Bereich für Link festlegen */
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {

                /** URL meiner Zieladresse */
                val url = "https://luckyv-streamer.frozenpenguin.media/"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        /** Position des Links im Text festlegen */
        val startIndex = fullText.indexOf(linkText)
        val endIndex = startIndex + linkText.length

        /** Link zum SpannableString hinzufügen */
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvDescription1.text = spannableString
        binding.tvDescription1.movementMethod = LinkMovementMethod.getInstance()

        /** Instanz der StyleSpan-Klasse für den fettgedruckten Text */
        val boldSpan = StyleSpan(Typeface.BOLD)

        /** Position des fettgedruckten Textabschnitts im Text festlegen */
        val stringAbschnittStartIndex = fullText.indexOf("bereits existierenden")
        val stringAbschnittEndIndex = stringAbschnittStartIndex + "bereits existierenden".length

        /** StyleSpan zum SpannableString hinzufügen, um den Text fettgedruckt darzustellen */
        spannableString.setSpan(
            boldSpan,
            stringAbschnittStartIndex,
            stringAbschnittEndIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        /** formatierten SpannableString als Text für die TextView setzen*/
        binding.tvDescription1.text = spannableString

        /** Referenz zur MainActivity erhalten */
        val mainActivity = requireActivity() as MainActivity
        mainActivity.einblenden()

        //todo: beschreibung
        val dropdownSort = binding.dropDownSort

        binding.btnSort.setOnClickListener {
            // Überprüfen Sie den aktuellen Zustand der Sichtbarkeit des Dropdown-Filters
            val isVisible = binding.dropDownSort.visibility == View.VISIBLE

            // Ändern Sie die Sichtbarkeit basierend auf dem aktuellen Zustand
            binding.dropDownSort.visibility = if (isVisible) View.GONE else View.VISIBLE
        }

        val filterOptionsFilter =
            listOf<String>(
                "Sortieren nach:",
                "Name (A-Z)",
                "Name (Z-A)",
                "Charname (A-Z)",
                "Charname (Z-A)",
                "Fraktion (A-Z)",
                "Fraktion (Z-A)"
            )

        val dropdownFilterAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            filterOptionsFilter
        )
        dropdownSort.adapter = dropdownFilterAdapter

        dropdownSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = dropdownSort.selectedItem.toString()

                if (dropdownSort.visibility == View.VISIBLE) {
                    when (selectedOption) {
                        filterOptionsFilter[1] -> {
                            sortedStreamersLive = datasetLive.sortedBy { it.name }
                            sortedStreamersOffline = datasetOffline.sortedBy { it.name }
                        }

                        filterOptionsFilter[2] -> {
                            sortedStreamersLive = datasetLive.sortedByDescending { it.name }
                            sortedStreamersOffline = datasetOffline.sortedByDescending { it.name }
                        }

                        filterOptionsFilter[3] -> {
                            sortedStreamersLive = datasetLive.sortedBy { it.ic_name }
                            sortedStreamersOffline = datasetOffline.sortedBy { it.ic_name }
                        }

                        filterOptionsFilter[4] -> {
                            sortedStreamersLive = datasetLive.sortedByDescending { it.ic_name }
                            sortedStreamersOffline =
                                datasetOffline.sortedByDescending { it.ic_name }
                        }

                        filterOptionsFilter[5] -> {
                            sortedStreamersLive = datasetLive.sortedBy { it.fraktion }
                            sortedStreamersOffline = datasetOffline.sortedBy { it.fraktion }
                        }

                        filterOptionsFilter[6] -> {
                            sortedStreamersLive = datasetLive.sortedByDescending { it.fraktion }
                            sortedStreamersOffline =
                                datasetOffline.sortedByDescending { it.fraktion }
                        }
                    }

                    /** Aktualisiert den Adapter mit sortedStreamersLive/Offline  */
                    val adapterLive = LiveAdapter(sortedStreamersLive, viewModel)
                    streamerListLive.adapter = adapterLive

                    val adapterOffline = OfflineAdapter(sortedStreamersOffline, viewModel)
                    adapterOffline.scrollToPositionCallback = this@HomeFragment

                    streamerListOffline.adapter = adapterOffline

                    binding.rvStreamerOnline.scrollToPosition(position)
                    binding.rvStreamerOffline.scrollToPosition(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun scrollToPosition(position: Int) {
        val layoutManager = binding.rvStreamerOffline.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(position, 0)
    }
}

