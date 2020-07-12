package com.example.easyfindapp.models

import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemModel():ViewModel() {
    val  choosedItem:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun setvalue(text1:String){
        choosedItem.value=text1
    }

    fun getsvalue():MutableLiveData<String>{
        return choosedItem
    }




}