package com.tarun.cbcnewsappassignment.util

import android.view.View

/**
 * A trivial extension function to set the visibility VISIBLE or GONE based on the [shouldShow] param.
 * @param shouldShow indicates what visibility to set for the view.
 */
fun View.shouldShow(shouldShow: Boolean) {
    visibility = if (shouldShow) View.VISIBLE else View.GONE
}