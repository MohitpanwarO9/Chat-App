package com.example.letstalk.modelUser

class ChatMessage (val fromId:String , val id:String , val text:String , val timeStamp:Long , val toId:String){
    constructor():this("","","",-1,"")
}