package com.abschlussapp.majateichmann.luckyvstreamerlist.others.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.local.StreamerDatabase
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.remote.StreamerApi

const val TAG = "AppRepository"

/**
 * Diese Klasse holt die Informationen und stellt sie mithilfe von live Data dem Rest
 * der App zur Verfügung
 */
class AppRepository(private val api: StreamerApi, private val database: StreamerDatabase) {

    /**
     * Diese Funktion ruft die Daten aus dem API Service ab und speichert die Antwort in der
     * Variable memes. Falls der Call nicht funktioniert, wird die Fehlermeldung geloggt
     */
    val streamerList: LiveData<List<Streamer>> = database.streamerDao.getAll()
    val streamersOnline: LiveData<List<Streamer>> = database.streamerDao.showLive()
    val streamersOffline: LiveData<List<Streamer>> = database.streamerDao.showOffline()

    suspend fun getStreamer(){
        try{
            val streamerData = api.retrofitService.getStreamers().streamer

            // Zusätzliche Variable hinzufügen (nicht in API)
            val favorisiert: Boolean = false

            for(streamer in streamerData){
                streamer.favorisiert = favorisiert
            }

            database.streamerDao.insertAll(streamerData)
        }catch(e: Exception){
            Log.e(TAG,"Error loading Data from API: $e")
        }
    }

    suspend fun deleteAllStreamers(){
        try{
            database.streamerDao.deleteAll()
        }catch(e: Exception){
            Log.e(TAG,"Failed to delete all Streamers from Database: $e")
        }
    }

    suspend fun updateStreamer(streamer: Streamer) {
        // Aktualisieren Sie den Streamer in der Datenbank
        database.streamerDao.update(streamer)
    }
}