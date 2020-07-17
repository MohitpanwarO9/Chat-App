package com.example.letstalk.login

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.letstalk.MainActivity
import com.example.letstalk.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*
import kotlin.math.log

class Registration : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        //init auth Firebase

        //create user account
        register_button.setOnClickListener {
            performRegister()
        }
        //switch to login page
        Already_have.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        Bt_selectImage_regist.setOnClickListener {
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== Activity.RESULT_OK && data!=null){
            Log.d("select","photo selected")
            val uri=data.data
            val bitmap=MediaStore.Images.Media.getBitmap(contentResolver,uri)
            val bitmapDrawable=BitmapDrawable(bitmap)
            Bt_selectImage_regist.setBackgroundDrawable(bitmapDrawable)
        }else{
            Log.d("select","photo not selected")
        }
    }

        private fun performRegister(){

            val emailUser = Ed_Email_regist.text.toString()
            val passwordUser = Ed_Password_regist.text.toString()

                    if(emailUser.isEmpty()||passwordUser.isEmpty()){
                        Toast.makeText(this, "email or password is empty", Toast.LENGTH_SHORT).show()
                    }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailUser, passwordUser)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        val user = FirebaseAuth.getInstance().currentUser
                        val intent=Intent(this,MainActivity::class.java)
                        intent.putExtra("user",user)
                        startActivity(intent)
                    }
                    else
                    {
                        Toast.makeText(this, "Fail to login", Toast.LENGTH_LONG).show()
                         return@addOnCompleteListener
                    }
                }
        }


}


