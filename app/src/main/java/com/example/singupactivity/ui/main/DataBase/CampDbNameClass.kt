package com.example.singupactivity.ui.main.DataBase

import android.provider.BaseColumns

object CampDbNameClass : BaseColumns{

    /**
     * Data Base
     */

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "Camp.db"

    /**
     * Table Authorization
     */

    const val TABLE_NAME_AUTHORIZATION = "authorization"

    const val COLUMN_NAME_ID_AUTHORIZATION = "id_authorization"
    const val COLUMN_NAME_LOGIN = "login"
    const val COLUMN_NAME_PASSWORD = "password"
    const val COLUMN_NAME_SQUAD = "squad"

    const val CREATE_TABLE_AUTHORIZATION =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME_AUTHORIZATION (" +
                "$COLUMN_NAME_ID_AUTHORIZATION  INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "$COLUMN_NAME_LOGIN  TEXT," +
                "$COLUMN_NAME_PASSWORD TEXT,"+
                "$COLUMN_NAME_SQUAD INTEGER)"

    const val DELETE_TABLE_AUTHORIZATION = "DROP TABLE IF EXISTS $TABLE_NAME_AUTHORIZATION"

    /**
     * Table Squad
     */

    const val TABLE_NAME_SQUAD = "squad"

    const val COLUMN_NAME_ID_SQUAD = "id_squad"
    const val COLUMN_NAME_SQUAD_NAME = "squad_name"
    const val COLUMN_NAME_SQUAD_NUMBER = "squad_number"

    const val CREATE_TABLE_SQUAD =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME_SQUAD (" +
                "$COLUMN_NAME_ID_SQUAD INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "$COLUMN_NAME_SQUAD_NAME  TEXT," +
                "$COLUMN_NAME_SQUAD_NUMBER INTEGER" +
                ")"

    const val DELETE_TABLE_SQUAD = "DROP TABLE IF EXISTS $TABLE_NAME_SQUAD"

    /**
     * Table Counselor
     */

    const val TABLE_NAME_COUNSELOR = "counselor"

    const val COLUMN_NAME_ID_COUNSELOR = "id_counselor"
    const val COLUMN_NAME_COUNSELOR_NAME = "counselor_name"
    const val COLUMN_NAME_COUNSELOR_SURNAME = "counselor_surname"
    const val COLUMN_NAME_COUNSELOR_PATRONYMIC = "counselor_patronymic"
    const val COLUMN_NAME_COUNSELOR_BIRTHDAY = "counselor_birthday"
    const val COLUMN_NAME_COUNSELOR_NUMBER = "counselor_number"
    const val COLUMN_NAME_ID_SQUAD_COUNSELOR = "id_squad"
    const val COLUMN_NAME_ID_AUTHORIZATION_COUNSELOR = "id_authorization"

    const val CREATE_TABLE_COUNSELOR =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME_COUNSELOR (" +
                "$COLUMN_NAME_ID_COUNSELOR INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "$COLUMN_NAME_COUNSELOR_NAME  TEXT," +
                "$COLUMN_NAME_COUNSELOR_SURNAME TEXT,"+
                "$COLUMN_NAME_COUNSELOR_PATRONYMIC TEXT,"+
                "$COLUMN_NAME_COUNSELOR_BIRTHDAY DATETIME,"+
                "$COLUMN_NAME_COUNSELOR_NUMBER INTEGER," +
                "FOREIGN KEY ($COLUMN_NAME_ID_SQUAD_COUNSELOR) REFERENCES $TABLE_NAME_SQUAD($COLUMN_NAME_ID_SQUAD)"+
                "FOREIGN KEY ($COLUMN_NAME_ID_AUTHORIZATION_COUNSELOR) REFERENCES $TABLE_NAME_AUTHORIZATION($COLUMN_NAME_ID_AUTHORIZATION)"+
                ")"

    const val DELETE_TABLE_COUNSELOR = "DROP TABLE IF EXISTS $TABLE_NAME_COUNSELOR"

    /**
     * Table Week Event
     */

    const val TABLE_NAME_WEEK_EVENT = "week_event"

    const val COLUMN_NAME_ID_EVENT = "id_event"
    const val COLUMN_NAME_DATE = "date"
    const val COLUMN_NAME_TIME = "time"
    const val COLUMN_NAME_EVENT_NAME = "event_name"

    const val CREATE_TABLE_WEEK_EVENT =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME_WEEK_EVENT (" +
                "$COLUMN_NAME_ID_EVENT  INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "$COLUMN_NAME_DATE DATETIME," +
                "$COLUMN_NAME_TIME DATETIME,"+
                "$COLUMN_NAME_EVENT_NAME TEXT"+
                ")"

    const val DELETE_TABLE_WEEK_EVENT = "DROP TABLE IF EXISTS $TABLE_NAME_WEEK_EVENT"

    /**
     * Table Achievements
     */

    const val TABLE_NAME_ACHIEVEMENTS = "achievements"

    const val COLUMN_NAME_ID_ACHIEVEMENTS = "id_achievements"
    const val COLUMN_NAME_ACHIEVEMENTS_PLACE = "achievements_place"
    const val COLUMN_NAME_ID_SQUAD_ACHIEVEMENTS = "id_squad"
    const val COLUMN_NAME_ID_EVENT_ACHIEVEMENTS = "id_event"

    const val CREATE_TABLE_ACHIEVEMENTS =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME_ACHIEVEMENTS (" +
                "$COLUMN_NAME_ID_ACHIEVEMENTS INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "$COLUMN_NAME_ACHIEVEMENTS_PLACE INTEGER," +
                "FOREIGN KEY ($COLUMN_NAME_ID_SQUAD_ACHIEVEMENTS) REFERENCES $TABLE_NAME_SQUAD($COLUMN_NAME_ID_SQUAD)"+
                "FOREIGN KEY ($COLUMN_NAME_ID_EVENT_ACHIEVEMENTS) REFERENCES $TABLE_NAME_WEEK_EVENT($COLUMN_NAME_ID_EVENT)"+
                ")"

    const val DELETE_TABLE_ACHIEVEMENTS = "DROP TABLE IF EXISTS $TABLE_NAME_ACHIEVEMENTS"

    /**
     * Table Child
     */

    const val TABLE_NAME_CHILD = "child"

    const val COLUMN_NAME_ID_CHILD = "id_child"
    const val COLUMN_NAME_CHILD_NAME = "child_name"
    const val COLUMN_NAME_CHILD_SURNAME = "child_surname"
    const val COLUMN_NAME_CHILD_PATRONYMIC = "child_patronymic"
    const val COLUMN_NAME_CHILD_BIRTHDAY = "child_birthday"
    const val COLUMN_NAME_PARENTS_NUMBER = "parents_number"
    const val COLUMN_NAME_ID_SQUAD_CHILD = "id_squad"

    const val CREATE_TABLE_CHILD =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME_CHILD (" +
                "$COLUMN_NAME_ID_CHILD INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "$COLUMN_NAME_CHILD_NAME  TEXT," +
                "$COLUMN_NAME_CHILD_SURNAME TEXT,"+
                "$COLUMN_NAME_CHILD_PATRONYMIC TEXT,"+
                "$COLUMN_NAME_CHILD_BIRTHDAY DATETIME,"+
                "$COLUMN_NAME_PARENTS_NUMBER INTEGER," +
                "FOREIGN KEY ($COLUMN_NAME_ID_SQUAD_CHILD) REFERENCES $TABLE_NAME_SQUAD($COLUMN_NAME_ID_SQUAD)"+
                ")"

    const val DELETE_TABLE_CHILD = "DROP TABLE IF EXISTS $TABLE_NAME_CHILD"

    /**
     * Table Room
     */

    const val TABLE_NAME_ROOM = "room"

    const val COLUMN_NAME_ID_ROOM = "id_room"
    const val COLUMN_NAME_FLOOR = "floor"
    const val COLUMN_NAME_ROOM_NUMBER = "room_number"
    const val COLUMN_NAME_QUANTITY_CHILD = "quantity_child"

    const val CREATE_TABLE_ROOM =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME_ROOM (" +
                "$COLUMN_NAME_ID_ROOM  INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "$COLUMN_NAME_FLOOR  INTEGER," +
                "$COLUMN_NAME_ROOM_NUMBER INTEGER,"+
                "$COLUMN_NAME_QUANTITY_CHILD INTEGER)"

    const val DELETE_TABLE_ROOM = "DROP TABLE IF EXISTS $TABLE_NAME_ROOM"

    /**
     * Table Living
     */

    const val TABLE_NAME_LIVING = "living"

    const val COLUMN_NAME_ID_LIVING = "id_child"
    const val COLUMN_NAME_ID_ROOM_LIVING = "id_room"
    const val COLUMN_NAME_ID_CHILD_LIVING = "id_child"

    const val CREATE_TABLE_LIVING =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME_LIVING (" +
                "$COLUMN_NAME_ID_LIVING INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "FOREIGN KEY ($COLUMN_NAME_ID_ROOM_LIVING) REFERENCES $TABLE_NAME_ROOM($COLUMN_NAME_ID_ROOM)"+
                "FOREIGN KEY ($COLUMN_NAME_ID_CHILD_LIVING) REFERENCES $TABLE_NAME_CHILD($COLUMN_NAME_ID_CHILD)"+
                ")"

    const val DELETE_TABLE_LIVING = "DROP TABLE IF EXISTS $TABLE_NAME_LIVING"

    /**
     * Table Daily Schedule
     */

    const val TABLE_NAME_DAILY_SCHEDULE = "daily schedule"

    const val COLUMN_NAME_ID_DAY = "id_day"
    const val COLUMN_NAME_TIME_EVENT = "time_event"
    const val COLUMN_NAME_NAME_EVENT = "name_event"
    const val COLUMN_NAME_DATE_EVENT = "date_event"

    const val CREATE_TABLE_DAILY_SCHEDULE =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME_DAILY_SCHEDULE (" +
                "$COLUMN_NAME_ID_DAY  INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "$COLUMN_NAME_TIME_EVENT  DATETIME," +
                "$COLUMN_NAME_NAME_EVENT TEXT,"+
                "$COLUMN_NAME_DATE_EVENT DATETIME)"

    const val DELETE_TABLE_DAILY_SCHEDULE = "DROP TABLE IF EXISTS $TABLE_NAME_DAILY_SCHEDULE"

}