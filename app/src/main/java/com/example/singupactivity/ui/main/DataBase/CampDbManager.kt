package com.example.singupactivity.ui.main.DataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class CampDbManager(context: Context) {

    private val campDbHelper = CampDbHelper(context)
    private lateinit var db: SQLiteDatabase

    fun openDb() {
        db = campDbHelper.writableDatabase
    }

    fun insertToTableAuthorization(login : String, password : String, squad : Int){
        val cv = ContentValues().apply {
            put(CampDbNameClass.COLUMN_NAME_LOGIN, login)
            put(CampDbNameClass.COLUMN_NAME_PASSWORD, password)
            put(CampDbNameClass.COLUMN_NAME_SQUAD, squad)
        }
            val rowID = db.insert(CampDbNameClass.TABLE_NAME_AUTHORIZATION, null, cv)
    }

    @SuppressLint("Range")
    fun selectToTableAuthorization(const: String) : ArrayList<String> {
        openDb()
        val dataList = ArrayList<String>()
        val cursor = db.query(CampDbNameClass.TABLE_NAME_AUTHORIZATION, null,null,
            null,null,null,null)

        while(cursor?.moveToNext()!!){
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }

    fun closeDb() {
        campDbHelper.close()
    }

}