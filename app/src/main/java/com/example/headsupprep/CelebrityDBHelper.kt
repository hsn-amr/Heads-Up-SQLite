package com.example.headsupprep

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CelebrityDBHelper(context: Context): SQLiteOpenHelper(context, "Celebrities.db", null, 1) {

    private var sqLiteDatabase: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Celebrity (Name text, Taboo1 text, Taboo2 text, Taboo3 text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun saveCelebrity(celebrity: Celebrity): Long {
        val cv = ContentValues()
        cv.put("Name", celebrity.name)
        cv.put("Taboo1", celebrity.taboo1)
        cv.put("Taboo2", celebrity.taboo2)
        cv.put("Taboo3", celebrity.taboo3)
        return sqLiteDatabase.insert("Celebrity", null, cv)
    }

    fun retrieveAllCelebrities(): ArrayList<Celebrity>{
        val celebrities = ArrayList<Celebrity>()
        val cursor: Cursor = sqLiteDatabase.query("Celebrity",null,null,null,null,null,null)

        if(cursor.moveToFirst()){
            do {
                val celebrity = Celebrity()
                celebrity.name = cursor.getString(cursor.getColumnIndex("Name"))
                celebrity.taboo1 = cursor.getString(cursor.getColumnIndex("Taboo1"))
                celebrity.taboo2 = cursor.getString(cursor.getColumnIndex("Taboo2"))
                celebrity.taboo3 = cursor.getString(cursor.getColumnIndex("Taboo3"))
                celebrities.add(celebrity)
            }while (cursor.moveToNext())
        }
        return celebrities
    }
}