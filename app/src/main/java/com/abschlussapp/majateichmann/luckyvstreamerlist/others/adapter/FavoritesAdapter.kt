package com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.R

class FavoritesAdapter(
    var itemList: List<Streamer>,
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
        val ivRedDot: ImageView = itemView.findViewById(R.id.iv_red_dot)
        val tvCharName: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktion: TextView = itemView.findViewById(R.id.tv_fraktion)
        val btnFavorites: Button = itemView.findViewById(R.id.btn_favorites)
    }

    //Funktion zum Erstellen des richtigen ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            liveItem -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_favorites_live, parent, false)
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
                    .inflate(R.layout.list_item_favorites_offline, parent, false)
                val viewHolder = ViewHolderOfflineItem(view)

                viewHolder.btnFavorites.setOnClickListener {
                    val position = viewHolder.adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val streamer = itemList[position]
                        streamer.favorisiert = !streamer.favorisiert
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
    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var streamer = itemList[position]
        val itemType = getItemViewType(position)
        when (itemType) {
            liveItem -> {
                val itemLive = streamer
                val viewHolder = holder as ViewHolderLiveItem

                // Daten für ViewHolderLiveItem setzen
                holder.ivStreamVorschau.load(streamer.logo_url)
                viewHolder.tvStreamerName.text = itemLive.name
                viewHolder.tvCharName.text = itemLive.ic_name
                viewHolder.tvFraktion.text = itemLive.fraktion
                viewHolder.ivGreenDot.setImageResource(R.drawable.green_dot)

                // Klicklistener für den Button "btn_favorisieren" hinzufügen
                viewHolder.btnFavorites.setOnClickListener {
                    // Toggle favorite status
                    itemLive.favorisiert = !itemLive.favorisiert

                    // Setze das entsprechende Herz-Drawable basierend auf dem favorisiert-Status
                    val drawableResId = if (itemLive.favorisiert) {
                        R.drawable.red_heart
                    } else {
                        R.drawable.grey_heart
                    }
                    viewHolder.btnFavorites.setBackgroundResource(drawableResId)

                    // Aufruf der updateStreamer-Funktion
                    updateStreamer(itemLive)
                }

                // Setze das Herz-Drawable basierend auf dem favorisiert-Status
                val drawableResId = if (itemLive.favorisiert) {
                    R.drawable.red_heart
                } else {
                    R.drawable.grey_heart
                }
                viewHolder.btnFavorites.setBackgroundResource(drawableResId)
            }

            offlineItem -> {
                val itemOffline = itemList[position]
                val viewHolder = holder as ViewHolderOfflineItem

                val logoUrl = itemOffline.logo_url

                // Daten für ViewHolderLiveItem setzen
                holder.ivStreamVorschau.load(streamer.logo_url)
                viewHolder.tvStreamerName.text = itemOffline.name
                viewHolder.tvCharName.text = itemOffline.ic_name
                viewHolder.tvFraktion.text = itemOffline.fraktion
                viewHolder.ivRedDot.setImageResource(R.drawable.red_dot)

                // Klicklistener für den Button "btn_favorisieren" hinzufügen
                viewHolder.btnFavorites.setOnClickListener {
                    // Toggle favorite status
                    itemOffline.favorisiert = !itemOffline.favorisiert

                    // Setze das entsprechende Herz-Drawable basierend auf dem favorisiert-Status
                    val drawableResId = if (itemOffline.favorisiert) {
                        R.drawable.red_heart
                    } else {
                        R.drawable.grey_heart
                    }
                    viewHolder.btnFavorites.setBackgroundResource(drawableResId)

                    // Aufruf der updateStreamer-Funktion
                    updateStreamer(itemOffline)
                }

                // Setze das Herz-Drawable basierend auf dem favorisiert-Status
                val drawableResId = if (itemOffline.favorisiert) {
                    R.drawable.red_heart
                } else {
                    R.drawable.grey_heart
                }
                viewHolder.btnFavorites.setBackgroundResource(drawableResId)
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
