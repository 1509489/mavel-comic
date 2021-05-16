package com.pixelart.mavelcomics.ui.comicdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pixelart.mavelcomics.datasource.NetworkResource
import com.pixelart.mavelcomics.models.Comic
import com.pixelart.mavelcomics.repository.ComicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ComicDetailViewModel @Inject constructor(
    private val repository: ComicsRepository
) : ViewModel() {
    fun fetchComic(id: String): LiveData<NetworkResource<Comic>> {
        return repository.fetchComic(id)
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    }
}
