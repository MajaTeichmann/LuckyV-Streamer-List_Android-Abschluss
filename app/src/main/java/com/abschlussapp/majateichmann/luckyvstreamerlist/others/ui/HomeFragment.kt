package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.abschlussapp.majateichmann.luckyvstreamerlist.LanguageChangeListener
import com.abschlussapp.majateichmann.luckyvstreamerlist.MainActivity
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.FragmentHomeBinding
import com.abschlussapp.majateichmann.luckyvstreamerlist.live.LiveAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.offline.OfflineAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.PreferenceManager
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer

private var dropdownPosition: Int = 0

class HomeFragment : Fragment(), LanguageChangeListener {

    /** Hier wird das ViewModel, in dem die Logik stattfindet, geholt */
    private val viewModel: MainViewModel by activityViewModels()

    /** Das binding für das HomeFragment wird deklariert */
    private lateinit var binding: FragmentHomeBinding

    private var datasetLive: List<Streamer> = emptyList()
    private var datasetOffline: List<Streamer> = emptyList()
    private var sortedStreamersLive: List<Streamer> = emptyList()
    private var sortedStreamersOffline: List<Streamer> = emptyList()

    // Deklariere eine Variable, um den aktuellen Scroll-Zustand zu speichern
    private var scrollPositionLive: Int = 0
    private var scrollPositionOffline: Int = 0

    private lateinit var tvDescription1: TextView
    private lateinit var tvDescription2: TextView
    private lateinit var tvDescription3: TextView
    private lateinit var tvSortBtn: Button

    /** Lifecycle Funktion onCreateView
     * Hier wird das binding initialisiert und das Layout gebaut */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        tvDescription1 = binding.tvDescription1
        tvDescription2 = binding.tvDescription2
        tvDescription3 = binding.tvDescription3
        tvSortBtn = binding.btnSort

        return binding.root
    }

    /** Lifecycle Funktion onViewCreated
     * Hier werden die Elemente eingerichtet und z.B. onClickListener gesetzt */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Hier wird das Viewmodel beobachtet und je nachdem, ob als Sprache "de" oder "en" aktiv ist,
        // werden die TextViews die deutschen oder englischen String-Versionen anzeigen
        viewModel.language.observe(
            viewLifecycleOwner
        ) {
            if (it == "de") {
                updateTextViewsForGerman()

            } else {
                updateTextViewsForEnglish()
            }
        }

        val streamerListLive = binding.rvStreamerOnline
        val streamerListOffline = binding.rvStreamerOffline

        /** GridLayoutManger für die RecyclerViews erstellen */
        val gridLayoutManagerLive = GridLayoutManager(requireContext(), 3)
        gridLayoutManagerLive.isAutoMeasureEnabled = true
        streamerListLive.layoutManager = gridLayoutManagerLive

        val gridLayoutManagerOffline = GridLayoutManager(requireContext(), 3)
        gridLayoutManagerOffline.isAutoMeasureEnabled = true
        streamerListOffline.layoutManager = gridLayoutManagerOffline

        /** Bei einem Klick auf btnRefresh sollen die Informationen erneut abgerufen werden */
        binding.btnRefresh.setOnClickListener {
            viewModel.loadData()
        }

        val adapterLive = LiveAdapter(viewModel)
        streamerListLive.adapter = adapterLive

        /** Die Variable streamer wird beobachtet und bei einer Änderung wird der LiveAdapter der
         * Recyclerview neu gesetzt */
        viewModel.streamersOnline.observe(
            viewLifecycleOwner
        ) { streamers ->
            datasetLive = streamers
            binding.tvNumberPlayersOnline.text = streamers.size.toString()
            adapterLive.submitList(datasetLive)
        }

        val adapterOffline = OfflineAdapter(viewModel)
        streamerListOffline.adapter = adapterOffline

        /** Die Variable streamer wird beobachtet und bei einer Änderung wird der OfflineAdapter der
         * Recyclerview neu gesetzt */
        viewModel.streamersOffline.observe(
            viewLifecycleOwner
        ) { streamers ->
            datasetOffline = streamers
            adapterOffline.submitList(datasetOffline)
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
        streamerListLive.setHasFixedSize(true)
        streamerListOffline.setHasFixedSize(true)

        fun germanStringModification() {
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

            // Den String-Ressourcenwert für "de_app_description_line2" abrufen
            val descriptionLine2 = resources.getString(R.string.de_app_description_line2)

            // Erstelle eine SpannableString mit demselben Inhalt wie der ursprüngliche Text
            val spannableStringDescription2 = SpannableString(descriptionLine2)

            // Unterstreichungsstil auf den SpannableString anwenden
            spannableStringDescription2.setSpan(
                UnderlineSpan(),
                0,
                descriptionLine2.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Fettdruckstil auf den SpannableString anwenden
            spannableStringDescription2.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                descriptionLine2.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Setze den modifizierten SpannableString als Text für das TextView
            tvDescription2.text = spannableStringDescription2
        }

        fun englishStringModification() {
            /** Link in den Fließtext hinzufügen */
            val fullText = "This app is the app version of an already existing website, " +
                    "which lists all players streaming their LuckyV RP on Twitch."
            val linkText = "website"
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
            val stringAbschnittStartIndex = fullText.indexOf("already existing")
            val stringAbschnittEndIndex = stringAbschnittStartIndex + "already existing".length

            /** StyleSpan zum SpannableString hinzufügen, um den Text fettgedruckt darzustellen */
            spannableString.setSpan(
                boldSpan,
                stringAbschnittStartIndex,
                stringAbschnittEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            /** formatierten SpannableString als Text für die TextView setzen*/
            binding.tvDescription1.text = spannableString

            // Den String-Ressourcenwert für "en_app_description_line2" abrufen
            val descriptionLine2 = resources.getString(R.string.en_app_description_line2)

            // Erstelle eine SpannableString mit demselben Inhalt wie der ursprüngliche Text
            val spannableStringDescription2 = SpannableString(descriptionLine2)

            // Unterstreichungsstil auf den SpannableString anwenden
            spannableStringDescription2.setSpan(
                UnderlineSpan(),
                0,
                descriptionLine2.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Fettdruckstil auf den SpannableString anwenden
            spannableStringDescription2.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                descriptionLine2.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Setze den modifizierten SpannableString als Text für das TextView
            tvDescription2.text = spannableStringDescription2
        }

        /** Referenz zur MainActivity erhalten */
        val mainActivity = requireActivity() as MainActivity
        mainActivity.einblenden()

        val dropdownSort = binding.dropDownSort

        binding.btnSort.setOnClickListener {
            // Überprüfen Sie den aktuellen Zustand der Sichtbarkeit des Dropdown-Filters
            val isVisible = binding.dropDownSort.visibility == View.VISIBLE

            // Ändern Sie die Sichtbarkeit basierend auf dem aktuellen Zustand
            binding.dropDownSort.visibility = if (isVisible) View.GONE else View.VISIBLE
        }

        //Dropdown Auswahlmöglichkeiten
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
                dropdownPosition = position

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
                    binding.rvStreamerOnline.scrollToPosition(position)
                    binding.rvStreamerOffline.scrollToPosition(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        viewModel.language.observe(
            viewLifecycleOwner
        ) { language ->
            if (language == "de") {
                updateTextViewsForGerman()
                germanStringModification()
            } else {
                updateTextViewsForEnglish()
                englishStringModification()
            }
        }
    }

    /** Die Funktion onSaveInstanceState wird überschrieben um den aktuellen Scroll-Zustand
     * der RecyclerViews zu speichern. */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("scrollPositionLive", scrollPositionLive)
        outState.putInt("scrollPositionOffline", scrollPositionOffline)
    }

    /** Die Funktion onViewStateRestored wird überschrieben um den vorherigen Scroll-Zustand
     * der RecyclerViews wiederherzustellen. */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            scrollPositionLive = savedInstanceState.getInt("scrollPositionLive")
            scrollPositionOffline = savedInstanceState.getInt("scrollPositionOffline")
        }
        scrollPositionLive = (binding.rvStreamerOnline.layoutManager as LinearLayoutManager)
            .findFirstVisibleItemPosition()
        scrollPositionOffline = (binding.rvStreamerOffline.layoutManager as LinearLayoutManager)
            .findFirstVisibleItemPosition()
    }

    override fun onLanguageChanged() {
        val language = PreferenceManager.getLanguagePreference(requireContext())

        if (language == "de") {
            updateTextViewsForGerman()
        } else {
            updateTextViewsForEnglish()
        }
    }

    fun updateTextViewsForGerman() {
        tvDescription1.text = resources.getString(R.string.de_app_description_line1)
        tvDescription2.text = resources.getString(R.string.de_app_description_line2)
        tvDescription3.text = resources.getString(R.string.de_app_description_line3)
        tvSortBtn.text = resources.getString(R.string.de_sort_button)
    }

    fun updateTextViewsForEnglish() {
        tvDescription1.text = resources.getString(R.string.en_app_description_line1)
        tvDescription2.text = resources.getString(R.string.en_app_description_line2)
        tvDescription3.text = resources.getString(R.string.en_app_description_line3)
        tvSortBtn.text = resources.getString(R.string.en_sort_button)
    }
}

