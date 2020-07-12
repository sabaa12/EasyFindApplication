package com.example.easyfindapp.models

import java.time.format.DateTimeFormatter

class CreatePostModel(val ID:Int,val title:String,val description:String,val create_date:String,val experience_level:String,val skills:MutableList<String>) {
}