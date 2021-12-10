package com.example.singupactivity.ui.main.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CounselorFragment : Fragment() {

    lateinit var campDbManager: CampDbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = activity?.let { CampDbManager(it) }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_counselor, container, false)

        val imageProfile = view.findViewById<ImageView>(R.id.imageProfile)
        val textProfile = view.findViewById<TextView>(R.id.textProfile)

        val tvNameCounselor = view.findViewById<TextView>(R.id.tvNameCounselor)
        val tvSurnameCounselor = view.findViewById<TextView>(R.id.tvSurnameCounselor)
        val tvPatronamycCounselor = view.findViewById<TextView>(R.id.tvPatronamycCounselor)
        val tvBirthdayCounselor = view.findViewById<TextView>(R.id.tvBirthdayCounselor)
        val tvPhoneNumberCounselor = view.findViewById<TextView>(R.id.tvPhoneNumberCounselor)
        val fabCounselor = view.findViewById<FloatingActionButton>(R.id.fabCounselor)


        fabCounselor.setOnClickListener {
            addAndEditCounselor()
        }

        return view

    }

    @SuppressLint("InflateParams")
    private fun addAndEditCounselor() {

        val view = LayoutInflater.from(context).inflate(R.layout.add_counselor, null)

        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(requireActivity())
        alertDialogBuilderUserInput.setView(view)

        val etNameCounselor = view.findViewById<TextView>(R.id.etNameCounselor)
        val etSurnameCounselor = view.findViewById<TextView>(R.id.etSurnameCounselor)
        val etPatronamycCounselor = view.findViewById<TextView>(R.id.etPatronamycCounselor)
        val etBirthdayCounselor = view.findViewById<TextView>(R.id.etBirthdayCounselor)
        val etPhoneNumberCounselor = view.findViewById<TextView>(R.id.etPhoneNumberCounselor)
    }

}