package com.example.singupactivity.ui.main.DataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_ACHIEVEMENTS_PLACE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_AVATAR
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
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_EVENT_ACHIEVEMENTS
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_ACHIEVEMENTS
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_LOGIN
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_LOGIN_AVATAR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_LOGIN_COUNSELOR
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
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.TABLE_NAME_AVATAR
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
     * Table Avatar
     */
    fun insertToTableAvatar(img: ByteArray, login: String) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_LOGIN_AVATAR, login)
            put(COLUMN_NAME_AVATAR, img)
        }
        val rowID = db.insert(TABLE_NAME_AVATAR, null, cv)

        closeDb()
    }

    @SuppressLint("Range")
    fun selectToTableAvatarLogin(const: String): ArrayList<String> {
        openDb()
        val dataList = ArrayList<String>()
        val cursor = db.query(
            TABLE_NAME_AVATAR, null, null,
            null, null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }

    @SuppressLint("Range")
    fun selectToTableAvatarImage(const: String): ArrayList<ByteArray> {
        openDb()
        val dataList = ArrayList<ByteArray>()
        val cursor = db.query(
            TABLE_NAME_AVATAR, null, null,
            null, null, null, null
        )
        while (cursor.moveToNext()) {
            val dataText = cursor.getBlob(cursor.getColumnIndex(const))
            dataList.add(dataText)
        }
        cursor.close()
        return dataList
    }

    fun updateRawToTableAvatar(
        img: ByteArray, login: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_LOGIN_AVATAR, login)
            put(COLUMN_NAME_AVATAR, img)
        }

        val updCount = db.update(
            TABLE_NAME_AVATAR, cv, "login_avatar = ?",
            arrayOf(login)
        )
        closeDb()
    }

    /**
     * Table Authorization
     */

    fun insertToTableAuthorization(login: String, password: String, squad: String) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_LOGIN, login)
            put(COLUMN_NAME_PASSWORD, password)
            put(COLUMN_NAME_SQUAD, squad)
        }
        val rowID = db.insert(TABLE_NAME_AUTHORIZATION, null, cv)
        closeDb()
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

    fun insertToTableSquad(squadName: String, squadNumber: String) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_SQUAD_NAME, squadName)
            put(COLUMN_NAME_SQUAD_NUMBER, squadNumber)
        }
        val rowID = db.insert(TABLE_NAME_SQUAD, null, cv)
        closeDb()
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

    @SuppressLint("Range")
    fun selectToTableSquad(const: String, searchText: String, selectionArguments: String): ArrayList<String> {
        openDb()
        val sqlQuery = ("select * from $TABLE_NAME_SQUAD where $selectionArguments = '$searchText';")
        val dataList = ArrayList<String>()
        val cursor = db.rawQuery(sqlQuery, null)

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        closeDb()
        return dataList
    }


    fun deleteRawToTableSquads(const: String) {
        openDb()
        val delCount = db.delete(TABLE_NAME_SQUAD, "squad_name = '$const'", null)
        closeDb()
    }

    fun updateRawToTableSquads(
        squadName: String, squadNumber: String,
        squadsNameUpdatePosition: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_SQUAD_NAME, squadName)
            put(COLUMN_NAME_SQUAD_NUMBER, squadNumber)
        }

        val updCount = db.update(
            TABLE_NAME_SQUAD, cv, "squad_name = ?",
            arrayOf(squadsNameUpdatePosition)
        )
        closeDb()
    }

    /**
     * Table Counselor
     */

    fun insertToTableCounselor(
        counselorName: String, counselorSurname: String,
        counselorPatronymic: String, counselorBirthday: String,
        counselorNumber: String, loginCounselor: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_COUNSELOR_NAME, counselorName)
            put(COLUMN_NAME_COUNSELOR_SURNAME, counselorSurname)
            put(COLUMN_NAME_COUNSELOR_PATRONYMIC, counselorPatronymic)
            put(COLUMN_NAME_COUNSELOR_BIRTHDAY, counselorBirthday)
            put(COLUMN_NAME_COUNSELOR_NUMBER, counselorNumber)
            put(COLUMN_NAME_LOGIN_COUNSELOR, loginCounselor)

        }
        val rowID = db.insert(TABLE_NAME_COUNSELOR, null, cv)
        closeDb()
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

    fun updateRawToTableCounselor(
        counselorName: String, counselorSurname: String,
        counselorPatronymic: String, counselorBirthday: String,
        counselorNumber: String, loginCounselor: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_COUNSELOR_NAME, counselorName)
            put(COLUMN_NAME_COUNSELOR_SURNAME, counselorSurname)
            put(COLUMN_NAME_COUNSELOR_PATRONYMIC, counselorPatronymic)
            put(COLUMN_NAME_COUNSELOR_BIRTHDAY, counselorBirthday)
            put(COLUMN_NAME_COUNSELOR_NUMBER, counselorNumber)
        }

        val updCount = db.update(
            TABLE_NAME_COUNSELOR, cv, "login_counselor = ?",
            arrayOf(loginCounselor)
        )
        closeDb()

    }

    /**
     *  Table Week Event
     */

    fun insertToTableWeekEvent(
        date: String, time: String,
        eventName: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_DATE, date)
            put(COLUMN_NAME_TIME, time)
            put(COLUMN_NAME_EVENT_NAME, eventName)

        }
        val rowID = db.insert(TABLE_NAME_WEEK_EVENT, null, cv)
        closeDb()
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

    @SuppressLint("Range")
    fun selectToTableWeekEvent(const: String, searchText: String, selectionArguments: String): ArrayList<String> {
        openDb()
        val sqlQuery = ("select * from $TABLE_NAME_WEEK_EVENT where $selectionArguments = '$searchText';")
        val dataList = ArrayList<String>()
        val cursor = db.rawQuery(sqlQuery, null)

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        closeDb()
        return dataList
    }

    fun deleteRawToTableWeekEvents(const: String) {
        openDb()
        val delCount = db.delete(TABLE_NAME_WEEK_EVENT, "event_name = '$const'", null)
        closeDb()
    }

    fun updateRawToTableWeekEvents(
        timeEvent: String,
        nameEvent: String,
        dateEvent: String,
        nameEventUpdatePosition: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_TIME, timeEvent)
            put(COLUMN_NAME_EVENT_NAME, nameEvent)
            put(COLUMN_NAME_DATE, dateEvent)
        }

        val updCount = db.update(
            TABLE_NAME_WEEK_EVENT, cv, "event_name = ?",
            arrayOf(nameEventUpdatePosition)
        )
        closeDb()
    }

    /**
     *  Table Achievements
     */

    fun insertToTableAchievements(achievementsPlace: String, achievementsSquad:  String, achievementsEvent: String) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_ACHIEVEMENTS_PLACE, achievementsPlace)
            put(COLUMN_NAME_SQUAD_ACHIEVEMENTS, achievementsSquad)
            put(COLUMN_NAME_EVENT_ACHIEVEMENTS, achievementsEvent)

        }
        val rowID = db.insert(TABLE_NAME_ACHIEVEMENTS, null, cv)
        closeDb()
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

    @SuppressLint("Range")
    fun selectToTableAchievements(const: String, searchText: String, selectionArguments: String): ArrayList<String> {
        openDb()
        val sqlQuery = ("select * from $TABLE_NAME_ACHIEVEMENTS where $selectionArguments = '$searchText';")
        val dataList = ArrayList<String>()
        val cursor = db.rawQuery(sqlQuery, null)

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        closeDb()
        return dataList
    }

    fun deleteRawToTableAchievements(const: String) {
        openDb()
        val delCount = db.delete(TABLE_NAME_ACHIEVEMENTS, "achievements_place = '$const'", null)
        closeDb()
    }

    fun updateRawToTableAchievements(
        place: String,
        achievementsSquad:  String,
        achievementsEvent: String,
        placeUpdatePosition: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_ACHIEVEMENTS_PLACE, place)
            put(COLUMN_NAME_SQUAD_ACHIEVEMENTS, achievementsSquad)
            put(COLUMN_NAME_EVENT_ACHIEVEMENTS, achievementsEvent)
        }

        val updCount = db.update(
            TABLE_NAME_ACHIEVEMENTS, cv, "achievements_place = ?",
            arrayOf(placeUpdatePosition)
        )
        closeDb()
    }

    /**
     * Table Child
     */

    fun insertToTableChild(
        childName: String, childSurname: String,
        childPatronymic: String, childBirthday: String,
        parentsNumber: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_CHILD_NAME, childName)
            put(COLUMN_NAME_CHILD_SURNAME, childSurname)
            put(COLUMN_NAME_CHILD_PATRONYMIC, childPatronymic)
            put(COLUMN_NAME_CHILD_BIRTHDAY, childBirthday)
            put(COLUMN_NAME_PARENTS_NUMBER, parentsNumber)
        }
        val rowID = db.insert(TABLE_NAME_CHILD, null, cv)
        closeDb()
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

    fun deleteRawToTableChild(const: String) {
        openDb()
        val delCount = db.delete(TABLE_NAME_CHILD, "child_name = '$const'", null)
        closeDb()
    }

    fun updateRawToTableChild(
        nameChildUpdate: String,
        surnameChildUpdate: String,
        patronamycChildUpdate: String,
        parentsPhoneNumberUpdate: String,
        birthdayChildUpdate: String,
        nameChildUpdatePosition: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_CHILD_NAME, nameChildUpdate)
            put(COLUMN_NAME_CHILD_SURNAME, surnameChildUpdate)
            put(COLUMN_NAME_CHILD_PATRONYMIC, patronamycChildUpdate)
            put(COLUMN_NAME_CHILD_BIRTHDAY, parentsPhoneNumberUpdate)
            put(COLUMN_NAME_PARENTS_NUMBER, birthdayChildUpdate)

        }

        val updCount = db.update(
            TABLE_NAME_CHILD, cv, "child_name = ?",
            arrayOf(nameChildUpdatePosition)
        )
        closeDb()
    }

    /**
     * Table Room
     */

    fun insertToTableRoom(
        floor: String, roomNumber: String,
        quantityChild: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_FLOOR, floor)
            put(COLUMN_NAME_ROOM_NUMBER, roomNumber)
            put(COLUMN_NAME_QUANTITY_CHILD, quantityChild)

        }
        val rowID = db.insert(TABLE_NAME_ROOM, null, cv)
        closeDb()
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

    @SuppressLint("Range")
    fun selectToTableRoom(const: String, searchText: String, selectionArguments: String): ArrayList<String> {
        openDb()
        val sqlQuery = ("select * from $TABLE_NAME_ROOM where $selectionArguments = '$searchText';")
        val dataList = ArrayList<String>()
        val cursor = db.rawQuery(sqlQuery, null)

        while (cursor?.moveToNext()!!) {
            val dataText = cursor.getString(cursor.getColumnIndex(const))
            dataList.add(dataText.toString())
        }
        cursor.close()
        closeDb()
        return dataList
    }

    fun deleteRawToTableRoom(const: String) {
        openDb()
        val delCount = db.delete(TABLE_NAME_ROOM, "room_number = '$const'", null)
        closeDb()
    }

    fun updateRawToTableRoom(
        floorUpdate: String,
        roomNumberUpdate: String,
        quantityUpdate: String,
        roomNumberUpdatePosition: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_FLOOR, floorUpdate)
            put(COLUMN_NAME_ROOM_NUMBER, roomNumberUpdate)
            put(COLUMN_NAME_QUANTITY_CHILD, quantityUpdate)

        }

        val updCount = db.update(
            TABLE_NAME_ROOM, cv, "room_number = ?",
            arrayOf(roomNumberUpdatePosition)
        )
        closeDb()
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

    @SuppressLint("Range")
    fun selectToTableDailySchedule(const: String, searchText: String, selectionArguments: String): ArrayList<String> {
        openDb()
        val sqlQuery = ("select * from $TABLE_NAME_DAILY_SCHEDULE where $selectionArguments = '$searchText';")
        val dataList = ArrayList<String>()
        val cursor = db.rawQuery(sqlQuery, null)

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

    fun updateRawToTableDailySchedule(
        timeEvent: String, nameEvent: String,
        dateEvent: String, nameEventUpdatePosition: String
    ) {
        openDb()
        val cv = ContentValues().apply {
            put(COLUMN_NAME_TIME_EVENT, timeEvent)
            put(COLUMN_NAME_NAME_EVENT, nameEvent)
            put(COLUMN_NAME_DATE_EVENT, dateEvent)
        }

        val updCount = db.update(
            TABLE_NAME_DAILY_SCHEDULE, cv, "name_event = ?",
            arrayOf(nameEventUpdatePosition)
        )
        closeDb()
    }

    fun closeDb() {
        campDbHelper.close()
    }


}