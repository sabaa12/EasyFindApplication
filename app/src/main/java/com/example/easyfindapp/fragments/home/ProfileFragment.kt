package com.example.easyfindapp.fragments.home

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.easyfindapp.R
import com.example.easyfindapp.adapters.SkillsRecyclerAdapter
import com.example.easyfindapp.extensions.setImage
import com.example.easyfindapp.fragments.BaseFragment
import com.example.easyfindapp.models.DeveloperUserModel
import com.example.easyfindapp.models.EmployerCompanyModel
import com.example.easyfindapp.models.EmployerPersonUserModel
import com.example.easyfindapp.network.EndPoints
import com.example.easyfindapp.network.ResponseCallback
import com.example.easyfindapp.network.ResponseLoader
import com.example.easyfindapp.user_preference.UserPreference
import com.example.easyfindapp.utils.COMPANY
import com.example.easyfindapp.utils.DEVELOPER
import com.example.easyfindapp.utils.EMPLOYER
import com.example.easyfindapp.utils.PERSON
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : BaseFragment() {
    private val userRole = UserPreference.getData(UserPreference.ROLE)
    private val employerType = UserPreference.getData(UserPreference.EMPLOYER_TYPE)
    override fun getFragmentLayout() = R.layout.fragment_profile

    override fun startFragmentConfiguration(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        init()
    }

    private fun init() {
        getUserProfile()
    }

    private fun getUserProfile() {
        if (userRole == DEVELOPER) {
            getUserProfileResponse(EndPoints.GET_DEVELOPER_USER)
        } else if (userRole == EMPLOYER && employerType == PERSON) {
            getUserProfileResponse(EndPoints.GET_EMPLOYER_USER_PERSON)
        } else if (userRole == EMPLOYER && employerType == COMPANY) {
            getUserProfileResponse(EndPoints.GET_EMPLOYER_USER_COMPANY)
        } 
    }

    private fun getUserProfileResponse(path: String) {
        ResponseLoader.getResponseWithID(
            path,
            UserPreference.getData(UserPreference.USER_ID)!!,
            null,
            object : ResponseCallback {
                override fun onSuccess(response: String) {
                    if (userRole == DEVELOPER) {
                        val developerUserModel =
                            Gson().fromJson(response, DeveloperUserModel::class.java)
                        displayDeveloperProfile(developerUserModel)
                    } else if (userRole == EMPLOYER && employerType == PERSON) {
                        val employerPersonUserModel =
                            Gson().fromJson(response, EmployerPersonUserModel::class.java)
                        displayEmployerPersonProfile(employerPersonUserModel)
                    } else if (userRole == EMPLOYER && employerType == COMPANY) {
                        val employerCompanyModel =
                            Gson().fromJson(response, EmployerCompanyModel::class.java)
                        displayEmployerCompanyProfile(employerCompanyModel)
                    }
                }

                override fun onFailure(response: String) {
                    Toast.makeText(activity!!, response, Toast.LENGTH_LONG).show()
                }

                override fun onError(response: String) {
                    Toast.makeText(activity!!, response, Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun displayDeveloperProfile(
        developerUserModel: DeveloperUserModel
    ) {
        itemView!!.userDeveloperGroup.visibility = View.VISIBLE
        itemView!!.userRoleView.text = developerUserModel.role
        itemView!!.userProfileImage.setImage(Uri.parse(developerUserModel.photoUrl))
        itemView!!.userUserNameView.text = "Username: ${developerUserModel.userName}"
        itemView!!.userEmailAddressView.text = "Email Address: ${developerUserModel.emailAddress}"
        itemView!!.userPositionView.text = "Position: ${developerUserModel.position}"
        itemView!!.userGenderView.text = "Gender: ${developerUserModel.gender}"
        itemView!!.userAgeView.text = "Age: ${developerUserModel.age}"
        manageSkillsRecyclerView(developerUserModel.skills)
    }

    private fun manageSkillsRecyclerView(skills: MutableList<String>) {
        itemView!!.userSkillsRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        itemView!!.userSkillsRecyclerView.adapter = SkillsRecyclerAdapter(skills)
    }

    @SuppressLint("SetTextI18n")
    private fun displayEmployerPersonProfile(employerPersonUserModel: EmployerPersonUserModel) {
        itemView!!.userPersonGroup.visibility = View.VISIBLE

        itemView!!.userRoleView.text = employerPersonUserModel.role
        itemView!!.userProfileImage.setImage(Uri.parse(employerPersonUserModel.photoUrl))
        itemView!!.userUserNameView.text = "Username: ${employerPersonUserModel.userName}"
        itemView!!.userEmailAddressView.text =
            "Email Address: ${employerPersonUserModel.emailAddress}"
        itemView!!.userPositionView.text =
            "Employer Type: ${UserPreference.getData(UserPreference.EMPLOYER_TYPE)}"
        itemView!!.userGenderView.text = "Gender: ${employerPersonUserModel.gender}"
        itemView!!.userAgeView.text = "Age: ${employerPersonUserModel.age}"
    }

    @SuppressLint("SetTextI18n")
    private fun displayEmployerCompanyProfile(employerCompanyModel: EmployerCompanyModel) {
        itemView!!.userRoleView.text = userRole
        itemView!!.userProfileImage.setImage(Uri.parse(employerCompanyModel.companyLogo))
        itemView!!.userUserNameView.text = "Company name: ${employerCompanyModel.companyName}"
        itemView!!.userEmailAddressView.text =
            "Email Address: ${UserPreference.getData(UserPreference.EMAIL_ADDRESS)}"
        itemView!!.userPositionView.text =
            "Employer Type: ${UserPreference.getData(UserPreference.EMPLOYER_TYPE)}"
    }
}