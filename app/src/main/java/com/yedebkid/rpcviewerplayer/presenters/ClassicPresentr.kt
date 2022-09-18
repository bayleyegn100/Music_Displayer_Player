package com.yedebkid.rpcviewerplayer.presenters

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.yedebkid.rpcviewerplayer.database.MusicsDao
import com.yedebkid.rpcviewerplayer.model.domain.SongDomainData
import com.yedebkid.rpcviewerplayer.rest.MusicRepo
import com.yedebkid.rpcviewerplayer.rest.MusicRepoImplementation
import io.reactivex.disposables.CompositeDisposable

interface ClassicPresentr {
    fun initialization(viewContract: ViewContractForClassic)
    fun checkNetworkConnection(connectivityManager: ConnectivityManager?)
    fun getClassicMusics()
    fun destroy()
}
class ClassicMusicPresenterImplementation(
    private val musicsDao: MusicsDao,
    private val repo: MusicRepo = MusicRepoImplementation(musicsDao),
    private var viewContractClassic: ViewContractForClassic? = null,
    private val disposable: CompositeDisposable = CompositeDisposable(),
    private val musicRepository: MusicRepo = MusicRepoImplementation(musicsDao)
) : ClassicPresentr {
    override fun initialization(viewContract: ViewContractForClassic) {
        viewContractClassic = viewContract
    }

    override fun checkNetworkConnection(connectivityManager: ConnectivityManager?) {
        connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
            if(!it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                viewContractClassic?.onFailure((Throwable("No internet connection!")))
            }
        }
    }

    override fun getClassicMusics() {
        viewContractClassic?.loadingClassicMusic(true)
        repo.getMusicsClassic()
            .subscribe(
                { classicMusics -> viewContractClassic?.onSuccess(classicMusics) },
                { error -> viewContractClassic?.onFailure(error) }
            ).also { disposable.add(disposable) }
    }

    override fun destroy() {
        disposable.clear()
        viewContractClassic = null
    }
}

interface ViewContractForClassic {
    fun loadingClassicMusic(isLoading: Boolean)
    fun onSuccess(ClassicMusics: List<SongDomainData>)
    fun onFailure(error: Throwable)
    fun isNetworkAvailable(isAvailable: Boolean)
}