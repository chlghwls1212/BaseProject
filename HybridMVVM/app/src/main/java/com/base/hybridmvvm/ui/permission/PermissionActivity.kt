package com.base.hybridmvvm.ui.permission

import android.content.Intent
import android.os.Bundle
import com.base.hybridmvvm.databinding.ActivityPermissionBinding
import com.base.hybridmvvm.ui.base.BaseActivity
import com.base.hybridmvvm.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PermissionActivity : BaseActivity() {

    private lateinit var binding: ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun requestPermission(){

    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}