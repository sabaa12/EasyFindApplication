package com.example.easyfindapp.network

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
//    @GET("{path}")
//    fun getUsers(@Path ("path") path : String) : Call<String>
    @FormUrlEncoded
    @POST("{path}")
    fun requestPOST(@Path ("path") path : String, @FieldMap fieldMap: MutableMap<String,String>) : Call<String>
}