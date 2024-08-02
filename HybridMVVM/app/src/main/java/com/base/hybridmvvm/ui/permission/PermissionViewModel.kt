package com.base.hybridmvvm.ui.permission

import android.app.Application
import com.base.hybridmvvm.BaseApplication
import com.base.hybridmvvm.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(application: Application) :
    BaseViewModel(application)
