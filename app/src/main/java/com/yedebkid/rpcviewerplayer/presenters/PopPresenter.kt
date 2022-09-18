package com.yedebkid.rpcviewerplayer.presenters

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.yedebkid.rpcviewerplayer.database.MusicsDao
import com.yedebkid.rpcviewerplayer.model.domain.SongDomainData
import com.yedebkid.rpcviewerplayer.rest.MusicRepo
import com.yedebkid.rpcviewerplayer.rest.MusicRepoImplementation
import io.reactivex.disposables.CompositeDisposable

interface PopPresenter {
    fun initialization(viewContract: ViewContractForPop)
    fun checkNetworkConnection(connectivityManager: ConnectivityManager?)
    fun getPopMusics()
    fun destroy()
}
class PopMusicPresenterImplementation(
    private val musicsDao: MusicsDao,
    private val repo: MusicRepo = MusicRepoImplementation(musicsDao),
    private var viewContractPop: ViewContractForPop? = null,
    private val disposable: CompositeDisposable = CompositeDisposable(),
    private val musicRepository: MusicRepo = MusicRepoImplementation(musicsDao)
) : PopPresenter {
    override fun initialization(viewContract: ViewContractForPop) {
        viewContractPop = viewContract
    }
    override fun checkNetworkConnection(connectivityManager: ConnectivityManager?) {
        connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
            if(!it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                viewContractPop?.onFailure((Throwable("No internet connection!")))
            }
        }
    }
    override fun getPopMusics() {
        viewContractPop?.loadingPopMusic(true)
        repo.getMusicsPop()
            .subscribe(
                { popMusics -> viewContractPop?.onSuccess(popMusics) },
                { error -> viewContractPop?.onFailure(error) }
            ).also { disposable.add(disposable) }
    }
    override fun destroy() {
        disposable.clear()
        viewContractPop = null
    }
}
interface ViewContractForPop {
    fun loadingPopMusic(isLoading: Boolean)
    fun onSuccess(popMusics: List<SongDomainData>)
    fun onFailure(error: Throwable)
    fun isNetworkAvailable(isAvailable: Boolean)
}
