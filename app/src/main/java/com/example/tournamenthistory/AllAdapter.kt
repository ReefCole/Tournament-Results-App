package com.example.tournamenthistory

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView

class AllAdapter(private val data: List<Result>, private val listener: (Result) -> Unit) : RecyclerView.Adapter<AllAdapter.ViewHolder>()  {

    var tracker: SelectionTracker<Result>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.row_all, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    // tracker handler for multi select
    override fun onBindViewHolder(holder: AllAdapter.ViewHolder, position: Int) {
        tracker?.let {
            holder.bind(data[position], it.isSelected(data[position]))
        }
    }

    //functions to serve the ItemKeyProvider for multi select
    fun getItem(position: Int) = data[position]

    fun getPosition(id: Int) = data.indexOfFirst { it.id == id }


    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        private val player1: TextView = v.findViewById(R.id.p1)
        private val player2: TextView = v.findViewById(R.id.p2)
        private val score: TextView = v.findViewById(R.id.score)
        private val edit: ImageView = v.findViewById(R.id.edit)

        // sets data from db, winner is displayed in bold
        fun bind(item: Result, isActivated: Boolean = false) {
            itemView.isActivated = isActivated
            player1.text = item.p1
            player2.text = item.p2
            val tempScore = item.p1Score.toString() + "-" + item.p2Score.toString()
            score.text = tempScore
            if (item.p1Score > item.p2Score)
                player1.setTypeface(null, Typeface.BOLD)
            else
                player2.setTypeface(null, Typeface.BOLD)
            edit.setOnClickListener {listener(item) }
        }

        // function for using ItemDetailsLookup
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Result> =
            object : ItemDetailsLookup.ItemDetails<Result>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Result? = data[position]
            }

    }

}