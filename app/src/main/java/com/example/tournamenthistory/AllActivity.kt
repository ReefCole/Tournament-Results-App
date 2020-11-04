package com.example.tournamenthistory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AllActivity : AppCompatActivity(), ActionMode.Callback {

    private lateinit var adapter: AllAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var tracker: SelectionTracker<Result>? = null
    private var selectedPostItems: MutableList<Result> = mutableListOf()
    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all)

        val resultList = findViewById<RecyclerView>(R.id.recyclerAll)

        linearLayoutManager = LinearLayoutManager(this)
        resultList.layoutManager = linearLayoutManager

        // set up database, retrieve full table as a list
        val db = DataBaseHandler(this)
        val numData = db.retrieveAll()

        //sets up All adapter
        adapter = AllAdapter(numData) {showEdit(it)}
        resultList.adapter = adapter

        // selection tracker is built and attached to adapter
        tracker = SelectionTracker.Builder<Result>(
            "mySelection",
            resultList,
            MyItemKeyProviderAll(adapter),
            MyItemDetailsLookupAll(resultList),
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
                            if (actionMode == null) actionMode = startSupportActionMode(this@AllActivity)
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
        return listOf(res1, res2)
    }

    // used to refresh the recycler view after items are deleted or activity is returned to after edit
    fun refresh() {
        finish()
        startActivity(Intent(this, AllActivity::class.java))
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