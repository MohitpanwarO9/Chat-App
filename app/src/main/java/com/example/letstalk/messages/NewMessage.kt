package com.example.letstalk.messages


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.letstalk.R
import com.example.letstalk.login.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_list_new_message.view.*

class NewMessage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title="select user"
//        val adapter=GroupAdapter<GroupieViewHolder>()
//
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//
//        recycleView_newMessage.adapter=adapter
        fetchUser()
    }

    private fun fetchUser(){
        val ref=FirebaseDatabase.getInstance().getReference("/User")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter=GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach{
                    val user=it.getValue(User::class.java)
                    if(user!=null){
                        adapter.add(UserItem(user))
                    }
                }
                recycleView_newMessage.adapter=adapter
            }

        })
    }

}

class UserItem(val user:User): Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_newMessage.text=user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.userImage_newMessage)
    }
    override fun getLayout(): Int {
        return R.layout.user_list_new_message
    }

}