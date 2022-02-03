package com.example.singupactivity.ui.main.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Fragment.Search.SearchFragment
import com.example.singupactivity.ui.main.Fragment.addFragmentToActivity
import android.view.Menu
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_NAME_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME_EVENT
import com.example.singupactivity.ui.main.Fragment.BottomSheet.TYPE
import com.example.singupactivity.ui.main.Fragment.Search.SearchFragmentEvents
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDS
import com.example.singupactivity.ui.main.Objects.Search.ArgumentsSearchFragmentSelected


class SearchActivity : AppCompatActivity() {



    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
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

        if (ArgumentsSearchFragmentSelected.arg.isEmpty()){
            when(intent.getStringExtra(TYPE)){
                "DailySchedule" ->  addFragmentToActivity(R.id.container, SearchFragment.newInstance())
                "Events" ->  addFragmentToActivity(R.id.container, SearchFragmentEvents.newInstance())
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
                ArgumentsDS.arg = COLUMN_NAME_NAME_EVENT
                true
            }
            R.id.menuArgTime -> {
                ArgumentsDS.arg = COLUMN_NAME_TIME_EVENT
                true
            }
            R.id.menuArgDate -> {
                ArgumentsDS.arg = COLUMN_NAME_DATE_EVENT
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}