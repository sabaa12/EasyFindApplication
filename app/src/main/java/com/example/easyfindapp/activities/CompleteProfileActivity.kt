package com.example.easyfindapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.easyfindapp.R
import com.example.easyfindapp.fragments.complete_profile.CompleteDeveloperProfileFragment

class CompleteProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)
        openCompleteProfileDeveloper()
    }

    private fun openCompleteProfileDeveloper() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.completeProfileContainer,
            CompleteDeveloperProfileFragment(),
            "CompleteDeveloperProfile"
        )
        transaction.commit()
    }
}