package com.example.easyfindapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import android.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easyfindapp.R
import com.example.easyfindapp.adapters.SkillsRecyclerAdapter
import com.example.easyfindapp.models.CreatePostModel
import com.example.easyfindapp.models.ItemModel
import com.example.easyfindapp.network.EndPoints
import com.example.easyfindapp.network.ResponseCallback
import com.example.easyfindapp.network.ResponseLoader
import com.example.easyfindapp.tools.Tools
import com.example.easyfindapp.user_preference.UserPreference
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_post_employer.*
import kotlinx.android.synthetic.main.fragment_complete_developer_profile.view.*
import kotlinx.android.synthetic.main.fragment_complete_developer_profile.view.addSkillBtn
import kotlinx.android.synthetic.main.loader_layout.*

private lateinit var skillsRecyclerAdapter: SkillsRecyclerAdapter
private val skills: MutableList<String> = mutableListOf()
 lateinit var iemModel: ItemModel
var explevel:String=""
class CreatePostEmployerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post_employer)
        init()
    }

    private fun init(){
        listeners()
        manageSkillRecyclerView()
          iemModel =
            ViewModelProvider(this).get(ItemModel::class.java)


        iemModel.getsvalue().observe(this, Observer {
            experiencelevel.text = it.toString()
            explevel=it
        })

    }

    private fun listeners(){
        addSkillBtn.setOnClickListener(){
            getSkillValue()

        }
        createpostbtn.setOnClickListener(){

            val title=titleTV.text.toString()
            val descriptionn=description.text.toString()
            if(title.isEmpty() || descriptionn.isEmpty()||descriptionn.isEmpty()|| explevel =="")
                Tools.errorDialog(this,"all fields requared","please fill all fields","close")
            else{
                val model=CreatePostModel(UserPreference.getData(UserPreference.USER_ID)!!.toInt(),title,descriptionn,System.currentTimeMillis().toString(),
                    explevel,skills)
                val stringJson = Gson().toJson(model)
                val parameters = mutableMapOf<String, String>()
                parameters["json"] = stringJson
                ResponseLoader.getPostResponse(
                    EndPoints.COMPLETE_PROFILE_DEVELOPERS,
                    parameters,
                    spinLoaderView,
                    object : ResponseCallback {
                        override fun onSuccess(response: String) {
                            Toast.makeText(this@CreatePostEmployerActivity!!, "post created successfully", Toast.LENGTH_LONG).show()
                            titleTV.text.clear()
                            description.text.clear()
                            inputAddSkillsField.text.clear()
                            skills.clear()
                            skillsRecyclerAdapter.notifyDataSetChanged()
                        }

                        override fun onFailure(response: String) {
                            Toast.makeText(this@CreatePostEmployerActivity!!, response, Toast.LENGTH_LONG).show()                        }

                        override fun onError(response: String) {
                            Toast.makeText(this@CreatePostEmployerActivity!!, response, Toast.LENGTH_LONG).show()                        }
                    })
            }
        }

        experiencelevel.setOnClickListener(){
            val choosedItem=Tools.chooserDialog(this,iemModel)
        }
    }

    private fun getSkillValue() {
        if (inputAddSkillsField.text.isEmpty()) {
            Toast.makeText(this, "Fill this field to add new skill", Toast.LENGTH_LONG).show()
        } else {
            skills.add(inputAddSkillsField.text.toString())
            inputAddSkillsField.text.clear()
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