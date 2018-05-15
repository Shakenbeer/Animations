package com.shakenbeer.animations.zubko

import android.content.Context
import android.util.AttributeSet

class SquareImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : android.support.v7.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val h = MeasureSpec.getSize(widthMeasureSpec)
        val hSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, hSpec)
    }
}
