package com.abschlussapp.majateichmann.luckyvstreamerlist.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

// als Datenbank-Entität definierte Datenklasse "Streamer"
@Entity
class Streamer(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    //Datenklasse besteht aus 7 Eigenschaften, wovon 6 als Parameter für den Konstruktor definiert sind
    val title: String,
    val logo_url: String,
    val live: Boolean,
    // weil fraktion in manchen fällen "null" ist
//    var fraktion: String?,
    val ic_name: String?
)

// Aufbau der API "LuckyV Streamer List"
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