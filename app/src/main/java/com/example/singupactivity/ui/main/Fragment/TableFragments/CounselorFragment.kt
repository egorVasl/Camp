package com.example.singupactivity.ui.main.Fragment.TableFragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Activity.OnDataPass
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
import com.example.singupactivity.ui.main.Fragment.BottomSheet.COUNSELOR_BOTTOM_BUNDLE_KEY
import com.example.singupactivity.ui.main.Fragment.BottomSheet.COUNSELOR_BOTTOM_REQUEST_KEY
import com.example.singupactivity.ui.main.Fragment.BottomSheet.CounselorBottomSheetDialog
import com.example.singupactivity.ui.main.Fragment.ProgressBarDialog
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.NavigationActviy.ArgumentsNAlogin
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.InputStream


private const val REQUEST_IMAGE_CAPTURE_CAMERA_COUNSELOR = 1
private const val REQUEST_IMAGE_CAPTURE_GALLERY_COUNSELOR = 2

class CounselorFragment : Fragment() {

    lateinit var adapter: CounselorAdapter
    lateinit var rv: RecyclerView
    private lateinit var pb: ProgressBar
    private lateinit var ib: ImageButton

    private lateinit var progressDialog: ProgressBarDialog

    lateinit var campDbManager: CampDbManager
    private lateinit var imageProfile: ImageView
    private lateinit var bottomProfileText: TextView
    private lateinit var profileText: TextView


    private lateinit var loginList: ArrayList<String>
    private lateinit var counselorName: ArrayList<String>
    private lateinit var counselorSurname: ArrayList<String>
    private lateinit var counselorPatronymic: ArrayList<String>
    private lateinit var counselorBirthday: ArrayList<String>
    private lateinit var counselorPhoneNumber: ArrayList<String>
    private lateinit var counselorLoginCounselor: ArrayList<String>

    private var mDataPasser: OnDataPass? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDataPasser = context as OnDataPass?
    }

    private fun getData(const: String): ArrayList<String> {
        return campDbManager.selectToTableCounselor(const)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = CampDbManager(act)
        adapter = CounselorAdapter()
        progressDialog = ProgressBarDialog(act)
    }


    override fun onStart() {
        super.onStart()
        loginList =
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
        setFragmentResultListener(COUNSELOR_BOTTOM_REQUEST_KEY) { _, bundle ->
            if (bundle.getBoolean(COUNSELOR_BOTTOM_BUNDLE_KEY))
                setUpRecyclerView()
            progressDialog.dismiss()
            pb.isVisible = false
        }

        setUpRecyclerView()
        progressDialog.dismiss()
        pb.isVisible = false


        bottomProfileText.text = ArgumentsNAlogin.login
    }

    private fun setUpRecyclerView() {
        progressDialog.show()

        adapter.counselorList.clear()

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
        counselorPatronymic =
            runBlocking {
                async {
                    getData(COLUMN_NAME_COUNSELOR_PATRONYMIC)
                }.await()
            }
        counselorBirthday =
            runBlocking {
                async {
                    getData(COLUMN_NAME_COUNSELOR_BIRTHDAY)
                }.await()
            }
        counselorPhoneNumber =
            runBlocking {
                async {
                    getData(COLUMN_NAME_COUNSELOR_NUMBER)
                }.await()
            }
        counselorLoginCounselor =
            runBlocking {
                async {
                    getData(COLUMN_NAME_LOGIN_COUNSELOR)
                }.await()
            }
        val counselorItemTitle =
            arrayListOf(
                ctx.getString(R.string.item_name),
                ctx.getString(R.string.item_surname),
                ctx.getString(R.string.item_patronymic),
                ctx.getString(R.string.item_birthday),
                ctx.getString(R.string.item_phone_number),
                )
        var counselorItemSubscription = emptyArray<String>()
        for ((i, _) in counselorLoginCounselor.withIndex()) {
            if (counselorLoginCounselor[i] ==
                ArgumentsNAlogin.login) {
                if (counselorName[i].isNotBlank()) {
                    counselorItemSubscription = arrayOf(
                        counselorName[i],
                        counselorSurname[i],
                        counselorPatronymic[i],
                        counselorBirthday[i],
                        counselorPhoneNumber[i]
                    )
                    for ((j, _) in counselorItemTitle.withIndex()) {
                        adapter.addCounselor(
                            CounselorDataClass(
                                counselorItemTitle[j],
                                counselorItemSubscription[j]
                            )
                        )
                    }
                }
            }
        }

        mDataPasser?.onDataPass(true);
        profileText.text =
            if (counselorItemSubscription.isNotEmpty())
                "${counselorItemSubscription[0]} ${counselorItemSubscription[1]}"
            else
                getString(R.string.name_surname)
        rv.adapter = adapter
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_counselor, container, false)
        val fabCounselor = view.findViewById<FloatingActionButton>(R.id.fabCounselor)

        pb = view.findViewById(R.id.pb)

        rv = view.findViewById(R.id.rcCounselor)
        rv.layoutManager = LinearLayoutManager(act)
        rv.itemAnimator = DefaultItemAnimator()

        profileText = view.findViewById(R.id.headerProfileTextCounselor)
        bottomProfileText = view.findViewById(R.id.bottomProfileTextCounselor)
        imageProfile = view.findViewById(R.id.imageProfile)
        ib = view.findViewById(R.id.ibMenuCounselor)

        imageProfile.setOnClickListener {
            val popupMenu =
                androidx.appcompat.widget.PopupMenu(ctx, imageProfile)
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
                ctx,
                popupMenu.menu as MenuBuilder,
                imageProfile
            )
            menuHelper.setForceShowIcon(true)
            menuHelper.show()
        }

        ib.setOnClickListener {
            val popupMenu =
                androidx.appcompat.widget.PopupMenu(ctx, ib)
            popupMenu.inflate(R.menu.delete_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete -> {
                        deleteDataFromRecyclerView()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            val menuHelper = MenuPopupHelper(
                ctx,
                popupMenu.menu as MenuBuilder,
                ib
            )
            menuHelper.setForceShowIcon(true)
            menuHelper.show()
        }


        fabCounselor.setOnClickListener {

            CounselorBottomSheetDialog.newInstance()
                .show(this.parentFragmentManager, "bottomDialogDS")

        }

        rv.adapter = adapter
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteDataFromRecyclerView(){
        runBlocking {
            async {
                campDbManager.updateRawToTableCounselor(
                    "",
                    "",
                    "",
                    "",
                    "",
                    ArgumentsNAlogin.login)
            }.await()
        }

        profileText.text = getString(R.string.name_surname)
        mDataPasser?.onDataPass(true);
        adapter.counselorList.clear()
        rv.adapter?.notifyDataSetChanged()
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
        runBlocking {
            async {
                campDbManager.updateRawToTableAvatar(byteArray, ArgumentsNAlogin.login)
            }.await()
        }
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