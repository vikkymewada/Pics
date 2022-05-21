package com.vikky.pics.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vikky.pics.data.DataRepoImpl
import com.vikky.pics.data.model.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class HomeViewModel @Inject constructor(
    private val repository: DataRepoImpl
) : ViewModel() {

    lateinit var photoList: Flow<PagingData<Photo>>

    init {
        fetchImages()
    }
    private fun fetchImages() {
        viewModelScope.launch {
            try {
                photoList = repository.getPhotos().cachedIn(viewModelScope)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}