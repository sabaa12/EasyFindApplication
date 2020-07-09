package com.example.easyfindapp.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.easyfindapp.App

fun ImageView.setImage(uri : Uri) {
    Glide.with(App.appInstance!!.applicationContext).load(uri).into(this)
}