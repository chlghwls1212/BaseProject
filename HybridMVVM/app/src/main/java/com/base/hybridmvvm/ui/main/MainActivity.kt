package com.base.hybridmvvm.ui.main

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.viewModels
import com.base.hybridmvvm.databinding.ActivityMainBinding
import com.base.hybridmvvm.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webView: WebView = binding.mainWebView
        webView.loadUrl("file:///android_asset/TestPage.html");
    }
}