package com.tarun.cbcnewsappassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarun.cbcnewsappassignment.data.ArticleRepository
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
    private val repository: ArticleRepository by inject()

    // Live lists of Articles to be shown in the list of news articles.
    val headlineArticles: LiveData<List<Article>> by lazy { repository.articles }
    val storyArticles: LiveData<List<Article>> by lazy { repository.storyArticles }
    val videoArticles: LiveData<List<Article>> by lazy { repository.videoArticles }

    // Live boolean which stores the current network connectivity status.
    private var _isNetworkConnectivityAvailable: MutableLiveData<Boolean> = MutableLiveData()
    val isNetworkConnectivityAvailable: LiveData<Boolean> = _isNetworkConnectivityAvailable

    // Live boolean indicating if data is currently being loaded or not.
    private var _isLoadingData: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadingData: LiveData<Boolean> = _isLoadingData

    // Live string to store error message that came as a result of failure to load data.
    private var _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    // This keeps a track of what page should be loaded.
    private var page: Int = 1

    init {
        monitorNetworkStatus()
        fetchArticles(page)
    }

    /**
     * This method gets called when a user has reached the end of list while scrolling.
     */
    fun userReachedEndOfList() {
        fetchArticles(++page)
    }

    /**
     * Handles the event when network connection has been rstored.
     */
    fun networkConnectivityRestored() {
        reloadArticles()
    }

    /**
     * Handles the event when user tapped the Error message view.
     */
    fun errorMessageTapped() {
        reloadArticles()
        // Reset the error message value.
        _errorMsg.value = null
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
    private fun reloadArticles() = launchDataLoad {
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
                _isLoadingData.value = true
                block()
            } catch (error: FetchArticlesError) {
                _errorMsg.value = error.message
            } finally {
                _isLoadingData.value = false
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