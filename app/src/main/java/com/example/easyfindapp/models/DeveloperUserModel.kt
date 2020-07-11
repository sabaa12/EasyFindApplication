package com.example.easyfindapp.models

import com.google.gson.annotations.SerializedName

data class DeveloperUserModel(
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("email_address")
    val emailAddress: String,
    @SerializedName("age")
    val age: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("position")
    val position: String,
    @SerializedName("photo_url")
    val photoUrl: String,
    @SerializedName("skills")
    val skills: MutableList<String>,
    @SerializedName("role")
    val role: String

)