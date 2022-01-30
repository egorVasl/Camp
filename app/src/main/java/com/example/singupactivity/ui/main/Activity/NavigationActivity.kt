package com.example.singupactivity.ui.main.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.singupactivity.R
import com.example.singupactivity.databinding.ActivityNavigtionBinding
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import android.graphics.BitmapFactory

import android.net.Uri
import android.view.View
import java.io.FileNotFoundException
import java.io.InputStream


class
NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigtionBinding
    lateinit var campDbManager: CampDbManager
    lateinit var imageProfile: ImageView

    var message : String? = null

    private val pickImage = 1


    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        binding = ActivityNavigtionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        campDbManager = CampDbManager(this)
        campDbManager.openDb()


        val intent = intent
         message = getIntent().extras?.get("KEY").toString()


//        val frag= CounselorFragment()
//        frag.arguments = bundleOf("message" to message.toString())


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

//        val loginList =
//            campDbManager.selectToTableAuthorization(CampDbNameClass.COLUMN_NAME_LOGIN)
//        for ((i, item) in loginList.withIndex()) {
//            if (loginList[i] == etLogin.text.toString()) {
//                loginIsTrue = true
//            }
//        }
        val headerLayout: View = binding.navigationView.getHeaderView(0)

        imageProfile = headerLayout.findViewById<ImageView>(R.id.imageHeaderProfile)



        imageProfile.setOnClickListener {

            val photoPickerIntent: Intent = Intent(Intent.ACTION_PICK)

            photoPickerIntent.type = "image/*"

            startActivityForResult(photoPickerIntent, pickImage);

        }

    }

    fun getMyData(): String? {
        return message
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            pickImage -> {
                if (resultCode === RESULT_OK) {
                    try {
                        val imageUri: Uri = data?.data!!
                        val imageStream: InputStream? =
                            contentResolver.openInputStream(imageUri)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        imageProfile.setImageBitmap(selectedImage)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        campDbManager.closeDb()
        super.onDestroy()
    }
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
