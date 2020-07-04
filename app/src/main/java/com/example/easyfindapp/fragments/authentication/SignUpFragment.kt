package com.example.easyfindapp.fragments.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import com.example.easyfindapp.App
import com.example.easyfindapp.R
import com.example.easyfindapp.activities.AuthenticationActivity
import com.example.easyfindapp.activities.DashBoardActivity
import com.example.easyfindapp.fragments.BaseFragment
import com.example.easyfindapp.models.SignUpResponseModel
import com.example.easyfindapp.network.EndPoints
import com.example.easyfindapp.network.ResponseCallback
import com.example.easyfindapp.network.ResponseLoader
import com.example.easyfindapp.user_preference.UserPreference
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import kotlinx.android.synthetic.main.loader_layout.*


class SignUpFragment : BaseFragment() {
    private var position: String? = null
    override fun getFragmentLayout() = R.layout.fragment_sign_up

    override fun startFragmentConfiguration(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        checkUserRole()
        init()
    }

    private fun init() {
        itemView!!.alreadyHaveAccountView.setOnClickListener {
            openSignInFragment()
        }

        itemView!!.signUpButton.setOnClickListener {
            checkEmptyFields()
        }
    }

    private fun openSignInFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.authenticationContainer,
            SignInFragment(), "SignInFragment"
        )
        transaction.addToBackStack("SignInFragment")
        transaction.commit()
    }

    private fun checkEmptyFields() {
        if (inputEmailAddressSignUp.text.isEmpty() || inputPasswordSignUp.text.isEmpty() ||
            inputConfirmPasswordSignUp.text.isEmpty()
        ) {
            Toast.makeText(
                activity, "Pleas fill all fields", Toast.LENGTH_LONG
            ).show()

        } else if (inputPasswordSignUp.text.toString() != inputConfirmPasswordSignUp.text.toString()) {
            Toast.makeText(
                activity, "Passwords Mismatch!", Toast.LENGTH_LONG
            ).show()
        } else if (position == null) {
            Toast.makeText(activity, "Please select your Role", Toast.LENGTH_LONG).show()
        } else {
            sendSignUpRequest(
                inputEmailAddressSignUp.text.toString(),
                inputPasswordSignUp.text.toString(),
                position!!
            )
        }
    }

    private fun sendSignUpRequest(email: String, password: String, role: String) {
        val parameters = mutableMapOf<String, String>()
        parameters["email_address"] = email
        parameters["password"] = password
        parameters["role"] = role
        ResponseLoader.getPostResponse(
            EndPoints.SIGN_UP,
            parameters,
            spinLoaderView,
            object : ResponseCallback {
                override fun onSuccess(response: String) {
                    parseSignUpResponse(response)
                }

                override fun onFailure(response: String) {
                    Toast.makeText(activity, response, Toast.LENGTH_LONG).show()
                }

                override fun onError(response: String) {
                    Toast.makeText(activity, response, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun openDashBoardActivity() {
        val intent = Intent(activity!!.applicationContext, DashBoardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity!!.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun saveUserInformation(userID: String) {
        UserPreference.saveData(UserPreference.USER_ID, userID)
    }

    private fun parseSignUpResponse(response: String) {
        val signUpResponseModel = Gson().fromJson(response, SignUpResponseModel::class.java)
        if (signUpResponseModel.registered) {
            openDashBoardActivity()
            saveUserInformation(signUpResponseModel.userID)
        } else {
            Toast.makeText(activity, signUpResponseModel.status, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkUserRole(): String? {
        position = null
        itemView!!.selectUserRole.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.employerPosition -> {
                    position = "Employer"
                }
                R.id.developerPosition -> {
                    position = "Developer"
                }
            }
        }
        return position
    }
}