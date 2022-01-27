package com.example.singupactivity.ui.main.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.text.TextUtils
import android.content.DialogInterface
import com.example.singupactivity.R
import android.app.AlertDialog
import android.content.Context
import android.util.TypedValue
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.*
import com.example.singupactivity.ui.main.Adapter.DailyScheduleAdapter
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_NAME_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME_EVENT


class DailyScheduleFragment : Fragment() {

    lateinit var adapter: DailyScheduleAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var rv: RecyclerView
    lateinit var cardSearch: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY) { _, bundle ->
            addAndEditSchedule(false, null, -1)
        }
        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_SEARCH) { _, _ ->
            showSearchSchedule()
        }
        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF) { _, _ ->
            importPDF()
        }

        campDbManager = activity?.let { CampDbManager(it) }!!
        adapter = DailyScheduleAdapter(this@DailyScheduleFragment)
        val eventTimeList = campDbManager.selectToTableDailySchedule(COLUMN_NAME_TIME_EVENT)
        val eventNameList = campDbManager.selectToTableDailySchedule(COLUMN_NAME_NAME_EVENT)
        val eventDateList = campDbManager.selectToTableDailySchedule(COLUMN_NAME_DATE_EVENT)
        for ((i, elm) in eventTimeList.withIndex()) {
            adapter.addDailySchedule(
                DailyScheduleDataClass(
                    eventTimeList[i],
                    eventNameList[i], eventDateList[i]
                )
            )

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_daily_schedule, container, false)
        rv = view.findViewById(R.id.rcDailySchedule)
        val fabDailySchedule = view.findViewById<FloatingActionButton>(R.id.fabDailySchedule)

        rv.layoutManager = LinearLayoutManager(activity)
        rv.itemAnimator = DefaultItemAnimator()

        fabDailySchedule.setOnClickListener {

            DailyScheduleBottomSheetDialog.newInstance()
                .show(this.parentFragmentManager, "bottomDialog")

        }

        rv.adapter = adapter
        return view
    }


    private fun showSearchSchedule() {
        rv.let { recyclerView ->
            if (recyclerView.isNotEmpty()) {
                cardSearch = view!!.findViewById(R.id.cardSearch)
                val etCardSearch = view?.findViewById<EditText>(R.id.etSearchDailySchedule)
                if (cardSearch.isVisible) {
                    cardSearch.isVisible = false
                    val layoutParams = rv.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.topMargin = context?.let { 0.toDp(it) }!!
                    rv.layoutParams = layoutParams
                } else {
                    cardSearch.isVisible = true

                    val layoutParams = rv.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.topMargin = context?.let { 70.toDp(it) }!!
                    rv.layoutParams = layoutParams

                    selectionArgs(etCardSearch?.text.toString())
                }
            } else {
                alert(R.string.empty_list_text)
            }
        }

    }

    private fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()


    private fun selectionArgs(searchText: String) {

        val view = LayoutInflater.from(context).inflate(R.layout.selection_arguments_layout, null)


        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(requireActivity())
        alertDialogBuilderUserInput.setView(view)


        val checkNameEvent = view.findViewById<CheckBox>(R.id.checkNameEvent)
        val checkTimeEvent = view.findViewById<CheckBox>(R.id.checkTimeEvent)
        val checkDateEvent = view.findViewById<CheckBox>(R.id.checkDateEvent)



        alertDialogBuilderUserInput
            .setCancelable(false)
            .setNegativeButton(R.string.cancellation){dialogBox,_->
                cardSearch.isVisible = false
                val layoutParams = rv.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.topMargin = context?.let { 0.toDp(it) }!!
                rv.layoutParams = layoutParams
                dialogBox.cancel()
            }
            .setPositiveButton(R.string.contin) { _, _ -> }
        val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            val selectionArguments = when  {
                checkNameEvent.isChecked -> COLUMN_NAME_NAME_EVENT
                checkTimeEvent.isChecked -> COLUMN_NAME_TIME_EVENT
                checkDateEvent.isChecked -> COLUMN_NAME_DATE_EVENT
                else ->""
            }
             if(!checkNameEvent.isChecked && !checkTimeEvent.isChecked && !checkDateEvent.isChecked){
                    Toast.makeText(requireActivity(), R.string.select_type_search, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                else {
                 searchSchedule(
                     searchText = searchText,
                     selectionArguments = selectionArguments
                 )
                    alertDialog.dismiss()
                }
        })
    }

    private fun searchSchedule(searchText: String, selectionArguments: String) {
val yest = selectionArguments
    }

    private fun importPDF() {

    }


    @SuppressLint("InflateParams")
    fun addAndEditSchedule(
        isUpdate: Boolean,
        dailyScheduleDataClass: DailyScheduleDataClass?,
        position: Int
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.add_daily_schedule, null)


        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(requireActivity())
        alertDialogBuilderUserInput.setView(view)

        val newDayTitle = view.findViewById<TextView>(R.id.newDayTitle)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etData = view.findViewById<EditText>(R.id.etData)
        val etTime = view.findViewById<EditText>(R.id.etTime)
        val etNameUpdate: String? = dailyScheduleDataClass?.nameEvent

        newDayTitle.text = if (!isUpdate) "Добавить" else "Редактировать"

        if (isUpdate && dailyScheduleDataClass != null) {
            etName.setText(dailyScheduleDataClass.nameEvent)
            etData.setText(dailyScheduleDataClass.dateEvent)
            etTime.setText(dailyScheduleDataClass.timeEvent)
        }
        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(if (isUpdate) "Обновить" else "Сохранить"
            ) { _, _ -> }
            .setNegativeButton(if (isUpdate) "Удалить" else "Закрыть",
                DialogInterface.OnClickListener { dialogBox, id ->
                    if (isUpdate) {
                        deleteDailySchedule(
                            position = position,
                            const = etName.text.toString()
                        )
                    } else {
                        dialogBox.cancel()
                    }
                })

        val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            when {
                TextUtils.isEmpty(etName.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.enter_name_event, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                TextUtils.isEmpty(etData.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.enter_data_event, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                TextUtils.isEmpty(etTime.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.enter_time_event, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                else -> {
                    alertDialog.dismiss()
                }
            }
            if (isUpdate && dailyScheduleDataClass != null) {
                if (etNameUpdate != null) {
                    updateDailySchedule(
                        nameEventUpdate = etName.text.toString(),
                        dateEventUpdate = etData.text.toString(),
                        timeEventUpdate = etTime.text.toString(),
                        nameEventUpdatePosition = etNameUpdate,
                        position = position
                    )
                }

            } else {
                createDailySchedule(
                    nameEventCreate = etName.text.toString(),
                    dateEventCreate = etData.text.toString(),
                    timeEventCreate = etTime.text.toString()
                )
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteDailySchedule(const: String, position: Int) {

        campDbManager.deleteRawToTableDailySchedule(const)

        adapter.removeDailySchedule(position)

    }

    private fun updateDailySchedule(
        timeEventUpdate: String,
        nameEventUpdate: String,
        dateEventUpdate: String,
        nameEventUpdatePosition: String,
        position: Int
    ) {
        campDbManager.updateRawToTableDailySchedule(
            nameEvent = nameEventUpdate,
            dateEvent = dateEventUpdate,
            timeEvent = timeEventUpdate,
            nameEventUpdatePosition = nameEventUpdatePosition
        )

        val dailyScheduleDataClassUpdate = DailyScheduleDataClass(
            timeEvent = timeEventUpdate,
            nameEvent = nameEventUpdate,
            dateEvent = dateEventUpdate
        )

        adapter.updateDailySchedule(position, dailyScheduleDataClassUpdate)

    }

    private fun createDailySchedule(
        timeEventCreate: String, nameEventCreate: String,
        dateEventCreate: String
    ) {
        campDbManager.insertToTableDailySchedule(
            nameEvent = nameEventCreate,
            dateEvent = dateEventCreate,
            timeEvent = timeEventCreate
        )

        val dailyScheduleDataClassCreate = DailyScheduleDataClass(
            timeEvent = timeEventCreate,
            nameEvent = nameEventCreate,
            dateEvent = dateEventCreate
        )

        adapter.addDailySchedule(dailyScheduleDataClassCreate)

    }

    private fun alert(massage: Int) {
        val builder = AlertDialog.Builder(act)
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