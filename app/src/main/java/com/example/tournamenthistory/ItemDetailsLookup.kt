package com.example.tournamenthistory

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Result>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Result>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as SearchAdapter.ViewHolder)
                .getItemDetails()
        }
        return null
    }
}

class MyItemDetailsLookupAll(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Result>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Result>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as AllAdapter.ViewHolder)
                .getItemDetails()
        }
        return null
    }
}