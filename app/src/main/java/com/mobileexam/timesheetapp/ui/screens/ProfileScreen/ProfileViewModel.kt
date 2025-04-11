package com.mobileexam.timesheetapp.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    // Mutable state for user details
    private val _fullName = MutableStateFlow("John Doe")
    val fullName: StateFlow<String> = _fullName

    private val _username = MutableStateFlow("johndoe")
    val username: StateFlow<String> = _username

    private val _email = MutableStateFlow("johndoe@example.com")
    val email: StateFlow<String> = _email

    private val _phoneNumber = MutableStateFlow("+63 912 345 6789")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri


    // Update functions
    fun updateFullName(newName: String) {
        _fullName.value = newName
    }

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePhoneNumber(newPhoneNumber: String) {
        _phoneNumber.value = newPhoneNumber
    }

//    fun updateProfileImage(newUri: Uri) {
//        _profileImageUri.value = newUri
//    }
}
