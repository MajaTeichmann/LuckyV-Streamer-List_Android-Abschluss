package com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer

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

    // zeige mir alle Streamer an, die live sind
    @Query("SELECT * FROM STREAMER WHERE live = 1")
    fun showLive() : LiveData<List<Streamer>>

    @Query("SELECT * FROM STREAMER WHERE live = 0")
    fun showOffline(): LiveData<List<Streamer>>
}