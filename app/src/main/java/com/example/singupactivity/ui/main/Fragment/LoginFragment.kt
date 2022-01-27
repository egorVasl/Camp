package com.example.singupactivity.ui.main.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Activity.NavigationActivity
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass


class LoginFragment : Fragment() {




    private lateinit var campDbManager: CampDbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = activity?.let { CampDbManager(it) }!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        val btLogin = view.findViewById<Button>(R.id.btLogin)
        val etLogin = view.findViewById<EditText>(R.id.etLogin)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etSquad = view.findViewById<EditText>(R.id.etSquad)
        var loginIsTrue = false
        var passwordIsTrue = false
        var squadIsTrue = false

        btLogin.setOnClickListener {

            if (etLogin.text.isNullOrEmpty() or etPassword.text.isNullOrEmpty() or
                etSquad.text.isNullOrEmpty()
            ) {

                alert(R.string.no_data_massage)

            } else {
                val loginList =
                    campDbManager.selectToTableAuthorization(CampDbNameClass.COLUMN_NAME_LOGIN)
                for ((i, item) in loginList.withIndex()) {
                    if (loginList[i] == etLogin.text.toString()) {
                        loginIsTrue = true
                    }
                }
                val passwordList =
                    campDbManager.selectToTableAuthorization(CampDbNameClass.COLUMN_NAME_PASSWORD)
                for ((i, item) in passwordList.withIndex()) {
                    if (passwordList[i] == etPassword.text.toString()) {
                        passwordIsTrue = true
                    }
                }
                val squadList =
                    campDbManager.selectToTableAuthorization(CampDbNameClass.COLUMN_NAME_SQUAD)
                for ((i, item) in squadList.withIndex()) {
                    if (squadList[i] == etSquad.text.toString()) {
                        squadIsTrue = true
                    }
                }

                if (squadIsTrue and passwordIsTrue and loginIsTrue) {

                    val result = etLogin.text.toString()


                    val intent = Intent(
                        activity?.baseContext,
                        NavigationActivity::class.java
                    )
                    intent.putExtra("KEY", result)
                    startActivity(intent)

                    Toast.makeText(
                        activity,
                        R.string.successful_authorization,
                        Toast.LENGTH_SHORT
                    ).show()

                    etLogin.text.clear()
                    etPassword.text.clear()
                    etSquad.text.clear()
                } else {
                    alert(R.string.unsuccessful_authorization)
                }
            }
        }
        return view
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