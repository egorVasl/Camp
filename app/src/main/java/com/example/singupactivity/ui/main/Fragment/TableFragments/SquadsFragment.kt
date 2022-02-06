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
import com.example.singupactivity.databinding.AddEditSquadsBinding
import com.example.singupactivity.ui.main.Adapter.SquadsAdapter
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.Data.SquadsDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD_NUMBER
import com.example.singupactivity.ui.main.Fragment.BottomSheet.*
import com.example.singupactivity.ui.main.Fragment.BottomSheet.RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentDSDataClass
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag
import com.example.singupactivity.ui.main.Objects.Squads.ArgumentsSquadsDataClass
import com.example.singupactivity.ui.main.Objects.Squads.ArgumentsSquadsFlag
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class SquadsFragment : Fragment() {

    lateinit var adapter: SquadsAdapter
    lateinit var campDbManager: CampDbManager
    lateinit var rv: RecyclerView


    private fun getData(const: String): ArrayList<String> {
        return campDbManager.selectToTableSquad(
            const
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_SQUADS) { _, _ ->
            addAndEditSquads(false, null, -1)
        }

        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_SQUADS) { _, _ ->
            if (adapter.squadsList.isEmpty())
                alert(
                    getString(R.string.no_data_to_save_title),
                    getString(R.string.no_data_to_save)
                )
            else
                importTextFile()
        }

        campDbManager = activity?.let { CampDbManager(it) }!!
        adapter = SquadsAdapter(this@SquadsFragment)
        campDbManager.openDb()
        val squadNameList =
            runBlocking {
                async {
                    getData(COLUMN_NAME_SQUAD_NAME)
                }.await()
            }

        val squadNumberList =
            runBlocking {
                async {
                    getData(COLUMN_NAME_SQUAD_NUMBER)
                }.await()
            }
        for ((i, elm) in squadNameList.withIndex()) {
            adapter.addSquads(
                SquadsDataClass(
                    squadName = squadNameList[i],
                    squadNumber =  squadNumberList[i]
                )
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        val response = SquadsDataClass(
            squadName = ArgumentsSquadsDataClass.squadName,
            squadNumber = ArgumentsSquadsDataClass.squadNumber
        )
        val responseUpdate = SquadsDataClass(
            squadName = ArgumentsSquadsDataClass.squadNameUpdate,
            squadNumber = ArgumentsSquadsDataClass.squadNumberUpdate
        )
        if (ArgumentsSquadsFlag.isUpdate) {
            adapter.let {
                for ((i, _) in it.squadsList.withIndex()) {
                    if (it.squadsList[i] == response)
                        it.updateSquads(i, responseUpdate)
                    it.notifyDataSetChanged()
                }
            }
        } else {
            adapter.let {
                for ((i, _) in it.squadsList.withIndex()) {
                    if (it.squadsList[i] == response)
                        it.removeSquads(i)
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

        val letDirectory = File(path, "Squads")
        letDirectory.mkdirs()
        val file = File(letDirectory, "Squads $currentDate.txt")
        try {
            FileOutputStream(file).use { stream ->
                adapter.let {
                    for ((i, _) in it.squadsList.withIndex()) {
                        stream.write("Отряд:${i + 1}\n".toByteArray())
                        stream.write("Номер: ${it.squadsList[i].squadNumber}\n".toByteArray())
                        stream.write("Название отряда: ${it.squadsList[i].squadName}\n".toByteArray())

                    }
                }
                stream.close()
                alert(
                    "Файл успешно создан!",
                    "Путь: $path/Squads/Squads $currentDate.txt"
                )
            }
        } catch (exe: IOException) {
            Toast.makeText(ctx, "Ошибка создания файла: $exe", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_squads, container, false)
        rv = view.findViewById(R.id.rcSquads)
        val fabSquads = view.findViewById<FloatingActionButton>(R.id.fabSquads)

        rv.layoutManager = LinearLayoutManager(act)
        rv.itemAnimator = DefaultItemAnimator()

        fabSquads.setOnClickListener {
            SquadsBottomSheetDialog.newInstance()
                .show(this.parentFragmentManager, "bottomDialogSquads")
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
                            updateSquads(
                                squadsNameUpdate = tiNameSquads.editText?.text.toString(),
                                squadsNumberUpdate = tiNumberSquads.editText?.text.toString(),
                                squadsNameUpdatePosition =  etNameUpdate,
                                position = position
                            )
                        }

                    } else {
                        createSquads(
                            squadsName = tiNameSquads.editText?.text.toString(),
                            squadsNumber = tiNumberSquads.editText?.text.toString()
                        )
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

        adapter.updateSquads(position,squadsDataClass)

    }

    private fun createSquads(
        squadsName: String,
        squadsNumber: String
    ) {
        runBlocking {
            async {
                campDbManager.insertToTableSquad(
                    squadName = squadsName,
                    squadNumber = squadsNumber
                )
            }.await()
        }

        val squadsDataClass = SquadsDataClass(
            squadName = squadsName,
            squadNumber = squadsNumber
        )


        adapter.addSquads(squadsDataClass)

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
    override fun onDestroy() {
        campDbManager.closeDb()
        super.onDestroy()
    }

}