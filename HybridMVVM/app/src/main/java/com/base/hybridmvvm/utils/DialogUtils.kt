package com.base.hybridmvvm.utils

import android.content.Context
import com.base.hybridmvvm.ui.custom.PermissionDialog

object DialogUtils {
    fun showPermissionDialog(
        context: Context,
        title: String,
        content: String,
        positiveButtonText: String,
        onPositiveButtonClick: () -> Unit,
        negativeButtonText: String?,
        onNegativeButtonClick: (() -> Unit)?,
    ) {
        PermissionDialog(
            context = context,
            title = title,
            content = content,
            positiveButtonText = positiveButtonText,
            onPositiveButtonClick = onPositiveButtonClick,
            negativeButtonText = negativeButtonText,
            onNegativeButtonClick = onNegativeButtonClick
        ).show()
    }
}