package com.example.easyfindapp.fragments.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.easyfindapp.R
import com.example.easyfindapp.activities.DashBoardActivity
import com.example.easyfindapp.extensions.emailValidation
import com.example.easyfindapp.fragments.BaseFragment
import com.example.easyfindapp.models.SignInResponseModel
import com.example.easyfindapp.network.EndPoints
import com.example.easyfindapp.network.ResponseCallback
import com.example.easyfindapp.network.ResponseLoader
import com.example.easyfindapp.tools.Tools
import com.example.easyfindapp.user_preference.UserPreference
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.inputEmailAddressSignIn
import kotlinx.android.synthetic.main.loader_layout.*

class SignInFragment : BaseFragment() {
    private var emailValid: Boolean = false
    override fun getFragmentLayout() = R.layout.fragment_sign_in

    override fun startFragmentConfiguration(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        init()
    }

    private fun init() {
        clickListeners()
        checkEmailValidation()
    }

    private fun clickListeners() {
        itemView!!.createNewAccountView.setOnClickListener {
            openSignUpFragment()
        }

        itemView!!.signInButton.setOnClickListener {
            checkEmptyFields()
        }
    }

    private fun checkEmailValidation() {
        itemView!!.inputEmailAddressSignIn.emailValidation {
            emailValid = it
        }
    }

    private fun checkEmptyFields() {
        if (itemView!!.inputEmailAddressSignIn.text.isEmpty() || itemView!!.inputPasswordSignIn.text.isEmpty()
        ) {
            Tools.errorDialog(
                activity!!,
                resources.getString(R.string.required_fields),
                resources.getString(R.string.fill_sign_in_required_fields),
                resources.getString(R.string.try_again)
            )
        } else if (!emailValid) {
            Tools.errorDialog(
                activity!!,
                resources.getString(R.string.input_field_validation),
                resources.getString(R.string.fill_valid_email),
                resources.getString(R.string.try_again)
            )
        } else {
            sendSignUpRequest(
                itemView!!.inputEmailAddressSignIn.text.toString(),
                itemView!!.inputPasswordSignIn.text.toString()
            )
        }
    }

    private fun openSignUpFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.authenticationContainer,
            SignUpFragment()
        )
        transaction.commit()
    }

    private fun sendSignUpRequest(email: String, password: String) {
        val parameters = mutableMapOf<String, String>()
        parameters["email_address"] = email
        parameters["password"] = password
        ResponseLoader.getPostResponse(
            EndPoints.SIGN_IN,
            parameters,
            spinLoaderView,
            object : ResponseCallback {
                override fun onSuccess(response: String) {
                    parserSignInResponse(response)
                }

                override fun onFailure(response: String) {
                    Toast.makeText(activity, response, Toast.LENGTH_LONG).show()
                }

                override fun onError(response: String) {
                    Toast.makeText(activity, response, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun parserSignInResponse(response: String) {
        val signInResponseModel = Gson().fromJson(response, SignInResponseModel::class.java)
        if (signInResponseModel.status == "ok") {
            openDashBoardActivity()
            saveUserInformation(signInResponseModel.userID)
        }
    }

    private fun saveUserInformation(userID: String) {
        UserPreference.saveData(UserPreference.USER_ID, userID)
    }

    private fun openDashBoardActivity() {
        val intent = Intent(activity!!.applicationContext, DashBoardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity!!.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}