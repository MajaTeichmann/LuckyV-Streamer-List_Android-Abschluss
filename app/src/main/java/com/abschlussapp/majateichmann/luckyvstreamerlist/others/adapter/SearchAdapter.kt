package com.abschlussapp.majateichmann.luckyvstreamerlist.others.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.abschlussapp.majateichmann.luckyvstreamerlist.others.data.datamodels.Streamer

//todo: erstellen

//TODO: Kommentare bearbeitet ❌
class SearchAdapter(
    context: Context,
    streamer: List<Streamer>
) : ArrayAdapter<Streamer>(context, android.R.layout.simple_dropdown_item_1line, streamer) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View{
        val view = convertView?: LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false)
        val item = getItem(position)
        if (item != null) {
            view.findViewById<TextView>(android.R.id.text1)?.text = item.name
        }

        return view
    }
}