package com.example.letstalk.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.letstalk.MainActivity
import com.example.letstalk.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //login button
        Bt_login.setOnClickListener {
            performLogin()
        }
        //back to registration page
        back_register_editText.setOnClickListener{
            startActivity(Intent(this, Registration::class.java))
            finish()
        }
    }



    private fun performLogin(){
        val emailLogin=Ed_userEmail_login.text.toString()
        val passwordLogin=Ed_userPassword_login.text.toString()

        if(emailLogin.isEmpty() || passwordLogin.isEmpty()){
            Toast.makeText(this, "Email or Password can't be empty", Toast.LENGTH_SHORT).show()
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailLogin,passwordLogin)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val user = FirebaseAuth.getInstance().currentUser
                    val intent=Intent(this, MainActivity::class.java)
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