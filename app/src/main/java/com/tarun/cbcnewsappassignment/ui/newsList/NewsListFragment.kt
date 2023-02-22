package com.tarun.cbcnewsappassignment.ui.newsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tarun.cbcnewsappassignment.R
import com.tarun.cbcnewsappassignment.databinding.FragmentNewsListBinding
import com.tarun.cbcnewsappassignment.model.Article
import com.tarun.cbcnewsappassignment.util.shouldShow
import com.tarun.cbcnewsappassignment.viewmodel.SharedNewsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

private const val ARG_ARTICLE_TYPE = "article_type"

/**
 * A fragment showing a list of news articles.
 * Use the [NewsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsListFragment : Fragment() {
    private lateinit var ui: FragmentNewsListBinding
    private lateinit var articleType: String
    private val viewModel: SharedNewsViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleType = it.getString(ARG_ARTICLE_TYPE) ?: Article.ArticleType.NONE.type
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        ui = FragmentNewsListBinding.inflate(inflater, container, false)
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoadingStatus()
        setupAdapter()
        setupErrorMessageView()
        monitorListScroll()
    }

    /**
     * Set up and attach the [ArticleListAdapter] to the recyclerview.
     */
    private fun setupAdapter() {
        val adapter = ArticleListAdapter()
        ui.articlesRecyclerView.adapter = adapter
        ui.articlesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        observeArticlesListDataSource(adapter)
    }

    /**
     * Start observing the live articles list to be fed into the adapter automatically whenever
     * there's an update in it.
     */
    private fun observeArticlesListDataSource(adapter: ArticleListAdapter) {
        val articlesLiveData = when (requireArguments().getString(ARG_ARTICLE_TYPE)) {
            Article.ArticleType.STORY.type -> viewModel.storyArticles
            Article.ArticleType.VIDEO.type -> viewModel.videoArticles
            else -> viewModel.headlineArticles
        }

        articlesLiveData.observe(viewLifecycleOwner) {
            // Update the cached copy of articles in the adapter.
            it.let {
                adapter.submitList(it)
                ui.noArticlesPlaceholder.shouldShow(it.isEmpty())
            }
        }
    }

    /**
     * This method monitors the user's scrolling in order to load more data when user reaches
     * the end of the list.
     */
    private fun monitorListScroll() {
        ui.nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY -> // on scroll change we are checking when users scroll as bottom.
            val nestedScrollView = v as NestedScrollView
            if (scrollY == nestedScrollView.getChildAt(0).measuredHeight - nestedScrollView.measuredHeight) {
                viewModel.userReachedEndOfList()
            }
        }
    }

    /**
     * Observe the data loading status in order to show/hide the loader.
     */
    private fun observeLoadingStatus() {
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            // Update the cached copy of articles in the adapter.
            it.let {
                ui.progressBar.shouldShow(it)
            }
        }
    }

    /**
     * Sets up the Error message view.
     */
    private fun setupErrorMessageView() {
        ui.errorView.setOnClickListener {
            viewModel.errorMessageTapped()
            it.shouldShow(false)
        }
        observeErrorMessage()
    }

    /**
     * Observe the data loading status in order to show/hide the loader.
     */
    private fun observeErrorMessage() {
        viewModel.errorMsg.observe(viewLifecycleOwner) {
            // Update the cached copy of articles in the adapter.
            it.let {
                with(ui.errorView) {
                    shouldShow(it != null)
                    text = getString(R.string.error_text, it)
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param articleType The type of the news articles to be shown in this fragment
         * @return A new instance of fragment NewsListFragment.
         */
        @JvmStatic
        fun newInstance(articleType: String) =
            NewsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ARTICLE_TYPE, articleType)
                }
            }
    }
}