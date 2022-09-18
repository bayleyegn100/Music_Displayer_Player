package com.yedebkid.rpcviewerplayer.presenters

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.yedebkid.rpcviewerplayer.database.MusicsDao
import com.yedebkid.rpcviewerplayer.model.domain.SongDomainData
import com.yedebkid.rpcviewerplayer.rest.MusicRepo
import com.yedebkid.rpcviewerplayer.rest.MusicRepoImplementation
import io.reactivex.disposables.CompositeDisposable

interface RockPresenter {

    fun initialization(viewContract: ViewContractForRock)
    fun checkNetworkConnection(connectivityManager: ConnectivityManager?)
    fun getRockMusics()
    fun destroy()
}
class RockMusicPresenterImplementation(
    private val musicsDao: MusicsDao,
    private val repo: MusicRepo = MusicRepoImplementation(musicsDao),
    private var viewContractRock: ViewContractForRock? = null,
    private val disposable: CompositeDisposable = CompositeDisposable(),
    private val musicRepository: MusicRepo = MusicRepoImplementation(musicsDao)
) : RockPresenter {
    override fun initialization(viewContract: ViewContractForRock) {
        viewContractRock = viewContract
    }

    override fun checkNetworkConnection(connectivityManager: ConnectivityManager?) {
        connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
            if(!it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                viewContractRock?.onFailure((Throwable("No internet connection!")))
            }
        }
    }

    override fun getRockMusics() {
        viewContractRock?.loadingRockMusic(true)
        repo.getMusicsRock()
            .subscribe(
                { rockMusics -> viewContractRock?.onSuccess(rockMusics) },
                { error -> viewContractRock?.onFailure(error) }
            ).also { disposable.add(disposable) }
    }

    override fun destroy() {
        disposable.clear()
        viewContractRock = null
    }
}

interface ViewContractForRock {
    fun loadingRockMusic(isLoading: Boolean)
    fun onSuccess(rockMusics: List<SongDomainData>)
    fun onFailure(error: Throwable)
    fun isNetworkAvailable(isAvailable: Boolean)

}