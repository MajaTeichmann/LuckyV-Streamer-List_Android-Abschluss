package com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter

import android.text.TextUtils
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
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui.MainViewModel

class FavoritesOfflineAdapter(
    private val dataset: List<Streamer>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /** ViewHolder für OfflineItem */
    class ViewHolderOfflineItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStreamVorschau: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamerName: TextView = itemView.findViewById(R.id.tv_streamername)
        val tvCharName: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktion: TextView = itemView.findViewById(R.id.tv_fraktion)
        val like: AppCompatImageButton = itemView.findViewById(R.id.btn_favorites)
    }

    /** Funktion zum Erstellen des richtigen ViewHolders */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_favorites_offline, parent, false)

        return ViewHolderOfflineItem(view)
    }

    /** Funktion zum Abrufen der Anzahl an ListItems */
    override fun getItemCount(): Int {
        return dataset.size
    }

    /** Funktion zum Binden der Daten an ViewHolder */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val streamer = dataset[position]

        val viewHolder = holder as ViewHolderOfflineItem

        if (!streamer.live) {
            viewHolder.tvStreamerName.text = streamer.name
            viewHolder.tvCharName.text = streamer.ic_name
        }

        if (streamer.favorisiert) {
            viewHolder.like.setBackgroundResource(R.drawable.red_heart)
        } else {
            viewHolder.like.setBackgroundResource(R.drawable.grey_heart)
        }

        /** Falls der Wert im übergebenen Datensatz null ist, befülle ihn mit leerem String */
        if (streamer.fraktion == null) {
            viewHolder.tvFraktion.visibility = View.GONE
        }

        if (streamer.ic_name == null) {
            viewHolder.tvCharName.visibility = View.GONE
        }

        /** Befülle TextView mit Wert aus übergebener Variable (aus API) */
        val fraktion = viewHolder.tvFraktion
        fraktion.text = streamer.fraktion

        val icName = viewHolder.tvCharName
        icName.text = streamer.ic_name

        val streamerName = viewHolder.tvStreamerName
        streamerName.text = streamer.name

        /** Falls der String im TextView zu lang ist, um in eine Zeile zu passen, kürze ihn am Ende mit "..." ab */
        fraktion.ellipsize = TextUtils.TruncateAt.END
        fraktion.maxLines = 1
        fraktion.isSingleLine = true

        icName.ellipsize = TextUtils.TruncateAt.END
        icName.maxLines = 1
        icName.isSingleLine = true

        streamerName.ellipsize = TextUtils.TruncateAt.END
        streamerName.maxLines = 1
        streamerName.isSingleLine = true

        /** Logo-URL laden */
        viewHolder.ivStreamVorschau.load(streamer.logo_url)

        /** Button-Click-Listener */
        holder.like.setOnClickListener {
            streamer.favorisiert = !streamer.favorisiert
            viewModel.updateStreamer(streamer)
        }
    }
}