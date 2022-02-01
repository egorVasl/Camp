package com.example.singupactivity.ui.main.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
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
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_AVATAR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_LOGIN_AVATAR
import com.example.singupactivity.ui.main.Objects.NavigationActviy.ArgumentsNAlogin
import java.io.*

private const val REQUEST_IMAGE_CAPTURE_CAMERA = 1
private const val REQUEST_IMAGE_CAPTURE_GALLERY = 2
class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigtionBinding
    lateinit var campDbManager: CampDbManager
    lateinit var imageProfile: ImageView
    lateinit var bottomProfileText: TextView

    var message: String? = null

    override fun onStart() {
        super.onStart()

        bottomProfileText.text = ArgumentsNAlogin.login

        val loginList = campDbManager.selectToTableAvatarLogin(COLUMN_NAME_LOGIN_AVATAR)
        val imgByteArray = campDbManager.selectToTableAvatarImage(COLUMN_NAME_AVATAR)
        for ((i, elm) in loginList.withIndex()) {
            if (loginList[i] == ArgumentsNAlogin.login){
                try {
                    val bmp = BitmapFactory.decodeByteArray(imgByteArray[i], 0, imgByteArray[i].size)
                    imageProfile.setImageBitmap(bmp)
                } catch (exe: RuntimeException){
                    alert(R.string.error_uploading_image_repit_operation)
                }

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("InflateParams", "RestrictedApi")
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

        val headerLayout: View = binding.navigationView.getHeaderView(0)

        bottomProfileText = headerLayout.findViewById(R.id.bottomProfileTest)
        imageProfile = headerLayout.findViewById<ImageView>(R.id.imageHeaderProfile)

        imageProfile.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.choice_action_profile_image, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.choiceFromGallery -> {
                        choicePhotoFromGallery()
                        true
                    }
                    else -> {
                        takePhoto()
                        true
                    }
                }
            }
        }
    }

    private fun choicePhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_GALLERY);
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_CAMERA);
    }

    fun getMyData(): String? {
        return message
    }
    private fun uploadPhotoFromCamera(cameraExtras: Bundle?) {
        (cameraExtras?.get("data") as? Bitmap)?.let {
            try {
                imageProfile.setImageBitmap(it)
                saveImageBitmapAvatar(it)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, R.string.error_uploading_image, Toast.LENGTH_SHORT).show()
            }
        } ?:Toast.makeText(this, R.string.error_uploading_image, Toast.LENGTH_SHORT).show()

    }

    private fun uploadPhotoFromGallery(uri: Uri?) {
        try {
            val imageUri: Uri = uri!!
            val imageStream: InputStream? =
                contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            imageProfile.setImageBitmap(selectedImage)
            saveImageBitmapAvatar(selectedImage)
        } catch (e: Exception) {
            e.printStackTrace()
           Toast.makeText(this, R.string.error_uploading_image, Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE_CAMERA -> uploadPhotoFromCamera(data?.extras)
            REQUEST_IMAGE_CAPTURE_GALLERY -> uploadPhotoFromGallery(data?.data)

        }
    }

    private fun saveImageBitmapAvatar(img : Bitmap){
        val stream = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.PNG, 0, stream)
        val byteArray = stream.toByteArray()
        campDbManager.updateRawToTableAvatar(byteArray, ArgumentsNAlogin.login)
    }

    override fun onDestroy() {
        campDbManager.closeDb()
        super.onDestroy()
    }

    private fun alert(massage: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.notification)
            .setMessage(massage)
            .setCancelable(false)
            .setPositiveButton(R.string.contin) { dialog, _ ->
                dialog.dismiss()

            }

        val alert = builder.create()
        alert.show()
    }
}
