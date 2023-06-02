package com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.StreamerDiffUtil
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.ui.MainViewModel

//TODO: Kommentare bearbeitet ❌

class FavoritesLiveAdapter(
    private val viewModel: MainViewModel
) : ListAdapter<Streamer, FavoritesLiveAdapter.ItemViewHolder>(StreamerDiffUtil()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        // Gib den Namen des Streamers als ID zurück
        return currentList[position].name.hashCode().toLong()
    }

    // ViewHolder für liveItem
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStreamVorschau: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamername: TextView = itemView.findViewById(R.id.tv_streamername)
        val tvCharname: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktion: TextView = itemView.findViewById(R.id.tv_fraktion)
        val like: AppCompatImageButton = itemView.findViewById(R.id.btn_favorites)

        fun getAdapterPositionInRecyclerView(): Int {
            return bindingAdapterPosition
        }
    }

    // Funktion zum Erstellen des richtigen ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_favorites_live, parent, false)

        return ItemViewHolder(itemLayout)
    }

    /** hier findet der Recyclingprozess statt
     * Die vom ViewHolder bereitgestellten Parameter erhalten die Information des Listeneintrags */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        /** streamer aus dem dataset holen */
        val streamerList = currentList[position]

        if (streamerList.favorisiert) {
            holder.like.setBackgroundResource(R.drawable.red_heart)
        } else {
            holder.like.setBackgroundResource(R.drawable.grey_heart)
        }

        if (!streamerList.live) {
            holder.tvStreamername.text = streamerList.name
            holder.tvCharname.text = streamerList.ic_name
        }

        /** falls der wert im übergebenen Datensatz null ist, befülle ihn mit leerem string */
        if (streamerList.fraktion == null) {
            holder.tvFraktion.visibility = View.GONE
        }

        /** befülle textview mit wert aus übergebener variable (aus API) */
        val fraktion = holder.tvFraktion
        fraktion.text = streamerList.fraktion

        /** falls der string im textview zu lang ist, um in eine zeile zu passen,
         * kürze ihn am ende mit "..." ab */
        fraktion.ellipsize = TextUtils.TruncateAt.END
        fraktion.maxLines = 1
        fraktion.isSingleLine = true

        if (streamerList.ic_name == null) {
            holder.tvCharname.visibility = View.GONE
        }

        val icName = holder.tvCharname
        icName.text = streamerList.ic_name

        icName.ellipsize = TextUtils.TruncateAt.END
        icName.maxLines = 1
        icName.isSingleLine = true

        /** Logo-URL Laden */
        holder.ivStreamVorschau.load(streamerList.logo_url)

        val streamerName = holder.tvStreamername
        streamerName.text = streamerList.name

        streamerName.ellipsize = TextUtils.TruncateAt.END
        streamerName.maxLines = 1
        streamerName.isSingleLine = true

        /** Button-Click-Listener */
        holder.like.setOnClickListener {

            val adapterPositionInRecyclerView = holder.getAdapterPositionInRecyclerView()
            val streamerPosition = currentList[adapterPositionInRecyclerView]

            streamerPosition.favorisiert = !streamerPosition.favorisiert
            viewModel.updateStreamer(streamerPosition)

            notifyItemChanged(position)
        }
    }
}