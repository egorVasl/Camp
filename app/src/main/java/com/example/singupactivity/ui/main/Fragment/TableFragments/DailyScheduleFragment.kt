package com.example.singupactivity.ui.main.Fragment.TableFragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.text.TextUtils
import com.example.singupactivity.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.*
import com.example.singupactivity.ui.main.Adapter.DailyScheduleAdapter
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_NAME_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME_EVENT
import android.widget.Toast
import com.example.singupactivity.databinding.AddDailyScheduleBinding
import com.example.singupactivity.ui.main.Fragment.*
import com.example.singupactivity.ui.main.Fragment.BottomSheet.RATES_BOTTOM_REQUEST_KEY
import com.example.singupactivity.ui.main.Fragment.BottomSheet.RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentDSDataClass
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import androidx.databinding.DataBindingUtil
import com.example.singupactivity.ui.main.Fragment.BottomSheet.DSBottomSheetDialog
import com.example.singupactivity.ui.main.Notification.AlarmReceiver.Companion.CHANNEL_ID
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import android.app.AlarmManager

import android.app.PendingIntent

import android.content.Context

import android.content.Intent

import com.example.singupactivity.ui.main.Notification.AlarmReceiver
import com.example.singupactivity.ui.main.Notification.AlarmReceiver.Companion.DATE_EVENT_EXTRA
import com.example.singupactivity.ui.main.Notification.AlarmReceiver.Companion.IS_EVENT
import com.example.singupactivity.ui.main.Notification.AlarmReceiver.Companion.NAME_EVENT_EXTRA
import com.example.singupactivity.ui.main.Notification.AlarmReceiver.Companion.NOTIFICATION_ID
import com.example.singupactivity.ui.main.Notification.AlarmReceiver.Companion.TIME_EVENT_EXTRA
import com.example.singupactivity.ui.main.Objects.Notification.ArgumentsNotification


class DailyScheduleFragment : Fragment() {
    lateinit var adapter: DailyScheduleAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var rv: RecyclerView

    lateinit var picker: MaterialTimePicker
    lateinit var calendar: Calendar
    lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY) { _, _ ->
            addAndEditSchedule(false, null, -1)
        }

        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF) { _, _ ->
            if (adapter.dailyScheduleList.isEmpty())
                alert(
                    getString(R.string.no_data_to_save_title),
                    getString(R.string.no_data_to_save)
                )
            else
                importTextFile()
        }

        campDbManager = CampDbManager(act)

        adapter = DailyScheduleAdapter(this@DailyScheduleFragment)
        val eventTimeList =
            runBlocking {
                async {
                    getData(COLUMN_NAME_TIME_EVENT)
                }.await()
            }
        val eventNameList =
            runBlocking {
                async {
                    getData(COLUMN_NAME_NAME_EVENT)
                }.await()
            }

        val eventDateList = runBlocking {
            async {
                getData(COLUMN_NAME_DATE_EVENT)
            }.await()
        }

        for ((i, _) in eventTimeList.withIndex()) {
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

        rv.layoutManager = LinearLayoutManager(act)
        rv.itemAnimator = DefaultItemAnimator()

        fabDailySchedule.setOnClickListener {

            DSBottomSheetDialog.newInstance()
                .show(this.parentFragmentManager, "bottomDialogDS")

        }

        rv.adapter = adapter
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        val response = DailyScheduleDataClass(
            timeEvent = ArgumentDSDataClass.timeEvent,
            nameEvent = ArgumentDSDataClass.nameEvent,
            dateEvent = ArgumentDSDataClass.dateEvent
        )
        val responseUpdate = DailyScheduleDataClass(
            timeEvent = ArgumentDSDataClass.timeEventUpdate,
            nameEvent = ArgumentDSDataClass.nameEventUpdate,
            dateEvent = ArgumentDSDataClass.dateEventUpdate
        )
        if (ArgumentsDSFlag.isUpdate) {
            adapter.let {
                for ((i, _) in it.dailyScheduleList.withIndex()) {
                    if (it.dailyScheduleList[i] == response)
                        it.updateDailySchedule(i, responseUpdate)
                    it.notifyDataSetChanged()
                }
            }
        } else {
            adapter.let {
                for ((i, _) in it.dailyScheduleList.withIndex()) {
                    if (it.dailyScheduleList[i] == response)
                        it.removeDailySchedule(i)
                    it.notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun importTextFile() {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())

        val path = context?.getExternalFilesDir(null)

        val letDirectory = File(path, "Daily schedule")
        letDirectory.mkdirs()
        val file = File(letDirectory, "Daily schedule $currentDate.txt")
        try {
            FileOutputStream(file).use { stream ->
                adapter.let {
                    for ((i, _) in it.dailyScheduleList.withIndex()) {
                        stream.write("????????:${i + 1}\n".toByteArray())
                        stream.write("????????: ${it.dailyScheduleList[i].dateEvent}\n".toByteArray())
                        stream.write("???????????????? ??????????????????????: ${it.dailyScheduleList[i].nameEvent}\n".toByteArray())
                        stream.write("??????????: ${it.dailyScheduleList[i].timeEvent}\n\n".toByteArray())

                    }
                }
                stream.close()
                alert(
                    "???????? ?????????????? ????????????!",
                    "????????: $path/Daily schedule/Daily schedule $currentDate.txt"
                )
            }
        } catch (exe: IOException) {
            Toast.makeText(ctx, "???????????? ???????????????? ??????????: $exe", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("InflateParams")
    fun addAndEditSchedule(
        isUpdate: Boolean,
        dailyScheduleDataClass: DailyScheduleDataClass?,
        position: Int
    ) {
        val binding: AddDailyScheduleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                context
            ), R.layout.add_daily_schedule, null, false
        )
        val etNameUpdate: String? = dailyScheduleDataClass?.nameEvent

        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(act)
        alertDialogBuilderUserInput.setView(binding.root)
        with(binding) {

            imageTime.setOnClickListener {
                showTimePicker(binding)
            }
            tiTimeEditText.setOnClickListener {
                showTimePicker(binding)
            }


            tiDateEditText.setOnClickListener {
                showDatePicker(binding)
            }
            imageDate.setOnClickListener {
                showDatePicker(binding)
            }
            newDayTitle.text = if (!isUpdate) getString(R.string.add) else getString(R.string.edit)

            if (isUpdate && dailyScheduleDataClass != null) {
                tiName.editText?.setText(dailyScheduleDataClass.nameEvent)
//                tiDate.editText?.setText(dailyScheduleDataClass.dateEvent)
//                tiTime.editText?.setText(dailyScheduleDataClass.timeEvent)
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
                        deleteDailySchedule(
                            position = position,
                            const = tiName.editText?.text.toString()
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
                        TextUtils.isEmpty(tiName.editText?.text.toString()) -> {
                            binding.tiName.error = getString(R.string.enter_name_event)
                            binding.tiName.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiDate.editText?.text.toString()) -> {
                            binding.tiDate.error = getString(R.string.enter_data_event)
                            binding.tiDate.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiTime.editText?.text.toString()) -> {
                            binding.tiTime.error = getString(R.string.enter_time_event)
                            binding.tiTime.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        else -> {
                            alertDialog.dismiss()
                        }
                    }
                    if (isUpdate && dailyScheduleDataClass != null) {
                        if (etNameUpdate != null) {
                            updateDailySchedule(
                                nameEventUpdate = tiName.editText?.text.toString(),
                                dateEventUpdate = tiDate.editText?.text.toString(),
                                timeEventUpdate = tiTime.editText?.text.toString(),
                                nameEventUpdatePosition = etNameUpdate,
                                position = position
                            )
                            createNotificationChannel()
                            setNotification(binding)


                        }

                    } else {
                        createDailySchedule(
                            nameEventCreate = tiName.editText?.text.toString(),
                            dateEventCreate = tiDate.editText?.text.toString(),
                            timeEventCreate = tiTime.editText?.text.toString()
                        )
                        createNotificationChannel()
                        setNotification(binding)
                    }
                })
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setNotification(binding: AddDailyScheduleBinding) {
        val intent = Intent(act.applicationContext, AlarmReceiver::class.java)
        with(binding) {
            intent.putExtra(DATE_EVENT_EXTRA, tiDate.editText?.text.toString())
            intent.putExtra(TIME_EVENT_EXTRA, tiTime.editText?.text.toString())
            intent.putExtra(NAME_EVENT_EXTRA, tiName.editText?.text.toString())
            intent.putExtra(IS_EVENT, false)

            val pendingIntent = PendingIntent.getBroadcast(
                act.applicationContext,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                getTime(),
                pendingIntent
            )
        }
    }

    private fun getTime(): Long {
        val minute = picker.minute
        val hour = picker.hour
        val day = datePickerDialog.datePicker.dayOfMonth
        val month = datePickerDialog.datePicker.month
        val year = datePickerDialog.datePicker.year

        val calendarSave = Calendar.getInstance()
        calendarSave.set(year, month, day, hour-1, minute)
        return calendarSave.timeInMillis
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(binding: AddDailyScheduleBinding) {
        calendar = Calendar.getInstance()
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val mMonth = calendar.get(Calendar.MONTH)
        val mYear = calendar.get(Calendar.YEAR)
        datePickerDialog = DatePickerDialog(
            ctx,
            { _, year, month, dayOfMonth ->
                binding.tiDate.editText?.setText(
                    "$dayOfMonth.${String.format("%02d", (month + 1))}.$year"
                )
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calendar.set(Calendar.MONTH, month + 1)
                calendar.set(Calendar.YEAR, year)
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun showTimePicker(binding: AddDailyScheduleBinding) {

        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText(R.string.select_time)
            .build()
        picker.show(act.supportFragmentManager, CHANNEL_ID)
        picker.addOnPositiveButtonClickListener {
            if (picker.hour >= 12) {
                binding.tiTime.editText?.setText(
                    "${
                        String.format(
                            "%02d",
                            (picker.hour)
                        )
                    } : ${String.format("%02d", picker.minute)} ????"
                )
            } else {
                binding.tiTime.editText?.setText(
                    "${
                        String.format(
                            "%02d",
                            (picker.hour)
                        )
                    } : ${String.format("%02d", picker.minute)} ????"
                )
            }


            calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, picker.hour)
            calendar.set(Calendar.MINUTE, picker.minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
    }


    private fun createNotificationChannel() {
        if (SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channelIDRemainderChannel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteDailySchedule(const: String, position: Int) {

        runBlocking {
            async {
                campDbManager.deleteRawToTableDailySchedule(const)
            }.await()
        }


        adapter.removeDailySchedule(position)

    }

    private fun updateDailySchedule(
        timeEventUpdate: String,
        nameEventUpdate: String,
        dateEventUpdate: String,
        nameEventUpdatePosition: String,
        position: Int
    ) {

        runBlocking {
            async {
                campDbManager.updateRawToTableDailySchedule(
                    nameEvent = nameEventUpdate,
                    dateEvent = dateEventUpdate,
                    timeEvent = timeEventUpdate,
                    nameEventUpdatePosition = nameEventUpdatePosition
                )
            }.await()
        }


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
        runBlocking {
            async {
                campDbManager.insertToTableDailySchedule(
                    nameEvent = nameEventCreate,
                    dateEvent = dateEventCreate,
                    timeEvent = timeEventCreate
                )
            }.await()
        }


        val dailyScheduleDataClassCreate = DailyScheduleDataClass(
            timeEvent = timeEventCreate,
            nameEvent = nameEventCreate,
            dateEvent = dateEventCreate
        )

        adapter.addDailySchedule(dailyScheduleDataClassCreate)

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

    private fun getData(const: String): ArrayList<String> {
        return campDbManager.selectToTableDailySchedule(
            const
        )
    }
}