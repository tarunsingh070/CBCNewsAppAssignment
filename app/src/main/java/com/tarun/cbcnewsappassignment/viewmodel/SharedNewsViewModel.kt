package com.tarun.cbcnewsappassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarun.cbcnewsappassignment.data.CBCNewsRepository
import com.tarun.cbcnewsappassignment.data.FetchArticlesError
import com.tarun.cbcnewsappassignment.model.Article
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SharedNewsViewModel : ViewModel(), KoinComponent {
    private val repository: CBCNewsRepository by inject()
    //Live list of Articles to be shown in the list of news articles.
    val articles: LiveData<List<Article>> by lazy { repository.articles }

    // This keeps a track of what page will be loaded.
    var page: Int = 1

    init {
        fetchArticles(page)
    }

    /**
     * Fetches the list of updated articles.
     * @param page The page number to be fetched
     */
    private fun fetchArticles(page: Int) = launchDataLoad {
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
}