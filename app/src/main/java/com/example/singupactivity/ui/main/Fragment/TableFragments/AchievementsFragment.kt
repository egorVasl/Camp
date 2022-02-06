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
import com.example.singupactivity.databinding.AddEditAchievementsBinding
import com.example.singupactivity.ui.main.Adapter.AchievementsAdapter
import com.example.singupactivity.ui.main.Data.AchievementsDataClass
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_ACHIEVEMENTS_PLACE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_EVENT_ACHIEVEMENTS
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_EVENT_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_NAME_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_ACHIEVEMENTS
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_NUMBER
import com.example.singupactivity.ui.main.Fragment.BottomSheet.*
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.Achievements.ArgumentsAchievementsDataClass
import com.example.singupactivity.ui.main.Objects.Achievements.ArgumentsAchievementsFlag
import com.example.singupactivity.ui.main.Objects.Arguments
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentDSDataClass
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class AchievementsFragment : Fragment() {

    lateinit var adapter: AchievementsAdapter
    lateinit var campDbManager: CampDbManager
    lateinit var rv: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = CampDbManager(act)
        adapter = AchievementsAdapter(this@AchievementsFragment)
        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_ACHIEVEMENTS) { _, _ ->
            addAndEditAchievements(false, null, -1)
        }

        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_ACHIEVEMENTS) { _, _ ->
            if (adapter.achievementsList.isEmpty())
                alert(
                    getString(R.string.no_data_to_save_title),
                    getString(R.string.no_data_to_save)
                )
            else
                importTextFile()
        }

        val squadNameAchievementsList =
            runBlocking {
                async {
                    getData(COLUMN_NAME_SQUAD_ACHIEVEMENTS)
                }.await()
            }

        val placeList =
            runBlocking {
                async {
                    getData(COLUMN_NAME_ACHIEVEMENTS_PLACE)
                }.await()
            }

        val eventNameList =
            runBlocking {
                async {
                    getData(COLUMN_NAME_EVENT_ACHIEVEMENTS)
                }.await()
            }
        for ((i, _) in squadNameAchievementsList.withIndex()) {
            adapter.addAchievements(
                AchievementsDataClass(
                    squadName = squadNameAchievementsList[i],
                    place = placeList[i],
                    eventName = eventNameList[i]
                )
            )

        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun importTextFile() {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())

        val path = context?.getExternalFilesDir(null)

        val letDirectory = File(path, "Achievements")
        letDirectory.mkdirs()
        val file = File(letDirectory, "Achievements $currentDate.txt")
        try {
            FileOutputStream(file).use { stream ->
                adapter.let {
                    for ((i, _) in it.achievementsList.withIndex()) {
                        stream.write("Награда:${i + 1}\n".toByteArray())
                        stream.write("Мероприятие: ${it.achievementsList[i].eventName}\n".toByteArray())
                        stream.write("Занфтое место: ${it.achievementsList[i].place}\n".toByteArray())
                        stream.write("Название отряда: ${it.achievementsList[i].squadName}\n\n".toByteArray())

                    }
                }
                stream.close()
                alert(
                    "Файл успешно создан!",
                    "Путь: $path/Achievements/Achievements $currentDate.txt"
                )
            }
        } catch (exe: IOException) {
            Toast.makeText(ctx, "Ошибка создания файла: $exe", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        val response = AchievementsDataClass(
            eventName = ArgumentsAchievementsDataClass.eventName,
            place = ArgumentsAchievementsDataClass.place,
            squadName = ArgumentsAchievementsDataClass.squadName
        )
        val responseUpdate = AchievementsDataClass(
            eventName = ArgumentsAchievementsDataClass.eventNameUpdate,
            place = ArgumentsAchievementsDataClass.placeUpdate,
            squadName = ArgumentsAchievementsDataClass.squadNameUpdate
        )
        if (ArgumentsAchievementsFlag.isUpdate) {
            adapter.let {
                for ((i, _) in it.achievementsList.withIndex()) {
                    if (it.achievementsList[i] == response)
                        it.updateAchievements(i, responseUpdate)
                    it.notifyDataSetChanged()
                }
            }
        } else {
            adapter.let {
                for ((i, _) in it.achievementsList.withIndex()) {
                    if (it.achievementsList[i] == response)
                        it.removeAchievements(i)
                    it.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_achievements, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rcAchievements)
        val fabAchievements = view.findViewById<FloatingActionButton>(R.id.fabAchievements)

        rv.layoutManager = LinearLayoutManager(act)
        rv.itemAnimator = DefaultItemAnimator()

        fabAchievements.setOnClickListener {
            AchievementsBottomSheetDialog.newInstance()
                .show(this.parentFragmentManager, "bottomDialogAchievements")
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
        val binding: AddEditAchievementsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                context
            ), R.layout.add_edit_achievements, null, false
        )
        val etNameUpdate: String? = achievementsDataClass?.place

        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(act)
        alertDialogBuilderUserInput.setView(binding.root)

        with(binding) {

            newAchievementTitle.text =
                if (!isUpdate) getString(R.string.add) else getString(R.string.edit)

            if (isUpdate && achievementsDataClass != null) {
                tiAchievementEvent.editText?.setText(achievementsDataClass.eventName)
                tiAchievementPlace.editText?.setText(achievementsDataClass.place)
                tiAchievementSquad.editText?.setText(achievementsDataClass.squadName)

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
                        deleteAchievements(
                            position = position,
                            const = tiAchievementPlace.editText?.text.toString()
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
                        TextUtils.isEmpty(tiAchievementEvent.editText?.text.toString()) -> {
                            binding.tiAchievementEvent.error =
                                getString(R.string.enter_achievemenets_name_event)
                            binding.tiAchievementEvent.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiAchievementPlace.editText?.text.toString()) -> {
                            binding.tiAchievementPlace.error =
                                getString(R.string.enter_achievemenets_plase)
                            binding.tiAchievementPlace.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiAchievementSquad.editText?.text.toString()) -> {
                            binding.tiAchievementSquad.error = getString(R.string.enter_name_squads)
                            binding.tiAchievementSquad.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        else -> {
                            alertDialog.dismiss()
                        }
                    }
                    if (isUpdate && achievementsDataClass != null) {
                        if (etNameUpdate != null) {

                            updateAchievements(
                                squadNameUpdate = tiAchievementSquad.editText?.text.toString(),
                                placeUpdate = tiAchievementPlace.editText?.text.toString(),
                                eventNameUpdate = tiAchievementEvent.editText?.text.toString(),
                                placeUpdatePosition = etNameUpdate,
                                position = position
                            )
                        }
                    } else {
                        val squadNameList =
                            runBlocking {
                                async {
                                    getDataSquads(COLUMN_NAME_SQUAD_NAME,tiAchievementSquad.editText?.text.toString() )
                                }.await()
                            }

                        val eventNameList =
                            runBlocking {
                                async {
                                    getDataEvents(COLUMN_NAME_EVENT_NAME, tiAchievementEvent.editText?.text.toString())
                                }.await()
                            }
                        if ( eventNameList.isNotEmpty() && squadNameList.isNotEmpty()) {
                            if (eventNameList.isNotEmpty()) {
                                if (squadNameList.isNotEmpty()) {
                                    createAchievements(
                                        squadName = tiAchievementSquad.editText?.text.toString(),
                                        place = tiAchievementPlace.editText?.text.toString(),
                                        eventName = tiAchievementEvent.editText?.text.toString()
                                    )
                                } else {

                                    alert(
                                        getString(R.string.notification),
                                        getString(R.string.no_squad)
                                    )
                                }
                            } else {
                                alert(
                                    getString(R.string.notification),
                                    getString(R.string.no_event)
                                )
                            }
                        } else {
                            alert(
                                getString(R.string.notification),
                                getString(R.string.no_dat)
                            )
                        }
                    }
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteAchievements(const: String, position: Int) {

        runBlocking {
            async {
                campDbManager.deleteRawToTableAchievements(const)
            }.await()
        }

        adapter.removeAchievements(position)

    }

    private fun updateAchievements(
        squadNameUpdate: String,
        placeUpdate: String,
        eventNameUpdate: String,
        placeUpdatePosition: String,
        position: Int
    ) {

        runBlocking {
            async {
                campDbManager.updateRawToTableAchievements(
                    place = placeUpdate,
                    achievementsEvent = eventNameUpdate,
                    achievementsSquad = squadNameUpdate,
                    placeUpdatePosition = placeUpdatePosition
                )
            }.await()
        }


        val achievementsDataClass = AchievementsDataClass(
            squadName = squadNameUpdate,
            place = placeUpdate,
            eventName = eventNameUpdate
        )

        adapter.updateAchievements(position, achievementsDataClass)

    }

    private fun createAchievements(
        squadName: String,
        place: String,
        eventName: String
    ) {

        runBlocking {
            async {
                campDbManager.insertToTableAchievements(
                    achievementsPlace = place,
                    achievementsEvent = eventName,
                    achievementsSquad = squadName
                )

            }.await()
        }

        val achievementsDataClass = AchievementsDataClass(
            squadName = squadName,
            place = place,
            eventName = eventName
        )

        adapter.addAchievements(achievementsDataClass)

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
        return campDbManager.selectToTableAchievements(
            const
        )
    }


    private fun getDataEvents(const: String, searchText: String): ArrayList<String> {
        return  campDbManager.selectToTableWeekEvent(
            const,
            searchText,
            COLUMN_NAME_EVENT_NAME
        )
    }

    private fun getDataSquads(const: String, searchText: String): ArrayList<String> {
        return campDbManager.selectToTableSquad(
            const,
            searchText,
            COLUMN_NAME_SQUAD_NAME
        )
    }
}
