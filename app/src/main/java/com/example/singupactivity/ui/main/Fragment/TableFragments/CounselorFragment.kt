package com.example.singupactivity.ui.main.Fragment.TableFragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Adapter.AchievementsAdapter
import com.example.singupactivity.ui.main.Adapter.CounselorAdapter
import com.example.singupactivity.ui.main.Data.CounselorDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_AVATAR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_BIRTHDAY
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_NUMBER
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_PATRONYMIC
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_SURNAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_LOGIN_AVATAR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_LOGIN_COUNSELOR
import com.example.singupactivity.ui.main.Fragment.BottomSheet.CounselorBottomSheetDialog
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.Counselor.ArgumentsCounselorItem
import com.example.singupactivity.ui.main.Objects.NavigationActviy.ArgumentsNAlogin
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.concurrent.thread

private const val REQUEST_IMAGE_CAPTURE_CAMERA_COUNSELOR = 1
private const val REQUEST_IMAGE_CAPTURE_GALLERY_COUNSELOR = 2

class CounselorFragment : Fragment() {

    lateinit var adapter: CounselorAdapter
    lateinit var rv: RecyclerView


    lateinit var campDbManager: CampDbManager
    lateinit var imageProfile: ImageView
    private lateinit var bottomProfileText: TextView
    lateinit var profileText: TextView


    lateinit var loginList : ArrayList<String>
    lateinit var counselorName : ArrayList<String>
    lateinit var counselorSurname : ArrayList<String>
    lateinit var counselorPatronymic : ArrayList<String>
    lateinit var counselorBirthday : ArrayList<String>
    lateinit var counselorPhoneNumber : ArrayList<String>
    lateinit var counselorLoginCounselor : ArrayList<String>

    private val counselorItemTitle =
        arrayListOf("Имя: ", "Фамилия: ", "Отчество: ", " Дата рождения: ", "Номер телефона: ")
    private var counselorItemSubscription = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        campDbManager = CampDbManager(act)
        adapter = CounselorAdapter()

//
//            counselorName =
//                campDbManager.selectToTableCounselor(COLUMN_NAME_COUNSELOR_NAME)
//            counselorSurname =
//                campDbManager.selectToTableCounselor(COLUMN_NAME_COUNSELOR_SURNAME)
//            counselorPatronymic =
//                campDbManager.selectToTableCounselor(COLUMN_NAME_COUNSELOR_PATRONYMIC)
//            counselorBirthday =
//                campDbManager.selectToTableCounselor(COLUMN_NAME_COUNSELOR_BIRTHDAY)
//            counselorPhoneNumber =
//                campDbManager.selectToTableCounselor(COLUMN_NAME_COUNSELOR_NUMBER)
//            counselorLoginCounselor =
//                campDbManager.selectToTableCounselor(COLUMN_NAME_LOGIN_COUNSELOR)
//
//
//
//        for ((i, elm) in counselorLoginCounselor.withIndex()) {
//            if (counselorLoginCounselor[i] == ArgumentsNAlogin.login) {
//                if (counselorName[i].isNotBlank()) {
//                    counselorItemSubscription[0] = counselorName[i]
//                    counselorItemSubscription[1] = counselorSurname[i]
//                    counselorItemSubscription[2] = counselorPatronymic[i]
//                    counselorItemSubscription[3] = counselorBirthday[i]
//                    counselorItemSubscription[4] = counselorPhoneNumber[i]
//
//                }
//            }
//        }
//
//        for ((i, firstElm) in counselorItemTitle.withIndex()) {
//            for ((j, secondElm) in counselorItemSubscription.withIndex()) {
//                adapter.addCounselor(
//                    CounselorDataClass(
//                        counselorItemTitle[i],
//                        counselorItemSubscription[j]
//                    )
//                )
//            }
//        }
//        rv.adapter = adapter

    }


    override fun onStart() {
        super.onStart()
         loginList =
            campDbManager.selectToTableAvatarLogin(COLUMN_NAME_LOGIN_AVATAR)
        val imgByteArray =
            campDbManager.selectToTableAvatarImage(COLUMN_NAME_AVATAR)

        for ((i, elm) in loginList.withIndex()) {
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
//
//
//        profileText.text =
//            if (counselorItemSubscription[0].isNotEmpty())
//                "${counselorItemSubscription[0]}  ${counselorItemSubscription[1]}"
//        else
//            "Имя Фамилия"
        bottomProfileText.text = ArgumentsNAlogin.login
//        rv.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_counselor, container, false)
        val fabCounselor = view.findViewById<FloatingActionButton>(R.id.fabCounselor)

        rv = view.findViewById(R.id.rcCounselor)
        rv.layoutManager = LinearLayoutManager(act)
        rv.itemAnimator = DefaultItemAnimator()

        profileText = view.findViewById(R.id.headerProfileTextCounselor)
        bottomProfileText = view.findViewById(R.id.bottomProfileTextCounselor)
        imageProfile = view.findViewById(R.id.imageProfile)

        imageProfile.setOnClickListener {
            val popupMenu = PopupMenu(ctx, view)
            popupMenu.menuInflater.inflate(R.menu.choice_action_profile_image, popupMenu.menu)
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



        fabCounselor.setOnClickListener {

            CounselorBottomSheetDialog.newInstance()
                .show(this.parentFragmentManager, "bottomDialogDS")

        }

        rv.adapter = adapter
        return view
    }

    private fun choicePhotoFromGallery() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI).apply {
                type = "image/*"
            }

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_GALLERY_COUNSELOR);
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_CAMERA_COUNSELOR);
    }

    private fun uploadPhotoFromCamera(cameraExtras: Bundle?) {
        (cameraExtras?.get("data") as? Bitmap)?.let {
            try {
                imageProfile.setImageBitmap(it)
                saveImageBitmapAvatar(it)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(ctx, R.string.error_uploading_image, Toast.LENGTH_SHORT).show()
            }
        } ?: Toast.makeText(ctx, R.string.error_uploading_image, Toast.LENGTH_SHORT).show()

    }

    private fun uploadPhotoFromGallery(uri: Uri?) {
        try {
            val imageUri: Uri = uri!!
            val imageStream: InputStream? =
                act.contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            imageProfile.setImageBitmap(selectedImage)
            saveImageBitmapAvatar(selectedImage)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(ctx, R.string.error_uploading_image, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE_CAMERA_COUNSELOR -> uploadPhotoFromCamera(data?.extras)
            REQUEST_IMAGE_CAPTURE_GALLERY_COUNSELOR -> uploadPhotoFromGallery(data?.data)

        }
    }

    private fun saveImageBitmapAvatar(img: Bitmap) {
        val stream = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.PNG, 0, stream)
        val byteArray = stream.toByteArray()
        campDbManager.updateRawToTableAvatar(byteArray, ArgumentsNAlogin.login)
    }


    private fun alert(massage: Int) {
        val builder = AlertDialog.Builder(ctx)
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