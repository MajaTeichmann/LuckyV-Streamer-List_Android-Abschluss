package com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer

class FavoritesAdapter(
    var itemList: List<Streamer>,
//    private val addFavorite: (name: String) -> Unit,
//    private val deleteFavorite: (name: String) -> Unit,
    private val updateStreamer: (Streamer) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // View-Typen definieren
    private val liveItem = 1
    private val offlineItem = 2

    // ViewHolder für liveItem
    class ViewHolderLiveItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStreamVorschauOnline: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamerNameOnline: TextView = itemView.findViewById(R.id.tv_streamername)
        val ivGreenDot: ImageView = itemView.findViewById(R.id.iv_green_dot)
        val tvCharNameOnline: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktionOnline: TextView = itemView.findViewById(R.id.tv_fraktion)
        val btnFavoritesOnline: AppCompatImageButton = itemView.findViewById(R.id.btn_favorites)
    }

    // ViewHolder für offlineItem
    class ViewHolderOfflineItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStreamVorschauOffline: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamerNameOffline: TextView = itemView.findViewById(R.id.tv_streamername)
        val ivRedDot: ImageView = itemView.findViewById(R.id.iv_red_dot)
        val tvCharNameOffline: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktionOffline: TextView = itemView.findViewById(R.id.tv_fraktion)
        val btnFavoritesOffline: AppCompatImageButton = itemView.findViewById(R.id.btn_favorites)
    }

    // Funktion zum Erstellen des richtigen ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            liveItem -> {
                val view = inflater.inflate(R.layout.list_item_favorites_live, parent, false)
                ViewHolderLiveItem(view)
            }

            offlineItem -> {
                val view = inflater.inflate(R.layout.list_item_favorites_offline, parent, false)
                ViewHolderOfflineItem(view)
            }

            else -> throw IllegalArgumentException("Ungültiger View-Typ")
        }
    }

    // Funktion zum Binden der Daten an ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val streamer = itemList[position]
        val itemType = getItemViewType(position)

        when (itemType) {
            liveItem -> {
                val viewHolderOn = holder as ViewHolderLiveItem
                if (streamer.favorisiert) {
                    viewHolderOn.btnFavoritesOnline.setBackgroundResource(R.drawable.red_heart)
                } else {
                    viewHolderOn.btnFavoritesOnline.setBackgroundResource(R.drawable.grey_heart)
                }
                // Daten für ViewHolderLiveItem setzen
                viewHolderOn.ivStreamVorschauOnline.load(streamer.logo_url)
                viewHolderOn.tvStreamerNameOnline.text = streamer.name
                viewHolderOn.tvCharNameOnline.text = streamer.ic_name
                viewHolderOn.tvFraktionOnline.text = streamer.fraktion
                viewHolderOn.ivGreenDot.setImageResource(R.drawable.green_dot)

                // Klicklistener für den Button "btn_favorisieren" hinzufügen
                viewHolderOn.btnFavoritesOnline.setOnClickListener {
                    if (streamer.favorisiert) {
                        deleteFavorite(streamerName = streamer.name, position)
                    } else {
                        addFavorite(streamerName = streamer.name, position)
                    }
                    val updatedStreamer = streamer.copy(favorisiert = !streamer.favorisiert)
                    updateStreamer(updatedStreamer)
                }

                // Falls der Wert im übergebenen Datensatz null ist, befülle ihn mit leerem String
                if (streamer.fraktion == null) {
                    viewHolderOn.tvFraktionOnline.visibility = View.GONE
                }

                if (streamer.ic_name == null) {
                    viewHolderOn.tvCharNameOnline.visibility = View.GONE
                }

                // Befülle TextView mit Wert aus übergebener Variable (aus API)
                val fraktionOn = viewHolderOn.tvFraktionOnline
                fraktionOn.text = streamer.fraktion

                val icNameOn = viewHolderOn.tvCharNameOnline
                icNameOn.text = streamer.ic_name

                val streamerNameOn = viewHolderOn.tvStreamerNameOnline
                streamerNameOn.text = streamer.name

                // Falls der String im TextView zu lang ist, um in eine Zeile zu passen, kürze ihn am Ende mit "..." ab
                fraktionOn.ellipsize = TextUtils.TruncateAt.END
                fraktionOn.maxLines = 1
                fraktionOn.isSingleLine = true

                icNameOn.ellipsize = TextUtils.TruncateAt.END
                icNameOn.maxLines = 1
                icNameOn.isSingleLine = true

                streamerNameOn.ellipsize = TextUtils.TruncateAt.END
                streamerNameOn.maxLines = 1
                streamerNameOn.isSingleLine = true

                // Logo-URL laden
                viewHolderOn.ivStreamVorschauOnline.load(streamer.logo_url)
            }

            offlineItem -> {
                val viewHolderOff = holder as ViewHolderOfflineItem
                if (streamer.favorisiert) {
                    viewHolderOff.btnFavoritesOffline.setBackgroundResource(R.drawable.red_heart)
                } else {
                    viewHolderOff.btnFavoritesOffline.setBackgroundResource(R.drawable.grey_heart)
                }

                // Daten für ViewHolderLiveItem setzen
                viewHolderOff.ivStreamVorschauOffline.load(streamer.logo_url)
                viewHolderOff.tvStreamerNameOffline.text = streamer.name
                viewHolderOff.tvCharNameOffline.text = streamer.ic_name
                viewHolderOff.tvFraktionOffline.text = streamer.fraktion
                viewHolderOff.ivRedDot.setImageResource(R.drawable.red_dot)

                viewHolderOff.btnFavoritesOffline.setOnClickListener {
                    if (streamer.favorisiert) {
                        deleteFavorite(streamerName = streamer.name, position)
                    } else {
                        addFavorite(streamerName = streamer.name, position)
                    }
                    val updatedStreamer = streamer.copy(favorisiert = !streamer.favorisiert)
                    updateStreamer(updatedStreamer)
                }

                if (!streamer.live) {
                    viewHolderOff.tvStreamerNameOffline.text = streamer.name
                    viewHolderOff.tvCharNameOffline.text = streamer.ic_name
                }

                // Falls der Wert im übergebenen Datensatz null ist, befülle ihn mit leerem String
                if (streamer.fraktion == null) {
                    viewHolderOff.tvFraktionOffline.visibility = View.GONE
                }
                if (streamer.ic_name == null) {
                    viewHolderOff.tvCharNameOffline.visibility = View.GONE
                }

                // Befülle TextView mit Wert aus übergebener Variable (aus API)
                val fraktionOff = viewHolderOff.tvFraktionOffline
                fraktionOff.text = streamer.fraktion

                val icNameOff = viewHolderOff.tvCharNameOffline
                icNameOff.text = streamer.ic_name

                val streamerNameOff = viewHolderOff.tvStreamerNameOffline
                streamerNameOff.text = streamer.name

                // Falls der String im TextView zu lang ist, um in eine Zeile zu passen, kürze ihn am Ende mit "..." ab
                fraktionOff.ellipsize = TextUtils.TruncateAt.END
                fraktionOff.maxLines = 1
                fraktionOff.isSingleLine = true

                icNameOff.ellipsize = TextUtils.TruncateAt.END
                icNameOff.maxLines = 1
                icNameOff.isSingleLine = true

                streamerNameOff.ellipsize = TextUtils.TruncateAt.END
                streamerNameOff.maxLines = 1
                streamerNameOff.isSingleLine = true

                // Logo-URL laden
                viewHolderOff.ivStreamVorschauOffline.load(streamer.logo_url)
            }

            else -> {
                throw IllegalArgumentException("Ungültiger View-Typ")
            }
        }
    }

    // todo: Funktion zum Toggeln des Favoritenstatus
//    private fun toggleFavorite(streamer: Streamer) {
//        val updatedStreamer = streamer.copy(favorisiert = !streamer.favorisiert)
//        updateStreamer(updatedStreamer)
//    }

    // Funktion zum Hinzufügen eines Favoriten
    private fun addFavorite(streamerName: String, position: Int): Int {
        val streamer = itemList[position]
        return when {
            streamer.live && streamer.favorisiert -> Log.i(
                "Favoriten-Test ADD",
                "Online-Streamer $streamerName konnte nicht zuden Favoriten hinzugefügt werden"
            )

            !streamer.live && streamer.favorisiert -> Log.i(
                "Favoriten-Test ADD",
                "Offline-Streamer $streamerName konnte nicht zu den Favoriten hinzugefügt werden"
            )

            streamer.live && !streamer.favorisiert -> Log.i(
                "Favoriten-Test ADD",
                " Online-Streamer $streamerName wurde den Favoriten hinzugefügt"
            )

            !streamer.live && !streamer.favorisiert -> Log.i(
                "Favoriten-Test ADD",
                "Offline-Streamer $streamerName wurde den Favoriten hinzugefügt"
            )

            else -> {
                Log.i("Favoriten-Test", "Streamer $streamerName wurde aus den Favoriten entfernt")
            }
        }
    }

    // Funktion zum Löschen eines Favoriten
    private fun deleteFavorite(streamerName: String, position: Int): Int {
        val streamer = itemList[position]
        return when {
            streamer.live && streamer.favorisiert -> Log.i(
                "Favoriten-Test DEL",
                "Online-Streamer $streamerName konnte nicht aus den Favoriten entfernt"
            )

            !streamer.live && streamer.favorisiert -> Log.i(
                "Favoriten-Test DEL",
                "Offline-Streamer $streamerName konnte nicht aus den Favoriten entfernt"
            )

            streamer.live && !streamer.favorisiert -> Log.i(
                "Favoriten-Test DEL",
                " Online-Streamer $streamerName wurde aus den Favoriten entfernt"
            )

            !streamer.live && !streamer.favorisiert -> Log.i(
                "Favoriten-Test DEL",
                "Offline-Streamer $streamerName wurde aus den Favoriten entfernt"
            )

            else -> {
                Log.i("Favoriten-Test", "Streamer $streamerName wurde aus den Favoriten entfernt")
            }
        }
    }

    // Funktion zum Abrufen der Anzahl an ListItems
    override fun getItemCount(): Int {
        return itemList.size
    }

    // Methode zum Abrufen des View-Typs eines Listenelements basierend auf seiner Position
    override fun getItemViewType(position: Int): Int {
        val item = itemList[position]
        return when {
            item.live && item.favorisiert -> liveItem
            !item.live && item.favorisiert -> offlineItem
            else -> throw IllegalArgumentException("Ungültiger View-Typ")
        }
    }
}