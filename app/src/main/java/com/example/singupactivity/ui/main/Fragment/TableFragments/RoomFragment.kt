package com.example.singupactivity.ui.main.Fragment.TableFragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.AddEditRoomBinding
import com.example.singupactivity.ui.main.Adapter.RoomAdapter
import com.example.singupactivity.ui.main.Data.RoomDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_FLOOR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_QUANTITY_CHILD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_ROOM_NUMBER
import com.example.singupactivity.ui.main.Fragment.BottomSheet.*
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag
import com.example.singupactivity.ui.main.Objects.Room.ArgumentsRoomDataClass
import com.example.singupactivity.ui.main.Objects.Room.ArgumentsRoomFlag
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class RoomFragment : Fragment() {

    lateinit var adapter: RoomAdapter
    lateinit var campDbManager: CampDbManager

    private fun getData(const: String): ArrayList<String> {
        return campDbManager.selectToTableRoom(
            const
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_ROOM) { _, _ ->
            addAndEditRoom(false, null, -1)
        }

        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_ROOM) { _, _ ->
            if (adapter.roomList.isEmpty())
                alert(
                    getString(R.string.no_data_to_save_title),
                    getString(R.string.no_data_to_save)
                )
            else
                importTextFile()
        }


        campDbManager = CampDbManager(act)
        adapter = RoomAdapter(this@RoomFragment)

        val floorList =
            runBlocking {
                async {
                    getData(COLUMN_NAME_FLOOR)
                }.await()
            }

        val roomNumberList =
            runBlocking {
                async {
                    getData(COLUMN_NAME_ROOM_NUMBER)
                }.await()
            }

        val quantityList =
            runBlocking {
                async {
                    getData(COLUMN_NAME_QUANTITY_CHILD)
                }.await()
            }

        for ((i, elm) in floorList.withIndex()) {
            adapter.addRoom(
                RoomDataClass(
                    floor =  floorList[i],
                    roomNumber = roomNumberList[i],
                    quantity =  quantityList[i]
                )
            )
        }

    }



    @SuppressLint("SimpleDateFormat")
    private fun importTextFile() {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())

        val path = context?.getExternalFilesDir(null)

        val letDirectory = File(path, "Rooms")
        letDirectory.mkdirs()
        val file = File(letDirectory, "Rooms $currentDate.txt")
        try {
            FileOutputStream(file).use { stream ->
                adapter.let {
                    for ((i, _) in it.roomList.withIndex()) {
                        stream.write("Комната:${i + 1}\n".toByteArray())
                        stream.write("Этажд: ${it.roomList[i].floor}\n".toByteArray())
                        stream.write("Номер комнаты: ${it.roomList[i].roomNumber}\n".toByteArray())
                        stream.write("Кол-во детей: ${it.roomList[i].quantity}\n\n".toByteArray())

                    }
                }
                stream.close()
                alert(
                    "Файл успешно создан!",
                    "Путь: $path/Rooms/Rooms $currentDate.txt"
                )
            }
        } catch (exe: IOException) {
            Toast.makeText(ctx, "Ошибка создания файла: $exe", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        val response = RoomDataClass(
            floor = ArgumentsRoomDataClass.floor,
            roomNumber = ArgumentsRoomDataClass.roomNumber,
            quantity = ArgumentsRoomDataClass.quantity
        )
        val responseUpdate = RoomDataClass(
            floor = ArgumentsRoomDataClass.floorUpdate,
            roomNumber = ArgumentsRoomDataClass.roomNumberUpdate,
            quantity = ArgumentsRoomDataClass.quantityUpdate
        )
        if (ArgumentsRoomFlag.isUpdate) {
            adapter.let {
                for ((i, _) in it.roomList.withIndex()) {
                    if (it.roomList[i] == response)
                        it.updateRoom(i, responseUpdate)
                    it.notifyDataSetChanged()
                }
            }
        } else {
            adapter.let {
                for ((i, _) in it.roomList.withIndex()) {
                    if (it.roomList[i] == response)
                        it.removeRoom(i)
                    it.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_room, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rcRoom)
        val fabRoom = view.findViewById<FloatingActionButton>(R.id.fabRoom)

        rv.layoutManager = LinearLayoutManager(act)
        rv.itemAnimator = DefaultItemAnimator()

        fabRoom.setOnClickListener {
            RoomBottomSheetDialog.newInstance()
                .show(this.parentFragmentManager, "bottomDialogRoom")
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
        val binding: AddEditRoomBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                context
            ), R.layout.add_edit_room, null, false
        )
        val etNameUpdate: String? = roomDataClass?.roomNumber

        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(act)
        alertDialogBuilderUserInput.setView(binding.root)

        with(binding) {

            newRoomTitle.text = if (!isUpdate) getString(R.string.add) else getString(R.string.edit)

            if (isUpdate && roomDataClass != null) {
                tiFloor.editText?.setText(roomDataClass.floor)
                tiRoomNumber.editText?.setText(roomDataClass.roomNumber)
                tiQuantity.editText?.setText(roomDataClass.quantity)
            }
            alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(
                    if (isUpdate) getString(R.string.update) else getString(R.string.save)
                ) { _, _ -> }
                .setNegativeButton(
                    if (isUpdate) getString(R.string.delete) else getString(R.string.close)
                ) { dialogBox, _ ->
                    if (isUpdate) {
                        deleteRoom(
                            position = position,
                            const = tiRoomNumber.editText?.text.toString()
                        )
                    } else {
                        dialogBox.cancel()
                    }
                }

            val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
            alertDialog.window?.decorView?.setBackgroundResource(R.drawable.add_dialog_shape)
            alertDialog.show()
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(View.OnClickListener {
                    when {
                        TextUtils.isEmpty(tiFloor.editText?.text.toString()) -> {
                            binding.tiFloor.error = getString(R.string.enter_floor)
                            binding.tiFloor.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiRoomNumber.editText?.text.toString()) -> {
                            binding.tiRoomNumber.error = getString(R.string.enter_number_room)
                            binding.tiRoomNumber.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiQuantity.editText?.text.toString()) -> {
                            binding.tiQuantity.error = getString(R.string.enter_quantity_child)
                            binding.tiQuantity.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        else -> {
                            alertDialog.dismiss()
                        }
                    }
                    if (isUpdate && roomDataClass != null) {
                        if (etNameUpdate != null) {
                            updateRoom(
                                floorUpdate = tiFloor.editText?.text.toString(),
                                roomNumberUpdate = tiRoomNumber.editText?.text.toString(),
                                quantityUpdate = tiQuantity.editText?.text.toString(),
                                roomNumberUpdatePosition = etNameUpdate,
                                position = position
                            )
                        }

                    } else {
                        createRoom(
                            floor = tiFloor.editText?.text.toString(),
                            roomNumber = tiRoomNumber.editText?.text.toString(),
                            quantity = tiQuantity.editText?.text.toString()
                        )
                    }
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteRoom(const: String, position: Int) {

        runBlocking {
            async {
                campDbManager.deleteRawToTableRoom(const)
            }.await()
        }

        adapter.removeRoom(position)

    }

    private fun updateRoom(
        floorUpdate: String,
        roomNumberUpdate: String,
        quantityUpdate: String,
        roomNumberUpdatePosition: String,
        position: Int
    ) {
        runBlocking {
            async {
                campDbManager.updateRawToTableRoom(
                    floorUpdate = floorUpdate,
                    roomNumberUpdate = roomNumberUpdate,
                    quantityUpdate = quantityUpdate,
                    roomNumberUpdatePosition = roomNumberUpdatePosition
                )
            }.await()
        }

        val roomDataClass = RoomDataClass(
            floor = floorUpdate,
            roomNumber = roomNumberUpdate,
            quantity = quantityUpdate
        )

        adapter.updateRoom(position, roomDataClass)

    }

    private fun createRoom(
        floor: String,
        roomNumber: String,
        quantity: String
    ) {

        runBlocking {
            async {
                campDbManager.insertToTableRoom(
                    floor = floor,
                    roomNumber = roomNumber,
                    quantityChild = quantity
                )
            }.await()
        }


        val roomDataClass = RoomDataClass(
            floor = floor,
            roomNumber = roomNumber,
            quantity = quantity
        )

        adapter.addRoom(roomDataClass)

    }

    private fun alert(title: String, massage: String) {
        val builder = AlertDialog.Builder(act)
        builder.setTitle(title)
            .setMessage(massage)
            .setCancelable(false)
            .setPositiveButton(R.string.contin) { dialog, _ ->
                dialog.dismiss()

            }

        val alert = builder.create()
        alert.show()
    }

}