package com.aidanlaing.exoplanets.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.aidanlaing.exoplanets.R

class NoConnectionView: LinearLayout {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.view_no_connection, this)
    }

}