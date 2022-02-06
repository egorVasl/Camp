package com.example.singupactivity.ui.main.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.CREATE_TABLE_ACHIEVEMENTS
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.CREATE_TABLE_AUTHORIZATION
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.CREATE_TABLE_AVATAR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.CREATE_TABLE_CHILD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.CREATE_TABLE_COUNSELOR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.CREATE_TABLE_DAILY_SCHEDULE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.CREATE_TABLE_LIVING
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.CREATE_TABLE_ROOM
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.CREATE_TABLE_SQUAD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.CREATE_TABLE_WEEK_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DATABASE_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DATABASE_VERSION
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DELETE_TABLE_ACHIEVEMENTS
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DELETE_TABLE_AUTHORIZATION
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DELETE_TABLE_AVATAR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DELETE_TABLE_CHILD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DELETE_TABLE_COUNSELOR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DELETE_TABLE_DAILY_SCHEDULE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DELETE_TABLE_LIVING
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DELETE_TABLE_ROOM
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DELETE_TABLE_SQUAD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.DELETE_TABLE_WEEK_EVENT

class CampDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_AUTHORIZATION)
        db?.execSQL(CREATE_TABLE_SQUAD)
        db?.execSQL(CREATE_TABLE_COUNSELOR)
        db?.execSQL(CREATE_TABLE_WEEK_EVENT)
        db?.execSQL(CREATE_TABLE_ACHIEVEMENTS)
        db?.execSQL(CREATE_TABLE_CHILD)
        db?.execSQL(CREATE_TABLE_ROOM)
        db?.execSQL(CREATE_TABLE_LIVING)
        db?.execSQL(CREATE_TABLE_DAILY_SCHEDULE)
        db?.execSQL(CREATE_TABLE_AVATAR)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DELETE_TABLE_AUTHORIZATION)
        db?.execSQL(DELETE_TABLE_SQUAD)
        db?.execSQL(DELETE_TABLE_COUNSELOR)
        db?.execSQL(DELETE_TABLE_WEEK_EVENT)
        db?.execSQL(DELETE_TABLE_ACHIEVEMENTS)
        db?.execSQL(DELETE_TABLE_CHILD)
        db?.execSQL(DELETE_TABLE_ROOM)
        db?.execSQL(DELETE_TABLE_LIVING)
        db?.execSQL(DELETE_TABLE_DAILY_SCHEDULE)
        db?.execSQL(DELETE_TABLE_AVATAR)
        onCreate(db)
    }




}