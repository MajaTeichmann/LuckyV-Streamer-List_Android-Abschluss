package com.abschlussapp.majateichmann.luckyvstreamerlist.adapter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import java.util.Locale


//Diese Klasse verwendet eine List-Implementierung als Datenquelle und implementiert die Filterable-Schnittstelle, um die automatische Vervollständigungsfunktion zu ermöglichen:

//Diese Klasse erweitert die ArrayAdapter-Klasse und implementiert die Filterable-Schnittstelle, um die automatische Vervollständigungsfunktion zu ermöglichen.
//class FilterAdapter(context: Context, private val streamer: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line),
//    Filterable, Parcelable {
//
//    private val streamerFilter = StreamerFilter()
//
//    constructor(parcel: Parcel) : this(
//        TODO("context"),
//        parcel.createStringArrayList()
//    ) {
//    }
//
//    override fun getCount(): Int {
//        return streamer.size
//    }
//
//    override fun getItem(position: Int): String? {
//        return streamer[position]
//    }
//
//    override fun getFilter(): Filter {
//        return streamerFilter
//    }
//
//    //Die performFiltering()-Methode filtert die Liste der Vorschläge basierend auf dem eingegebenen Text und gibt eine FilterResults-Instanz zurück, die die Ergebnisse enthält.
//    inner class StreamerFilter : Filter() {
//        override fun performFiltering(constraint: CharSequence?): FilterResults {
//            val filterResults = FilterResults()
//
//            if (constraint != null) {
//                val suggestions = streamer.filter { it.lowercase(Locale.ROOT).startsWith(constraint.toString().lowercase(
//                    Locale.ROOT)) }
//                filterResults.values = suggestions
//                filterResults.count = suggestions.size
//            }
//
//            return filterResults
//        }
//
//        //Die publishResults()-Methode aktualisiert die Anzeige mit den gefundenen Vorschlägen
//        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//            if (results != null && results.count > 0) {
//                clear()
//                addAll(results.values as List<String>)
//                notifyDataSetChanged()
//            } else {
//                notifyDataSetInvalidated()
//            }
//        }
//
//        // Die convertResultToString()-Methode gibt den Text zurück, der in das EditText-Feld eingefügt werden soll.
//        override fun convertResultToString(resultValue: Any?): CharSequence {
//            return resultValue as CharSequence
//        }
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeStringList(streamer)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<FilterAdapter> {
//        override fun createFromParcel(parcel: Parcel): FilterAdapter {
//            return FilterAdapter(parcel)
//        }
//
//        override fun newArray(size: Int): Array<FilterAdapter?> {
//            return arrayOfNulls(size)
//        }
//    }
//}