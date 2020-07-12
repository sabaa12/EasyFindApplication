package com.example.easyfindapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easyfindapp.R
import com.example.easyfindapp.adapters.SkillsRecyclerAdapter
import com.example.easyfindapp.models.CreatePostModel
import com.example.easyfindapp.models.viewmodels.PostViewModel
import com.example.easyfindapp.network.EndPoints
import com.example.easyfindapp.network.ResponseCallback
import com.example.easyfindapp.network.ResponseLoader
import com.example.easyfindapp.tools.Tools
import com.example.easyfindapp.user_preference.UserPreference
import com.example.easyfindapp.utils.EXPERIENCE_LEVEL
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_post_employer.*
import kotlinx.android.synthetic.main.loader_layout.*

class CreatePostEmployerActivity : AppCompatActivity() {
    private lateinit var skillsRecyclerAdapter: SkillsRecyclerAdapter
    private val skills: MutableList<String> = mutableListOf()
    lateinit var postViewModel: PostViewModel
    var explevel: String = EXPERIENCE_LEVEL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post_employer)
        init()
    }

    private fun init() {
        listeners()
        manageSkillRecyclerView()
        postViewModel =
            ViewModelProvider(this).get(PostViewModel::class.java)

        postViewModel.getValue().observe(this, Observer {
            experienceLevel.text = it.toString()
            explevel = it
        })
    }

    private fun listeners() {
        addSkillBtn.setOnClickListener {
            getSkillValue()

        }
        createPostBtn.setOnClickListener {

            val title = inputTitle.text.toString()
            val description = inputDescription.text.toString()
            if (title.isEmpty() || description.isEmpty() || skills.size == 0 || description.isEmpty() || explevel == EXPERIENCE_LEVEL)
                Tools.errorDialog(
                    this,
                    resources.getString(R.string.required_fields),
                    resources.getString(R.string.fill_create_post_fields),
                    resources.getString(R.string.close)
                )
            else {
                val model = CreatePostModel(
                    UserPreference.getData(UserPreference.USER_ID)!!.toInt(),
                    title,
                    description,
                    System.currentTimeMillis().toString(),
                    explevel,
                    skills
                )
                val stringJson = Gson().toJson(model)
                val parameters = mutableMapOf<String, String>()
                parameters["json"] = stringJson
                ResponseLoader.getPostResponse(
                    EndPoints.CREATE_POST,
                    parameters,
                    spinLoaderView,
                    object : ResponseCallback {
                        override fun onSuccess(response: String) {
                            Toast.makeText(
                                this@CreatePostEmployerActivity,
                                "Post created successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            inputTitle.text.clear()
                            inputDescription.text.clear()
                            inputAddSkillsFieldView.text.clear()
                            skills.clear()
                            skillsRecyclerAdapter.notifyDataSetChanged()
                        }

                        override fun onFailure(response: String) {
                            Toast.makeText(
                                this@CreatePostEmployerActivity,
                                response,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onError(response: String) {
                            Toast.makeText(
                                this@CreatePostEmployerActivity,
                                response,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            }
        }

        experienceLevel.setOnClickListener() {
            val selectedExperienceLevel = Tools.chooserDialog(this, postViewModel)
        }
    }

    private fun getSkillValue() {
        if (inputAddSkillsFieldView.text.isEmpty()) {
            Toast.makeText(this, "Fill this field to add new skill", Toast.LENGTH_LONG).show()
        } else {
            skills.add(inputAddSkillsFieldView.text.toString())
            inputAddSkillsFieldView.text.clear()
            skillsRecyclerAdapter.notifyDataSetChanged()
        }
    }

    private fun manageSkillRecyclerView() {
        skillsRecyclerView.layoutManager =
            LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        skillsRecyclerAdapter = SkillsRecyclerAdapter(skills)
        skillsRecyclerView.adapter = skillsRecyclerAdapter
        skillsRecyclerAdapter.notifyDataSetChanged()

    }
}