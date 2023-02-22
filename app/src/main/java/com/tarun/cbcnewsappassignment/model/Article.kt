package com.tarun.cbcnewsappassignment.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Article represents a news article fetched from the CBC API.
 */
@Entity(tableName = "article_table")
data class Article(
    @PrimaryKey val id: Int,
    val title: String,
    @ColumnInfo(name = "published_at") val publishedAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @SerializedName("type") val type: String,
    @ColumnInfo(name = "type_attributes") @SerializedName("typeAttributes") val attributes: TypeAttributes
) {

    /**
     * Enum class representing the different types of articles that can be received.
     * @param type The article type string
     */
    enum class ArticleType(val type: String) { NONE(" "), STORY("story"), VIDEO("video") }
}