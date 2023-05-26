package com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer

//TODO: Kommentare bearbeitet ❌

@Dao
interface StreamerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(streamer: Streamer): Long

    @Transaction
    suspend fun insertAll(streamerList: List<Streamer>) {
        for (streamer in streamerList) {
            val oldStreamer = getEntityByName(streamer.name)

            val newStreamer = streamer

            /** Bloß nicht entfernen!!!
             * "database.streamerDao.getEntityByName(streamer.name)" gibt null zurück,
             * wenn Streamer nicht in Datenbank */
            if (oldStreamer != null) {
                newStreamer.favorisiert = oldStreamer.favorisiert
            }
            insert(newStreamer)

        }
    }

    @Query("SELECT * from Streamer")
    fun getAll(): LiveData<List<Streamer>>

    @Update
    suspend fun update(streamer: Streamer)

    @Query("DELETE from Streamer")
    suspend fun deleteAll()

    // zeige mir alle Streamer an, die live sind
    @Query("SELECT * FROM STREAMER WHERE live = 1")
    fun showLive(): LiveData<List<Streamer>>

    // zeige mir alle Streamer an, die offline sind
    @Query("SELECT * FROM STREAMER WHERE live = 0")
    fun showOffline(): LiveData<List<Streamer>>

    // zeige mir alle Streamer an, die favorisiert sind
    @Query("SELECT * FROM STREAMER WHERE favorisiert = 1")
    fun showFavorites(): LiveData<List<Streamer>>

    @Query("UPDATE Streamer SET favorisiert = 1 WHERE name = :name")
    suspend fun addFavorite(name: String)

    @Query("UPDATE Streamer SET favorisiert = 0 WHERE name = :name")
    suspend fun removeFavorite(name: String)

    @Query("SELECT * FROM Streamer WHERE name = :name")
    fun getEntityByName(name: String): Streamer

    @Query("UPDATE Streamer SET favorisiert = :favorisiert WHERE name = :name")
    fun updateExcludedVariable(name: String, favorisiert: Boolean)
}