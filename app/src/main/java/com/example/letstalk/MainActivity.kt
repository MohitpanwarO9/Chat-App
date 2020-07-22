package com.example.letstalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.letstalk.login.LoginActivity
import com.example.letstalk.messages.NewMessage
import com.example.letstalk.modelUser.ChatMessage
import com.example.letstalk.modelUser.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.users_list_main_page.view.*

class MainActivity : AppCompatActivity() {

    companion object{
        var currentUser: User?=null
    }

    private lateinit var mAuth:FirebaseAuth
    private val adapter=GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycleView_mainActivity.adapter=adapter

        //init auth Firebase
        mAuth= FirebaseAuth.getInstance()

        fetchCurrentUser()
        listenForLatestMessages()


    }
    val latestMessagesMap=HashMap<String,ChatMessage>()

    private fun recycleViewRefresh(){
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(WithUser(it))
        }
    }

    private fun listenForLatestMessages(){
        val frmId=FirebaseAuth.getInstance().uid
        val refer=FirebaseDatabase.getInstance().getReference("/latest-Message/$frmId")
            refer.addChildEventListener(object : ChildEventListener{

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                    val chatMessage=snapshot.getValue(ChatMessage::class.java)?:return
                    latestMessagesMap[snapshot.key!!]=chatMessage
                    recycleViewRefresh()
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                    val chatMessage=snapshot.getValue(ChatMessage::class.java)?:return
                    latestMessagesMap[snapshot.key!!]=chatMessage
                    recycleViewRefresh()
                }

                override fun onCancelled(error: DatabaseError) {
                }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }
                override fun onChildRemoved(snapshot: DataSnapshot){
                }
            })
    }


    private fun fetchCurrentUser(){
        val ref=FirebaseDatabase.getInstance().getReference("/User/${mAuth.uid}")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser=snapshot.getValue(User::class.java)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser==null){
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.new_messages->{
                startActivity(Intent(this,NewMessage::class.java))
            }
            R.id.signout->{
                mAuth.signOut()
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

class WithUser(private val chatIMessage: ChatMessage):Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.latest_message_mainActivity.text=chatIMessage.text
        //viewHolder.itemView.Username_mainActivity.text=
    }
    override fun getLayout(): Int {

        return R.layout.users_list_main_page
    }

}