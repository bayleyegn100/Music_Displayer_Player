package com.yedebkid.rpcviewerplayer.database

import com.yedebkid.rpcviewerplayer.model.domain.SongDomainData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//Local music repository
interface LocalMusicRepo {
    fun getMusicLocalPop(): Single<List<SongDomainData>>
    fun getMusicLocalRock(): Single<List<SongDomainData>>
    fun getMusicLocalClassic(): Single<List<SongDomainData>>
}
class LocalMusicRepoImplementation(
    private val musicsDatabase: MusicsDao,
): LocalMusicRepo {
    override fun getMusicLocalPop(): Single<List<SongDomainData>> =
        musicsDatabase.getLocalPopMusic()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getMusicLocalRock(): Single<List<SongDomainData>> =
        musicsDatabase.getLocalRockMusic()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getMusicLocalClassic(): Single<List<SongDomainData>> =
        musicsDatabase.getLocalClassicMusic()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}