package com.abschlussapp.majateichmann.luckyvstreamerlist.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.AppRepository
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.local.getDatabase
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.remote.LuckyV_StreamerApiService
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.remote.StreamerApi
import com.abschlussapp.majateichmann.luckyvstreamerlist.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

enum class ApiStatus {LOADING, ERROR, DONE}

const val TAG = "MainViewModel"

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // hier wird eine AppRepository Instanz erstellt, mit dem Parameter StreamerApi
    private val database = getDatabase(application)
    private val repository = AppRepository(StreamerApi, database)


    /**
     * Diese Funktion ruft die Repository-Funktion zum Laden der Streamer
     * innerhalb einer Coroutine auf
     */
    private val _loading = MutableLiveData<ApiStatus>()
    val loading: LiveData<ApiStatus>
        get() = _loading

    // hier werden die memes aus dem repository in einer eigenen Variablen gespeichert
    val streamer = repository.streamerList

    // zuerst werden alle Einträge aus der Datenbank gelöscht und anschließend wird versucht,
    // den API-Call über das Repository zu starten

    init{
        loadData()
    }
    fun loadData(){
        viewModelScope.launch {
            //gespeicherten Streamer aus Datenbank löschen, um alle Datensätze neue reinzuladen
            try{
                repository.deleteAllStreamer()
            }catch(e: Exception){
                Log.e(TAG,"Delete from Database failed: $e")
            }
            // Streamer aus API in die Datenbank laden
            _loading.value = ApiStatus.LOADING
            try{
                repository.getStreamer()
                _loading.value = ApiStatus.DONE
            }catch(e: Exception){
                Log.e(TAG,"Loading Data failed: $e")
                if(streamer.value.isNullOrEmpty()){
                    _loading.value = ApiStatus.ERROR
                }else{
                    _loading.value = ApiStatus.DONE
                }
            }
        }
    }
}