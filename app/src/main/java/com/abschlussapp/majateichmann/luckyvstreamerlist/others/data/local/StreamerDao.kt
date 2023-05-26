package com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer

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

    /** Lade alle Streamer aus der Datenbank */
    @Query("SELECT * from Streamer")
    fun getAll(): LiveData<List<Streamer>>

    /** Update alle Streamerdatensätze in der Datenbank */
    @Update
    suspend fun update(streamer: Streamer)

    /** Zeige alle Streamer an, die live sind (live = 1 -> live = true) */
    @Query("SELECT * FROM STREAMER WHERE live = 1")
    fun showLive(): LiveData<List<Streamer>>

    /** Zeige alle Streamer an, die offline sind (live = 0 -> live = false) */
    @Query("SELECT * FROM STREAMER WHERE live = 0")
    fun showOffline(): LiveData<List<Streamer>>

    /** Erhalte alle Streamer mit dem Namen (Names des jeweiligen Streamers) */
    @Query("SELECT * FROM Streamer WHERE name = :name")
    fun getEntityByName(name: String): Streamer

    /** Zeige alle Streamer an, die favorisiert sind (favorisiert = 1 -> favorisiert = true) */
    @Query("SELECT * FROM STREAMER WHERE favorisiert = 1")
    fun showFavorites(): LiveData<List<Streamer>>

    /** Setze die Variable favorisiert auf true, wenn name = (Names des jeweiligen Streamers) ist */
    @Query("UPDATE Streamer SET favorisiert = 1 WHERE name = :name")
    suspend fun addFavorite(name: String)

    /** Setze die Variable favorisiert auf false, wenn name = (Names des jeweiligen Streamers) ist */
    @Query("UPDATE Streamer SET favorisiert = 0 WHERE name = :name")
    suspend fun removeFavorite(name: String)

    /** Setze die Variable favorisiert auf den Wert der lokalen Variable favorisiert,
     * wenn name = (Names des jeweiligen Streamers) ist */
    @Query("UPDATE Streamer SET favorisiert = :favorisiert WHERE name = :name")
    fun updateExcludedVariable(name: String, favorisiert: Boolean)
}