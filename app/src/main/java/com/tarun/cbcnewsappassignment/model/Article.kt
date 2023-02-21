package com.tarun.cbcnewsappassignment.model

import com.google.gson.annotations.SerializedName

/**
 * Article represents a news article fetched from the CBC API.
 */
data class Article(val id: Int,
                   val title: String,
                   val publishedAt: Long,
                   val updatedAt: Long,
                   val type: String,
                   @SerializedName("typeAttributes") val attributes: TypeAttributes)