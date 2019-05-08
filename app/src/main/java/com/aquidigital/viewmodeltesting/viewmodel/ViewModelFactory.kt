package com.aquidigital.viewmodeltesting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aquidigital.viewmodeltesting.api.UserService
import com.aquidigital.viewmodeltesting.view.MainViewModel

class ViewModelFactory (private val userService: UserService
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(this.userService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
