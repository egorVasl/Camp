package com.example.singupactivity.ui.main.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.singupactivity.R
import com.example.singupactivity.databinding.ActivityNavigtionBinding

class
NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigtionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigtionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var drawerLayout: DrawerLayout = binding.drawerLayout

        binding.imageMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navigationView.itemIconTintList

    }

//    override fun setupToolbar() = with((act as BaseActivity).toolbar) {
//        title = "Camp"
//        setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
//        inflateMenu(R.menu.transfer_menu)
//        setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.menu_info -> {
//                    showInfoDialog()
//                    true
//                }
//                else -> false
//            }
//        }
//    }
}