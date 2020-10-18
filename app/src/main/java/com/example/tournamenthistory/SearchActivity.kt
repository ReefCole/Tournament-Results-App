package com.example.tournamenthistory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class SearchActivity : AppCompatActivity(), ActionMode.Callback {

    private lateinit var adapter: SearchAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var tracker: SelectionTracker<Result>? = null
    private var selectedPostItems: MutableList<Result> = mutableListOf()
    private var actionMode: ActionMode? = null
    private var key: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val numberList = findViewById<RecyclerView>(R.id.recyclerSearch)

        linearLayoutManager = LinearLayoutManager(this)
        numberList.layoutManager = linearLayoutManager

        key = intent.getStringExtra("search")

        findViewById<TextView>(R.id.resultsText).text = key

        //val numData = initData()
        val db = DataBaseHandler(this)
        val numData = db.retrieveSearch(key)

        adapter = SearchAdapter(numData, key) {showEdit(it)}
        numberList.adapter = adapter

        tracker = SelectionTracker.Builder<Result>(
            "mySelection",
            numberList,
            MyItemKeyProvider(adapter),
            MyItemDetailsLookup(numberList),
            StorageStrategy.createParcelableStorage(Result::class.java)
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        adapter.tracker = tracker

        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    tracker?.let {
                        selectedPostItems = it.selection.toMutableList()
                        if (selectedPostItems.isEmpty()) {
                            actionMode?.finish()
                        } else {
                            if (actionMode == null) actionMode = startSupportActionMode(this@SearchActivity)
                            actionMode?.title = "${selectedPostItems.size}"
                        }
                        Log.i("post", selectedPostItems.size.toString())
                    }
                }
            })
    }

     private fun showEdit(item: Result) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("result", item)
        startActivity(intent)
    }

    private fun initData() : List<Result>{
        val res1 = Result(0,"one", "two", 1, 2, false)
        val res2 = Result(1,"three", "four", 3, 2, true)
        val res3 = Result(2,"four", "three", 1, 3, false)
        val res4 = Result(3,"three", "one", 0, 3, true)
        return listOf(res1, res2, res3, res4)
    }

    fun refresh() {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra("search", key)
        finish()
        startActivity(intent)
    }


    override fun onRestart() {
        super.onRestart()
        refresh()
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_view_delete -> {
                val db = DataBaseHandler(this)
                selectedPostItems.forEach{db.deleteData(it.id.toString())}
                refresh()
            }
        }
        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.let {
            val inflater: MenuInflater = it.menuInflater
            inflater.inflate(R.menu.action, menu)
            return true
        }
        return false
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        Log.i("post", "closing")
        adapter.tracker?.clearSelection()
        //adapter.notifyDataSetChanged()
        actionMode = null
    }

}