package com.tarun.cbcnewsappassignment.data

import androidx.lifecycle.LiveData
import com.tarun.cbcnewsappassignment.api.CBCNewsApiService
import com.tarun.cbcnewsappassignment.db.ArticleDao
import com.tarun.cbcnewsappassignment.model.Article
import kotlinx.coroutines.withTimeout
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Repository class to handle the operation of retrieving the data requested.
 */
class CBCNewsRepository : KoinComponent {
    private val apiService: CBCNewsApiService by inject()

    private val articleDao: ArticleDao by inject()
    val articles: LiveData<List<Article>> by lazy { articleDao.getArticles() }

    /**
     * Fetches the list of articles from API.
     * @param page The page number to fetch the list from.
     */
    suspend fun fetchArticles(page: Int = 1) {
        try {
            val result = withTimeout(10_000) {
                    apiService.fetchNewsArticles(page)
            }

            // Save articles to the Database
            result.let { articleDao.insert(it) }
        } catch (error: Throwable) {
            throw FetchArticlesError("Unable to fetch articles", error)
        }
    }
}

class FetchArticlesError(message: String, cause: Throwable) : Throwable(message, cause)