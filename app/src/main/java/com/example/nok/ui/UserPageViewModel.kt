package com.example.nok.ui

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class UserPageViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<SignOutState>(SignOutState.Nothing)
    val state = _state.asStateFlow()

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        _state.value = SignOutState.LoggedOut
    }
}

sealed class SignOutState {
    object Nothing: SignOutState()
    object LoggedOut: SignOutState()
}