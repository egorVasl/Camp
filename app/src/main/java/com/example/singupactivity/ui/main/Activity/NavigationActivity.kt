package com.example.singupactivity.ui.main.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.singupactivity.databinding.ActivityNavigtionBinding

class
NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigtionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigtionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var drawerLayout : DrawerLayout = binding.drawerLayout

        binding.imageMenu.setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }

    }
}