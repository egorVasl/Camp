package com.example.singupactivity.ui.main.Fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.singupactivity.ui.main.Activity.NavigationActivity
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.DataBase.CampDbManager



class SignupFragment : Fragment() {


    val campDbManager = activity?.let { CampDbManager(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_signup, container, false)
        val btSignup = view.findViewById<Button>(R.id.btSignup)
        val etLogin = view.findViewById<EditText>(R.id.etLogin)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etRepeatPassword = view.findViewById<EditText>(R.id.etRepeatPasswors)
        val etSquad = view.findViewById<EditText>(R.id.etSquad)
        var loginIsTrue = false
        var passwordIsTrue = false
        var squadIsTrue = false

        btSignup.setOnClickListener {



            if (etLogin.text.isNullOrEmpty() or
                etPassword.text.isNullOrEmpty() or
                etRepeatPassword.text.isNullOrEmpty() or
                etSquad.text.isNullOrEmpty()
            ) {

                alert(R.string.no_data_massage)

            } else {

                if (etPassword.text.toString() == etRepeatPassword.text.toString()) {

                    val loginList =
                        campDbManager?.selectToTableAuthorization(etLogin.text.toString())
                    if (loginList != null) {
                        for ((i, item) in loginList.withIndex()) {
                            if (loginList[i] == etLogin.text.toString()) {
                                loginIsTrue = true
                            }
                        }
                    }
                    val passwordList =
                        campDbManager?.selectToTableAuthorization(etPassword.text.toString())
                    if (passwordList != null) {
                        for ((i, item) in passwordList.withIndex()) {
                            if (passwordList[i] == etPassword.text.toString()) {
                                passwordIsTrue = true
                            }
                        }
                    }
                    val squadList =
                        campDbManager?.selectToTableAuthorization(etSquad.text.toString())
                    if (squadList != null) {
                        for ((i, item) in squadList.withIndex()) {
                            squadIsTrue = squadList[i] == etSquad.text.toString()
                        }
                    }

                    if (squadIsTrue and passwordIsTrue and loginIsTrue) {

                        alert(R.string.alredy_registered)

                    } else {

                        campDbManager?.insertToTableAuthorization(
                            login = etLogin.text.toString(),
                            password = etPassword.text.toString(),
                            squad = etSquad.text.toString().toInt()
                        )

                        startActivity(Intent(activity, NavigationActivity::class.java))
                        Toast.makeText(
                            activity,
                            R.string.successful_authorization_and_login,
                            Toast.LENGTH_SHORT
                        ).show();


                    }
                } else {

                    alert(R.string.uncorrect_password)

                }
            }

        }
        return view
    }

    fun alert(massage: Int) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(R.string.notification)
            .setMessage(massage)
            .setCancelable(false)
            .setPositiveButton(R.string.close, DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()

            })

        val alert = builder.create()
        alert.show()
    }
}