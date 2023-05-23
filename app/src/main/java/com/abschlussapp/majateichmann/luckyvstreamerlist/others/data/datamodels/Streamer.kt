package com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

// als Datenbank-Entität definierte Datenklasse "Streamer"
@Entity
data class Streamer(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    //Datenklasse besteht aus 7 Eigenschaften, wovon 6 als Parameter für den Konstruktor definiert sind
    val title: String,
    val logo_url: String,
    val live: Boolean,
    // weil fraktion und ic_name in manchen fällen "NULL" ist
    var fraktionOff: String?,
    var ic_nameOff: String?,

    // Zusätzliche Variable hinzufügen (nicht in API)
    var favorisiert: Boolean = false
)