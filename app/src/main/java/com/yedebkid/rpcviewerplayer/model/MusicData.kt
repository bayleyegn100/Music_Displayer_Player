package com.yedebkid.rpcviewerplayer.model


import com.google.gson.annotations.SerializedName

data class MusicData(
    @SerializedName("resultCount")
    val resultCount: Int?,
    @SerializedName("results")
    val results: List<Songs?>?
)