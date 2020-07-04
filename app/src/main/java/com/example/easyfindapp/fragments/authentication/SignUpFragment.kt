package com.example.easyfindapp.fragments.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.easyfindapp.R
import com.example.easyfindapp.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class SignUpFragment : BaseFragment() {
    override fun getFragmentLayout() = R.layout.fragment_sign_up

    override fun startFragmentConfiguration(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        init()
    }

    private fun init() {
        itemView!!.alreadyHaveAccountView.setOnClickListener {
            openSignInFragment()
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

}