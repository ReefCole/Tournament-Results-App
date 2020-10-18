package com.example.tournamenthistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(private val data: List<Result>, private val name: String, private val listener: (Result) -> Unit) : RecyclerView.Adapter<SearchAdapter.ViewHolder>()  {

    var tracker: SelectionTracker<Result>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.row_search, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
            tracker?.let {
                holder.bind(data[position], it.isSelected(data[position]))
            }
    }

    fun getItem(position: Int) = data[position]

    fun getPosition(id: Int) = data.indexOfFirst { it.id == id }


    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        private val opponent: TextView = v.findViewById(R.id.p1)
        private val icon: ImageView = v.findViewById(R.id.winLoss)
        private val score: TextView = v.findViewById(R.id.score)
        private val edit: ImageView = v.findViewById(R.id.edit)
        private var tempScore = ""


        fun bind(item: Result, isActivated: Boolean = false) {
            itemView.isActivated = isActivated

            (item.p1Score > item.p2Score).let {
                if (name == item.p1) {
                    opponent.text = item.p2
                    tempScore = item.p1Score.toString() + "-" + item.p2Score.toString()
                    if (it) icon.setImageResource(R.drawable.w)
                    else icon.setImageResource(R.drawable.l)
                } else {
                    opponent.text = item.p1
                    tempScore = item.p2Score.toString() + "-" + item.p1Score.toString()
                    if (it) icon.setImageResource(R.drawable.l)
                    else icon.setImageResource(R.drawable.w)
                }
            }
            if (isActivated) icon.setImageResource(R.drawable.tick)
            score.text = tempScore
            edit.setOnClickListener { listener(item) }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Result> =
            object : ItemDetailsLookup.ItemDetails<Result>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Result? = data[position]

            }
    }

}