package com.example.singupactivity.ui.main.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.singupactivity.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.example.singupactivity.ui.main.Adapter.ViewPagerAdapter
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.Fragment.LoginFragment
import com.example.singupactivity.ui.main.Fragment.SignupFragment


class CampMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val campDbManager = CampDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        campDbManager.openDb()

        setUpTabs()
    }

    private fun setUpTabs() {
        val viewPager: ViewPager = binding.viewPager
        val tabs: TabLayout = binding.tabs
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(LoginFragment(), "Login")
        adapter.addFragment(SignupFragment(), "Signup")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

    }

    override fun onDestroy() {
        campDbManager.closeDb()
        super.onDestroy()
    }

}