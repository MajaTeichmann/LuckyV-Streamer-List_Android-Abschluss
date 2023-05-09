import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.abschlussapp.majateichmann.luckyvstreamerlist.R
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.AppRepository
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.datamodels.Streamer
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.datamodels.StreamerList
import com.abschlussapp.majateichmann.luckyvstreamerlist.data.remote.StreamerApi

/**
 * Diese Klasse organisiert mithilfe der ViewHolder Klasse das Recycling
 */
class LiveAdapter(
    private val dataset: List<Streamer>
) : RecyclerView.Adapter<LiveAdapter.ItemViewHolder>() {

    /**
     * der ViewHolder umfasst die View und stellt einen Listeneintrag dar
     */
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStreamVorschau: ImageView = itemView.findViewById(R.id.iv_stream_vorschau)
        val tvStreamername: TextView = itemView.findViewById(R.id.tv_streamername)
        val tvCharname: TextView = itemView.findViewById(R.id.tv_charname)
        val tvFraktion: TextView = itemView.findViewById(R.id.tv_fraktion)
    }


    /**
     * hier werden neue ViewHolder erstellt
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_live, parent, false)

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

        if(streamer.fraktion==null){
            streamer.fraktion = "{nicht hinterlegt}"
        }
        holder.tvFraktion.text = streamer.fraktion

        if(streamer.ic_name==null){
            streamer.ic_name = "{nicht hinterlegt}"
        }
        holder.tvCharname.text = streamer.ic_name

        Log.e("test", streamer.live.toString())


        //Logo-URL Laden
        holder.ivStreamVorschau.load(streamer.logo_url)

        holder.tvStreamername.text = streamer.name

    }
}