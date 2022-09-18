package com.yedebkid.rpcviewerplayer.view

import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.yedebkid.rpcviewerplayer.adapter.MusicAdapter
import com.yedebkid.rpcviewerplayer.database.MusicDatabase
import com.yedebkid.rpcviewerplayer.databinding.FragmentPopBinding
import com.yedebkid.rpcviewerplayer.model.domain.SongDomainData
import com.yedebkid.rpcviewerplayer.presenters.PopMusicPresenterImplementation
import com.yedebkid.rpcviewerplayer.presenters.ViewContractForPop

class PopFragment : Fragment(), ViewContractForPop {

    private val binding by lazy {
        FragmentPopBinding.inflate(layoutInflater)
    }

    private val musicDatabase by lazy {
        MusicDatabase
    }

    private val musicAdapter by lazy {
        MusicAdapter()
    }
    private val presenter by lazy {  // The presenter object view will use to interact with
        PopMusicPresenterImplementation(
            musicsDao = musicDatabase.getMusicDatabase(context).getMusicsDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.initialization(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        presenter.checkNetworkConnection(
            getSystemService(requireContext(), ConnectivityManager::class.java)
        )
        // Inflate the layout for this fragment
        binding.recyclerview.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = musicAdapter
        }
        savedInstanceState?.getParcelable<Parcelable>("RECYCLE_STATE")?.let {
            binding.recyclerview.layoutManager?.onRestoreInstanceState(it)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        presenter.getPopMusics()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putParcelable(
                "RECYCLER_STATE",
                binding.recyclerview.layoutManager?.onSaveInstanceState()
            )
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.recyclerview.layoutManager?.onRestoreInstanceState(
            savedInstanceState?.getParcelable(
                "RECYCLE_STATE"
            )
        )
    }

    override fun onStop() {
        super.onStop()
        presenter.destroy()
    }

    override fun loadingPopMusic(isLoading: Boolean) {
        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
    }

    override fun onSuccess(popMusics: List<SongDomainData>) {
        musicAdapter.updateFlowers(popMusics)

        Toast.makeText(
            requireContext(),
            "Success: ${popMusics.first().artistName}",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onFailure(error: Throwable) {
        AlertDialog.Builder(requireActivity())
            .setTitle("Error Occurred.")
            .setMessage(error.localizedMessage)
            .setNegativeButton("DISMISS") { dialog, _ ->
                dialog.dismiss()
            }
    }

    override fun isNetworkAvailable(isAvailable: Boolean) {
        if (isAvailable) {
            AlertDialog.Builder(requireActivity())
                .setTitle("Error Occurred.")
                .setMessage("No internet connection")
                .setNegativeButton("DISMISS") { dialog, _ ->
                    dialog.dismiss()
                }

        }
    }
}