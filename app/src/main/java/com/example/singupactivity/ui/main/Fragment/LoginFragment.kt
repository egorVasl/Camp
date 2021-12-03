package com.example.singupactivity.ui.main.Fragment

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.singupactivity.ui.main.Activity.NavigationActivity
import com.example.singupactivity.ui.main.DataBase.CampDbManager


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LoginFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val campDbManager = activity?.let { CampDbManager(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        val view:View = inflater.inflate(R.layout.fragment_login, container, false)
        val btLogin = view.findViewById<Button>(R.id.btLogin)
        val etLogin = view.findViewById<EditText>(R.id.etLogin)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etSquad = view.findViewById<EditText>(R.id.etSquad)
        var loginIsTrue = false
        var passwordIsTrue = false
        var squadIsTrue = false

       btLogin.setOnClickListener{

//           campDbManager?.insertToTableAuthorization(etLogin.text.toString(),
//               etPassword.text.toString(),etSquad.text.toString().toInt())
           if (etLogin.text.isNullOrEmpty() or etPassword.text.isNullOrEmpty() or
               etSquad.text.isNullOrEmpty()){

               alert(R.string.no_data_massage)

           } else {
              val loginList = campDbManager?.selectToTableAuthorization(etLogin.text.toString())
               if (loginList != null) {
                   for ((i, item) in loginList.withIndex()){
                       if(loginList[i]  == etLogin.text.toString()){
                           loginIsTrue = true
                       }
                   }
               }
               val passwordList = campDbManager?.selectToTableAuthorization(etPassword.text.toString())
               if (passwordList != null) {
                   for ((i, item) in passwordList.withIndex()){
                       if(passwordList[i]  == etPassword.text.toString()){
                           passwordIsTrue = true
                       }
                   }
               }
               val squadList = campDbManager?.selectToTableAuthorization(etSquad.text.toString())
               if (squadList != null) {
                   for ((i, item) in squadList.withIndex()){
                       squadIsTrue = squadList[i]  == etSquad.text.toString()
                   }
               }

               if (squadIsTrue and passwordIsTrue and loginIsTrue) {

                   startActivity(Intent(activity, NavigationActivity::class.java))
                   Toast.makeText(activity, R.string.successful_authorization, Toast.LENGTH_SHORT).show();

               } else {
                   alert(R.string.unsuccessful_authorization)
               }
           }

        }
        return view
    }

    fun alert(massage: Int){
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(R.string.notification)
            .setMessage(massage)
            .setCancelable(false)
            .setPositiveButton(R.string.close, DialogInterface.OnClickListener {
                    dialog, id ->
                dialog.dismiss()

            })

        val alert = builder.create()
        alert.show()
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}