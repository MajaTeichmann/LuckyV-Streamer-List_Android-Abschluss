package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import com.abschlussapp.majateichmann.luckyvstreamerlist.live.LiveAdapter
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.abschlussapp.majateichmann.luckyvstreamerlist.MainActivity
import com.abschlussapp.majateichmann.luckyvstreamerlist.offline.OfflineAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.FragmentHomeBinding
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer


class HomeFragment : Fragment() {

    // Hier wird das ViewModel, in dem die Logik stattfindet, geholt
    private val viewModel: MainViewModel by activityViewModels()

    // Das binding für das HomeFragment wird deklariert
    private lateinit var binding: FragmentHomeBinding

    private var dataset: List<Streamer> = emptyList()

    /**
     * Lifecycle Funktion onCreateView
     * Hier wird das binding initialisiert und das Layout gebaut
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Lifecycle Funktion onViewCreated
     * Hier werden die Elemente eingerichtet und z.B. onClickListener gesetzt
     */

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val streamerListLive = binding.rvStreamerOnline
        val streamerListOffline = binding.rvStreamerOffline

        // GridLayoutManger für die RecyclerViews erstellen
        val gridLayoutManagerLive = GridLayoutManager(requireContext(), 3)
        val gridLayoutManagerOffline = GridLayoutManager(requireContext(), 3)
        streamerListLive.layoutManager = gridLayoutManagerLive
        streamerListOffline.layoutManager = gridLayoutManagerOffline

        // Bei einem Klick auf btnRefresh sollen die Informationen erneut abgerufen werden
        binding.btnRefresh.setOnClickListener {
            viewModel.loadData()
        }

        // Die Variable streamer wird beobachtet und bei einer Änderung wird der com.abschlussapp.majateichmann.luckyvstreamerlist.live.LiveAdapter der
        // Recyclerview neu gesetzt.
        viewModel.streamersOnline.observe(
            viewLifecycleOwner
        ) { streamers ->
            dataset = streamers
            binding.tvNumberPlayersOnline.text = streamers.size.toString()

            val adapter = LiveAdapter(dataset,viewModel)

            streamerListLive.adapter = adapter
        }

        viewModel.streamersOffline.observe(
            viewLifecycleOwner
        ) {
            streamerListOffline.adapter = OfflineAdapter(it)
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

        // Standardansicht setzen (LiveStreamer anzeigen, Button Streamers online deaktiviert)
        updateRecyclerViews(true)

        binding.btnStreamersLive.setOnClickListener {
            updateRecyclerViews(true)
        }

        binding.btnStreamersOffline.setOnClickListener {
            updateRecyclerViews(false)
        }


        // Verbesserte Performance bei fixer Listengröße
        streamerListLive.setHasFixedSize(true)
        streamerListOffline.setHasFixedSize(true)

        //Link hinzufügen
        val fullText =
            "Bei dieser App handelt es sich um die App-Version einer bereits existierenden Internetseite, welche alle Spieler auflistet, die ihr LuckyV RP auf Twitch streamen."
        val linkText = "Internetseite"
        val spannableString = SpannableString(fullText)

        //Klickbarer Bereich für Link festlegen
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                //URL meiner Zieladresse
                val url = "https://luckyv-streamer.frozenpenguin.media/"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
        // Position des Links im Text festlegen
        val startIndex = fullText.indexOf(linkText)
        val endIndex = startIndex + linkText.length

        // Link zum SpannableString hinzufügen
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvDescription1.text = spannableString
        binding.tvDescription1.movementMethod = LinkMovementMethod.getInstance()

        // Erstelle eine Instanz der StyleSpan-Klasse für den fettgedruckten Textstil
        val boldSpan = StyleSpan(Typeface.BOLD)

        // Position des zu fettgedruckenden Textabschnitts im Text festlegen
        val stringAbschnittStartIndex = fullText.indexOf("bereits existierenden")
        val stringAbschnittEndIndex = stringAbschnittStartIndex + "bereits existierenden".length

        // Füge den StyleSpan zum SpannableString hinzu, um den Text fettgedruckt darzustellen
        spannableString.setSpan(
            boldSpan,
            stringAbschnittStartIndex,
            stringAbschnittEndIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Setze den formatierten SpannableString als Text für die TextView
        binding.tvDescription1.text = spannableString

        // Referenz zur MainActivity erhalten
        val mainActivity = requireActivity() as MainActivity
        mainActivity.einblenden()
    }
}