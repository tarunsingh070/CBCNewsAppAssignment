package com.tarun.cbcnewsappassignment.ui.newsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tarun.cbcnewsappassignment.databinding.FragmentNewsListBinding
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
    private val viewModel: SharedNewsViewModel by activityViewModel()
    private var articleType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleType = it.getString(ARG_ARTICLE_TYPE)
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
        setupAdapter()
    }

    /**
     * Set up and attach the [ArticleListAdapter] to the recyclerview and start observing the
     * live articles list to be fed into the adapter automatically whenever there's a change in it.
     */
    private fun setupAdapter() {
        val adapter = ArticleListAdapter()
        ui.articlesRecyclerView.adapter = adapter
        ui.articlesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.articles.observe(viewLifecycleOwner) {
            // Update the cached copy of articles in the adapter.
            it.let {
                adapter.submitList(it)
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
        fun newInstance(articleType: String? = null) =
            NewsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ARTICLE_TYPE, articleType)
                }
            }
    }
}