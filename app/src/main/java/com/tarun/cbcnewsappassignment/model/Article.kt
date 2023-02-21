package com.tarun.cbcnewsappassignment.model

import com.google.gson.annotations.SerializedName

/**
 * Article represents a news article fetched from the CBC API.
 */
data class Article(val id: Int,
                   val title: String,
                   @SerializedName("published_at") val publishedAt: Long,
                   @SerializedName("updated_at") val updatedAt: Long,
                   @SerializedName("type") val type: String,
                   @SerializedName("typeAttributes") val attributes: TypeAttributes)