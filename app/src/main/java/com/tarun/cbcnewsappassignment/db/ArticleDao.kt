package com.tarun.cbcnewsappassignment.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tarun.cbcnewsappassignment.model.Article

@Dao
interface ArticleDao {
    /**
     * Retrieves all articles from DB in descending order by publishing date.
     */
    @Query("SELECT * FROM article_table ORDER BY published_at DESC")
    fun getArticles(): LiveData<List<Article>>

    /**
     * Retrieves all articles from DB filtered by the [articleType] argument in descending order
     * by publishing date.
     */
    @Query("SELECT * FROM article_table WHERE type LIKE :articleType ORDER BY published_at DESC")
    fun getArticlesByType(articleType: String): LiveData<List<Article>>

    /**
     * Insert the list of articles in the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles: List<Article>)

    /**
     * Delete all articles from the database.
     */
    @Query("DELETE FROM article_table")
    suspend fun deleteAll()
}