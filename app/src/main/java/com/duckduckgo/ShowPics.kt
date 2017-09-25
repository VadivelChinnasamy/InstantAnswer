package com.duckduckgo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.duckdcukgo.instant.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*


class ShowPics : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (intent.getStringExtra("image").isNotEmpty()) {
            Picasso.with(this)
                    .load(intent.getStringExtra("image"))
                    .into(imageView, object : Callback {
                        override fun onError() {
                            progressBar.visibility = View.GONE
                        }

                        override fun onSuccess() {
                            progressBar.visibility = View.GONE


                        }

                    })
        } else {
            progressBar.visibility = View.GONE
            Picasso.with(this)
                    .load(R.mipmap.ic_logo)
                    .into(imageView)


        }
    }
}


