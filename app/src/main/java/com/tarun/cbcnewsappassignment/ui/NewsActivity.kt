package com.tarun.cbcnewsappassignment.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tarun.cbcnewsappassignment.R
import com.tarun.cbcnewsappassignment.databinding.ActivityNewsBinding
import com.tarun.cbcnewsappassignment.model.Article
import com.tarun.cbcnewsappassignment.ui.newsList.NewsListFragment
import com.tarun.cbcnewsappassignment.util.shouldShow
import com.tarun.cbcnewsappassignment.viewmodel.SharedNewsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Base activity for showing the fragments.
 */
class NewsActivity : AppCompatActivity() {

    private lateinit var ui: ActivityNewsBinding
    private val viewModel: SharedNewsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(ui.root)

        observeNetworkConnectivityStatus()

        setupBottomNavView()
    }

    /**
     * Sets up the bottom navigation view.
     */
    private fun setupBottomNavView() {
        val headlinesFragment = NewsListFragment.newInstance(Article.ArticleType.NONE.type)
        val storiesFragment = NewsListFragment.newInstance(Article.ArticleType.STORY.type)
        val videosFragment = NewsListFragment.newInstance(Article.ArticleType.VIDEO.type)

        ui.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.headlines -> setFragment(headlinesFragment)
                R.id.story -> setFragment(storiesFragment)
                R.id.video -> setFragment(videosFragment)
            }
            true
        }

        // Set the Headlines fragment initially.
        setFragment(headlinesFragment)
    }

    private fun setFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()

    /**
     * Observe the network connectivity status to take appropriate actions.
     */
    private fun observeNetworkConnectivityStatus() {
        viewModel.isNetworkConnectivityAvailable.observe(this) {
            // Show/hide offline view based on network connection status.
            ui.offlineView.shouldShow(!it)
            if (it) viewModel.networkConnectivityRestored()
        }
    }
}