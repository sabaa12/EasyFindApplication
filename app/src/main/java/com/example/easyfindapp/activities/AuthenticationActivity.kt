package com.example.easyfindapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.easyfindapp.R
import com.example.easyfindapp.fragments.authentication.SignUpFragment
import com.example.easyfindapp.network.ResponseCallback
import com.example.easyfindapp.network.ResponseLoader

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        openSignUpFragment()
    }

    private fun openSignUpFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.authenticationContainer,
            SignUpFragment(), "SignUpFragment")
        transaction.commit()
    }

}