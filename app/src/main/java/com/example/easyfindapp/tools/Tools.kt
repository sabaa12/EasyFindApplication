package com.example.easyfindapp.tools

import android.app.Dialog
import android.content.Context
import android.util.Patterns
import android.view.ViewGroup
import com.example.easyfindapp.R
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
}