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
import com.example.singupactivity.ui.main.Adapter.SquadsAdapter
import com.example.singupactivity.ui.main.Data.SquadsDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SquadsFragment : Fragment() {

    lateinit var adapter: SquadsAdapter
    lateinit var campDbManager: CampDbManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        campDbManager = activity?.let { CampDbManager(it) }!!
        adapter = SquadsAdapter(this@SquadsFragment)
        campDbManager.openDb()
        val squadNameList = campDbManager.selectToTableSquad(CampDbNameClass.COLUMN_NAME_SQUAD_NAME)
        val squadNumberList =
            campDbManager.selectToTableSquad(CampDbNameClass.COLUMN_NAME_SQUAD_NUMBER)
        for ((i, elm) in squadNameList.withIndex()) {
            adapter.addSquads(
                SquadsDataClass(
                    squadName = squadNameList[i],
                    squadNumber =  squadNumberList[i]
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_squads, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rcSquads)
        val fabSquads = view.findViewById<FloatingActionButton>(R.id.fabSquads)

        rv.layoutManager = GridLayoutManager(activity, 3)
        rv.itemAnimator = DefaultItemAnimator()

        fabSquads.setOnClickListener {
            addAndEditSquads(false, null, -1)
        }

        rv.adapter = adapter

        return view
    }

    @SuppressLint("InflateParams")
    fun addAndEditSquads(
        isUpdate: Boolean,
        squadsDataClass: SquadsDataClass?,
        position: Int
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.add_edit_squads, null)


        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(requireActivity())
        alertDialogBuilderUserInput.setView(view)

        val newSquadsTitle = view.findViewById<TextView>(R.id.newSquadsTitle)
        val etSquadsName = view.findViewById<EditText>(R.id.etSquadsName)
        val etSquadsNumber = view.findViewById<EditText>(R.id.etSquadsNumber)
        val etNameUpdate: String? = squadsDataClass?.squadName

        newSquadsTitle.text = if (!isUpdate) "Добавить" else "Редактировать"

        if (isUpdate && squadsDataClass != null) {
            etSquadsName.setText(squadsDataClass.squadName)
            etSquadsNumber.setText(squadsDataClass.squadNumber)
        }
        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(if (isUpdate) "Обновить" else "Сохранить",
                DialogInterface.OnClickListener { dialogBox, id -> })
            .setNegativeButton(if (isUpdate) "Удалить" else "Закрыть",
                DialogInterface.OnClickListener { dialogBox, id ->
                    if (isUpdate) {
                        deleteSquads(
                            position = position,
                            const = etSquadsName.text.toString()
                        )
                    } else {
                        dialogBox.cancel()
                    }
                })

        val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            when {
                TextUtils.isEmpty(etSquadsName.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                TextUtils.isEmpty(etSquadsNumber.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                else -> {
                    alertDialog.dismiss()
                }
            }
            if (isUpdate && squadsDataClass != null) {
                if (etNameUpdate != null) {
                    updateSquads(
                        squadsNameUpdate = etSquadsName.text.toString(),
                        squadsNumberUpdate = etSquadsNumber.text.toString(),
                        squadsNameUpdatePosition =  etNameUpdate,
                        position = position
                    )
                }

            } else {
                createSquads(
                    squadsName = etSquadsName.text.toString(),
                    squadsNumber = etSquadsNumber.text.toString()
                )
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteSquads(const: String, position: Int) {

        campDbManager.deleteRawToTableSquads(const)

        adapter.removeSquads(position)

    }

    private fun updateSquads(
        squadsNameUpdate: String,
        squadsNumberUpdate: String,
        squadsNameUpdatePosition: String,
        position: Int
    ) {
        campDbManager.updateRawToTableSquads(
            squadName = squadsNameUpdate,
            squadNumber = squadsNumberUpdate,
            squadsNameUpdatePosition = squadsNameUpdatePosition
        )

        val squadsDataClass = SquadsDataClass(
            squadName = squadsNameUpdate,
            squadNumber = squadsNumberUpdate
        )

        adapter.updateSquads(position,squadsDataClass)

    }

    private fun createSquads(
        squadsName: String,
        squadsNumber: String
    ) {
        campDbManager.insertToTableSquad(
            squadName = squadsName,
            squadNumber = squadsNumber
        )

        val squadsDataClass = SquadsDataClass(
            squadName = squadsName,
            squadNumber = squadsNumber
        )


        adapter.addSquads(squadsDataClass)

    }
    override fun onDestroy() {
        campDbManager.closeDb()
        super.onDestroy()
    }

}