package com.pixelart.mavelcomics.ui.comiclist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pixelart.mavelcomics.models.Comic
import com.pixelart.mavelcomics.repository.ComicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ComicListViewModel @Inject constructor(
    private val repository: ComicsRepository
) : ViewModel() {

    fun fetchComics(): LiveData<PagingData<Comic>> {
        return repository.fetchComics().asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
            .cachedIn(viewModelScope)
    }
}
