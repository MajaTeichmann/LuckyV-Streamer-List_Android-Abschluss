package com.abschlussapp.majateichmann.luckyvstreamerlist.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.datamodels.Streamer

@Dao
interface StreamerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(streamer: List<Streamer>)

    @Query("SELECT * from Streamer")
    fun getAll(): LiveData<List<Streamer>>

    @Update
    suspend fun update(streamer: Streamer)

    @Query("DELETE from Streamer")
    suspend fun deleteAll()

//    // zeige mir alle Streamer an, die live sind
//    @Query("SELECT * FROM STREAMER WHERE live = 1")
//    suspend fun showLive()
//
//    @Query("SELECT * FROM STREAMER WHERE live = 0")
//    suspend fun showOffline()
}