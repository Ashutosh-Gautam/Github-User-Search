package com.example.githubsearch.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubsearch.data.source.remote.repositories.SearchRepository
import com.example.githubsearch.ui.viewmodels.SearchViewModel

class SearchViewModelFactory(private val searchRepository: SearchRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(searchRepository) as T
    }
}