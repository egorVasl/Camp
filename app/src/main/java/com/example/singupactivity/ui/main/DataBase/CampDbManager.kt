package com.example.singupactivity.ui.main.DataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class CampDbManager(context: Context) {

    val campDbHelper = CampDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = campDbHelper.writableDatabase
    }

    fun insertToTableAuthorization(login : String, password : String, squad : Int){
        val cv = ContentValues().apply {
            put(CampDbNameClass.COLUMN_NAME_LOGIN, login)
            put(CampDbNameClass.COLUMN_NAME_PASSWORD, password)
            put(CampDbNameClass.COLUMN_NAME_SQUAD, squad)
        }

        db?.insertOrThrow(CampDbNameClass.TABLE_NAME, null, cv)
    }

    @SuppressLint("Range", "Recycle")
    fun selectToTableAuthorization(const: String) : ArrayList<String> {
        val dataList = ArrayList<String>()
//        val cursor = db?.query(CampDbNameClass.TABLE_NAME, arrayOf(const),null,
//            null,null,null,null)
        val cursor = db?.rawQuery("select $const from ${CampDbNameClass.TABLE_NAME}", null)

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
//object Data {
//
//    var emailList = ArrayList<String>()
//    var passwordList = ArrayList<String>()
//    var squadList = ArrayList<String>()
//
//}