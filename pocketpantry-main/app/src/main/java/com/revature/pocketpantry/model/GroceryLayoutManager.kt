package com.revature.pocketpantry.model

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GroceryLayoutManager(
    val context: Context
):
    LinearLayoutManager(context) {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : this(context) {
        val properties = getProperties(context, attrs, defStyleAttr, defStyleRes)
        orientation    = properties.orientation
        reverseLayout  = properties.reverseLayout
        stackFromEnd   = properties.stackFromEnd
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        spanLayoutSize(super.generateDefaultLayoutParams())

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): RecyclerView.LayoutParams =
        spanLayoutSize(super.generateLayoutParams(lp))

    private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams)= layoutParams.apply {
        width = RecyclerView.LayoutParams.MATCH_PARENT
    }
}