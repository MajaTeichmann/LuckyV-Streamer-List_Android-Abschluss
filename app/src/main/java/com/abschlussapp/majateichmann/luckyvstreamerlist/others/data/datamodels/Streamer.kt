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
    var fraktion: String?,
    var ic_name: String?,

    // Zusätzliche Variable hinzufügen (nicht in API)
    var favorisiert: Boolean = false
)

//todo: Aufbau der API "LuckyV Streamer List"
//{
// "online":5,
// "offline":182,
// "viewer":1064,
// "player":53,
// "streamer":[
//              {
//              "name":"5houze",
//              "display_name":"5houze",
//              "title":"Wir schauen mal was geht! | LS-Taxi | [LuckyV] [DIE FLUPPEN]",
//              "viewer":0,
//              "started":null,
//              "followers":132,
//              "logo_url":"https:\/\/static-cdn.jtvnw.net\/jtv_user_pictures\/1eaf51ab-48fd-40ed-9088-65874d7bc1d0-profile_image-300x300.png",
//              "live":false,
//              "last_online":1682373487,
//              "fraktion":"LS Taxi",
//              "ic_name":"Jonathan Klein"
//              }
//            ]
//}