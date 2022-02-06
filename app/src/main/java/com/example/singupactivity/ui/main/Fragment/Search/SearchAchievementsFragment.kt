package com.example.singupactivity.ui.main.Fragment.Search

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.AddEditAchievementsBinding
import com.example.singupactivity.ui.main.Adapter.SearchAdapters.SearchAchievementsAdapter
import com.example.singupactivity.ui.main.Data.AchievementsDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_ACHIEVEMENTS_PLACE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_EVENT_ACHIEVEMENTS
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_ACHIEVEMENTS
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.Achievements.ArgumentsAchievementsDataClass
import com.example.singupactivity.ui.main.Objects.Achievements.ArgumentsAchievementsFlag
import com.example.singupactivity.ui.main.Objects.Arguments
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class SearchAchievementsFragment : Fragment() {

    lateinit var adapter: SearchAchievementsAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var rv: RecyclerView
    lateinit var etCardSearch: EditText

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchAchievementsFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = CampDbManager(act)
        adapter = SearchAchievementsAdapter(this@SearchAchievementsFragment)
    }


    private fun getData(const: String, searchText: String): ArrayList<String> {
        return campDbManager.selectToTableAchievements(
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
                    adapter.achievementsList.clear()
                    val squadNameAchievementsList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_SQUAD_ACHIEVEMENTS, searchText)
                            }.await()
                        }

                    val placeList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_ACHIEVEMENTS_PLACE, searchText)
                            }.await()
                        }

                    val eventNameList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_EVENT_ACHIEVEMENTS, searchText)
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
                    rv.adapter = adapter
                    if (adapter.achievementsList.isEmpty()) {
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
                        ArgumentsAchievementsFlag.isUpdate = false
                        ArgumentsAchievementsDataClass.eventName =
                            adapter.achievementsList[position].eventName
                        ArgumentsAchievementsDataClass.place =
                            adapter.achievementsList[position].place
                        ArgumentsAchievementsDataClass.squadName =
                            adapter.achievementsList[position].squadName
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
                            ArgumentsAchievementsFlag.isUpdate = true
                            ArgumentsAchievementsDataClass.eventName =
                                adapter.achievementsList[position].eventName
                            ArgumentsAchievementsDataClass.place =
                                adapter.achievementsList[position].place
                            ArgumentsAchievementsDataClass.squadName =
                                adapter.achievementsList[position].squadName
                            ArgumentsAchievementsDataClass.eventNameUpdate =
                                tiAchievementEvent.editText?.text.toString()
                            ArgumentsAchievementsDataClass.placeUpdate =
                                tiAchievementPlace.editText?.text.toString()
                            ArgumentsAchievementsDataClass.squadNameUpdate =
                                tiAchievementSquad.editText?.text.toString()
                            updateAchievements(
                                squadNameUpdate = tiAchievementSquad.editText?.text.toString(),
                                placeUpdate = tiAchievementPlace.editText?.text.toString(),
                                eventNameUpdate = tiAchievementEvent.editText?.text.toString(),
                                placeUpdatePosition = etNameUpdate,
                                position = position
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