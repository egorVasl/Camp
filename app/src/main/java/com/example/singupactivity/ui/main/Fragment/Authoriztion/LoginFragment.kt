package com.example.singupactivity.ui.main.Fragment.Authoriztion

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
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Activity.ConteinerActivityExit
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_LOGIN
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_PASSWORD
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_SQUAD
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Objects.NavigationActviy.ArgumentsNAlogin
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class LoginFragment : Fragment() {


    private val loginService: ExecutorService = Executors.newFixedThreadPool(1)


    private lateinit var campDbManager: CampDbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = activity?.let { CampDbManager(it) }!!

    }

    private fun getData(const: String): ArrayList<String> {
        return campDbManager.selectToTableAuthorization(const)
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
                    runBlocking {
                        async {
                            getData(COLUMN_NAME_LOGIN)
                        }.await()
                    }

                for ((i, _) in loginList.withIndex()) {
                    if (loginList[i] == etLogin.text.toString()) {
                        loginIsTrue = true
                    }
                }
                val passwordList =
                    runBlocking {
                        async {
                            getData(COLUMN_NAME_PASSWORD)

                        }.await()
                }
                for ((i, _) in passwordList.withIndex()) {
                    if (passwordList[i] == etPassword.text.toString()

                    ) {
                        passwordIsTrue = true
                    }
                }
                val squadList =
                    runBlocking {
                    async {
                        getData(COLUMN_NAME_SQUAD)

                    }.await()
                }
                for ((i, _) in squadList.withIndex()) {
                    if (squadList[i] == etSquad.text.toString()) {
                        squadIsTrue = true
                    }
                }

                if (loginIsTrue and passwordIsTrue and squadIsTrue) {

                    ArgumentsNAlogin.login = etLogin.text.toString()

                    val intent = Intent(
                        act,
                        ConteinerActivityExit::class.java
                    )
                    startActivity(intent)

                    Toast.makeText(
                        act,
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