package com.example.easyfindapp.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.easyfindapp.R
import com.example.easyfindapp.fragments.BaseFragment

class FavouritesFragment : BaseFragment() {
    override fun getFragmentLayout() = R.layout.fragment_favourites

    override fun startFragmentConfiguration(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {

    }


}