package com.yedebkid.rpcviewerplayer.model.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yedebkid.rpcviewerplayer.model.Songs

@Entity(tableName = "musics")
data class SongDomainData(
    //  "artistName": "collectionName": "artworkUrl60":"trackPrice":
    @PrimaryKey val artistName: String,
    val collectionName: String,
    val artWorkUrl60: String,
    val price: Double
//    val genre: String
)

fun List<Songs>.mapToDomainMusicList(): List<SongDomainData> =
    this.map {
        SongDomainData(
            artistName = it.artistName ?: "",
            collectionName = it.collectionName ?: "",
            artWorkUrl60 = it.artworkUrl60 ?: "",
            price = it.trackPrice ?: 0.0
//            genre = it.primaryGenreName ?: ""
        )
    }

//fun Songs.mapToDomainMusic(): SongDomainData =
//    SongDomainData(
//        artistName = this.artistName ?: "",
//        collectionName = this.collectionName ?: "",
//        artWorkUrl60 = this.artworkUrl60 ?: "",
//        price = this.trackPrice ?: 0.0
//    )


