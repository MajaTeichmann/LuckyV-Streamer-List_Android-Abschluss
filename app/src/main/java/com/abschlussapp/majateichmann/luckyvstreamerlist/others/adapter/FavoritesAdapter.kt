package com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        val ivStreamVorschauOnline: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamerNameOnline: TextView = itemView.findViewById(R.id.tv_streamername)
        val ivGreenDot: ImageView = itemView.findViewById(R.id.iv_green_dot)
        val tvCharNameOnline: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktionOnline: TextView = itemView.findViewById(R.id.tv_fraktion)
        val btnFavoritesOnline: Button = itemView.findViewById(R.id.btn_favorites)
    }

    //ViewHolder für offlineItem
    class ViewHolderOfflineItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStreamVorschauOffline: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamerNameOffline: TextView = itemView.findViewById(R.id.tv_streamername)
        val ivRedDot: ImageView = itemView.findViewById(R.id.iv_red_dot)
        val tvCharNameOffline: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktionOffline: TextView = itemView.findViewById(R.id.tv_fraktion)
        val btnFavoritesOffline: Button = itemView.findViewById(R.id.btn_favorites)
    }

    //Funktion zum Erstellen des richtigen ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            liveItem -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_favorites_live, parent, false)
                val viewHolder = ViewHolderLiveItem(view)

                viewHolder.btnFavoritesOnline.setOnClickListener {
                    val position = viewHolder.adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val streamer = itemList[position]
                        val updatedStreamer =
                            streamer.copy(favorisiert = !streamer.favorisiert) // Aktualisieren Sie den favorisiert-Status
                        updateStreamer(updatedStreamer)
                        notifyItemChanged(position)
                    }
                }

                return viewHolder
            }

            offlineItem -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_favorites_offline, parent, false)
                val viewHolder = ViewHolderOfflineItem(view)

                viewHolder.btnFavoritesOffline.setOnClickListener {
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

            else -> {
                throw IllegalArgumentException("Ungültiger View-Typ")
            }
        }
    }

    //Funktion zum binden der Daten an ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val streamer = itemList[position]
        val itemType = getItemViewType(position)

        when (itemType) {
            liveItem -> {
                val viewHolderOn = holder as ViewHolderLiveItem

                // Daten für ViewHolderLiveItem setzen
                viewHolderOn.ivStreamVorschauOnline.load(streamer.logo_url)
                viewHolderOn.tvStreamerNameOnline.text = streamer.name
                viewHolderOn.tvCharNameOnline.text = streamer.ic_nameOff
                viewHolderOn.tvFraktionOnline.text = streamer.fraktionOff
                viewHolderOn.ivGreenDot.setImageResource(R.drawable.green_dot)

                // falls der wert im übergebenen Datensatz null ist, befülle ihn mit leerem string
                if (streamer.fraktionOff == null) {
                    viewHolderOn.tvFraktionOnline.visibility = View.GONE
                }

                if (streamer.ic_nameOff == null) {
                    viewHolderOn.tvCharNameOnline.visibility = View.GONE
                }

                // befülle textview mit wert aus übergebener variable (aus API)
                val fraktionOn = viewHolderOn.tvFraktionOnline
                fraktionOn.text = streamer.fraktionOff

                val icNameOn = viewHolderOn.tvCharNameOnline
                icNameOn.text = streamer.ic_nameOff

                val streamerNameOn = viewHolderOn.tvStreamerNameOnline
                streamerNameOn.text = streamer.name


                // falls der string im textview zu lang ist, um in eine zeile zu passen, kürze ihn am ende mit "..." ab
                fraktionOn.ellipsize = TextUtils.TruncateAt.END
                fraktionOn.maxLines = 1
                fraktionOn.isSingleLine = true

                icNameOn.ellipsize = TextUtils.TruncateAt.END
                icNameOn.maxLines = 1
                icNameOn.isSingleLine = true

                streamerNameOn.ellipsize = TextUtils.TruncateAt.END
                streamerNameOn.maxLines = 1
                streamerNameOn.isSingleLine = true

                //todo: LOGSTATEMENTS LÖSCHEN
                Log.i("Online Streamer is online", streamer.live.toString())
                Log.i("Online Streamer is favorite", streamer.favorisiert.toString())

                //Logo-URL Laden
                viewHolderOn.ivStreamVorschauOnline.load(streamer.logo_url)

                // Klicklistener für den Button "btn_favorisieren" hinzufügen
                viewHolderOn.btnFavoritesOnline.setOnClickListener {
                    // Toggle favorite status
                    streamer.favorisiert = !streamer.favorisiert

                    // Setze das entsprechende Herz-Drawable basierend auf dem favorisiert-Status
                    val drawableResId = if (streamer.favorisiert) {
                        R.drawable.red_heart
                    } else {
                        R.drawable.grey_heart
                    }
                    viewHolderOn.btnFavoritesOnline.setBackgroundResource(drawableResId)

                    // Aufruf der updateStreamer-Funktion
                    updateStreamer(streamer)
                }

                // Setze das Herz-Drawable basierend auf dem favorisiert-Status
                val drawableResId = if (streamer.favorisiert) {
                    R.drawable.red_heart
                } else {
                    R.drawable.grey_heart
                }
                viewHolderOn.btnFavoritesOnline.setBackgroundResource(drawableResId)
            }

            offlineItem -> {
                val viewHolderOff = holder as ViewHolderOfflineItem

                // Daten für ViewHolderLiveItem setzen
                viewHolderOff.ivStreamVorschauOffline.load(streamer.logo_url)
                viewHolderOff.tvStreamerNameOffline.text = streamer.name
                viewHolderOff.tvCharNameOffline.text = streamer.ic_nameOff
                viewHolderOff.tvFraktionOffline.text = streamer.fraktionOff
                viewHolderOff.ivRedDot.setImageResource(R.drawable.red_dot)

                if (!streamer.live) {
                    viewHolderOff.tvStreamerNameOffline.text = streamer.name
                    viewHolderOff.tvCharNameOffline.text = streamer.ic_nameOff
                }

                // falls der wert im übergebenen Datensatz null ist, befülle ihn mit leerem string
                if (streamer.fraktionOff == null) {
                    viewHolderOff.tvFraktionOffline.visibility = View.GONE
                }
                if (streamer.ic_nameOff == null) {
                    viewHolderOff.tvCharNameOffline.visibility = View.GONE
                }

                // befülle textview mit wert aus übergebener variable (aus API)
                val fraktionOff = viewHolderOff.tvFraktionOffline
                fraktionOff.text = streamer.fraktionOff

                val icNameOff = viewHolderOff.tvCharNameOffline
                icNameOff.text = streamer.ic_nameOff

                val streamerNameOff = viewHolderOff.tvStreamerNameOffline
                streamerNameOff.text = streamer.name

                // falls der string im textview zu lang ist, um in eine zeile zu passen, kürze ihn am ende mit "..." ab
                fraktionOff.ellipsize = TextUtils.TruncateAt.END
                fraktionOff.maxLines = 1
                fraktionOff.isSingleLine = true

                icNameOff.ellipsize = TextUtils.TruncateAt.END
                icNameOff.maxLines = 1
                icNameOff.isSingleLine = true

                streamerNameOff.ellipsize = TextUtils.TruncateAt.END
                streamerNameOff.maxLines = 1
                streamerNameOff.isSingleLine = true

                //todo: LOGSTATEMENTS LÖSCHEN
                Log.i("Offline Streamer is online", streamer.live.toString())
                Log.i("Offline Streamer is favorite", streamer.favorisiert.toString())

                //Logo-URL Laden
                viewHolderOff.ivStreamVorschauOffline.load(streamer.logo_url)

                // Klicklistener für den Button "btn_favorisieren" hinzufügen
                viewHolderOff.btnFavoritesOffline.setOnClickListener {
                    // Toggle favorite status
                    streamer.favorisiert = !streamer.favorisiert

                    // Setze das entsprechende Herz-Drawable basierend auf dem favorisiert-Status
                    val drawableResId = if (streamer.favorisiert) {
                        R.drawable.red_heart
                    } else {
                        R.drawable.grey_heart
                    }
                    viewHolderOff.btnFavoritesOffline.setBackgroundResource(drawableResId)

                    // Aufruf der updateStreamer-Funktion
                    updateStreamer(streamer)
                }

                // Setze das Herz-Drawable basierend auf dem favorisiert-Status
                val drawableResId = if (streamer.favorisiert) {
                    R.drawable.red_heart
                } else {
                    R.drawable.grey_heart
                }
                viewHolderOff.btnFavoritesOffline.setBackgroundResource(drawableResId)
            }

            else -> {
                throw IllegalArgumentException("Ungültiger View-Typ")
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

    fun updateData(streamers: List<Streamer>) {
        itemList = streamers
        notifyDataSetChanged()
    }
}
