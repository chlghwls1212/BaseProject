package com.base.hybridmvvm.ui.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.base.hybridmvvm.databinding.PermissionDialogBinding
import com.base.hybridmvvm.ui.base.BaseCustomDialog

class PermissionDialog(
    context: Context,
    private val title: String,
    private val content: String,
    private val positiveButtonText: String,
    private val negativeButtonText: String?,
    private val onPositiveButtonClick: () -> Unit,
    private val onNegativeButtonClick: (() -> Unit)?
) : BaseCustomDialog(context) {

    private lateinit var binding: PermissionDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = PermissionDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 백그라운드 컬러 투명 (이걸 해줘야 background가 설정해준 모양으로 변함)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 설정한 텍스트 값들을 ViewBinding을 통해 설정
        binding.permTitle.text = title
        binding.permContent.text = content

        // Positive 버튼 설정
        binding.positiveButton.apply {
            text = positiveButtonText
            setOnClickListener {
                onPositiveButtonClick()
                dismiss()
            }
        }

        // Negative 버튼 설정 (옵션)
        if (negativeButtonText != null) {
            binding.negativeButton.apply {
                text = negativeButtonText
                setOnClickListener {
                    onNegativeButtonClick?.invoke()
                    dismiss()
                }
            }
        } else {
            binding.negativeButton.visibility = View.GONE
        }
    }

    override fun getLayoutResourceId(): Int {
        // ViewBinding을 사용하기 때문에 레이아웃 리소스 ID는 필요하지 않습니다.
        return 0
    }

    override fun getPositiveButtonId(): Int {
        return 0
    }

    override fun getNegativeButtonId(): Int {
        return 0
    }

    override fun onPositiveButtonClick() {
        onPositiveButtonClick.invoke()
    }

    override fun onNegativeButtonClick() {
        onNegativeButtonClick?.invoke()
    }
}
