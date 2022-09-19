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
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.yedebkid.rpcviewerplayer.R
import com.yedebkid.rpcviewerplayer.adapter.MusicAdapter
import com.yedebkid.rpcviewerplayer.database.MusicDatabase
import com.yedebkid.rpcviewerplayer.databinding.FragmentRockBinding
import com.yedebkid.rpcviewerplayer.model.domain.SongDomainData
import com.yedebkid.rpcviewerplayer.presenters.RockMusicPresenterImplementation
import com.yedebkid.rpcviewerplayer.presenters.ViewContractForRock

class RockFragment : Fragment(), ViewContractForRock {
    private val binding by lazy {
        FragmentRockBinding.inflate(layoutInflater)
    }
    private val musicAdapter by lazy {
        MusicAdapter()
    }
    private val presenter by lazy {
        RockMusicPresenterImplementation(
            musicsDao = MusicDatabase.getMusicDatabase(requireContext()).getMusicsDao()
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

        binding.recyclerview.apply {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = musicAdapter
        }

        savedInstanceState?.getParcelable<Parcelable>("RECYCLE_STATE")?.let {
            binding.recyclerview.layoutManager?.onRestoreInstanceState(it)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        presenter.getRockMusics()
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

    override fun loadingRockMusic(isLoading: Boolean) {
        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
    }

    override fun onSuccess(rockMusics: List<SongDomainData>) {
        musicAdapter.updateFlowers(rockMusics)
        Toast.makeText(
            requireContext(),
            "Success: ${rockMusics.first().artistName}",
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