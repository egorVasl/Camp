package com.example.singupactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.singupactivity.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.example.singupactivity.ui.main.Adapter.ViewPagerAdapter
import com.example.singupactivity.ui.main.LoginFragment
import com.example.singupactivity.ui.main.SignupFragment


class LoginSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

}