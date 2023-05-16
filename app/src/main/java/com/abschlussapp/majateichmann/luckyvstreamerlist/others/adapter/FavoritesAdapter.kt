package com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.R

class FavoritesAdapter(private val itemList: List<Streamer>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //View-Typen definieren
    private val liveItem = 1
    private val offlineItem = 2

    //ViewHolder für liveItem
    class ViewHolderLiveItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clOnlineStreamer: ConstraintLayout = itemView.findViewById(R.id.cl_online_streamer)
        val ivStreamVorschau: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamerName: TextView = itemView.findViewById(R.id.tv_streamername)
        val ivGreenDot: ImageView = itemView.findViewById(R.id.iv_green_dot)
        val clCharUndFraktion: ConstraintLayout = itemView.findViewById(R.id.cl_charundfraktion)
        val tvCharName: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktion: TextView = itemView.findViewById(R.id.tv_fraktion)
    }

    //ViewHolder für liveItem
    class ViewHolderOfflineItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clOnlineStreamer: ConstraintLayout = itemView.findViewById(R.id.cl_offline_streamer)
        val ivStreamVorschau: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamerName: TextView = itemView.findViewById(R.id.tv_streamername)
        val ivGreenDot: ImageView = itemView.findViewById(R.id.iv_red_dot)
        val clCharUndFraktion: ConstraintLayout = itemView.findViewById(R.id.cl_charundfraktion)
        val tvCharName: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktion: TextView = itemView.findViewById(R.id.tv_fraktion)
    }

    //Funktion zum Erstellen des richtigen ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            liveItem -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_live, parent, false)
                ViewHolderLiveItem(view)
            }

            offlineItem -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_offline, parent, false)
                ViewHolderOfflineItem(view)
            }

            else -> throw IllegalArgumentException("Ungültiger View-Typ")
        }
    }

    //Funktion zum binden der Daten an ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemType = getItemViewType(position)
        when (itemType) {
            liveItem -> {
                val itemLive = itemList[position]
                val viewHolder = holder as ViewHolderLiveItem
            }

            offlineItem -> {
                val itemOffline = itemList[position]
                val viewHolder = holder as ViewHolderOfflineItem
            }
        }
    }

    //Funktion zum Abrufen der Anzahl an ListItems
    override fun getItemCount(): Int {
        return itemList.size
    }

    // Methode zum Abrufen des View-Typs eines Listenelements basierend auf seiner Position
    override fun getItemViewType(position: Int): Int {
        val item = itemList[position]
        return when {
            item.live && item.favorisiert -> liveItem
            !item.live && item.favorisiert -> offlineItem
            else -> {
                Log.i("FavoritesAdapter","$item wurde nicht favorisiert")
            }
        }
    }
}
