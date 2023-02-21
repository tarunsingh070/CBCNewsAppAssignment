package com.tarun.cbcnewsappassignment.data

import androidx.lifecycle.MutableLiveData
import com.tarun.cbcnewsappassignment.api.CBCNewsApiService
import com.tarun.cbcnewsappassignment.model.Article
import kotlinx.coroutines.withTimeout
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CBCNewsRepository : KoinComponent {
    private val apiService: CBCNewsApiService by inject()

    val articles: MutableLiveData<List<Article>>  = MutableLiveData()

    /**
     * Fetches the list of articles from API.
     * @param page The page number to fetch the list from.
     */
    suspend fun fetchArticles(page: Int = 1) {
        try {
            val result = withTimeout(10_000) {
                    apiService.fetchNewsArticles(page)
            }

            articles.value = result
        } catch (error: Throwable) {
            throw FetchArticlesError("Unable to fetch articles", error)
        }
    }
}

class FetchArticlesError(message: String, cause: Throwable) : Throwable(message, cause)