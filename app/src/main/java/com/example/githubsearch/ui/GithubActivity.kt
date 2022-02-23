package com.example.githubsearch.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.githubsearch.R
import com.example.githubsearch.adapters.UserAdapter
import com.example.githubsearch.databinding.ActivityMainBinding
import com.example.githubsearch.ui.viewmodelfactory.SearchViewModelFactory
import com.example.githubsearch.ui.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class GithubActivity : AppCompatActivity() {

    private var searchJob: Job? = null
    private lateinit var binding : ActivityMainBinding
    @Inject
    lateinit var factory: SearchViewModelFactory
    private lateinit var viewModel: SearchViewModel
    private val adapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        setupUI()
        fetchUsers(getString(R.string.default_query))
    }

    private fun setupUI() {
        binding.container.rcView.adapter = adapter
        binding.container.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
        binding.container.swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        );
    }

    private fun fetchUsers(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchUsers(query).collectLatest {
                binding.container.swipeRefreshLayout.isRefreshing = false
                adapter.submitData(it)
            }
        }
        lifecycleScope.launch{
            adapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.refresh is LoadState.Loading){
                    binding.container.empty.isVisible = true
                    binding.container.rcView.isVisible = false
                    binding.container.emptyText.text = getString(R.string.loading)
                } else if (loadStates.refresh is LoadState.Error && adapter.itemCount == 0){
                    binding.container.empty.isVisible = true
                    binding.container.rcView.isVisible = false
                    binding.container.emptyText.text = getString(R.string.no_data)
                } else if (loadStates.refresh is LoadState.NotLoading && adapter.itemCount == 0){
                    binding.container.rcView.isVisible = false
                    binding.container.empty.isVisible = true
                    binding.container.emptyText.text = getString(com.example.githubsearch.R.string.no_data)
                } else if (loadStates.refresh is LoadState.NotLoading && adapter.itemCount > 0){
                    binding.container.rcView.isVisible = true
                    binding.container.empty.isVisible = false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val mSearch: MenuItem = menu.findItem(R.id.appSearchBar)
        val mSearchView: SearchView = mSearch.actionView as SearchView
        mSearchView.queryHint = getString(R.string.search)
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { fetchUsers(query) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchJob?.cancel()
    }
}