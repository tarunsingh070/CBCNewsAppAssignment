package com.tarun.cbcnewsappassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tarun.cbcnewsappassignment.databinding.ActivityNewsBinding

/**
 * Base activity for showing the fragments.
 */
class NewsActivity : AppCompatActivity() {

    private lateinit var ui: ActivityNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(ui.root)

        setFragment(NewsListFragment.newInstance())
    }

    private fun setFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()
}