package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.AppRepository
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.local.getDatabase
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.remote.StreamerApi
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }

const val TAG = "MainViewModel"

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun toggleFavoriteStatus(): Boolean {
        val isCurrentlyFavorite = _isFavorite.value ?: false
        val newFavoriteStatus = !isCurrentlyFavorite
        _isFavorite.value = newFavoriteStatus
        return newFavoriteStatus
    }

    private val _favoriteItems = MutableLiveData<List<Streamer>>()
    val favoriteItems: LiveData<List<Streamer>> = _favoriteItems

    fun addFavoriteItem(streamer: Streamer) {
        _favoriteItems.value = _favoriteItems.value?.plus(streamer)
    }

    fun removeFavoriteItem(streamer: Streamer) {
        _favoriteItems.value = _favoriteItems.value?.minus(streamer)
    }

    fun updateStreamer(streamer: Streamer) {
        viewModelScope.launch {
            repository.updateStreamer(streamer)
        }
    }


    // hier wird eine AppRepository Instanz erstellt, mit dem Parameter StreamerApi
    private val database = getDatabase(application)
    private val repository = AppRepository(StreamerApi, database)


    /**
     * Diese Funktion ruft die Repository-Funktion zum Laden der Streamer
     * innerhalb einer Coroutine auf
     */

    //Alle Streamer
    private val _loading = MutableLiveData<ApiStatus>()
    val loading: LiveData<ApiStatus>
        get() = _loading

    //AutoCompleteTextView
    private val _streamerSuche = MutableLiveData<List<Streamer>>()

    // hier werden die Streamer aus dem repository in einer eigenen Variablen gespeichert
    val streamer = repository.streamerList
    val streamersOnline = repository.streamersOnline
    val streamersOffline = repository.streamersOffline

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            //gespeicherten Streamer aus Datenbank löschen, um alle Datensätze neue reinzuladen
            try {
                repository.deleteAllStreamers()
            } catch (e: Exception) {
                Log.e(TAG, "Delete from Database failed: $e")
            }
            // Streamer aus API in die Datenbank laden
            _loading.value = ApiStatus.LOADING
            try {
                repository.getStreamer()
                _loading.value = ApiStatus.DONE
            } catch (e: Exception) {
                Log.e(TAG, "Loading Data failed: $e")
                if (streamersOnline.value.isNullOrEmpty() || streamersOffline.value.isNullOrEmpty()) {
                    _loading.value = ApiStatus.ERROR
                } else {
                    _loading.value = ApiStatus.DONE
                }
            }
        }
        _streamerSuche.value
    }
}