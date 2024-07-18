package com.base.hybridmvvm.ui.main

import androidx.lifecycle.viewModelScope
import com.base.hybridmvvm.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : BaseViewModel() {

    fun loadData() {
        viewModelScope.launch {
            // 데이터 로드 로직
        }
    }
}
