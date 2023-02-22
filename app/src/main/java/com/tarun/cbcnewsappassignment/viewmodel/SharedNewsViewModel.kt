package com.tarun.cbcnewsappassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarun.cbcnewsappassignment.data.CBCNewsRepository
import com.tarun.cbcnewsappassignment.data.FetchArticlesError
import com.tarun.cbcnewsappassignment.model.Article
import com.tarun.cbcnewsappassignment.util.NetworkMonitoringService
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * The Viewmodel class which is shared between the NewsActivity and NewsListFragment.
 */
class SharedNewsViewModel : ViewModel(), KoinComponent {
    private val repository: CBCNewsRepository by inject()

    // Live lists of Articles to be shown in the list of news articles.
    val headlineArticles: LiveData<List<Article>> by lazy { repository.articles }
    val storyArticles: LiveData<List<Article>> by lazy { repository.storyArticles }
    val videoArticles: LiveData<List<Article>> by lazy { repository.videoArticles }

    // Live boolean which stores the current network connectivity status
    private var _isNetworkConnectivityAvailable: MutableLiveData<Boolean> = MutableLiveData()
    val isNetworkConnectivityAvailable: LiveData<Boolean> = _isNetworkConnectivityAvailable

    // This keeps a track of what page will be loaded.
    var page: Int = 1

    init {
        monitorNetworkStatus()
        fetchArticles(page)
    }

    /**
     * This method gets called when a user has reached the end of list while scrolling.
     */
    fun userReachedEndOfList() {
        page++
        fetchArticles(page)
    }

    /**
     * Fetches the list of latest articles.
     * @param page The page number to be fetched
     */
    private fun fetchArticles(page: Int) = launchDataLoad {
        repository.fetchArticles(page)
    }

    /**
     * Reloads the list of latest articles using the same page number as was used last when offline.
     */
    fun reloadArticles() = launchDataLoad {
        repository.fetchArticles(page)
    }

    /**
     * Helper function to fetch the articles from API by launches a new coroutine without
     * blocking the current thread.
     *
     * @param block lambda to fetch the data via repository
     */
    private fun launchDataLoad(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (error: FetchArticlesError) {
                // Todo: Handle error
            } finally {
                // do something
            }
        }
    }

    /**
     * Starts monitoring the network connectivity status and performs operations based on the status.
     */
    private fun monitorNetworkStatus() {
        val networkMonitoringService = NetworkMonitoringService()
            .apply { this.isNetworkConnectivityAvailable = _isNetworkConnectivityAvailable }
        // When the app has just launched, only set the connectivity status explicitly if
        // no connection is available so as to not trigger observer if internet is already available.
        if (!networkMonitoringService.getInitialConnectionStatus()) {
            _isNetworkConnectivityAvailable.value = false
        } else {
            // Since, the device has network connectivity, so we can safely delete the
            // cached articles from DB as the updated articles will be fetched from server.
            deleteAllArticlesFromDatabase()
        }

        networkMonitoringService.monitorNetworkStatus()
    }

    /**
     * Delete all articles cached in the database.
     */
    private fun deleteAllArticlesFromDatabase() = launchDataLoad {
        repository.deleteAllArticlesFromDatabase()
    }
}