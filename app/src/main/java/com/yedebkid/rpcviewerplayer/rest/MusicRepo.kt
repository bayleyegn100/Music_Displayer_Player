package com.yedebkid.rpcviewerplayer.rest

import android.util.Log
import com.yedebkid.rpcviewerplayer.database.*
import com.yedebkid.rpcviewerplayer.model.domain.SongDomainData
import com.yedebkid.rpcviewerplayer.model.domain.mapToDomainMusicList
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val TAG = "MusicRepo"
interface MusicRepo {
    fun getMusicsPop(): Single<List<SongDomainData>>
    fun getMusicsRock(): Single<List<SongDomainData>>
    fun getMusicsClassic(): Single<List<SongDomainData>>
}
class MusicRepoImplementation(    //The repo layer is responsible a separation between the network connection on the database and all business logic
    private val musicsDao: MusicsDao,
    private val musicApi: MusicApi = Service.musicApiService,
    private val ioScheduler: Scheduler = Schedulers.io(),
    private val localMusicRepository: LocalMusicRepo = LocalMusicRepoImplementation(musicsDao)
): MusicRepo {
    override fun getMusicsPop(): Single<List<SongDomainData>> =
        musicApi.getPopMusics()
            .flatMap {
                localMusicRepository.getMusicLocalPop()
            }
            //.map { it.mapToDomainMusicList() }
            .doAfterSuccess { localMusicRepository.getMusicLocalPop()}
//            .doAfterSuccess{ Log.d(TAG, "Get all Pop Music: ${localMusicRepository.getMusics()}")}
            .subscribeOn(ioScheduler)
            .observeOn(AndroidSchedulers.mainThread())

    override fun getMusicsRock(): Single<List<SongDomainData>> =
        musicApi.getRockMusics()
            .flatMap {
                localMusicRepository.getMusicLocalRock()
            }
            //.map { it.mapToDomainMusicList() }
            .doAfterSuccess { localMusicRepository.getMusicLocalRock()}
//            .doAfterSuccess{ Log.d(TAG, "Get all Pop Music: ${localMusicRepository.getMusics()}")}
            .subscribeOn(ioScheduler)
            .observeOn(AndroidSchedulers.mainThread())

    override fun getMusicsClassic(): Single<List<SongDomainData>> =
        musicApi.getClassicMusics()
            .flatMap {
                localMusicRepository.getMusicLocalClassic()
            }
            //.map { it.mapToDomainMusicList() }
            .doAfterSuccess { localMusicRepository.getMusicLocalClassic()}
//            .doAfterSuccess{ Log.d(TAG, "Get all Pop Music: ${localMusicRepository.getMusics()}")}
            .subscribeOn(ioScheduler)
            .observeOn(AndroidSchedulers.mainThread())
}