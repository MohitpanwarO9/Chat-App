package com.example.letstalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.letstalk.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mauth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //init auth Firebase
        mauth= FirebaseAuth.getInstance()

        Bt_signout_main.setOnClickListener {
            mauth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()

        if(mauth.currentUser!=null){
            Tv_userEmail_main.text=mauth.currentUser!!.email
            Toast.makeText(this,"Already signed In",Toast.LENGTH_LONG).show()
        }
        else{
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }

}