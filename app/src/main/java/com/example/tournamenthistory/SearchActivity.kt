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

        // key to retrieve data from db
        key = intent.getStringExtra("search")

        findViewById<TextView>(R.id.resultsText).text = key

        // set up database, retrieve items using key
        val db = DataBaseHandler(this)
        val numData = db.retrieveSearch(key)

        //sets up search adapter using key
        adapter = SearchAdapter(numData, key) {showEdit(it)}
        numberList.adapter = adapter

        // selection tracker is built and attached to adapter
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

        //observer set up, creates action mode long click, remains until selected list is empty
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

    // used to refresh the recycler view after items are deleted or activity is returned to after edit
    private fun refresh() {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra("search", key)
        finish()
        startActivity(intent)
    }


    override fun onRestart() {
        super.onRestart()
        refresh()
    }

    // sets up delete button in action bar, deletes every selected item
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
    // inflates menu bar when action is created
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
        actionMode = null
    }

}