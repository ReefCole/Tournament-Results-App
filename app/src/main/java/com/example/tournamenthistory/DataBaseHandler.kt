package com.example.tournamenthistory

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "TourneyDB"
val TABLE_NAME =  "Results"
val COL_ID = "id"
val COL_P1 = "Player1"
val COL_P2 = "Player2"
val COL_S1 = "Score1"
val COL_S2 = "Score2"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
       // if (db?.rawQuery("SELECT name FROM $DATABASE_NAME WHERE type='table' AND name=$TABLE_NAME", null) == null) {
            val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_P1 + " VARCHAR(256), " +
                    COL_P2 + " VARCHAR(256), " +
                    COL_S1 + " INTEGER, " +
                    COL_S2 + " INTEGER)"
            db?.execSQL(createTable)
       // }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(result: Result) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_P1, result.p1)
        cv.put(COL_P2, result.p2)
        cv.put(COL_S1, result.p1Score)
        cv.put(COL_S2, result.p2Score)
        val success = db.insert(TABLE_NAME, null, cv)
        if (success == (0).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveAll(): MutableList<Result>{
        return readData("Select * from $TABLE_NAME")
    }

    fun retrieveSearch(search: String): MutableList<Result>{
        return readData("Select * from $TABLE_NAME Where $COL_P1='$search' OR $COL_P2='$search'")
    }

    private fun readData(query: String): MutableList<Result> {
        val list: MutableList<Result> = ArrayList()
        val db = this.readableDatabase
        val values = db.rawQuery(query, null)
        if (values.moveToFirst()) {
            do {
                val result = Result(
                values.getString(values.getColumnIndex(COL_ID)).toInt(),
                values.getString(values.getColumnIndex(COL_P1)),
                values.getString(values.getColumnIndex(COL_P2)),
                values.getString(values.getColumnIndex(COL_S1)).toInt(),
                values.getString(values.getColumnIndex(COL_S2)).toInt(),
                    (values.getString(values.getColumnIndex(COL_S1)).toInt() >
                    values.getString(values.getColumnIndex(COL_S2)).toInt()))
                list.add(result)
            }
            while (values.moveToNext())
        }
        return list
    }

    fun deleteData(res: String) {
        val db = this.readableDatabase
        db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(res))
       // db.close()
    }

    fun refreshDB() {
        val db = this.readableDatabase
        //db.rawQuery("Delete from $TABLE_NAME", null)
        db.execSQL("Delete from '$TABLE_NAME'")
        db.close()
        insertData(Result(0,"jake", "sunny", 1, 2, false))
        insertData(Result(0,"sunny", "will", 3, 2, true))
        insertData(Result(0,"will", "jake", 0, 2, false))
        insertData(Result(0,"sunny", "cailan", 3, 1, true))
        insertData(Result(0,"cailan", "will", 1, 3, false))
        insertData(Result(0,"jake", "skelo", 1, 2, false))
        insertData(Result(0,"skelo", "kaiza", 1, 3, false))
        insertData(Result(0,"cailan", "jake", 3, 2, true))
        insertData(Result(0,"sunny", "will", 3, 2, true))
        insertData(Result(0,"kaiza", "skelo", 2, 0, true))
        insertData(Result(0,"will", "cailan", 2, 0, true))
        insertData(Result(0,"skelo", "jake", 3, 0, true))
        insertData(Result(0,"skelo", "sunny", 2, 3, false))
        insertData(Result(0,"kaiza", "cailan", 3, 2, true))
        insertData(Result(0,"sunny", "jake", 1, 3, false))
        insertData(Result(0,"cailan", "kaiza", 0, 2, false))
        insertData(Result(0,"will", "kaiza", 1, 2, false))
    }

    fun updateResult(result: Result) {
        val db = this.readableDatabase
        var cv = ContentValues()
        val id = arrayOf(result.id.toString())
        cv.put(COL_P1, result.p1)
        cv.put(COL_P2, result.p2)
        cv.put(COL_S1, result.p1Score)
        cv.put(COL_S2, result.p2Score)
        val success = db.update(TABLE_NAME, cv, "$COL_ID=?", id)
        if (success == (0)) {
              Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
             Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkName(search: String): Boolean{
        return retrieveSearch(search).size != 0
    }
}