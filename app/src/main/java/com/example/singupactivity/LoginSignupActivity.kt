package com.example.singupactivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.singupactivity.Retofit.LoginRequest
import com.example.singupactivity.Retofit.RetrofitClientFile
import com.example.singupactivity.Retofit.TokenResponse
import com.example.singupactivity.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Response
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



       val request = LoginRequest("", "")
        RetrofitClientFile.api.login(request).enqueue(object : retrofit2.Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                val resp = response.body()
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

        })

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