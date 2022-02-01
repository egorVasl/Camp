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
import com.example.singupactivity.ui.main.Adapter.CounselorAdapter
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.Fragment.BottomSheet.CounselorBottomSheetDialog
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.NavigationActviy.ArgumentsNAlogin
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream
import java.io.InputStream

private const val REQUEST_IMAGE_CAPTURE_CAMERA_COUNSELOR = 1
private const val REQUEST_IMAGE_CAPTURE_GALLERY_COUNSELOR = 2

class CounselorFragment : Fragment() {

    lateinit var adapter: CounselorAdapter
    lateinit var rv: RecyclerView


    lateinit var campDbManager: CampDbManager
    lateinit var imageProfile: ImageView
    lateinit var bottomProfileText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = activity?.let { CampDbManager(it) }!!
    }


    override fun onStart() {
        super.onStart()
        val loginList =
            campDbManager.selectToTableAvatarLogin(CampDbNameClass.COLUMN_NAME_LOGIN_AVATAR)
        val imgByteArray =
            campDbManager.selectToTableAvatarImage(CampDbNameClass.COLUMN_NAME_AVATAR)
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
        bottomProfileText.text = ArgumentsNAlogin.login
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

        bottomProfileText = view.findViewById(R.id.bottomProfileText)
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