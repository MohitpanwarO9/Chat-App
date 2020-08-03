package com.example.letstalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.letstalk.login.LoginActivity
import com.example.letstalk.login.Registration
import kotlinx.android.synthetic.main.activity_checking_verify.*

class CheckingVerify : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checking_verify)


        ifDone.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
        checkEmail.setOnClickListener {
            val intent=Intent(this,Registration::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }
}