package com.test.toolboxmobile.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.toolboxmobile.data.model.Carousel
import com.test.toolboxmobile.data.repository.DataRepository
import com.test.toolboxmobile.utils.NetworkHelper
import com.test.toolboxmobile.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {

    private val _data = MutableLiveData<Resource<List<Carousel>>>()
    val data: LiveData<Resource<List<Carousel>>> get() = _data

    fun getData() {
        viewModelScope.launch {
            _data.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                dataRepository.data().let {
                    if (it.isSuccessful) {
                        _data.postValue(
                            Resource.success(
                                it.message(),
                                it.body()
                            )
                        )
                    } else {
                        _data.postValue(Resource.error("Error al obtener los datos", null))
                    }
                }
            } else {
                _data.postValue(Resource.error("No tiene acceso a internet", null))
            }
        }
    }

}