package com.example.singupactivity.ui.main.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class CampDbManager(val context: Context) {

    val campDbHelper = CampDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = campDbHelper.writableDatabase
    }

    fun insertToTableAuthorization(email : String, password : String, squad : Int){
        val values = ContentValues().apply {
            put(CampDbNameClass.COLUMN_NAME_EMAIL, email)
            put(CampDbNameClass.COLUMN_NAME_PASSWORD, password)
            put(CampDbNameClass.COLUMN_NAME_SQUAD, squad)
        }

        db?.insert(CampDbNameClass.TABLE_NAME, null, values)
    }

    fun closeDb() {
        campDbHelper.close()
    }

}