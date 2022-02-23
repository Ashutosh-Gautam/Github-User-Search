package com.example.githubsearch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubsearch.R
import com.example.githubsearch.databinding.UserDetailBinding
import com.example.githubsearch.ui.viewmodelfactory.SearchViewModelFactory
import com.example.githubsearch.ui.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GithubUserDetailActivity : AppCompatActivity() {
    private lateinit var binding : UserDetailBinding
    @Inject
    lateinit var factory: SearchViewModelFactory
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        val avatar = intent.getStringExtra("Avtar")
        val name = intent.getStringExtra("Name")
        Glide.with(this)
            .load(avatar)
            .into(binding.detailImage)
        binding.detailName.text = name
        name?.let { fetchUserDetail(it) }
        binding.toolbar.setNavigationOnClickListener { view ->
            onBackPressed()
        }
    }

    private fun fetchUserDetail(name: String){
        viewModel.fetchUserDetail(name)
        viewModel.userLiveData.observe(this, { data ->
            binding.repo.text = getString(R.string.company).plus(data?.company ?: "")
            binding.score.text = getString(R.string.location).plus(data?.location ?: "")
            binding.followers.text = getString(R.string.followers).plus(data?.followers.toString())
            binding.following.text = getString(R.string.following).plus(data?.following.toString())
        })
    }
}