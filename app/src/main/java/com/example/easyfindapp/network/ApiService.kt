package com.example.easyfindapp.network

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("{path}/{id}")
    fun requestGetWithID(@Path("path") path: String, @Path ("id") id : String): Call<String>

    @FormUrlEncoded
    @POST("{path}")
    fun requestPOST(
        @Path("path") path: String,
        @FieldMap fieldMap: MutableMap<String, String>
    ): Call<String>
}