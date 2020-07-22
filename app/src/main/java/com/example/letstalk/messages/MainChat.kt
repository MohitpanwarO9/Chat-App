package com.example.letstalk.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.letstalk.R
import com.example.letstalk.modelUser.ChatMessage
import com.example.letstalk.modelUser.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_main_chat.*
import kotlinx.android.synthetic.main.chat_form_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class MainChat : AppCompatActivity() {

    companion object{
        const val Tag="mainChat"
    }
    private val adapter=GroupAdapter<GroupieViewHolder>()
    var userIn:User?=null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_chat)

        recycleView_Mainchat.adapter=adapter

        val bundle:Bundle?=intent.extras
          userIn=bundle!!.getParcelable<User>("User_Key")

        supportActionBar?.title=userIn!!.username

        listenForMessages()

        sendBt_mainChat.setOnClickListener {
            Log.d(Tag,"Try to send Message")
            sendMessage()
        }
    }

    private fun listenForMessages(){
        val ref=FirebaseDatabase.getInstance().getReference("/messages")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage=snapshot.getValue(ChatMessage::class.java)
                val toId=userIn!!.uid
                if(chatMessage!=null){

                    if(chatMessage.fromId==toId && chatMessage.toId==FirebaseAuth.getInstance().uid){
                        adapter.add(ChatFromItem(chatMessage.text))
                    }
                    else if(chatMessage.toId==toId && chatMessage.fromId==FirebaseAuth.getInstance().uid){
                        adapter.add(ChatToItem(chatMessage.text))
                    }

                }
                recycleView_Mainchat.scrollToPosition(adapter.itemCount -1)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    private fun sendMessage(){
        val text=enterMessage_mainChat.text.toString()

        val fromId=FirebaseAuth.getInstance().uid

        val toId=userIn!!.uid
        val reference=FirebaseDatabase.getInstance().getReference("/messages").push()


        val chatMessage=ChatMessage(fromId!!,reference.key!!,text,System.currentTimeMillis()/1000,toId)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(Tag,"message stored in fireBase")
                enterMessage_mainChat.text.clear()
                recycleView_Mainchat.scrollToPosition(adapter.itemCount -1)
            }

        val latestMessageRef=FirebaseDatabase.getInstance().getReference("/latest-Message/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef=FirebaseDatabase.getInstance().getReference("/latest-Message/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }

}

class ChatFromItem(val text:String):Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_form_row
    }
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_from.text=text
    }

}

class ChatToItem(val text:String):Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_to.text=text
    }

}