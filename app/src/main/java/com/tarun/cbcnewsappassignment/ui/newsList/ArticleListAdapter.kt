/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tarun.cbcnewsappassignment.ui.newsList

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tarun.cbcnewsappassignment.databinding.ViewHolderArticleBinding
import com.tarun.cbcnewsappassignment.model.Article

/**
 * Adapter for showing the list of news articles.
 */
class ArticleListAdapter :
    ListAdapter<Article, ArticleListAdapter.ArticleViewHolder>(ARTICLES_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    /**
     * View holder class for each item in Article list.
     * @param binding The binding instance for the ArticleViewHolder layout.
     */
    class ArticleViewHolder(private val binding: ViewHolderArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            loadImage(article.attributes.imageUrl)
            binding.articleHeading.text = article.title
            binding.datePublished.text = getPublishedAtTimeString(article.publishedAt)
        }

        private fun loadImage(imageUrl: String) {
            Glide
                .with(binding.articleImage.context)
                .load(imageUrl)
                .into(binding.articleImage)
        }

        private fun getPublishedAtTimeString(timestamp: Long): CharSequence {
            return DateUtils.getRelativeTimeSpanString(timestamp)
        }

        companion object {
            fun create(parent: ViewGroup): ArticleViewHolder {
                val itemArticleBinding =
                    ViewHolderArticleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ArticleViewHolder(itemArticleBinding)
            }
        }

    }

    companion object {
        private val ARTICLES_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.updatedAt == newItem.updatedAt
            }
        }
    }
}
