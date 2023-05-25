package com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer

//@Dao
//interface StreamerDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAll(streamer: List<Streamer>)
//
//    @Query("SELECT * from Streamer")
//    fun getAll(): LiveData<List<Streamer>>
//
//    @Update
//    suspend fun update(streamer: Streamer)
//
//    @Query("DELETE from Streamer")
//    suspend fun deleteAll()
//
//    // zeige mir alle Streamer an, die live sind
//    @Query("SELECT * FROM STREAMER WHERE live = 1")
//    fun showLive() : LiveData<List<Streamer>>
//
//    // zeige mir alle Streamer an, die offline sind
//    @Query("SELECT * FROM STREAMER WHERE live = 0")
//    fun showOffline(): LiveData<List<Streamer>>
//
//    // zeige mir alle Streamer an, die favorisiert sind
//    @Query("SELECT * FROM STREAMER WHERE favorisiert = 1")
//    fun showFavorites(): LiveData<List<Streamer>>
//
//    @Query("UPDATE Streamer SET favorisiert = 1 WHERE name = :name")
//   fun addFavorite(name: String)
//
//    @Query("UPDATE Streamer SET favorisiert = 0 WHERE name = :name")
//    fun deleteFavorite(name: String)
//}

//TODO: VORSCHLAG CHAT GPT:

@Dao
interface StreamerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(streamer: Streamer)

    @Transaction
    suspend fun insertAll(streamerList: List<Streamer>){
        for(streamer in streamerList){
            insert(streamer.apply { favorisiert = getEntityByName(streamer.name).favorisiert })
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

    @Transaction
    suspend fun reloadEntity(entity: Streamer) {
        val name = entity.name
        val favorisiert = entity.favorisiert

        val reloadedEntity = getEntityByName(name)
        reloadedEntity.favorisiert = favorisiert

        update(reloadedEntity)

        // Andere Aktualisierungen hier, falls erforderlich
    }
}