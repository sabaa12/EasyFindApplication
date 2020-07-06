package com.example.easyfindapp.fragments.complete_profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfindapp.App
import com.example.easyfindapp.R
import com.example.easyfindapp.activities.AuthenticationActivity
import com.example.easyfindapp.activities.DashBoardActivity
import com.example.easyfindapp.adapters.SkillsRecyclerAdapter
import com.example.easyfindapp.fragments.BaseFragment
import com.example.easyfindapp.tools.Tools
import com.example.easyfindapp.user_preference.UserPreference
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_complete_profile.view.*
import kotlinx.android.synthetic.main.fragment_complete_developer_profile.view.*
import java.util.*

class CompleteDeveloperProfileFragment : BaseFragment() {
    private var imageUri: Uri? = null
    private lateinit var skillsRecyclerAdapter: SkillsRecyclerAdapter
    private val skills: MutableList<String> = mutableListOf()
    private var age: Int? = null
    private var gender: String? = null
    private var imagePictureSelected: Boolean = false
    override fun getFragmentLayout() = R.layout.fragment_complete_developer_profile

    override fun startFragmentConfiguration(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        init()
    }

    private fun init() {
        selectGender()
        selectAge()
        selectProfileImage()
        displayCompletedFields()
        clickListeners()
        manageSkillRecyclerView()
    }

    private fun clickListeners() {
        itemView!!.addSkillBtn.setOnClickListener {
            getSkillValue()
        }

        itemView!!.completeProfileBtn.setOnClickListener {
            checkEmptyFields()
        }
    }

    private fun checkEmptyFields() {
        val userName = itemView!!.inputUserNameDeveloper
        val position = itemView!!.inputPosition
        if (userName.text.isEmpty() || position.text.isEmpty() || age == null || gender == null || !imagePictureSelected || skills.size == 0) {
            Tools.errorDialog(
                activity!!,
                resources.getString(R.string.required_fields),
                resources.getString(R.string.fill_complete_profile_requred_fields),
                resources.getString(R.string.try_again)
            )
        } else {
            Toast.makeText(activity!!, "Profile Completed", Toast.LENGTH_LONG).show()
            openDashBoard()
        }
    }

    private fun openDashBoard() {
        val intent = Intent(activity!!, DashBoardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity!!.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun getSkillValue() {
        if (itemView!!.inputAddSkillsField.text.isEmpty()) {
            Toast.makeText(activity!!, "Fill this field to add new skill", Toast.LENGTH_LONG).show()
        } else {
            skills.add(itemView!!.inputAddSkillsField.text.toString())
            itemView!!.inputAddSkillsField.text.clear()
            skillsRecyclerAdapter.notifyDataSetChanged()
        }
    }

    private fun selectProfileImage() {
        itemView!!.selectProfileImageDeveloper.setOnClickListener {
            checkPermissions()
        }
    }

    private fun selectAge() {
        itemView!!.selectAgeDeveloper.minValue = 15
        itemView!!.selectAgeDeveloper.maxValue = 100
        itemView!!.selectAgeDeveloper.setOnValueChangedListener { _, _, newVal ->
            age = newVal
        }
    }

    private fun selectGender() {
        itemView!!.selectUserGender.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.genderMale -> gender = "Male"
                R.id.genderFemale -> gender = "Female"
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkReadExternalStoragePermission() && checkWriteExternalStoragePermission() && checkCameraPermission())) {
                requestPermissions()
            } else {
                imageUploadChooser()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size == 3) {
                if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                            && grantResults[2] == PackageManager.PERMISSION_GRANTED
                            )
                ) {
                    Toast.makeText(
                        activity,
                        "Please confirm permission to continue",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    imageUploadChooser()
                }
            }
        }
    }

    private fun imageUploadChooser() {
        Tools.uploadImageChooser(
            activity!!,
            resources.getString(R.string.choose),
            resources.getString(R.string.close)
        ) {
            when (it) {
                0 -> {
                    takeImageFromCamera()
                }
                1 -> {
                    takeImageFromGallery()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayCompletedFields() {
        itemView!!.emailAddressDeveloperView.text =
            UserPreference.getData(UserPreference.EMAIL_ADDRESS)
        itemView!!.userRoleViw.text = "Role: ${UserPreference.getData(UserPreference.ROLE)}"
    }

    private fun takeImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, TAKE_IMAGE_FROM_GALLERY_REQUEST_CODE)
    }

    private fun takeImageFromCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Picture from Camera")
        imageUri = activity!!.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )

        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(camera, TAKE_IMAGE_FROM_CAMERA_REQUEST_CODE)
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun manageSkillRecyclerView() {
        itemView!!.skillsRecyclerView.layoutManager =
            LinearLayoutManager(
                activity!!.applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        skillsRecyclerAdapter = SkillsRecyclerAdapter(skills)
        itemView!!.skillsRecyclerView.adapter = skillsRecyclerAdapter
        skillsRecyclerAdapter.notifyDataSetChanged()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_IMAGE_FROM_CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(itemView!!.selectProfileImageDeveloper)
                saveimage(imageUri!!)
                imagePictureSelected = true
            }
        } else if (requestCode == TAKE_IMAGE_FROM_GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Glide.with(this).load(data!!.data).into(itemView!!.selectProfileImageDeveloper)
            saveimage(data?.data!!)
            imagePictureSelected = true
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun saveimage(imageuri:Uri){
        var downloadurl:String=""
        val randomname:String= UUID.randomUUID().toString()
        val ref= FirebaseStorage.getInstance().getReference("images/$randomname")
        ref.putFile(imageuri!!).addOnSuccessListener {
            Toast.makeText(App.appInstance?.applicationContext,"image was uploaded successfully", Toast.LENGTH_SHORT).show()
            var downloadurl:String=""
            //get image url
            ref.downloadUrl.addOnSuccessListener {
                downloadurl=it.toString()
            }
        }.addOnFailureListener(){
            Toast.makeText(App.appInstance?.applicationContext,it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkReadExternalStoragePermission() = ActivityCompat.checkSelfPermission(
        activity!!.applicationContext,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun checkWriteExternalStoragePermission() = ActivityCompat.checkSelfPermission(
        activity!!.applicationContext,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun checkCameraPermission() = ActivityCompat.checkSelfPermission(
        activity!!.applicationContext,
        android.Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED


    companion object {
        const val PERMISSION_REQUEST_CODE = 10
        const val TAKE_IMAGE_FROM_GALLERY_REQUEST_CODE = 20
        const val TAKE_IMAGE_FROM_CAMERA_REQUEST_CODE = 30
    }
}