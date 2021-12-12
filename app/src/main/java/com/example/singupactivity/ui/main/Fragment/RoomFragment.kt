package com.example.singupactivity.ui.main.Fragment

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
import com.example.singupactivity.ui.main.Adapter.RoomAdapter
import com.example.singupactivity.ui.main.Data.RoomDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.google.android.material.floatingactionbutton.FloatingActionButton


class RoomFragment : Fragment() {

    lateinit var adapter: RoomAdapter
    lateinit var campDbManager: CampDbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        campDbManager = activity?.let { CampDbManager(it) }!!
        adapter = RoomAdapter(this@RoomFragment)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view:View = inflater.inflate(R.layout.fragment_room, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rcRoom)
        val fabRoom = view.findViewById<FloatingActionButton>(R.id.fabRoom)

        rv.layoutManager = GridLayoutManager(activity, 3)
        rv.itemAnimator = DefaultItemAnimator()

        fabRoom.setOnClickListener {
            addAndEditRoom(false, null, -1)
        }

        rv.adapter = adapter

        return view
    }

    @SuppressLint("InflateParams")
    fun addAndEditRoom(
        isUpdate: Boolean,
        roomDataClass: RoomDataClass?,
        position: Int
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.add_edit_room, null)


        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(requireActivity())
        alertDialogBuilderUserInput.setView(view)

        val newRoomTitle = view.findViewById<TextView>(R.id.newRoomTitle)
        val etFloor = view.findViewById<EditText>(R.id.etFloor)
        val etRoomNumber = view.findViewById<EditText>(R.id.etRoomNumber)
        val etQuantity = view.findViewById<EditText>(R.id.etQuantity)
        val etFloorUpdate: String? = roomDataClass?.floor

        newRoomTitle.text = if (!isUpdate) "Добавить" else "Редактировать"

        if (isUpdate && roomDataClass != null) {
            etFloor.setText(roomDataClass.floor)
            etRoomNumber.setText(roomDataClass.roomNumber)
            etQuantity.setText(roomDataClass.quantity)
        }
        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(if (isUpdate) "Обновить" else "Сохранить",
                DialogInterface.OnClickListener { dialogBox, id -> })
            .setNegativeButton(if (isUpdate) "Удалить" else "Закрыть",
                DialogInterface.OnClickListener { dialogBox, id ->
                    if (isUpdate) {
                        deleteRoom(
                            position = position,
                            const = etFloor.text.toString()
                        )
                    } else {
                        dialogBox.cancel()
                    }
                })

        val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(etFloor.text.toString())) {
                Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            } else if (TextUtils.isEmpty(etRoomNumber.text.toString())) {
                Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            } else if (TextUtils.isEmpty(etQuantity.text.toString())) {
                Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            } else {
                alertDialog.dismiss()
            }
            if (isUpdate && roomDataClass != null) {
                if (etFloorUpdate != null) {
                    updateRoom(
                        roomNumberUpdate = etRoomNumber.text.toString(),
                        quantityUpdate = etQuantity.text.toString(),
                        floorUpdate = etFloor.text.toString(),
                        floorUpdatePosition = etFloorUpdate,
                        position = position
                    )
                }

            } else {
                createRoom(
                    floor = etFloor.text.toString(),
                    roomNumber = etRoomNumber.text.toString(),
                    quantity = etQuantity.text.toString()
                )
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteRoom(const: String, position: Int) {

//        campDbManager.deleteRawToTableDailySchedule(const)

        adapter.removeRoom(position)

    }

    private fun updateRoom(
        floorUpdate: String,
        roomNumberUpdate: String,
        quantityUpdate: String,
        floorUpdatePosition: String,
        position: Int
    ) {
//        campDbManager.updateRawToTableDailySchedule(
//            nameEvent = nameEventUpdate,
//            dateEvent = dateEventUpdate,
//            timeEvent = floorUpdate,
//            nameEventUpdatePosition = nameEventUpdatePosition
//        )

        val roomDataClass = RoomDataClass(
            floor = floorUpdate,
            roomNumber = roomNumberUpdate,
            quantity = quantityUpdate
        )

        adapter.updateRoom(position,roomDataClass)

    }

    private fun createRoom(
        floor: String,
        roomNumber: String,
        quantity: String
    ) {
//        campDbManager.insertToTableDailySchedule(
//            nameEvent = nameEventCreate,
//            dateEvent = dateEventCreate,
//            timeEvent = timeEventCreate
//        )

        val roomDataClass = RoomDataClass(
            floor = floor,
            roomNumber = roomNumber,
            quantity = quantity
        )

        adapter.addRoom(roomDataClass)

    }

}