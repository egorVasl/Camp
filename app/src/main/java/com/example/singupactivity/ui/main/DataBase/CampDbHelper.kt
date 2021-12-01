package com.example.singupactivity.ui.main.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.CREATE_TABLE_AUTHORIZATION
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DATABASE_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DATABASE_VERSION
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DELETE_TABLE

class CampDbHelper(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_AUTHORIZATION)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DELETE_TABLE)
        onCreate(db)
    }


}