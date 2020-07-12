package com.example.easyfindapp.adapters

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfindapp.R
import com.example.easyfindapp.models.CreatePostModel
import kotlinx.android.synthetic.main.posts_recycler_view.view.*

class PostsRecyclerAdapter(private val posts:MutableList<CreatePostModel>):RecyclerView.Adapter<PostsRecyclerAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=ViewHolder   (
    LayoutInflater.from(parent.context)
    .inflate(R.layout.posts_recycler_view, parent, false)
    )

    override fun getItemCount()=posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind()
     }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var model:CreatePostModel
        var skills:String=""
        fun onBind(){
            model=posts[adapterPosition]
            itemView.title.text=model.title
            itemView.experience.text=model.experience_level
            itemView.description.text=model.description
            (0 until model.skills.size).forEach(){
                skills+="${model.skills[it]}, "
            }
            itemView.requiredSkills.text=skills
            d("asd",skills)
        }
    }
}