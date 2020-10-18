package com.example.tournamenthistory

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        findViewById<TextView>(R.id.title).text = "Add Result"

        val player1 = findViewById<EditText>(R.id.player1)

        val player2 = findViewById<EditText>(R.id.player2)

        val score1 = findViewById<EditText>(R.id.score1)

        val score2 = findViewById<EditText>(R.id.score2)

        val winner = findViewById<RadioButton>(R.id.winner1)

        val editButton = findViewById<Button>(R.id.addButton)
        editButton.text = "Add"

        editButton.setOnClickListener(){
            if (validate(player1.text.toString(), player2.text.toString(), score1.text.toString().toInt(), score2.text.toString().toInt(), winner.isChecked)) {
                val db = DataBaseHandler(this)
                db.insertData(
                    Result(
                        0,
                        player1.text.toString().toLowerCase(),
                        player2.text.toString().toLowerCase(),
                        score1.text.toString().toInt(),
                        score2.text.toString().toInt(),
                        winner.isChecked
                    )
                )
            }
        }
    }
    fun validate(p1: String, p2: String, s1: Int, s2: Int, win: Boolean): Boolean {
        var check = true

        when {
            !p1.all {it.isLetterOrDigit()} -> {
                check = false
                Toast.makeText(this, "invalid Characters for player 1", Toast.LENGTH_LONG).show()
            }
            !p2.all {it.isLetterOrDigit()} -> {
                check = false
                Toast.makeText(this, "invalid Characters for player 2", Toast.LENGTH_LONG).show()
            }

            p1.toLowerCase(Locale.ROOT) == p2.toLowerCase(Locale.ROOT) -> {
                check = false
                Toast.makeText(this, "identical players", Toast.LENGTH_LONG).show()
            }
            s1 == s2 -> {
                check = false
                Toast.makeText(this, "Draws not Allowed", Toast.LENGTH_LONG).show()
            }
            (s1 > s2) != win -> {
                check = false
                Toast.makeText(this, "Win doesn't match game count", Toast.LENGTH_LONG).show()
            }
        }
        return check
    }
}