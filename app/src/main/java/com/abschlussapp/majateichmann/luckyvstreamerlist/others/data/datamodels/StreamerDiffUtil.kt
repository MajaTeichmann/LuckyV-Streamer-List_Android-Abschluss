package com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels

import android.util.Log
import androidx.recyclerview.widget.DiffUtil

class StreamerDiffUtil: DiffUtil.ItemCallback<Streamer>() {
    override fun areItemsTheSame(oldItem: Streamer, newItem: Streamer): Boolean {

        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Streamer, newItem: Streamer): Boolean {

        Log.i("StreamerDiffUtil","$oldItem $newItem")

        return oldItem.favorisiert == newItem.favorisiert
    }

}