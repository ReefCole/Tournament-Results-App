package com.example.tournamenthistory

import androidx.recyclerview.selection.ItemKeyProvider

// key provider for each adapter
class MyItemKeyProvider(private val adapter: SearchAdapter) :
    ItemKeyProvider<Result>(SCOPE_CACHED) {

    override fun getKey(position: Int): Result? {
        return adapter.getItem(position)
    }

    override fun getPosition(key: Result): Int {
        return adapter.getPosition(key.id!!)
    }
}
class MyItemKeyProviderAll(private val adapter: AllAdapter) :
    ItemKeyProvider<Result>(SCOPE_CACHED) {

    override fun getKey(position: Int): Result? {
        return adapter.getItem(position)
    }

    override fun getPosition(key: Result): Int {
        return adapter.getPosition(key.id!!)
    }
}
