package com.example.letstalk.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.letstalk.R

class NewMessage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title="select user "
    }
}