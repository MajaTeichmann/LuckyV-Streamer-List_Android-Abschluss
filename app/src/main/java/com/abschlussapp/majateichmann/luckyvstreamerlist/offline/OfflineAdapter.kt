package com.abschlussapp.majateichmann.luckyvstreamerlist.offline

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer

/**
 * Diese Klasse organisiert mithilfe der ViewHolder Klasse das Recycling
 */
class OfflineAdapter(
    private val dataset: List<Streamer>
) : RecyclerView.Adapter<OfflineAdapter.ItemViewHolder>() {

    /**
     * der ViewHolder umfasst die View und stellt einen Listeneintrag dar
     */
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStreamVorschau: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamername: TextView = itemView.findViewById(R.id.tv_streamername)
        val tvCharname: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktion: TextView = itemView.findViewById(R.id.tv_fraktion)
        val streamerLayout: ConstraintLayout = itemView.findViewById(R.id.cl_offline_streamer)
    }

    /**
     * hier werden neue ViewHolder erstellt
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_offline, parent, false)

        return ItemViewHolder(itemLayout)
    }

    /**
     * damit der LayoutManager weiß, wie lang die Liste ist
     */
    override fun getItemCount(): Int {
        return dataset.size
    }

    /**
     * hier findet der Recyclingprozess statt
     * die vom ViewHolder bereitgestellten Parameter erhalten die Information des Listeneintrags
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        //streamer aus dem dataset holen
        var streamer = dataset[position]

        if (!streamer.live) {
            holder.tvStreamername.text = streamer.name
            holder.tvCharname.text = streamer.ic_name
        }

        // falls der wert im übergebenen Datensatz null ist, befülle ihn mit leerem string
        if (streamer.fraktion == null) {
            holder.tvFraktion.visibility = View.GONE
        }

        // befülle textview mit wert aus übergebener variable (aus API)
        val fraktion = holder.tvFraktion
        fraktion.text = streamer.fraktion

        // falls der string im textview zu lang ist, um in eine zeile zu passen, kürze ihn am ende mit "..." ab
        fraktion.ellipsize = TextUtils.TruncateAt.END
        fraktion.maxLines = 1
        fraktion.isSingleLine = true

        if (streamer.ic_name == null) {
            holder.tvCharname.visibility = View.GONE
        }

        val icName = holder.tvCharname
        icName.text = streamer.ic_name

        icName.ellipsize = TextUtils.TruncateAt.END
        icName.maxLines = 1
        icName.isSingleLine = true

        Log.e("Streamer is live", streamer.live.toString())

        //Logo-URL Laden
        holder.ivStreamVorschau.load(streamer.logo_url)

        val streamerName = holder.tvStreamername
        streamerName.text = streamer.name

        streamerName.ellipsize = TextUtils.TruncateAt.END
        streamerName.maxLines = 1
        streamerName.isSingleLine = true
    }
}