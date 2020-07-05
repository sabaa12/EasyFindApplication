package com.example.easyfindapp.tools

import android.app.Dialog
import android.content.Context
import android.util.Patterns
import android.view.ViewGroup
import com.example.easyfindapp.R
import kotlinx.android.synthetic.main.error_dialog_layout.*

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
}