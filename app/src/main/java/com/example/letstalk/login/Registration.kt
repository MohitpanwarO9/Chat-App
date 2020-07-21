package com.example.letstalk.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.letstalk.MainActivity
import com.example.letstalk.R
import com.example.letstalk.modelUser.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class Registration : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        

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

     private var selectedImgUri:Uri?=null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== Activity.RESULT_OK && data!=null){
            Log.d("RegisterActivity","photo selected")
            selectedImgUri=data.data
            val bitmap=MediaStore.Images.Media.getBitmap(contentResolver,selectedImgUri)
             Bt_selectImage_regist.alpha=0f
            selected_img_regist.setImageBitmap(bitmap)

        }else{
            Log.d("RegisterActivity","photo not selected")
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

                        Log.d("RegisterActivity","save image to firebase storage")
                        uploadImageToFirebase()

                        startActivity(Intent(this,MainActivity::class.java))
                    }
                    else
                    {
                        Toast.makeText(this, "Fail to Register", Toast.LENGTH_LONG).show()
                         return@addOnCompleteListener
                    }
                }
        }


        private fun uploadImageToFirebase(){
            if(selectedImgUri==null) return

            val filename=UUID.randomUUID().toString()
            val ref =FirebaseStorage.getInstance().getReference("/image/$filename")

            ref.putFile(selectedImgUri!!)
                .addOnSuccessListener {
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("RegisterActivity","$it")

                        saveToFirebase(it.toString())
                        }
                    }

                .addOnFailureListener {
                    Toast.makeText(this, "fail to upload ", Toast.LENGTH_SHORT).show()
                }

        }

        private fun saveToFirebase(profileURl:String){
            val uid=FirebaseAuth.getInstance().uid?:""
            val ref=FirebaseDatabase.getInstance().getReference("/User/$uid")

            val user= User(uid,Ed_Username_regist.text.toString(),Ed_Email_regist.text.toString(),profileURl)
            ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegisterActivity","finially save the user")

                    // starting main activity
                    val intent=Intent(this,MainActivity::class.java)
                    intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
        }

}





