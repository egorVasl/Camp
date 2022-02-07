package com.example.singupactivity.ui.main.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Fragment.addFragmentToActivity
import android.view.Menu
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_ACHIEVEMENTS_PLACE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_CHILD_BIRTHDAY
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_CHILD_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_CHILD_PATRONYMIC
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_CHILD_SURNAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_EVENT_ACHIEVEMENTS
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_EVENT_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_FLOOR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_NAME_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_PARENTS_NUMBER
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_QUANTITY_CHILD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_ROOM_NUMBER
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_ACHIEVEMENTS
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_NUMBER
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME_EVENT
import com.example.singupactivity.ui.main.Fragment.Search.*
import com.example.singupactivity.ui.main.Objects.Arguments


const val TYPE_ACTIVITY = "TYPE_ACTIVITY"

class SearchActivity : AppCompatActivity() {



    companion object {
        fun start(context: Context, typeActivity: String) {
            val intent = Intent(context, SearchActivity::class.java)
                .putExtra(TYPE_ACTIVITY, typeActivity)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.apply {
            title = getString(R.string.search)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (intent.getStringExtra(TYPE_ACTIVITY)!!.isNotEmpty()){
            when(intent.getStringExtra(TYPE_ACTIVITY)){
                "DailySchedule" ->  addFragmentToActivity(R.id.container, SearchDSFragment.newInstance())
                "Events" ->  addFragmentToActivity(R.id.container, SearchEventsFragment.newInstance())
                "Squads" -> addFragmentToActivity(R.id.container, SearchSquadsFragment.newInstance())
                "Room" -> addFragmentToActivity(R.id.container, SearchRoomFragment.newInstance())
                "Achievements" -> addFragmentToActivity(R.id.container, SearchAchievementsFragment.newInstance())
                "Child" -> addFragmentToActivity(R.id.container, SearchChildFragment.newInstance())
            }
        }




    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        when(intent.getStringExtra(TYPE_ACTIVITY)){
            "DailySchedule" -> menuInflater.inflate(R.menu.search_ds_events_menu, menu)
            "Events" -> menuInflater.inflate(R.menu.search_ds_events_menu, menu)
            "Squads" -> menuInflater.inflate(R.menu.search_squads_menu, menu)
            "Room" -> menuInflater.inflate(R.menu.search_room_menu, menu)
            "Achievements" -> menuInflater.inflate(R.menu.search_achievemenets_menu, menu)
            "Child" -> menuInflater.inflate(R.menu.search_child_menu, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menuArgName -> {
                when(intent.getStringExtra(TYPE_ACTIVITY)){
                    "DailySchedule" ->   Arguments.arg = COLUMN_NAME_NAME_EVENT
                    "Events" ->   Arguments.arg = COLUMN_NAME_EVENT_NAME
                }
                true
            }
            R.id.menuArgTime -> {
                when(intent.getStringExtra(TYPE_ACTIVITY)){
                    "DailySchedule" ->   Arguments.arg = COLUMN_NAME_TIME_EVENT
                    "Events" ->   Arguments.arg = COLUMN_NAME_TIME
                }
                true
            }
            R.id.menuArgDate -> {
                when(intent.getStringExtra(TYPE_ACTIVITY)){
                    "DailySchedule" ->   Arguments.arg = COLUMN_NAME_DATE_EVENT
                    "Events" ->   Arguments.arg = COLUMN_NAME_DATE
                }
                true
            }
            R.id.menuArgSquadsNumber ->{
                Arguments.arg = COLUMN_NAME_SQUAD_NUMBER
                true
            }
            R.id.menuArgSquadsName ->{
                Arguments.arg = COLUMN_NAME_SQUAD_NAME
                true
            }
            R.id.menuArgFloor ->{
                Arguments.arg = COLUMN_NAME_FLOOR
                true
            }
            R.id.menuArgRoomNumber ->{
                Arguments.arg = COLUMN_NAME_ROOM_NUMBER
                true
            }
            R.id.menuArgChildQuantity ->{
                Arguments.arg = COLUMN_NAME_QUANTITY_CHILD
                true
            }

            R.id.menuPlaceAchievements ->{
                Arguments.arg = COLUMN_NAME_ACHIEVEMENTS_PLACE
                true
            }
            R.id.menuSquadNumberAchievements ->{
                Arguments.arg = COLUMN_NAME_SQUAD_ACHIEVEMENTS
                true
            }
            R.id.menuNameEventAchievements ->{
                Arguments.arg = COLUMN_NAME_EVENT_ACHIEVEMENTS
                true
            }
            R.id.menuNameChild ->{
                Arguments.arg = COLUMN_NAME_CHILD_NAME
                true
            }
            R.id.menuSurnameChild ->{
                Arguments.arg = COLUMN_NAME_CHILD_SURNAME
                true
            }
            R.id.menuPatronymicChild ->{
                Arguments.arg = COLUMN_NAME_CHILD_PATRONYMIC
                true
            }
            R.id.menuBirthdayChild ->{
                Arguments.arg = COLUMN_NAME_CHILD_BIRTHDAY
                true
            }
            R.id.menuParentNumberChild ->{
                Arguments.arg = COLUMN_NAME_PARENTS_NUMBER
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}