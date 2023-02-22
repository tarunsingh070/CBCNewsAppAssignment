package com.tarun.cbcnewsappassignment.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tarun.cbcnewsappassignment.db.ArticleDao
import com.tarun.cbcnewsappassignment.db.ArticleDatabase
import com.tarun.cbcnewsappassignment.model.Article
import com.tarun.cbcnewsappassignment.model.TypeAttributes
import com.tarun.cbcnewsappassignment.observeOnce
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class ArticleDaoTest {

    private lateinit var articleDao: ArticleDao
    private lateinit var db: ArticleDatabase
    private val articles = getDummyArticlesList()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, ArticleDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        articleDao = db.articleDao()

        // Save some articles to the in memory database to run test upon.
        runBlocking {
            articleDao.insert(articles)
        }
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetArticles() = runBlocking {
        // Fetch the 3 articles inserted earlier and do a data integrity check.
        articleDao.getArticles().observeOnce {
            assertEquals(it.size, articles.size)
            assert(it.contains(articles[0]))
            assert(it.contains(articles[1]))
            assert(it.contains(articles[2]))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteArticles() = runBlocking {
        // Before testing the delete operation, verify that the DB indeed contains the articles.
        articleDao.getArticles().observeOnce {
            assertEquals(it.size, articles.size)
        }

        // Verify that after running deletion operation, DB returns zero articles.
        articleDao.deleteAll()
        articleDao.getArticles().observeOnce {
            assertEquals(it.size, 0)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getArticlesByType() = runBlocking {
        // Verify that the DB returns only the articles of the desired type.
        articleDao.getArticlesByType("video").observeOnce {
            assertEquals(it.size, 1)
        }
    }

    private fun getDummyArticlesList(): List<Article> {
        val article1 = Article(
            1, "Test headline 1", 1676971521424,
            1676975977769, "story", TypeAttributes("https://test-link")
        )
        val article2 = Article(
            2, "Test headline 2", 1676970000036,
            1676970000036, "video", TypeAttributes("https://test-link")
        )
        val article3 = Article(
            3, "Test headline 3", 1676973600518,
            1676973600518, "contentpackage", TypeAttributes("https://test-link")
        )
        return listOf(article1, article2, article3)
    }
}
