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
import com.example.singupactivity.databinding.AddEditSquadsBinding
import com.example.singupactivity.ui.main.Adapter.SearchAdapters.SearchSquadsAdapter
import com.example.singupactivity.ui.main.Data.SquadsDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_NUMBER
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.Arguments
import com.example.singupactivity.ui.main.Objects.Squads.ArgumentsSquadsDataClass
import com.example.singupactivity.ui.main.Objects.Squads.ArgumentsSquadsFlag
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class SearchSquadsFragment : Fragment() {

    lateinit var adapter: SearchSquadsAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var rv: RecyclerView
    lateinit var etCardSearch: EditText


    companion object {
        @JvmStatic
        fun newInstance() =
            SearchSquadsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = CampDbManager(act)
        adapter = SearchSquadsAdapter(this@SearchSquadsFragment)


    }

    private fun getData(const: String, searchText: String): ArrayList<String> {
        return campDbManager.selectToTableSquad(
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
                    adapter.squadsList.clear()
                    val squadNameList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_SQUAD_NAME, searchText)
                            }.await()
                        }

                    val squadNumberList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_SQUAD_NUMBER, searchText)
                            }.await()
                        }


                    for ((i, _) in squadNameList.withIndex()) {
                        adapter.addSquads(
                            SquadsDataClass(
                                squadName = squadNameList[i],
                                squadNumber = squadNumberList[i]
                            )
                        )

                    }
                    rv.adapter = adapter
                    if (adapter.squadsList.isEmpty()) {
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
    fun addAndEditSquads(
        isUpdate: Boolean,
        squadsDataClass: SquadsDataClass?,
        position: Int
    ) {
        val binding: AddEditSquadsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                context
            ), R.layout.add_edit_squads, null, false
        )
        val etNameUpdate: String? = squadsDataClass?.squadName

        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(act)
        alertDialogBuilderUserInput.setView(binding.root)

        with(binding) {

            newSquadsTitle.text =
                if (!isUpdate) getString(R.string.add) else getString(R.string.edit)

            if (isUpdate && squadsDataClass != null) {
                tiNumberSquads.editText?.setText(squadsDataClass.squadNumber)
                tiNameSquads.editText?.setText(squadsDataClass.squadName)
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
                        ArgumentsSquadsFlag.isUpdate = false
                        ArgumentsSquadsDataClass.squadName = adapter.squadsList[position].squadName
                        ArgumentsSquadsDataClass.squadNumber =
                            adapter.squadsList[position].squadNumber
                        deleteSquads(
                            position = position,
                            const = tiNameSquads.editText?.text.toString()
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
                        TextUtils.isEmpty(tiNumberSquads.editText?.text.toString()) -> {
                            binding.tiNumberSquads.error = getString(R.string.enter_number_squads)
                            binding.tiNumberSquads.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiNameSquads.editText?.text.toString()) -> {
                            binding.tiNameSquads.error = getString(R.string.enter_name_squads)
                            binding.tiNameSquads.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        else -> {
                            alertDialog.dismiss()
                        }
                    }
                    if (isUpdate && squadsDataClass != null) {
                        if (etNameUpdate != null) {
                            ArgumentsSquadsFlag.isUpdate = true
                            ArgumentsSquadsDataClass.squadName =
                                adapter.squadsList[position].squadName
                            ArgumentsSquadsDataClass.squadNumber =
                                adapter.squadsList[position].squadNumber
                            ArgumentsSquadsDataClass.squadNameUpdate =
                                tiNameSquads.editText?.text.toString()
                            ArgumentsSquadsDataClass.squadNumberUpdate =
                                tiNumberSquads.editText?.text.toString()
                            updateSquads(
                                squadsNameUpdate = tiNameSquads.editText?.text.toString(),
                                squadsNumberUpdate = tiNumberSquads.editText?.text.toString(),
                                squadsNameUpdatePosition = etNameUpdate,
                                position = position
                            )
                        }
                    }
                })

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteSquads(const: String, position: Int) {

        runBlocking {
            async {

                campDbManager.deleteRawToTableSquads(const)

            }.await()
        }
        adapter.removeSquads(position)

    }

    private fun updateSquads(
        squadsNameUpdate: String,
        squadsNumberUpdate: String,
        squadsNameUpdatePosition: String,
        position: Int
    ) {
        runBlocking {
            async {
                campDbManager.updateRawToTableSquads(
                    squadName = squadsNameUpdate,
                    squadNumber = squadsNumberUpdate,
                    squadsNameUpdatePosition = squadsNameUpdatePosition
                )
            }.await()
        }


        val squadsDataClass = SquadsDataClass(
            squadName = squadsNameUpdate,
            squadNumber = squadsNumberUpdate
        )

        adapter.updateSquads(position, squadsDataClass)

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