package com.abschlussapp.majateichmann.luckyvstreamerlist.Others.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abschlussapp.majateichmann.luckyvstreamerlist.Others.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.R

class FavoritesAdapter(
    private val dataset: List<Streamer>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class LIVE(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Halten Sie die Ansichtselemente für das erste Listenelement
        // Beispiel: val textView1: TextView = itemView.findViewById(R.id.textView1)
    }

    inner class OFFLINE(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Halten Sie die Ansichtselemente für das zweite Listenelement
        // Beispiel: val imageView1: ImageView = itemView.findViewById(R.id.imageView1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LIVE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_live, parent, false)
                LIVE(view)
            }

            OFFLINE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_offline, parent, false)
                OFFLINE(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val streamer = dataset[position]
        when (holder) {
            is LIVE -> {
                // Binden Sie die Daten für das erste Listenelement an LIVE
            }

            is OFFLINE -> {
                // Binden Sie die Daten für das zweite Listenelement an OFFLINE
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun getItemViewType(position: Int): Int {
        // Rückgabewert basierend auf der Position oder anderen Kriterien bestimmen
        // Beispiel: return VIEW_TYPE_1 oder VIEW_TYPE_2

        for(i in dataset){
            return if(i.live){
                LIVE
            }else{
                OFFLINE
            }
        }
        return LIVE and OFFLINE
    }

    companion object {
        private const val LIVE = 1
        private const val OFFLINE = 2
    }
}
