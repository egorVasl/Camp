package com.example.singupactivity.ui.main.Activity

import android.annotation.SuppressLint
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
import android.media.Image

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.core.graphics.get
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_AVATAR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_LOGIN_AVATAR
import com.example.singupactivity.ui.main.Objects.NavigationActviy.ArgumentsLogin
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_IMAGE_CAPTURE_CAMERA = 1
private const val REQUEST_IMAGE_CAPTURE_GALLERY = 2
class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigtionBinding
    lateinit var campDbManager: CampDbManager
    lateinit var imageProfile: ImageView

    var message: String? = null

    private val pickImage = 1


    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("InflateParams", "RestrictedApi")
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

        val loginList = campDbManager.selectToTableAvatarLogin(COLUMN_NAME_LOGIN_AVATAR)
        val imgByteArray = campDbManager.selectToTableAvatarImage(COLUMN_NAME_AVATAR)
        for ((i, elm) in loginList.withIndex()) {
           if (loginList[i] == ArgumentsLogin.login){
               val bmp = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.size)
               imageProfile.setImageBitmap(bmp)
           }
        }

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
                createImageFile(it)
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
    @Throws(IOException::class)
    private fun createImageFile(bitmap: Bitmap): File {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        storageDir?.let { storageDir ->
            val image = File.createTempFile(imageFileName, ".jpg", storageDir)
            val stream = FileOutputStream(image)
            stream.write(bytes.toByteArray())
            stream.close()
            return image
        } ?: throw IOException("Couldn't reach external cache dir")
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
        img.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        campDbManager.updateRawToTableAvatar(byteArray, ArgumentsLogin.login)
    }

    override fun onDestroy() {
        campDbManager.closeDb()
        super.onDestroy()
    }
}
