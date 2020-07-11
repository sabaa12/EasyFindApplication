package com.example.easyfindapp.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.easyfindapp.R
import com.example.easyfindapp.extensions.setImage
import com.example.easyfindapp.utils.IMAGE_URL
import kotlinx.android.synthetic.main.fragment_full_screen_image.view.*

class FullScreenImageFragment : BaseFragment() {

    override fun getFragmentLayout() = R.layout.fragment_full_screen_image

    override fun startFragmentConfiguration(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        if (this.requireArguments().getString(IMAGE_URL) != null) {
            displayFullScreenImage(this.requireArguments().getString(IMAGE_URL)!!)
        }
    }

    private fun displayFullScreenImage(imageUrl: String) {
        itemView!!.fullScreenImageView.setImage(Uri.parse(imageUrl))

    }
    companion object {
        fun fullScreenImageFragmentInstance(imageUrl: String): Fragment {
            val fragment = FullScreenImageFragment()
            val bundle = Bundle()
            bundle.putString(IMAGE_URL, imageUrl)
            fragment.arguments = bundle
            return fragment
        }
    }
}