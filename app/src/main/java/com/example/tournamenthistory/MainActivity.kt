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
        textBar.setOnClickListener {
            db.refreshDB()
            Toast.makeText(this, "DB refreshed", Toast.LENGTH_LONG).show()

        }

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
            //val intent = Intent(this, SearchActivity::class.java)
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        val res1 = Result(0,"one", "three", 1, 2, false)
        val res2 = Result(1,"one", "four", 3, 2, true)
     //   db.insertData(res1)
       // db.insertData(res2)
        Toast.makeText(this, db.retrieveAll().size.toString(), Toast.LENGTH_SHORT).show()
    }
}