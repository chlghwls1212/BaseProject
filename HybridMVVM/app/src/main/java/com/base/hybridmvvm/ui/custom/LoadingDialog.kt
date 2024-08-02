package com.base.hybridmvvm.ui.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import com.base.hybridmvvm.databinding.LoadingDialogBinding

class LoadingDialog private constructor(context: Context) : Dialog(context) {

    private lateinit var binding: LoadingDialogBinding

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoadingDialogBinding.inflate(LayoutInflater.from(context))

        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    companion object {
        @Volatile
        private var instance: LoadingDialog? = null

        private fun getInstance(context: Context): LoadingDialog {
            return instance ?: synchronized(this) {
                instance ?: LoadingDialog(context).also { instance = it }
            }
        }

        fun showLoading(context: Context) {
            getInstance(context).show()
        }

        fun hideLoading() {
            instance?.dismiss()
        }
    }
}
