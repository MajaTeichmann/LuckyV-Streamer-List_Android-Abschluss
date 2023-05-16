package com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.R

class FavoritesAdapter(
    private val itemList: List<Streamer>,
    private val updateStreamer: (Streamer) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //View-Typen definieren
    private val liveItem = 1
    private val offlineItem = 2

    //ViewHolder für liveItem
    class ViewHolderLiveItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStreamVorschau: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamerName: TextView = itemView.findViewById(R.id.tv_streamername)
        val ivGreenDot: ImageView = itemView.findViewById(R.id.iv_green_dot)
        val tvCharName: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktion: TextView = itemView.findViewById(R.id.tv_fraktion)
        val btnFavorites: Button = itemView.findViewById(R.id.btn_favorites)
    }

    //ViewHolder für offlineItem
    class ViewHolderOfflineItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStreamVorschau: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamerName: TextView = itemView.findViewById(R.id.tv_streamername)
        val ivGreenDot: ImageView = itemView.findViewById(R.id.iv_red_dot)
        val tvCharName: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktion: TextView = itemView.findViewById(R.id.tv_fraktion)
        val btnFavorites: Button = itemView.findViewById(R.id.btn_favorites)
    }

    //Funktion zum Erstellen des richtigen ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            liveItem -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_live, parent, false)
                val viewHolder = ViewHolderLiveItem(view)

                viewHolder.btnFavorites.setOnClickListener {
                    val position = viewHolder.adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val streamer = itemList[position]
                        streamer.favorisiert = true
                        updateStreamer(streamer)
                        notifyItemChanged(position)
                    }
                }

                return viewHolder
            }

            offlineItem -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_offline, parent, false)
                val viewHolder = ViewHolderLiveItem(view)

                viewHolder.btnFavorites.setOnClickListener {
                    val position = viewHolder.adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val streamer = itemList[position]
                        streamer.favorisiert = true
                        updateStreamer(streamer)
                        notifyItemChanged(position)
                    }
                }

                return viewHolder
            }

            else -> throw IllegalArgumentException("Ungültiger View-Typ")
        }
    }

    //Funktion zum binden der Daten an ViewHolder
    @SuppressLint("NotifyDataSetChanged", "ResourceType")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemType = getItemViewType(position)
        when (itemType) {
            liveItem -> {
                val itemLive = itemList[position]
                val viewHolder = holder as ViewHolderLiveItem

                // Daten für ViewHolderLiveItem setzen
                viewHolder.ivStreamVorschau.setImageResource(R.id.iv_stream_vorschau)
                viewHolder.tvStreamerName.text = itemLive.name
                viewHolder.tvCharName.text = itemLive.ic_name
                viewHolder.tvFraktion.text = itemLive.fraktion
                viewHolder.ivGreenDot.setImageResource(R.id.iv_green_dot)

                // Klicklistener für den Button "btn_favorisieren" hinzufügen
                viewHolder.btnFavorites.setOnClickListener {
                    // Setzen Sie die Variable favorisiert auf true
                    itemLive.favorisiert = true

                    // Aufruf der updateStreamer-Funktion
                    updateStreamer(itemLive)

                    // Aktualisieren Sie die Ansicht des Adapters
                    notifyItemChanged(position)
                }
            }

            offlineItem -> {
                val itemOffline = itemList[position]
                val viewHolder = holder as ViewHolderOfflineItem

                // Daten für ViewHolderLiveItem setzen
                viewHolder.ivStreamVorschau.setImageResource(R.id.iv_stream_vorschau)
                viewHolder.tvStreamerName.text = itemOffline.name
                viewHolder.tvCharName.text = itemOffline.ic_name
                viewHolder.tvFraktion.text = itemOffline.fraktion
                viewHolder.ivGreenDot.setImageResource(R.id.iv_red_dot)

                // Klicklistener für den Button "btn_favorisieren" hinzufügen
                viewHolder.btnFavorites.setOnClickListener {
                    // Setzen Sie die Variable favorisiert auf true
                    itemOffline.favorisiert = true

                    // Aufruf der updateStreamer-Funktion
                    updateStreamer(itemOffline)

                    // Aktualisieren Sie die Ansicht des Adapters
                    notifyItemChanged(position)
                }
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
                Log.i("FavoritesAdapter", "$item wurde nicht favorisiert")
            }
        }
    }
}
