package com.example.githubsearch.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubsearch.api.Result
import com.example.githubsearch.data.model.SearchDataResponse
import com.example.githubsearch.data.source.remote.repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository): ViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<SearchDataResponse.Items>>? = null
    val userLiveData = MutableLiveData<SearchDataResponse.Items?>()

    fun searchUsers(queryString: String): Flow<PagingData<SearchDataResponse.Items>> {
        currentQueryValue = queryString
        val newResult: Flow<PagingData<SearchDataResponse.Items>> =
            repository.getSearchResultStream(queryString).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun fetchUserDetail(login: String) {
        viewModelScope.launch {
            if (repository.fetchUserDetail(login) is Result.Success){
                val result = repository.fetchUserDetail(login) as Result.Success
                userLiveData.postValue(result.data)
            }

        }
    }
}