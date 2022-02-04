package com.example.singupactivity.ui.main.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Fragment.Search.SearchDSFragment
import com.example.singupactivity.ui.main.Fragment.addFragmentToActivity
import android.view.Menu
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_EVENT_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_NAME_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME_EVENT
import com.example.singupactivity.ui.main.Fragment.Search.SearchEventsFragment
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDS


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
            }
        }




    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_parameters_menu, menu)
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
                    "DailySchedule" ->   ArgumentsDS.arg = COLUMN_NAME_NAME_EVENT
                    "Events" ->   ArgumentsDS.arg = COLUMN_NAME_EVENT_NAME
                }
                true
            }
            R.id.menuArgTime -> {
                when(intent.getStringExtra(TYPE_ACTIVITY)){
                    "DailySchedule" ->   ArgumentsDS.arg = COLUMN_NAME_TIME_EVENT
                    "Events" ->   ArgumentsDS.arg = COLUMN_NAME_TIME
                }
                true
            }
            R.id.menuArgDate -> {
                when(intent.getStringExtra(TYPE_ACTIVITY)){
                    "DailySchedule" ->   ArgumentsDS.arg = COLUMN_NAME_DATE_EVENT
                    "Events" ->   ArgumentsDS.arg = COLUMN_NAME_DATE
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}