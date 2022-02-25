package com.example.singupactivity.ui.main.Fragment.Search

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.AddEditRoomBinding
import com.example.singupactivity.ui.main.Adapter.SearchAdapters.SearchRoomAdapter
import com.example.singupactivity.ui.main.Data.EventsDataClass
import com.example.singupactivity.ui.main.Data.RoomDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_FIO_CHILD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_FLOOR
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_QUANTITY_CHILD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_ROOM_NUMBER
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.Arguments
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentDSDataClass
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag
import com.example.singupactivity.ui.main.Objects.Room.ArgumentsRoomDataClass
import com.example.singupactivity.ui.main.Objects.Room.ArgumentsRoomFlag
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class SearchRoomFragment : Fragment() {

    lateinit var adapter: SearchRoomAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var rv: RecyclerView
    lateinit var etCardSearch: EditText


    companion object {
        @JvmStatic
        fun newInstance() =
            SearchRoomFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = CampDbManager(act)
        adapter = SearchRoomAdapter(this@SearchRoomFragment)

    }

    private fun getData(const: String, searchText: String): ArrayList<String> {
        return  campDbManager.selectToTableRoom(
            const,
            searchText,
            Arguments.arg
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        rv = view.findViewById(R.id.rcDailyScheduleSearch)
        rv.layoutManager = LinearLayoutManager(act)
        rv.itemAnimator = DefaultItemAnimator()
        rv.adapter = adapter
        val ibSearch = view.findViewById<ImageButton>(R.id.imageButtonSearch)
        etCardSearch = view.findViewById(R.id.etSearchDailySchedule)

        ibSearch?.setOnClickListener {
            if (Arguments.arg.isNotBlank()) {
                val searchText = etCardSearch.text.toString()
                if (searchText.isNotEmpty()) {
                    adapter.roomList.clear()
                    val floorList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_FLOOR, searchText)
                            }.await()
                        }

                    val roomNumberList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_ROOM_NUMBER, searchText)
                            }.await()
                        }

                    val quantityList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_QUANTITY_CHILD, searchText)
                            }.await()
                        }

                    val fioChildList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_FIO_CHILD, searchText)
                            }.await()
                        }

                    for ((i, _) in floorList.withIndex()) {
                        adapter.addRoom(
                            RoomDataClass(
                                floor =  floorList[i],
                                roomNumber = roomNumberList[i],
                                quantity =  quantityList[i],
                                FIOChild = fioChildList[i]
                            )
                        )
                    }
                    rv.adapter = adapter
                    if (adapter.roomList.isEmpty()) {
                        alert(
                            getString(R.string.no_data_after_search_title),
                            getString(R.string.no_data_after_search)
                        )
                        etCardSearch.text.clear()
                    }
                    Arguments.arg = ""

                } else {
                    Toast.makeText(
                        requireActivity(),
                        R.string.no_data_to_search,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                alert(
                    getString(R.string.no_argument_selected_title),
                    getString(R.string.no_argument_selected)
                )
            }
        }

        return view

    }




    @SuppressLint("InflateParams", "WrongConstant")
    fun addAndEditRoom(
        isUpdate: Boolean,
        roomDataClass: RoomDataClass?,
        position: Int
    ) {
        var selectedFIO = ""

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
                spChild.editText?.setText(roomDataClass.FIOChild)
            }
            alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(
                    if (isUpdate) getString(R.string.update) else getString(R.string.save)
                ) { _, _ -> }
                .setNegativeButton(if (isUpdate) getString(R.string.delete) else getString(R.string.close)
                ) { dialogBox, _ ->
                    if (isUpdate) {
                        ArgumentsRoomFlag.isUpdate = false
                        ArgumentsRoomDataClass.floor = adapter.roomList[position].floor
                        ArgumentsRoomDataClass.roomNumber = adapter.roomList[position].roomNumber
                        ArgumentsRoomDataClass.quantity = adapter.roomList[position].quantity
                        ArgumentsRoomDataClass.FIOChild = adapter.roomList[position].FIOChild
                        deleteRoom(
                            position = position,
                            const = tiRoomNumber.editText?.text.toString()
                        )
                    } else {
                        dialogBox.cancel()
                    }
                }
            spChildAutoCompleteTextView.setOnClickListener {
                if (getDataForMaterialSpinner().isNullOrEmpty()) {
                    binding.spChild.error = getString(R.string.child_list_empty)
                    binding.spChild.defaultHintTextColor =
                        ctx.getColorStateList(R.color.errorColor)
                    if (spChild.isFocused) {
                        spChild.endIconMode = R.color.errorColor
                    }
                }
            }
            if (!getDataForMaterialSpinner().isNullOrEmpty())
                spChildAutoCompleteTextView.setAdapter(
                    ArrayAdapter(
                        ctx,
                        R.layout.drop_down_child_item,
                        getDataForMaterialSpinner()
                    )
                )
            spChildAutoCompleteTextView.setOnItemClickListener { _, _, _, _ ->
                selectedFIO = spChildAutoCompleteTextView.text.toString()
            }
            if(spChild.isFocused) {
                spChild.endIconMode = R.color.teal_700
            }
            val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
            alertDialog.window?.decorView?.setBackgroundResource(R.drawable.add_dialog_shape)
            alertDialog.show()
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(View.OnClickListener {
                    when {
                        TextUtils.isEmpty(tiFloor.editText?.text.toString()) -> {
                            binding.tiFloor.error = getString(R.string.enter_floor)
                            binding.tiFloor.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiRoomNumber.editText?.text.toString()) -> {
                            binding.tiRoomNumber.error = getString(R.string.enter_number_room)
                            binding.tiRoomNumber.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiQuantity.editText?.text.toString()) -> {
                            binding.tiQuantity.error = getString(R.string.enter_quantity_child)
                            binding.tiQuantity.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        selectedFIO.isEmpty() -> {
                            binding.spChild.error = getString(R.string.select_FIO_child)
                            binding.spChild.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        else -> {
                            alertDialog.dismiss()
                        }
                    }
                    if (isUpdate && roomDataClass != null) {
                        if (etNameUpdate != null) {
                            ArgumentsRoomFlag.isUpdate = true
                            ArgumentsRoomDataClass.floor = adapter.roomList[position].floor
                            ArgumentsRoomDataClass.roomNumber = adapter.roomList[position].roomNumber
                            ArgumentsRoomDataClass.quantity = adapter.roomList[position].quantity
                            ArgumentsRoomDataClass.FIOChild = adapter.roomList[position].FIOChild
                            ArgumentsRoomDataClass.floorUpdate = tiFloor.editText?.text.toString()
                            ArgumentsRoomDataClass.roomNumberUpdate = tiRoomNumber.editText?.text.toString()
                            ArgumentsRoomDataClass.quantityUpdate = tiQuantity.editText?.text.toString()
                            ArgumentsRoomDataClass.FIOChildUpdate = selectedFIO

                            updateRoom(
                                floorUpdate = tiFloor.editText?.text.toString(),
                                roomNumberUpdate = tiRoomNumber.editText?.text.toString(),
                                quantityUpdate = tiQuantity.editText?.text.toString(),
                                roomNumberUpdatePosition = etNameUpdate,
                                position = position,
                                FIOChild = selectedFIO
                            )
                        }
                    }
                })

        }
    }

    private fun getDataChild(const: String): java.util.ArrayList<String> {
        return campDbManager.selectToTableChild(
            const
        )
    }

    private fun getDataForMaterialSpinner(): Array<String?> {
        val nameChildList =
            runBlocking {
                async {
                    getDataChild(CampDbNameClass.COLUMN_NAME_CHILD_NAME)
                }.await()
            }

        val surnameChildList =
            runBlocking {
                async {
                    getDataChild(CampDbNameClass.COLUMN_NAME_CHILD_SURNAME)
                }.await()
            }

        val patronymicChildList =
            runBlocking {
                async {
                    getDataChild(CampDbNameClass.COLUMN_NAME_CHILD_PATRONYMIC)
                }.await()
            }
        val childList = arrayOfNulls<String>(nameChildList.size)
        for ((i, _) in nameChildList.withIndex()) {
            childList[i] = "${surnameChildList[i]} ${nameChildList[i]} ${patronymicChildList[i]}"
        }
        return childList
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
        FIOChild: String,
        roomNumberUpdatePosition: String,
        position: Int
    ) {
        runBlocking {
            async {
                campDbManager.updateRawToTableRoom(
                    floorUpdate = floorUpdate,
                    roomNumberUpdate = roomNumberUpdate,
                    quantityUpdate = quantityUpdate,
                    roomNumberUpdatePosition = roomNumberUpdatePosition,
                    FIOChild = FIOChild
                )
            }.await()
        }

        val roomDataClass = RoomDataClass(
            floor = floorUpdate,
            roomNumber = roomNumberUpdate,
            quantity = quantityUpdate,
            FIOChild = FIOChild
        )

        adapter.updateRoom(position, roomDataClass)

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