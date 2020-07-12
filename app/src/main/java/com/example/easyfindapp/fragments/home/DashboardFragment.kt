package com.example.easyfindapp.fragments.home

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easyfindapp.R
import com.example.easyfindapp.adapters.PostsRecyclerAdapter
import com.example.easyfindapp.fragments.BaseFragment
import com.example.easyfindapp.models.CreatePostModel
import com.example.easyfindapp.network.EndPoints
import com.example.easyfindapp.network.ResponseCallback
import com.example.easyfindapp.network.ResponseLoader
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.loader_layout.*

class DashboardFragment : BaseFragment() {
    lateinit var adapter: PostsRecyclerAdapter
    var postsList= mutableListOf<CreatePostModel>()
    override fun getFragmentLayout() = R.layout.fragment_dashboard

    override fun startFragmentConfiguration(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        init()

    }
    private fun init(){
        ResponseLoader.getResponse(EndPoints.GET_POSTS,spinLoaderView, object : ResponseCallback {
            override fun onSuccess(response: String) {
                 var postsList=Gson().fromJson(response,Array<CreatePostModel>::class.java).toMutableList()
                setAdapter(postsList)
             }

            override fun onFailure(response: String) {
                Toast.makeText(context, response, Toast.LENGTH_LONG).show()
            }

            override fun onError(response: String) {
                Toast.makeText(context, response, Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun setAdapter(list:MutableList<CreatePostModel>){
        adapter= PostsRecyclerAdapter(list)
        postsRecyclerView.layoutManager= LinearLayoutManager(context)
        postsRecyclerView.adapter=adapter
    }
}