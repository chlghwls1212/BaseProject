package com.base.hybridmvvm.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import timber.log.Timber

abstract class BaseCustomDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(getPositiveButtonId(), getNegativeButtonId())
    }

    protected abstract fun getLayoutResourceId(): Int
    protected open fun getPositiveButtonId(): Int? = null
    protected open fun getNegativeButtonId(): Int? = null

    private fun initialize(positiveButtonId: Int?, negativeButtonId: Int?) {
        positiveButtonId?.let {
            val positiveButton: Button? = findViewById(it)
            positiveButton?.setOnClickListener {
                onPositiveButtonClick()
                dismiss()
            }
        }

        negativeButtonId?.let {
            val negativeButton: Button? = findViewById(it)
            negativeButton?.setOnClickListener {
                onNegativeButtonClick()
                dismiss()
            }
        }
    }

    protected open fun onPositiveButtonClick() {
        // 기본 OK 버튼 클릭 동작
        Timber.d("PositiveButton Test")
    }

    protected open fun onNegativeButtonClick() {
        // 기본 Cancel 버튼 클릭 동작
        Timber.d("NegativeButton Test")

    }
}

