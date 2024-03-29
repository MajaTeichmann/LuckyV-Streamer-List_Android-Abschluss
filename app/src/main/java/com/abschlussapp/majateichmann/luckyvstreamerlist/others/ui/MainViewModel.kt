package com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.PreferenceManager
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.AppRepository
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.local.getDatabase
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.remote.StreamerApi
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }

const val TAG = "MainViewModel"

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    fun updateStreamer(streamer: Streamer) {
        viewModelScope.launch {
            repository.updateStreamer(streamer)
        }
    }

    private val _language = MutableLiveData<String>()
    val language: LiveData<String>
        get() = _language

    //öffentliche Methode zum Festlegen des Sprachwerts
    fun setLanguage(language: String) {
        _language.value = language
    }

    private val _loading = MutableLiveData<ApiStatus>()
    val loading: LiveData<ApiStatus>
        get() = _loading

    private val database = getDatabase(application)
    private val repository = AppRepository(StreamerApi, database, context)

    val streamer = repository.streamerList
    val streamersOnline = repository.streamersOnline
    val streamersOffline = repository.streamersOffline

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
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
    }
}
