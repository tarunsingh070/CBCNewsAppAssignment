package com.tarun.cbcnewsappassignment.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tarun.cbcnewsappassignment.R
import com.tarun.cbcnewsappassignment.databinding.ActivityNewsBinding
import com.tarun.cbcnewsappassignment.ui.newsList.NewsListFragment
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

        setFragment(NewsListFragment.newInstance())
    }

    private fun setFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()

    private fun observeNetworkConnectivityStatus() {
        viewModel.isNetworkConnectivityAvailable.observe(this) {
            // Show/hide offline view based on network connection status.
            ui.offlineView.visibility = if (it) {
                // Reload the articles
                viewModel.reloadArticles()
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }
}