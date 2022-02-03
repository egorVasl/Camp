package com.example.singupactivity.ui.main.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.singupactivity.R
import com.example.singupactivity.databinding.ActivityNavigtionBinding
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_AVATAR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_SURNAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_LOGIN_AVATAR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_LOGIN_COUNSELOR
import com.example.singupactivity.ui.main.Objects.NavigationActviy.ArgumentsNAlogin
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.InputStream

private const val REQUEST_IMAGE_CAPTURE_CAMERA = 1
private const val REQUEST_IMAGE_CAPTURE_GALLERY = 2

interface OnDataPass {
    fun onDataPass(data: Boolean?)
}


class NavigationActivity : AppCompatActivity(), OnDataPass {
    private lateinit var binding: ActivityNavigtionBinding
    lateinit var campDbManager: CampDbManager
    lateinit var imageProfile: ImageView
    lateinit var bottomProfileText: TextView
    lateinit var headerProfileText: TextView
    private lateinit var counselorName: ArrayList<String>
    private lateinit var counselorSurname: ArrayList<String>
    private lateinit var counselorLoginCounselor: ArrayList<String>

    override fun onStart() {
        super.onStart()

        setUpTitleText()

        val loginList =
            runBlocking {
                async {
                    campDbManager.selectToTableAvatarLogin(COLUMN_NAME_LOGIN_AVATAR)
                }.await()
            }

        val imgByteArray =
            runBlocking {
                async {
                    campDbManager.selectToTableAvatarImage(COLUMN_NAME_AVATAR)
                }.await()
            }
        for ((i, _) in loginList.withIndex()) {
            if (loginList[i] == ArgumentsNAlogin.login) {
                try {
                    val bmp =
                        BitmapFactory.decodeByteArray(imgByteArray[i], 0, imgByteArray[i].size)
                    imageProfile.setImageBitmap(bmp)
                } catch (exe: RuntimeException) {
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
        imageProfile = headerLayout.findViewById(R.id.imageHeaderProfile)
        headerProfileText = headerLayout.findViewById(R.id.textHeaderProfile)


        imageProfile.setOnClickListener { view ->
            val popupMenu =
                androidx.appcompat.widget.PopupMenu(this, imageProfile)
            popupMenu.inflate(R.menu.choice_action_profile_image)
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
            val menuHelper = MenuPopupHelper(
                this,
                popupMenu.menu as MenuBuilder,
                imageProfile
            )
            menuHelper.setForceShowIcon(true)
            menuHelper.show()
        }
    }

    private fun choicePhotoFromGallery() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI).apply {
                type = "image/*"
            }

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_GALLERY);
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_CAMERA);
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
        } ?: Toast.makeText(this, R.string.error_uploading_image, Toast.LENGTH_SHORT).show()

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

    private fun saveImageBitmapAvatar(img: Bitmap) {
        val stream = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.PNG, 0, stream)
        val byteArray = stream.toByteArray()
        runBlocking {
            async {
                campDbManager.updateRawToTableAvatar(byteArray, ArgumentsNAlogin.login)
            }.await()
        }

    }

    private fun getData(const: String): ArrayList<String> {
        return campDbManager.selectToTableCounselor(const)

    }

    private fun setUpTitleText() {
        counselorName =
            runBlocking {
                async {
                    getData(COLUMN_NAME_COUNSELOR_NAME)
                }.await()
            }
        counselorSurname =
            runBlocking {
                async {
                    getData(COLUMN_NAME_COUNSELOR_SURNAME)
                }.await()
            }
        counselorLoginCounselor =
            runBlocking {
                async {
                    getData(COLUMN_NAME_LOGIN_COUNSELOR)
                }.await()
            }
        for ((i, _) in counselorLoginCounselor.withIndex()) {
            if (counselorLoginCounselor[i] ==
                ArgumentsNAlogin.login
            ) {
                headerProfileText.text =  if (counselorName[i].isNotEmpty())
                  "${counselorName[i]} \n ${counselorSurname[i]}"
                    else
                    getString(R.string.name_surname)
            }
        }

        bottomProfileText.text = ArgumentsNAlogin.login


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

    override fun onDataPass(data: Boolean?) {
        if(data == true)
            setUpTitleText()
    }
}
