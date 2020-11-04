package com.example.tournamenthistory

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = DataBaseHandler(this)

        val searchBar = findViewById<SearchView>(R.id.search)
        val textBar = findViewById<TextView>(R.id.title)

        // title used to refresh database, mostly for testing purposes.
        textBar.setOnClickListener {
            db.refreshDB()
            Toast.makeText(this, "DB refreshed", Toast.LENGTH_LONG).show()

        }

        // Set up player search bar
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.let { db.checkName(it.toLowerCase()) }!!) {
                    val intent = Intent(applicationContext, SearchActivity::class.java)
                    intent.putExtra("search", query.toLowerCase())
                    startActivity(intent)
                }
                else {
                    Toast.makeText(applicationContext, "No records of player", Toast.LENGTH_LONG).show()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        val allButton = findViewById<Button>(R.id.allButton)
        allButton.setOnClickListener{
            val intent = Intent(this, AllActivity::class.java)
            startActivity(intent)
        }
        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }
}