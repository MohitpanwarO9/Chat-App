package com.example.letstalk

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import kotlinx.android.synthetic.main.about.view.*

class About:androidx.fragment.app.DialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var Myabout=inflater!!.inflate(R.layout.about,container,false)

        var instagram=Myabout.instagram as ImageView
        var github=Myabout.github as ImageView
        var linkedin=Myabout.linkedin as ImageView

        instagram.setOnClickListener {

            val uriForApp: Uri = Uri.parse("https://www.instagram.com/mohitpanwar09/")
            val forApp = Intent(Intent.ACTION_VIEW, uriForApp)

            val uriForBrowser: Uri = Uri.parse("https://www.instagram.com/mohitpanwar09/")
            val forBrowser = Intent(Intent.ACTION_VIEW, uriForBrowser)


            try {
                startActivity( forApp, null)

            } catch (e: ActivityNotFoundException) {
                startActivity( forBrowser, null)

            }

        }

        github.setOnClickListener {

            val uriForApp: Uri = Uri.parse("https://github.com/mohitpanwar09")
            val forApp = Intent(Intent.ACTION_VIEW, uriForApp)

            val uriForBrowser: Uri = Uri.parse("https://github.com/mohitpanwar09")
            val forBrowser = Intent(Intent.ACTION_VIEW, uriForBrowser)


            try {
                startActivity( forApp, null)

            } catch (e: ActivityNotFoundException) {
                startActivity( forBrowser, null)

            }

        }

        linkedin.setOnClickListener {

            val uriForApp: Uri = Uri.parse("https://www.linkedin.com/in/mohit-panwar-82697b191/")
            val forApp = Intent(Intent.ACTION_VIEW, uriForApp)

            val uriForBrowser: Uri = Uri.parse("https://www.linkedin.com/in/mohit-panwar-82697b191/")
            val forBrowser = Intent(Intent.ACTION_VIEW, uriForBrowser)


            try {
                startActivity( forApp, null)

            } catch (e: ActivityNotFoundException) {
                startActivity( forBrowser, null)

            }

        }


        return Myabout

    }

}