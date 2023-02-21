package com.tarun.cbcnewsappassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tarun.cbcnewsappassignment.databinding.FragmentNewsListBinding

private const val ARG_ARTICLE_TYPE = "param1"

/**
 * A fragment showing a list of news articles.
 * Use the [NewsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsListFragment : Fragment() {
    private lateinit var ui: FragmentNewsListBinding
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