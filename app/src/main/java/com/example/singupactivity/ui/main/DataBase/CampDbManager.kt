package com.example.singupactivity.ui.main.DataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_ACHIEVEMENTS_PLACE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_CHILD_BIRTHDAY
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_CHILD_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_CHILD_PATRONYMIC
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_CHILD_SURNAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_BIRTHDAY
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_NUMBER
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_PATRONYMIC
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_SURNAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_EVENT_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_FLOOR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_ID_AUTHORIZATION_COUNSELOR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_ID_SQUAD_COUNSELOR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_LOGIN
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_NAME_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_PARENTS_NUMBER
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_PASSWORD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_QUANTITY_CHILD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_ROOM_NUMBER
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_NUMBER
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.TABLE_NAME_ACHIEVEMENTS
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.TABLE_NAME_AUTHORIZATION
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.TABLE_NAME_CHILD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.TABLE_NAME_COUNSELOR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.TABLE_NAME_DAILY_SCHEDULE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.TABLE_NAME_LIVING
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.TABLE_NAME_ROOM
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.TABLE_NAME_SQUAD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.TABLE_NAME_WEEK_EVENT


class CampDbManager(context: Context) {

    private val campDbHelper = CampDbHelper(context)
    private lateinit var db: SQLiteDatabase

    fun openDb() {
        db = campDbHelper.writableDatabase
    }

    /**
     * Table Authorization
     */

    fun insertToTableAuthorization(login: String, password: String, squad: Int) {
        val cv = ContentValues().apply {
            put(COLUMN_NAME_LOGIN, login)
            put(COLUMN_NAME_PASSWORD, password)
            put(COLUMN_NAME_SQUAD, squad)
        }
        val rowID = db.insert(TABLE_NAME_AUTHORIZATION, null, cv)

        val cvCounselor = ContentValues().apply {
            put(COLUMN_NAME_COUNSELOR_NAME, "null")
            put(COLUMN_NAME_COUNSELOR_SURNAME, "null")
            put(COLUMN_NAME_COUNSELOR_PATRONYMIC, "null")
            put(COLUMN_NAME_COUNSELOR_BIRTHDAY, "null")
            put(COLUMN_NAME_COUNSELOR_NUMBER, "null")
            put(COLUMN_NAME_ID_AUTHORIZATION_COUNSELOR, rowID)
            put(COLUMN_NAME_ID_SQUAD_COUNSELOR, "null")
        }

        val rowIDCounselor = db.insert(TABLE_NAME_COUNSELOR, null, cvCounselor)
    }

    @SuppressLint("Range")
    fun selectToTableAuthorization(const: String): ArrayList<String> {
        openDb()
        val dataList = ArrayList<String>()
        val cursor = db.query(
            TABLE_NAME_AUTHORIZATION, null, null,
            null, null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }

    /**
     * Table Squad
     */

    fun insertToTableSquad(squadName: String, squadNumber: Int) {
        val cv = ContentValues().apply {
            put(COLUMN_NAME_SQUAD_NAME, squadName)
            put(COLUMN_NAME_SQUAD_NUMBER, squadNumber)
        }
        val rowID = db.insert(TABLE_NAME_SQUAD, null, cv)
    }

    @SuppressLint("Range")
    fun selectToTableSquad(const: String): ArrayList<String> {
        openDb()
        val dataList = ArrayList<String>()
        val cursor = db.query(
            TABLE_NAME_SQUAD, null, null,
            null, null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }

    /**
     * Table Counselor
     */

    fun insertToTableCounselor(
        counselorName: String, counselorSurname: String,
        counselorPatronymic: String, counselorBirthday: String,
        counselorNumber: String
    ) {
        val cv = ContentValues().apply {
            put(COLUMN_NAME_COUNSELOR_NAME, counselorName)
            put(COLUMN_NAME_COUNSELOR_SURNAME, counselorSurname)
            put(COLUMN_NAME_COUNSELOR_PATRONYMIC, counselorPatronymic)
            put(COLUMN_NAME_COUNSELOR_BIRTHDAY, counselorBirthday)
            put(COLUMN_NAME_COUNSELOR_NUMBER, counselorNumber)
        }
        val rowID = db.insert(TABLE_NAME_COUNSELOR, null, cv)
    }

    @SuppressLint("Range")
    fun selectToTableCounselor(const: String): ArrayList<String> {
        openDb()
        val dataList = ArrayList<String>()
        val cursor = db.query(
            TABLE_NAME_COUNSELOR, null, null,
            null, null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }

    /**
     *  Table Week Event
     */

    fun insertToTableWeekEvent(
        date: String, time: String,
        eventName: String
    ) {
        val cv = ContentValues().apply {
            put(COLUMN_NAME_DATE, date)
            put(COLUMN_NAME_TIME, time)
            put(COLUMN_NAME_EVENT_NAME, eventName)

        }
        val rowID = db.insert(TABLE_NAME_WEEK_EVENT, null, cv)
    }

    @SuppressLint("Range")
    fun selectToTableWeekEvent(const: String): ArrayList<String> {
        openDb()
        val dataList = ArrayList<String>()
        val cursor = db.query(
            TABLE_NAME_WEEK_EVENT, null, null,
            null, null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }

    /**
     *  Table Achievements
     */

    fun insertToTableAchievements(achievements_place: Int) {
        val cv = ContentValues().apply {
            put(COLUMN_NAME_ACHIEVEMENTS_PLACE, achievements_place)
        }
        val rowID = db.insert(TABLE_NAME_ACHIEVEMENTS, null, cv)
    }

    @SuppressLint("Range")
    fun selectToTableAchievements(const: String): ArrayList<String> {
        openDb()
        val dataList = ArrayList<String>()
        val cursor = db.query(
            TABLE_NAME_ACHIEVEMENTS, null, null,
            null, null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }

    /**
     * Table Child
     */

    fun insertToTableChild(
        childName: String, childSurname: String,
        childPatronymic: String, childBirthday: String,
        parentsNumber: String
    ) {
        val cv = ContentValues().apply {
            put(COLUMN_NAME_CHILD_NAME, childName)
            put(COLUMN_NAME_CHILD_SURNAME, childSurname)
            put(COLUMN_NAME_CHILD_PATRONYMIC, childPatronymic)
            put(COLUMN_NAME_CHILD_BIRTHDAY, childBirthday)
            put(COLUMN_NAME_PARENTS_NUMBER, parentsNumber)
        }
        val rowID = db.insert(TABLE_NAME_CHILD, null, cv)
    }

    @SuppressLint("Range")
    fun selectToTableChild(const: String): ArrayList<String> {
        openDb()
        val dataList = ArrayList<String>()
        val cursor = db.query(
            TABLE_NAME_CHILD, null, null,
            null, null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }

    /**
     * Table Room
     */

    fun insertToTableRoom(
        floor: Int, roomNumber: Int,
        quantityChild: Int
    ) {
        val cv = ContentValues().apply {
            put(COLUMN_NAME_FLOOR, floor)
            put(COLUMN_NAME_ROOM_NUMBER, roomNumber)
            put(COLUMN_NAME_QUANTITY_CHILD, quantityChild)

        }
        val rowID = db.insert(TABLE_NAME_ROOM, null, cv)
    }

    @SuppressLint("Range")
    fun selectToTableRoom(const: String): ArrayList<String> {
        openDb()
        val dataList = ArrayList<String>()
        val cursor = db.query(
            TABLE_NAME_ROOM, null, null,
            null, null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }

    /**
     * Table Living
     */

//    fun insertToTableLiving(floor : Int, roomNumber : Int,
//                          quantityChild : Int){
//        val cv = ContentValues().apply {
//            put(COLUMN_NAME_FLOOR, floor)
//            put(COLUMN_NAME_ROOM_NUMBER, roomNumber)
//            put(COLUMN_NAME_QUANTITY_CHILD, quantityChild)
//
//        }
//        val rowID = db.insert(TABLE_NAME_ROOM, null, cv)
//    }

    @SuppressLint("Range")
    fun selectToTableLiving(const: String): ArrayList<String> {
        openDb()
        val dataList = ArrayList<String>()
        val cursor = db.query(
            TABLE_NAME_LIVING, null, null,
            null, null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }

    /**
     * Table Daily Schedule
     */

    fun insertToTableDailySchedule(
        timeEvent: String, nameEvent: String,
        dateEvent: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_TIME_EVENT, timeEvent)
            put(COLUMN_NAME_NAME_EVENT, nameEvent)
            put(COLUMN_NAME_DATE_EVENT, dateEvent)

        }
        val rowID = db.insert(TABLE_NAME_DAILY_SCHEDULE, null, cv)
        closeDb()
    }

    @SuppressLint("Range")
    fun selectToTableDailySchedule(const: String): ArrayList<String> {
        openDb()
        val dataList = ArrayList<String>()
        val cursor = db.query(
            TABLE_NAME_DAILY_SCHEDULE, null, null,
            null, null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        closeDb()
        return dataList
    }

    fun deleteRawToTableDailySchedule(const: String) {
        openDb()
        val delCount = db.delete(TABLE_NAME_DAILY_SCHEDULE, "name_event = '$const'", null)
        closeDb()
    }

    fun updateRawToTableDailySchedule(timeEvent: String, nameEvent: String,
                                      dateEvent: String, nameEventUpdatePosition: String) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_TIME_EVENT, timeEvent)
            put(COLUMN_NAME_NAME_EVENT, nameEvent)
            put(COLUMN_NAME_DATE_EVENT, dateEvent)
        }

        val updCount = db.update(TABLE_NAME_DAILY_SCHEDULE, cv, "name_event = ?",
            arrayOf(nameEventUpdatePosition)
        )
        closeDb()
    }

    fun closeDb() {
        campDbHelper.close()
    }

}