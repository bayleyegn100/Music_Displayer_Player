package com.yedebkid.rpcviewerplayer.rest

import com.yedebkid.rpcviewerplayer.model.Songs
import com.yedebkid.rpcviewerplayer.model.domain.SongDomainData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApi {

    @GET(PATH_SEARCH)
    fun getPopMusics(
    @Query("term") genre: String = "pop",
    @Query("amp;media") media: String = "music",
    @Query("amp;entity") entity: String = "song",
    @Query("amp;limit") limit: Int = 50
    ): Single<List<Songs>>

    @GET(PATH_SEARCH)
    fun getRockMusics(
        @Query("term") genre: String = "rock",
        @Query("amp;media") media: String = "music",
        @Query("amp;entity") entity: String = "song",
        @Query("amp;limit") limit: Int = 50
    ): Single<List<Songs>>

    @GET(PATH_SEARCH)
    fun getClassicMusics(
        @Query("term") genre: String = "classic",
        @Query("amp;media") media: String = "music",
        @Query("amp;entity") entity: String = "song",
        @Query("amp;limit") limit: Int = 50
    ): Single<List<Songs>>

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
        private const val PATH_SEARCH= "search"
    }

}