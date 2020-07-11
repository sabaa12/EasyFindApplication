package com.example.easyfindapp.tools

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.easyfindapp.R
import com.example.easyfindapp.activities.CreatePostEmployerActivity
import com.example.easyfindapp.models.ItemModel
import com.example.easyfindapp.user_preference.UserPreference
import kotlinx.android.synthetic.main.choose_experience.*
import kotlinx.android.synthetic.main.error_dialog_layout.*
import kotlinx.android.synthetic.main.error_dialog_layout.closeDialogView
import kotlinx.android.synthetic.main.image_uploader_chooser.*

object Tools {
    fun emailIsValid(text: String) = Patterns.EMAIL_ADDRESS.matcher(text).matches()

    fun errorDialog(context: Context, title: String, description: String, closeDialogText: String) {
        val errorDialog = Dialog(context)
        errorDialog.setContentView(R.layout.error_dialog_layout)
        errorDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val params: ViewGroup.LayoutParams = errorDialog.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        errorDialog.errorTitle.text = title
        errorDialog.errorDescription.text = description
        errorDialog.closeDialogView.text = closeDialogText
        errorDialog.show()
        errorDialog.closeDialogView.setOnClickListener {
            errorDialog.dismiss()
        }
    }

    fun chooserDialog(context: Activity,iemModel:ItemModel)  {
        val chooserdialog = Dialog(context)
        var clikeditem=""

        chooserdialog.setContentView(R.layout.choose_experience)
        chooserdialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val params: ViewGroup.LayoutParams = chooserdialog.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        chooserdialog.show()
        chooserdialog.entryid.setOnClickListener(){
            chooserdialog.entryclikedid.visibility=View.VISIBLE
            clikeditem="Entry level"

            chooserdialog.interchoceid.visibility=View.INVISIBLE
            chooserdialog.expertchoseid.visibility=View.INVISIBLE
        }
        chooserdialog.expertid.setOnClickListener(){
            chooserdialog.entryclikedid.visibility=View.INVISIBLE
            clikeditem="Expert"

            chooserdialog.interchoceid.visibility=View.INVISIBLE
            chooserdialog.expertchoseid.visibility=View.VISIBLE
        }
        chooserdialog.intermedieteid.setOnClickListener(){
            chooserdialog.entryclikedid.visibility=View.INVISIBLE
            clikeditem="Intermediate"

            chooserdialog.interchoceid.visibility=View.VISIBLE
            chooserdialog.expertchoseid.visibility=View.INVISIBLE
        }
        chooserdialog.saveexperience.setOnClickListener {
            iemModel.setvalue(clikeditem)
            chooserdialog.dismiss()

        }

    }

    fun uploadImageChooser(
        context: Context,
        title: String,
        closeDialogText: String,
        selectedChooser: (Int) -> Unit
    ) {
        val imageChooser = Dialog(context)
        imageChooser.setContentView(R.layout.image_uploader_chooser)
        imageChooser.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val params: ViewGroup.LayoutParams = imageChooser.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        imageChooser.imageChooserTitle.text = title
        imageChooser.closeDialogView.text = closeDialogText
        imageChooser.show()
        imageChooser.selectCamera.setOnClickListener {
            selectedChooser.invoke(0)
            imageChooser.dismiss()
        }

        imageChooser.selectGallery.setOnClickListener {
            selectedChooser.invoke(1)
            imageChooser.dismiss()
        }
        imageChooser.closeDialogView.setOnClickListener {
            imageChooser.dismiss()
        }
    }

    fun saveUserInformation(userID: String, role: String, emailAddress: String) {
        UserPreference.saveData(UserPreference.USER_ID, userID)
        UserPreference.saveData(UserPreference.ROLE, role)
        UserPreference.saveData(UserPreference.EMAIL_ADDRESS, emailAddress)
    }
}