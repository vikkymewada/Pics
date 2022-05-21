package com.vikky.pics.ui.home


import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.vikky.pics.Constants
import com.vikky.pics.R
import com.vikky.pics.data.model.Photo
import com.vikky.pics.ui.detail.DetailActivity
import com.vikky.pics.ui.home.adapter.PagingLoadStateAdapter
import com.vikky.pics.ui.home.adapter.PhotoAdapter
import com.vikky.pics.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
@ExperimentalPagingApi
class HomeActivity : AppCompatActivity(), PhotoAdapter.PhotoClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var view: CoordinatorLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var imageAdapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        if (!Util.isNetworkConnected()) {
            showError(R.string.no_internet)
            return
        }

        lifecycleScope.launchWhenResumed {
            viewModel.photoList.collectLatest { imageAdapter.submitData(it) }
        }
        lifecycleScope.launchWhenResumed {
            imageAdapter.loadStateFlow.collectLatest {
                swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }
        lifecycleScope.launchWhenResumed {
            imageAdapter.loadStateFlow.collect { loadState ->
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                    ?: loadState.source.refresh as? LoadState.Error
                errorState?.let {
                    showError(R.string.error_occurred)
                }
            }
        }
    }

    private fun initViews() {
        view = findViewById(R.id.coordinator)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            resources.getInteger(R.integer.span_count),
            StaggeredGridLayoutManager.VERTICAL
        )
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = staggeredGridLayoutManager
        imageAdapter = PhotoAdapter()
        recyclerView.adapter = imageAdapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(imageAdapter),
            footer = PagingLoadStateAdapter(imageAdapter)
        )

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            if (Util.isNetworkConnected()) {
                imageAdapter.refresh()
            } else {
                showError(R.string.no_internet)
            }
        }
        imageAdapter.listener = this@HomeActivity
    }

    private fun showError(resId: Int) {
        swipeRefreshLayout.isRefreshing = false
        if (!Util.isNetworkConnected()) {
            Snackbar.make(view,R.string.no_internet, Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(view,resId, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onPhotoClick(view: ImageView, item: Photo) {
        val intent = Intent(this@HomeActivity, DetailActivity::class.java)
        val options = ActivityOptions.makeSceneTransitionAnimation(
            this,
            view,
            Constants.SHARED_ELEMENT_TAG
        )
        intent.putExtra(Constants.SHARED_ELEMENT_TAG, item)
        startActivity(intent, options.toBundle())
    }
}