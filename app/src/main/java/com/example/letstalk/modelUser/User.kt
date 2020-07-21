package com.example.letstalk.modelUser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String,val username:String ,val email:String , val profileImageUrl:String):Parcelable{
    constructor():this("","","","")
}
