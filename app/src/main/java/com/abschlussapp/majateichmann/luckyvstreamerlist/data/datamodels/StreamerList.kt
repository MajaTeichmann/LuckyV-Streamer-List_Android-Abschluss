package com.abschlussapp.majateichmann.luckyvstreamerlist.data.datamodels

// diese Datenklasse beinhaltet eine Liste an Daten aus einer  weiteren Datenklasse ("Streamer")
data class StreamerList(
    val online: Long,
    val streamer: List<Streamer>
)