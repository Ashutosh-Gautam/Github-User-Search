package com.example.githubsearch.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GithubApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}