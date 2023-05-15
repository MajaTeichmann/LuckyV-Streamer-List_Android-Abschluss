package com.abschlussapp.majateichmann.luckyvstreamerlist.Others.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abschlussapp.majateichmann.luckyvstreamerlist.Others.data.datamodels.Streamer

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