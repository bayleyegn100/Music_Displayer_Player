package com.yedebkid.rpcviewerplayer.database

import androidx.room.*
import com.yedebkid.rpcviewerplayer.model.domain.SongDomainData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MusicsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusics(musics: List<SongDomainData>): Completable

    @Query("SELECT * FROM musics")
    fun getLocalPopMusic(): Single<List<SongDomainData>>

    @Query("SELECT * FROM musics")
    fun getLocalRockMusic(): Single<List<SongDomainData>>

    @Query("SELECT * FROM musics")
    fun getLocalClassicMusic(): Single<List<SongDomainData>>
}