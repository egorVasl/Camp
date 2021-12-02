package com.example.singupactivity.ui.main.DataBase

import android.provider.BaseColumns

object CampDbNameClass : BaseColumns{

    const val TABLE_NAME = "authorization"
    const val COLUMN_NAME_LOGIN = "login"
    const val COLUMN_NAME_PASSWORD = "password"
    const val COLUMN_NAME_SQUAD = "squad"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "Camp.db"

    const val CREATE_TABLE_AUTHORIZATION =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "${BaseColumns._ID}  INTEGER PRIMARY KEY," +
                "$COLUMN_NAME_LOGIN  TEXT," +
                "$COLUMN_NAME_PASSWORD TEXT,"+
                "$COLUMN_NAME_SQUAD INTEGER)"

    const val DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

}