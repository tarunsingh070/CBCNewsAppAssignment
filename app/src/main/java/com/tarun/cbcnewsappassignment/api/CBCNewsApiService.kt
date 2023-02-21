package com.tarun.cbcnewsappassignment.api

import com.tarun.cbcnewsappassignment.model.Article
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * CBC News network service interface to fetch news articles from the API.
 */
interface CBCNewsApiService {
    /**
     * Fetches a list of [Article].
     * @param page The page number to fetch the list of [Article] from.
     */
    @GET("items?lineupSlug=news")
    suspend fun fetchNewsArticles(@Query("page") page: Int): List<Article>
}