package com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//TODO: Kommentare bearbeitet ❌
@Database(entities = [Streamer::class], version = 1)
abstract class StreamerDatabase : RoomDatabase() {

    abstract val streamerDao: StreamerDao
}

private lateinit var INSTANCE: StreamerDatabase

fun getDatabase(context: Context): StreamerDatabase {
    synchronized(StreamerDatabase::class.java) {
        // Wenn keine Datenbank gefunden wurde, wird automatisch eine neue erstellt
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                StreamerDatabase::class.java,
                "streamer_database"
            )
                .build()
        }
    }
    // als return-Statement gebe ich meinen Context an (=INSTANCE)
    return INSTANCE
}

@OptIn(DelicateCoroutinesApi::class)
fun insertDataToDatabase(context: Context, data: List<Streamer>) {
    GlobalScope.launch(Dispatchers.IO) {
        val database = getDatabase(context)
        database.streamerDao.insertAll(data)
    }
}