package com.github.panpf.noad.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.panpf.noad.base.LifecycleAndroidViewModel
import com.github.panpf.noad.service.SkipAdAccessibilityService
import com.github.panpf.tools4a.service.ktx.isAccessibilityServiceEnabled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SkipAdViewModel(application: Application) : LifecycleAndroidViewModel(application) {

    val powerStateData = MutableLiveData(false)

    init {
        refreshPowerState()
    }

    fun refreshPowerState() {
        viewModelScope.launch {
            val powerOn = withContext(Dispatchers.IO) {
                application1.isAccessibilityServiceEnabled(SkipAdAccessibilityService::class.java)
            }
            powerStateData.postValue(powerOn)
        }
    }
}