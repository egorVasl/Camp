package com.example.singupactivity.ui.main.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.singupactivity.R
import com.example.singupactivity.databinding.ActivityNavigtionBinding
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import java.util.jar.Manifest
import javax.xml.transform.OutputKeys.VERSION

class
NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigtionBinding
    lateinit var campDbManager: CampDbManager

    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001


    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        binding = ActivityNavigtionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        campDbManager = CampDbManager(this)
        campDbManager.openDb()
        val drawerLayout: DrawerLayout = binding.drawerLayout

        binding.imageMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navigationView.itemIconTintList

        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.textTitle.text = destination.label
        }
        val view = LayoutInflater.from(this).inflate(R.layout.navigation_header, null)
        val imageProfile = view.findViewById<ImageView>(R.id.imageHeaderProfile)

        imageProfile.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {

                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)

            } else {
                selectImageFromGallery()
            }

        }




        override fun onDestroy() {
            campDbManager.closeDb()
            super.onDestroy()
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

    private fun selectImageFromGallery() {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_DENIED) {
                    selectImageFromGallery()
                } else {
                    Toast.
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
}
