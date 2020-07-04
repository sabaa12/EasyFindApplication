package com.example.easyfindapp.fragments.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.easyfindapp.R
import com.example.easyfindapp.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_in.view.*


class SignInFragment : BaseFragment() {
    override fun getFragmentLayout() = R.layout.fragment_sign_in

    override fun startFragmentConfiguration(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        init()
    }

    private fun init() {
        itemView!!.createNewAccountView.setOnClickListener {
            openSignUpFragment()
        }
    }

    private fun openSignUpFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.authenticationContainer,
            SignUpFragment()
        )
        transaction.commit()
    }

}