package com.abschlussapp.majateichmann.luckyvstreamerlist.ui

import LiveAdapter
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.abschlussapp.majateichmann.luckyvstreamerlist.MainActivity
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.adapter.OfflineAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.remote.StreamerApi
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.FragmentStartBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentStartBinding
    private lateinit var mainActivity: MainActivity

    //ProgressBar
    private lateinit var progressBar: ProgressBar

    private val handler = Handler(Looper.getMainLooper())
    private var progress = 0

    private val progressRunnable = object : Runnable {
        override fun run() {
            if (progress < 100) {
                // Erhöht den Fortschritt um 1
                progress++
                progressBar.progress = progress

                // Ruft Runnable jede Sekunde erneut auf
                handler.postDelayed(this, 1000)
            } else {
                // wenn Fortschritt 100 erreicht, ProgressBar ausblenden und zum nächsten Fragment navigieren
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Referenziere die MainActivity
        mainActivity = context as MainActivity
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater, container, false)
        progressBar = binding.progressBar

        //Aktiviert ProgressBar-Animation
        progressBar.isIndeterminate = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loading.observe(viewLifecycleOwner) {

            // Referenz zur MainActivity erhalten
            val mainActivity = requireActivity() as MainActivity
            mainActivity.ausblenden()

            when (it) {
                ApiStatus.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    progress = 0
                    progressBar.progress = progress
                    handler.post(progressRunnable)
                }

                else -> {
                    // Coroutine starten, um die Streamer-Daten von der API abzurufen
                    lifecycleScope.launch {
                        try {
                            progressBar.visibility = View.VISIBLE

                            // getStreamers() wird asynchron aufgerufen & Ergebnis wird in "streamers" gespeichert
                            val streamers = withContext(Dispatchers.IO) {
                                StreamerApi.retrofitService.getStreamers()
                            }

                            val recyclerViewLive: RecyclerView? =
                                view.findViewById(R.id.rv_streamer_online)
                            recyclerViewLive?.adapter = LiveAdapter(streamers.streamer)
                            val recyclerViewOffline: RecyclerView? =
                                view.findViewById(R.id.rv_Streamer_offline)
                            recyclerViewOffline?.adapter = OfflineAdapter(streamers.streamer)
                        } catch (e: Exception) {
                            // Wenn ein Fehler aufgetreten ist, wird der Fortschrittsbalken ausgeblendet und eine Fehlermeldung angezeigt.
                            progressBar.visibility = View.GONE

                            // Zeige einen Fehler-Dialog oder eine Fehlermeldung an.
                            Log.e(TAG, "Fehler beim Abrufen der Streamer-Daten: ${e.message}")

                        } finally {
                            // Fortschrittsbalken ausblenden, unabhängig davon, ob ein Fehler aufgetreten ist oder nicht
                            progressBar.visibility = View.GONE

                            // Navigation zum nächsten Fragment durchführen
                            val navController: NavController = findNavController(requireView())
                            navController.navigate(R.id.action_startFragment_to_homeFragment)
                        }
                    }
                    //entferne den handler der ProgressBar
                    handler.removeCallbacks(progressRunnable)
                }
            }
        }
    }
}