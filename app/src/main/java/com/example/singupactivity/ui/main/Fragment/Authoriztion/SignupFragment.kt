package com.example.singupactivity.ui.main.Fragment.Authoriztion

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Activity.ConteinerActivityExit
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.NavigationActviy.ArgumentsNAlogin
import java.io.ByteArrayOutputStream


class SignupFragment : Fragment() {


    private lateinit var campDbManager: CampDbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = activity?.let { CampDbManager(it) }!!
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
                        campDbManager.selectToTableAuthorization(CampDbNameClass.COLUMN_NAME_LOGIN)
                    for ((i, item) in loginList.withIndex()) {
                        if (loginList[i] == etLogin.text.toString()) {
                            loginIsTrue = true
                        }
                    }
//                    val passwordList =
//                        campDbManager.selectToTableAuthorization(CampDbNameClass.COLUMN_NAME_PASSWORD)
//                    for ((i, item) in passwordList.withIndex()) {
//                        if (passwordList[i] == etPassword.text.toString()) {
//                            passwordIsTrue = true
//                        }
//                    }
                    val squadList =
                        campDbManager.selectToTableAuthorization(CampDbNameClass.COLUMN_NAME_SQUAD)
                    for ((i, item) in squadList.withIndex()) {
                        if (squadList[i] == etSquad.text.toString()) {
                            squadIsTrue = true
                        }
                    }
                        if (loginIsTrue && squadIsTrue ) {

                            alert(R.string.alredy_registered)

                        } else {
                            ArgumentsNAlogin.login = etLogin.text.toString()
                            val stream = ByteArrayOutputStream()
                            getBitmapFromVectorDrawable(ctx,R.drawable.ic_baseline_person_24)?.compress(Bitmap.CompressFormat.PNG, 0, stream)
                            val byteArray = stream.toByteArray()
                            campDbManager.insertToTableAvatar(byteArray, etLogin.text.toString())

                            campDbManager.insertToTableAuthorization(
                                login = etLogin.text.toString(),
                                password = etPassword.text.toString(),
                                squad = etSquad.text.toString()
                            )


                            startActivity(Intent(activity, ConteinerActivityExit::class.java))
                            Toast.makeText(
                                activity,
                                R.string.successful_authorization_and_login,
                                Toast.LENGTH_SHORT
                            ).show()
                            etLogin.text.clear()
                            etPassword.text.clear()
                            etSquad.text.clear()
                            etRepeatPassword.text.clear()
                        }
                } else {

                    alert(R.string.uncorrect_password)

                }
            }

        }
        return view
    }
   private fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap? {
        var drawable = context?.let { ContextCompat.getDrawable(it, drawableId) }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
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