package com.example.githubsearch.di

import com.example.githubsearch.api.ApiInterface
import com.example.githubsearch.data.source.remote.repositories.SearchRepository
import com.example.githubsearch.ui.viewmodelfactory.SearchViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideSearchRepository(apiInterface: ApiInterface): SearchRepository {
        return SearchRepository(apiInterface)
    }

    @Provides
    @Singleton
    fun provideSearchViewModelFactory(repository: SearchRepository): SearchViewModelFactory {
        return SearchViewModelFactory(repository)
    }
}