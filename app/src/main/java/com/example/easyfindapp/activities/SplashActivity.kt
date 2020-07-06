package com.example.easyfindapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import com.example.easyfindapp.App
import com.example.easyfindapp.R
import com.example.easyfindapp.network.EndPoints
import com.example.easyfindapp.network.ResponseCallback
import com.example.easyfindapp.network.ResponseLoader
import com.example.easyfindapp.user_preference.UserPreference
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.loader_layout.*
import org.json.JSONObject

class SplashActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }

    private fun init() {
        handler = Handler()
        runnable = Runnable {
            checkUser()
        }
    }

    private fun openAuthenticationActivity() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onResume() {
        handler.postDelayed(runnable, 2000)
        super.onResume()
    }

    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
    }

    private fun checkUser() {
        if (UserPreference.getData(UserPreference.USER_ID)!!.isEmpty()) {
            openAuthenticationActivity()
        } else {
            checkUserIsCompleted(UserPreference.getData(UserPreference.USER_ID)!!)
        }
    }

    private fun checkUserIsCompleted(id: String) {
        ResponseLoader.isUserCompleteResponse(
            EndPoints.IS_USER_COMPLETED,
            id,
            null,
            object : ResponseCallback {
                override fun onSuccess(response: String) {
                    val jsonResponse = JSONObject(response)
                    if (jsonResponse.has("iscompleted")) {
                        val intent: Intent =
                            if (jsonResponse.get("iscompleted") == false && UserPreference.getData(
                                    UserPreference.ROLE
                                ) == "Developer"
                            ) {
                                Intent(
                                    App.appInstance!!.applicationContext,
                                    CompleteProfileActivity::class.java
                                )
                            } else {
                                Intent(
                                    App.appInstance!!.applicationContext,
                                    DashBoardActivity::class.java
                                )
                            }
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                }

                override fun onFailure(response: String) {
                    Toast.makeText(this@SplashActivity, response, Toast.LENGTH_LONG).show()
                }

                override fun onError(response: String) {
                    Toast.makeText(this@SplashActivity, response, Toast.LENGTH_LONG).show()
                }
            })
    }
}