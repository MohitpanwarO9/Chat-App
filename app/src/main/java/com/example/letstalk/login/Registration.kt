package com.example.letstalk.login

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.letstalk.CheckingVerify
import com.example.letstalk.MainActivity
import com.example.letstalk.R
import com.example.letstalk.modelUser.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class Registration : AppCompatActivity() {

    private val readImage=569

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        

        //create user account
        register_button.setOnClickListener {
            progressBar.visibility=View.VISIBLE
            register_button.isEnabled=false
            performRegister()
        }
        //switch to login page
        Already_have.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        Bt_selectImage_regist.setOnClickListener {
            checkPer()
        }

    }

    private fun fetchFromGallery(){

        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,0)
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

                    if(emailUser.isEmpty()||passwordUser.isEmpty()||selectedImgUri==null){
                        Toast.makeText(this, "Email, password or Image is empty", Toast.LENGTH_SHORT).show()

                        progressBar.visibility = View.GONE
                        register_button.isEnabled = true
                        return
                    }

                        FirebaseAuth.getInstance()
                            .createUserWithEmailAndPassword(emailUser, passwordUser)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    uploadImageToFirebase()

//                                    FirebaseAuth.getInstance().currentUser!!.sendEmailVerification()
//                                        .addOnCompleteListener {task->
//                                            if(task.isSuccessful){
//                                                Toast.makeText(this, "Verification Email has been send Please verify to continue", Toast.LENGTH_LONG).show()
//                                                uploadImageToFirebase()
//                                            }
//                                        }
//                                        .addOnFailureListener {
//                                            Toast.makeText(this, "Please check email and try again", Toast.LENGTH_LONG).show()
//                                            progressBar.visibility = View.GONE
//                                            register_button.isEnabled = true
//                                        }

                                } else {
                                    Toast.makeText(this, "Fail to Register", Toast.LENGTH_LONG)
                                        .show()
                                    progressBar.visibility = View.GONE
                                    register_button.isEnabled = true
                                    return@addOnCompleteListener
                                }
                            }
                    }




        private fun uploadImageToFirebase(){
            if(selectedImgUri==null) {
                error("Please select any image")
            }

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
                    progressBar.visibility=View.GONE
                    register_button.isEnabled=true
                }

        }

        private fun saveToFirebase(profileURl:String){
            val uid=FirebaseAuth.getInstance().uid?:""
            val ref=FirebaseDatabase.getInstance().getReference("/User/$uid")

            val user= User(uid,Ed_Username_regist.text.toString(),Ed_Email_regist.text.toString(),profileURl)
            ref.setValue(user)
                .addOnSuccessListener {
                    progressBar.visibility=View.GONE
                    Log.d("RegisterActivity","finially save the user")

                    val intent=Intent(this,MainActivity::class.java)
                    intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "fail to save User ", Toast.LENGTH_SHORT).show()
                    progressBar.visibility=View.GONE
                    register_button.isEnabled=true
                }
        }


    private fun checkPer(){

        if(Build.VERSION.SDK_INT>=23){
                if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED){

                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),readImage)
                    return
                }
            fetchFromGallery()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
                readImage->{
                    if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        fetchFromGallery()
                    }else{
                        Toast.makeText(this, "PERMISSION IS NOT GRANTED", Toast.LENGTH_SHORT).show()
                    }
                }
                else->super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }



}





