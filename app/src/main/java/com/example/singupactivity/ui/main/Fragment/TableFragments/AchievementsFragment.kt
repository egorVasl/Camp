package com.example.singupactivity.ui.main.Fragment.TableFragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Adapter.AchievementsAdapter
import com.example.singupactivity.ui.main.Data.AchievementsDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AchievementsFragment : Fragment() {

    lateinit var adapter: AchievementsAdapter
    lateinit var campDbManager: CampDbManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = activity?.let { CampDbManager(it) }!!
        adapter = AchievementsAdapter(this@AchievementsFragment)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_achievements, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rcAchievements)
        val fabAchievements = view.findViewById<FloatingActionButton>(R.id.fabAchievements)

        rv.layoutManager = GridLayoutManager(activity, 2)
        rv.itemAnimator = DefaultItemAnimator()

        fabAchievements.setOnClickListener {
            addAndEditAchievements(false, null, -1)
        }

        rv.adapter = adapter
        return view
    }

    @SuppressLint("InflateParams")
    fun addAndEditAchievements(
        isUpdate: Boolean,
        achievementsDataClass: AchievementsDataClass?,
        position: Int
    ) {
        val view = LayoutInflater.from(ctx).inflate(R.layout.add_edit_achievements, null)


        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(requireActivity())
        alertDialogBuilderUserInput.setView(view)

        val newAchievementTitle = view.findViewById<TextView>(R.id.newAchievementTitle)
        val etAchievementSquad = view.findViewById<EditText>(R.id.etAchievementSquad)
        val etAchievementPlace = view.findViewById<EditText>(R.id.etAchievementPlace)
        val etAchievementEvent = view.findViewById<EditText>(R.id.etAchievementEvent)
        val etNameUpdate: String? = achievementsDataClass?.place

        newAchievementTitle.text = if (!isUpdate) "Добавить" else "Редактировать"

        if (isUpdate && achievementsDataClass != null) {
            etAchievementSquad.setText(achievementsDataClass.squadName)
            etAchievementPlace.setText(achievementsDataClass.place)
            etAchievementEvent.setText(achievementsDataClass.eventName)
        }
        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(if (isUpdate) "Обновить" else "Сохранить",
                DialogInterface.OnClickListener { dialogBox, id -> })
            .setNegativeButton(if (isUpdate) "Удалить" else "Закрыть",
                DialogInterface.OnClickListener { dialogBox, id ->
                    if (isUpdate) {
                        deleteAchievements(
                            position = position,
                            const = etAchievementPlace.text.toString()
                        )
                    } else {
                        dialogBox.cancel()
                    }
                })

        val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            when {
                TextUtils.isEmpty(etAchievementSquad.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                TextUtils.isEmpty(etAchievementPlace.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                TextUtils.isEmpty(etAchievementEvent.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                else -> {
                    alertDialog.dismiss()
                }
            }
            if (isUpdate && achievementsDataClass != null) {
                if (etNameUpdate != null) {
                    updateAchievements(
                        squadNameUpdate = etAchievementSquad.text.toString(),
                        placeUpdate = etAchievementPlace.text.toString(),
                        eventNameUpdate = etAchievementEvent.text.toString(),
                        placeUpdatePosition = etNameUpdate,
                        position = position
                    )
                }

            } else {
                createAchievements(
                    squadName  = etAchievementSquad.text.toString(),
                    place = etAchievementPlace.text.toString(),
                    eventName = etAchievementEvent.text.toString()
                )
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteAchievements(const: String, position: Int) {

        campDbManager.deleteRawToTableAchievements(const)

        adapter.removeAchievements(position)

    }

    private fun updateAchievements(
        squadNameUpdate: String,
        placeUpdate: String,
        eventNameUpdate: String,
        placeUpdatePosition: String,
        position: Int
    ) {
        campDbManager.updateRawToTableAchievements(
            place = placeUpdate,
            placeUpdatePosition = placeUpdatePosition
        )

        val achievementsDataClass = AchievementsDataClass(
            squadName = squadNameUpdate,
            place = placeUpdate,
            eventName = eventNameUpdate
        )

        adapter.updateAchievements(position,achievementsDataClass)

    }

    private fun createAchievements(
        squadName: String,
        place: String,
        eventName: String
    ) {
        campDbManager.insertToTableAchievements(
            achievementsPlace = place
        )

        val achievementsDataClass = AchievementsDataClass(
            squadName = squadName,
            place = place,
            eventName = eventName
        )

        adapter.addAchievements(achievementsDataClass)

    }



}
