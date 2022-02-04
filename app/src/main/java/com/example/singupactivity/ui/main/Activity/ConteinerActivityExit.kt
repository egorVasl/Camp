package com.example.singupactivity.ui.main.Activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDS

class ConteinerActivityExit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.apply {
            title = getString(R.string.exit_activity_title)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conteiner)

        val intent = Intent(
            this,
            NavigationActivity::class.java
        )
        startActivity(intent)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.notification)
            .setMessage(R.string.exit_text)
            .setCancelable(false)
            .setNegativeButton(R.string.yes){_,_->
                super.onBackPressed()
            }
            .setPositiveButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(
                    this,
                    NavigationActivity::class.java
                )
                startActivity(intent)
            }

        val alert = builder.create()
        alert.show()
    }
}