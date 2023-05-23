package com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.live.LiveAdapter
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui.TAG

class FavoritesAdapter(
    var itemList: List<Streamer>,
    private val updateStreamer: (Streamer) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataset: List<Streamer> = emptyList()

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

        //wegen abwärtskompatibilität und mehr möglichkeiten als beim normalen ImageButton
        val btnFavoritesOnline: AppCompatImageButton = itemView.findViewById(R.id.btn_favorites)
    }

    //ViewHolder für offlineItem
    class ViewHolderOfflineItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStreamVorschauOffline: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamerNameOffline: TextView = itemView.findViewById(R.id.tv_streamername)
        val ivRedDot: ImageView = itemView.findViewById(R.id.iv_red_dot)
        val tvCharNameOffline: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktionOffline: TextView = itemView.findViewById(R.id.tv_fraktion)

        //wegen abwärtskompatibilität und mehr möglichkeiten als beim normalen ImageButton
        val btnFavoritesOffline: AppCompatImageButton = itemView.findViewById(R.id.btn_favorites)
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
                        val updatedStreamer = streamer.copy(favorisiert = !streamer.favorisiert)
                        val updatedItemList = itemList.toMutableList().apply { set(position, updatedStreamer) }
                        itemList = updatedItemList
                        notifyItemChanged(position)
                        updateStreamer(updatedStreamer)
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
                        val updatedStreamer = streamer.copy(favorisiert = !streamer.favorisiert)
                        val updatedItemList = itemList.toMutableList().apply { set(position, updatedStreamer) }
                        itemList = updatedItemList
                        notifyItemChanged(position)
                        updateStreamer(updatedStreamer)
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
                viewHolderOn.tvCharNameOnline.text = streamer.ic_name
                viewHolderOn.tvFraktionOnline.text = streamer.fraktion
                viewHolderOn.ivGreenDot.setImageResource(R.drawable.green_dot)

                // Klicklistener für den Button "btn_favorisieren" hinzufügen
                viewHolderOn.btnFavoritesOnline.setOnClickListener {
                    if (streamer.favorisiert) {
                        //todo:
                        Log.i(TAG, "Online Streamer ist favorisiert")
                        viewHolderOn.btnFavoritesOnline.setBackgroundResource(R.drawable.grey_heart)
                        streamer.favorisiert = false
                        //todo:
                        Log.i(TAG, "Online Streamer ist NICHT MEHR favorisiert")
                    } else {
                        //todo:
                        Log.i(TAG, "Online Streamer ist NICHT favorisiert")
                        viewHolderOn.btnFavoritesOnline.setBackgroundResource(R.drawable.red_heart)
                        streamer.favorisiert = true
                        //todo:
                        Log.i(TAG, "Online Streamer ist JETZT favorisiert")
                    }
                    val updatedStreamer = streamer.copy(favorisiert = !streamer.favorisiert)
                    updateStreamer(updatedStreamer)
                }

                // falls der wert im übergebenen Datensatz null ist, befülle ihn mit leerem string
                if (streamer.fraktion == null) {
                    viewHolderOn.tvFraktionOnline.visibility = View.GONE
                }

                if (streamer.ic_name == null) {
                    viewHolderOn.tvCharNameOnline.visibility = View.GONE
                }

                // befülle textview mit wert aus übergebener variable (aus API)
                val fraktionOn = viewHolderOn.tvFraktionOnline
                fraktionOn.text = streamer.fraktion

                val icNameOn = viewHolderOn.tvCharNameOnline
                icNameOn.text = streamer.ic_name

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

                //todo:
                Log.i("Online Streamer is online", streamer.live.toString())
                Log.i("Online Streamer is favorite", streamer.favorisiert.toString())

                //Logo-URL Laden
                viewHolderOn.ivStreamVorschauOnline.load(streamer.logo_url)
            }

            offlineItem -> {
                val viewHolderOff = holder as ViewHolderOfflineItem

                // Daten für ViewHolderLiveItem setzen
                viewHolderOff.ivStreamVorschauOffline.load(streamer.logo_url)
                viewHolderOff.tvStreamerNameOffline.text = streamer.name
                viewHolderOff.tvCharNameOffline.text = streamer.ic_name
                viewHolderOff.tvFraktionOffline.text = streamer.fraktion
                viewHolderOff.ivRedDot.setImageResource(R.drawable.red_dot)

                viewHolderOff.btnFavoritesOffline.setOnClickListener {
                    if (streamer.favorisiert) {
                        //todo:
                        Log.i(TAG, "Offline Streamer ist favorisiert")
                        viewHolderOff.btnFavoritesOffline.setBackgroundResource(R.drawable.grey_heart)
                        streamer.favorisiert = false
                        //todo:
                        Log.i(TAG, "Offline Streamer ist NICHT MEHR favorisiert")
                    } else if (!streamer.favorisiert) {
                        //todo:
                        Log.i(TAG, "Offline Streamer ist NICHT favorisiert")
                        viewHolderOff.btnFavoritesOffline.setBackgroundResource(R.drawable.red_heart)
                        streamer.favorisiert = true
                        //todo:
                        Log.i(TAG, "Offline Streamer ist JETZT favorisiert")
                    }
                    val updatedStreamer = streamer.copy(favorisiert = !streamer.favorisiert)
                    updateStreamer(updatedStreamer)
                }

                if (!streamer.live) {
                    viewHolderOff.tvStreamerNameOffline.text = streamer.name
                    viewHolderOff.tvCharNameOffline.text = streamer.ic_name
                }

                // falls der wert im übergebenen Datensatz null ist, befülle ihn mit leerem string
                if (streamer.fraktion == null) {
                    viewHolderOff.tvFraktionOffline.visibility = View.GONE
                }
                if (streamer.ic_name == null) {
                    viewHolderOff.tvCharNameOffline.visibility = View.GONE
                }

                // befülle textview mit wert aus übergebener variable (aus API)
                val fraktionOff = viewHolderOff.tvFraktionOffline
                fraktionOff.text = streamer.fraktion

                val icNameOff = viewHolderOff.tvCharNameOffline
                icNameOff.text = streamer.ic_name

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

                //todo:
                Log.i("Offline Streamer is online", streamer.live.toString())
                Log.i("Offline Streamer is favorite", streamer.favorisiert.toString())

                //Logo-URL Laden
                viewHolderOff.ivStreamVorschauOffline.load(streamer.logo_url)
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
                //todo:
                Log.i("FavoritesAdapter", "$item wurde nicht favorisiert")
            }
        }
    }
}
