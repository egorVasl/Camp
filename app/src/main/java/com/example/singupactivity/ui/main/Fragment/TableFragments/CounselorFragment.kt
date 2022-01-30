package com.example.singupactivity.ui.main.Fragment.TableFragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Activity.NavigationActivity
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.FileNotFoundException
import java.io.InputStream


class CounselorFragment : Fragment() {

    lateinit var campDbManager: CampDbManager
    private val pickImage = 1
    lateinit var image: ImageView


    var idAuthorization: String = ""
    var nameCounselor: String = "Арина"
    var surnameCounselor: String = "Викторовна"
    var patronamycCounselor: String = "Мазуренко"
    var birthdayCounselor: String = "05.01.2003"
    var phoneNumberCounselor: String = "375291764532"
    var result: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = activity?.let { CampDbManager(it) }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_counselor, container, false)
        val tvNameCounselor = view.findViewById<TextView>(R.id.tvNameCounselor)
        val tvSurnameCounselor = view.findViewById<TextView>(R.id.tvSurnameCounselor)
        val tvPatronamycCounselor = view.findViewById<TextView>(R.id.tvPatronamycCounselor)
        val tvBirthdayCounselor = view.findViewById<TextView>(R.id.tvBirthdayCounselor)
        val tvPhoneNumberCounselor = view.findViewById<TextView>(R.id.tvPhoneNumberCounselor)
        val fabCounselor = view.findViewById<FloatingActionButton>(R.id.fabCounselor)

         image = view.findViewById<ImageView>(R.id.imageProfile)

        image.setOnClickListener {

            val photoPickerIntent: Intent = Intent(Intent.ACTION_PICK)

            photoPickerIntent.type = "image/*"

            startActivityForResult(photoPickerIntent, pickImage);

        }


        val activity: NavigationActivity? = activity as NavigationActivity?
        result = activity?.getMyData()

        fabCounselor.setOnClickListener {
            val view1 = LayoutInflater.from(context).inflate(R.layout.add_counselor, null)

            val alertDialogBuilderUserInput: AlertDialog.Builder =
                AlertDialog.Builder(requireActivity())
            alertDialogBuilderUserInput.setView(view1)

            val etNameCounselor = view1.findViewById<TextView>(R.id.etNameCounselor)
            val etSurnameCounselor = view1.findViewById<TextView>(R.id.etSurnameCounselor)
            val etPatronamycCounselor = view1.findViewById<TextView>(R.id.etPatronamycCounselor)
            val etBirthdayCounselor = view1.findViewById<TextView>(R.id.etBirthdayCounselor)
            val etPhoneNumberCounselor = view1.findViewById<TextView>(R.id.etPhoneNumberCounselor)
//
//            etNameCounselor.text = nameCounselor
//            etSurnameCounselor.text = surnameCounselor
//            etPatronamycCounselor.text = patronamycCounselor
//            etBirthdayCounselor.text = birthdayCounselor
//            etPhoneNumberCounselor.text = phoneNumberCounselor

            alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(if (true) "Добавить" else "",
                    DialogInterface.OnClickListener { dialogBox, id ->


                         nameCounselor= etNameCounselor.text.toString()
                         surnameCounselor = etSurnameCounselor.text.toString()
                         patronamycCounselor  = etPatronamycCounselor.text.toString()
                         birthdayCounselor = etBirthdayCounselor.text.toString()
                         phoneNumberCounselor =  etPhoneNumberCounselor.text.toString()



//                        campDbManager.insertToTableCounselor(
//                            counselorName = etNameCounselor.text.toString(),
//                            counselorSurname = etSurnameCounselor.text.toString(),
//                            counselorPatronymic = etPatronamycCounselor.text.toString(),
//                            counselorBirthday = etBirthdayCounselor.text.toString(),
//                            counselorNumber = etPhoneNumberCounselor.text.toString(),
//                        )

                    })
                .setNegativeButton(if (true) "Закрыть" else "",
                    DialogInterface.OnClickListener { dialogBox, id ->

                    })

            tvNameCounselor.text = nameCounselor
            tvSurnameCounselor.text = surnameCounselor
            tvPatronamycCounselor.text = patronamycCounselor
            tvBirthdayCounselor.text = birthdayCounselor
            tvPhoneNumberCounselor.text = phoneNumberCounselor

            val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
            with(alertDialog) {
                show()
                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
                    when {
                        TextUtils.isEmpty(etNameCounselor.text.toString()) -> {
                            Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                                .show()
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(etSurnameCounselor.text.toString()) -> {
                            Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                                .show()
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(etPatronamycCounselor.text.toString()) -> {
                            Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                                .show()
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(etBirthdayCounselor.text.toString()) -> {
                            Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                                .show()
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(etPhoneNumberCounselor.text.toString()) -> {
                            Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                                .show()
                            return@OnClickListener
                        }
                        else -> {
                            dismiss()
                        }
                    }
                })
            }
        }

        tvNameCounselor.text = nameCounselor
        tvSurnameCounselor.text = surnameCounselor
        tvPatronamycCounselor.text = patronamycCounselor
        tvBirthdayCounselor.text = birthdayCounselor
        tvPhoneNumberCounselor.text = phoneNumberCounselor
        return view

    }

    @SuppressLint("InflateParams")
    private fun addAndEditCounselor(isUpdate: Boolean) {



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            pickImage -> {
                if (resultCode === AppCompatActivity.RESULT_OK) {
                    try {
                        val imageUri: Uri = data?.data!!
                        val imageStream: InputStream? =
                            activity?.contentResolver?.openInputStream(imageUri)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        image.setImageBitmap(selectedImage)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}