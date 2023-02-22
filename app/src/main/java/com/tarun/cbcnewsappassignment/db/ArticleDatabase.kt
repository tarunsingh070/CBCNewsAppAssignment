package com.tarun.cbcnewsappassignment.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tarun.cbcnewsappassignment.model.Article

/**
 * The main [RoomDatabase] class used to create a database for storing Articles.
 */
@Database(entities = [Article::class], version = 1)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}