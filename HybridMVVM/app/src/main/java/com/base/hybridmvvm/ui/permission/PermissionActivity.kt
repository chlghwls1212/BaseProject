package com.base.hybridmvvm.ui.permission

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.base.hybridmvvm.data.model.dangerousPermissions
import com.base.hybridmvvm.databinding.ActivityPermissionBinding
import com.base.hybridmvvm.ui.base.BaseActivity
import com.base.hybridmvvm.ui.main.MainActivity
import com.base.hybridmvvm.utils.PermissionUtils

class PermissionActivity : BaseActivity() {

    private lateinit var binding: ActivityPermissionBinding
    private val viewModel: PermissionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.permissionList.adapter = PermissionsAdapter(this, dangerousPermissions, viewModel)

        // 리스너 등록
        setListener()

        observeViewModel()
    }

    private fun setListener() {
        binding.MainNavBtn.setOnClickListener {
            navigateToMain()
        }

        binding.PermReqBtn.setOnClickListener {
            viewModel.requestPermission(this)
        }
    }

    private fun observeViewModel() {
        viewModel.permissionsGranted.observe(this, Observer { granted ->
            if (granted) {
                onPermissionsGranted()
            }
        })

        viewModel.deniedPermissions.observe(this, Observer { deniedPermissions ->
            showPermissionRationale(deniedPermissions)
        })
    }

    private fun showPermissionRationale(deniedPermissions: List<String>) {
        val rationalePermissions = PermissionUtils.getRationalePermissions(deniedPermissions)
        val showRationale = deniedPermissions.any { permission ->
            viewModel.shouldShowRationale(this,permission)
        }

        AlertDialog.Builder(this)
            .setTitle("권한요청")
            .setMessage("필수 $rationalePermissions 권한이 수락되지 않았습니다.")
            .setPositiveButton(if (showRationale) "확인" else "환경 설정") { _, _ ->
                if (showRationale) {
                    viewModel.requestPermission(this,deniedPermissions)
                } else {
                    viewModel.openAppSettings(this)
                }
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults) { granted, deniedPermissions ->
            if (granted) {
                onPermissionsGranted()
            } else {
                viewModel.requestPermission(this,deniedPermissions)
            }
        }
    }

    private fun onPermissionsGranted() {
        //Todo 권한 수락 이후
        navigateToMain()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}