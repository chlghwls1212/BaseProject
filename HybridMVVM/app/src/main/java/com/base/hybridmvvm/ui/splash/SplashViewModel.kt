package com.base.hybridmvvm.ui.splash

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.hybridmvvm.data.model.SplashRequest
import com.base.hybridmvvm.data.model.SplashResponse
import com.base.hybridmvvm.data.repository.SplashRepository
import com.base.hybridmvvm.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    application: Application,
    private val splashRepository: SplashRepository
) : BaseViewModel(application) {

    private val _splashResponse = MutableLiveData<SplashResponse?>()
    val splashResponse: LiveData<SplashResponse?> = _splashResponse

    private val _splashError = MutableLiveData<String?>()
    val splashError: LiveData<String?> = _splashError

    fun sendSplashRequest(deviceId: String) {
        viewModelScope.launch {
            try {
                val splashRequest = SplashRequest(deviceId)
                val response = splashRepository.sendSplashRequest(splashRequest)
                _splashResponse.value = response
            } catch (e: Exception) {
                _splashError.value = e.message
            }
        }
    }

}
