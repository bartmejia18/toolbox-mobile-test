package com.test.toolboxmobile.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.test.toolboxmobile.data.model.Auth
import com.test.toolboxmobile.data.repository.AuthRepository
import com.test.toolboxmobile.utils.NetworkHelper
import com.test.toolboxmobile.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {

    private val _auth = MutableLiveData<Resource<Auth>>()
    val auth: LiveData<Resource<Auth>> get() = _auth

    fun doLogin(sub: JsonObject){
        viewModelScope.launch {
            _auth.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                Log.d("--->", "$sub")
                authRepository.auth(sub).let {
                    if (it.isSuccessful) {
                        Log.d("--->", "isSuccessfull")
                        _auth.postValue(
                            Resource.success(
                                it.message(),
                                it.body()
                            )
                        )
                    } else {
                        _auth.postValue(
                            Resource.error("Error al iniciar sesión, verifique su información", null)
                        )
                    }
                }
            } else {
                _auth.postValue(Resource.error("No tiene accesso a internert", null))
            }
        }
    }

}