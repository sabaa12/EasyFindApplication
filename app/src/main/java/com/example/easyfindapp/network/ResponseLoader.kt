package com.example.easyfindapp.network

import android.view.View
import com.example.easyfindapp.network.EndPoints.HTTP_200_OK
import com.example.easyfindapp.network.EndPoints.HTTP_201_CREATED
import com.example.easyfindapp.network.EndPoints.HTTP_204_NO_CONTENT
import com.example.easyfindapp.network.EndPoints.HTTP_400_BAD_REQUEST
import com.example.easyfindapp.network.EndPoints.HTTP_401_UNAUTHORIZED
import com.example.easyfindapp.network.EndPoints.HTTP_404_NOT_FOUND
import com.example.easyfindapp.network.EndPoints.HTTP_500_INTERNAL_SERVER_ERROR
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ResponseLoader {

    fun getPostResponse(path : String, parameters : MutableMap<String,String>, loaderView: View?, responseCallback: ResponseCallback) {
        loaderView?.visibility = View.VISIBLE
        apiService.requestPOST(path,parameters).enqueue(callback(loaderView,responseCallback))
    }

    private fun callback(loaderView: View?, responseCallback: ResponseCallback) = object : Callback<String> {
        override fun onFailure(call: Call<String>, t: Throwable)  {
            if (loaderView != null) {
                loaderView.visibility = View.GONE
            }
            responseCallback.onFailure(t.toString())
        }

        override fun onResponse(call: Call<String>, response: Response<String>) {
            checkResponseCode(response, responseCallback, loaderView)
        }
    }

    private fun checkResponseCode(
        response: Response<String>,
        responseCallback: ResponseCallback,
        loaderView: View?
    ) {
        if (response.code() == HTTP_200_OK || response.code() == HTTP_201_CREATED)
            try {
                responseCallback.onSuccess(response.body().toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        else if (response.code() == HTTP_400_BAD_REQUEST)
            parseError(response.errorBody()!!.string(), responseCallback)
        else if (response.code() == HTTP_401_UNAUTHORIZED)
            parseError(response.errorBody()!!.string(), responseCallback)
        else if (response.code() == HTTP_404_NOT_FOUND)
            parseError(response.errorBody()!!.string(), responseCallback)
        else if (response.code() == HTTP_500_INTERNAL_SERVER_ERROR)
            responseCallback.onError(("Internal Sever Error"))
        else if (response.code() == HTTP_204_NO_CONTENT)
            parseError("No Content", responseCallback)
        else {
            responseCallback.onFailure("The request is incorrect!")
        }

        if (loaderView != null) {
            loaderView.visibility = View.GONE
        }
    }

    private fun parseError(errorBody: String, responseCallback: ResponseCallback) {
        try {
            val errorJson = JSONObject(errorBody)
            if (errorJson.has("status")) {
                responseCallback.onError(errorJson.getString("status"))
            }
        } catch (e : JSONException) {
            responseCallback.onError("Error, Try Again!")
        }
    }
}