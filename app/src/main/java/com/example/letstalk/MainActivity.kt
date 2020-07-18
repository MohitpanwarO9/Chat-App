package com.example.letstalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.letstalk.login.LoginActivity
import com.example.letstalk.messages.NewMessage
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.new_messages->{
                startActivity(Intent(this,NewMessage::class.java))
            }
            R.id.signout->{
                mauth.signOut()
                val intent=Intent(this,LoginActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_manu,menu)
        return super.onCreateOptionsMenu(menu)
    }

}