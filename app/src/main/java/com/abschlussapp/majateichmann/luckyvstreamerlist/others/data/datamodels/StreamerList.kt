package com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels

//TODO: Kommentare bearbeitet ❌

// diese Datenklasse beinhaltet eine Liste an Daten aus einer  weiteren Datenklasse ("Streamer")
data class StreamerList(
    val online: Int,
    val streamer: List<Streamer>
)