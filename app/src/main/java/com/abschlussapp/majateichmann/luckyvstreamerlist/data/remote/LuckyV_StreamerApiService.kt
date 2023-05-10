package com.abschlussapp.majateichmann.luckyvstreamerlist.data.remote

import com.abschlussapp.majateichmann.luckyvstreamerlist.data.datamodels.StreamerList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Basis URL der API in Konstante "BASE_URL" definieren
const val BASE_URL = "https://luckyv-streamer.frozenpenguin.media/"

// Moshi konvertiert Daten im JSON-Format, die aus der API kommen, in etwas, das von meiner Kotlin-Anwendung verarbeitet werden kann)
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofit wird von Moshi zum Konvertieren von JSON-Daten in Kotlin-Objekten verwendet
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


// "LuckyV_StreamerApiService" wird erstellt und definiert, welche Funktionen aufgerufen werden können,
// um Daten von der LuckyV-Streamer-API abzurufen. In diesem Fall gibt es nur "getStreamerList", die eine Liste von Streamern zurückgibt.
interface LuckyV_StreamerApiService{
    @GET("api/")
    // "suspend" bedeutet, dass Funktion asynchron arbeitet
    // (kann pausieren und später fortgesetzt werden, wenn sie auf Daten von der API wartet)
    suspend fun getStreamers(): StreamerList
}
//objekt "StreamerApi" wird erstellt
object StreamerApi{
    // "by lazy" sorgt dafür, dass die Instanz erst bei Bedarf initialisiert wird(verbessert Leistung)
    // "retrofit.create" wird verwendet, um Implementierung von "LuckyV_StreamerApiService" zu erstellen, die von "StreamerApi" Instanz verwendet wird
    val retrofitService: LuckyV_StreamerApiService by lazy{ retrofit.create(LuckyV_StreamerApiService::class.java)}
}