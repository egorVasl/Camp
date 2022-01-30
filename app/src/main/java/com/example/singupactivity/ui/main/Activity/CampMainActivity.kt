package com.example.singupactivity.ui.main.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.singupactivity.R
import com.example.singupactivity.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.example.singupactivity.ui.main.Adapter.ViewPagerAdapter
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.Fragment.Authoriztion.LoginFragment
import com.example.singupactivity.ui.main.Fragment.Authoriztion.SignupFragment


class CampMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var campDbManager: CampDbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        campDbManager = CampDbManager(this)
        campDbManager.openDb()

        setUpTabs()


    }

    private fun setUpTabs() {
        val viewPager: ViewPager = binding.viewPager
        val tabs: TabLayout = binding.tabs
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(LoginFragment(), getString(R.string.tab_text_1))
        adapter.addFragment(SignupFragment(),  getString(R.string.tab_text_2))
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

    }

    override fun onDestroy() {
        campDbManager.closeDb()
        super.onDestroy()
    }

}