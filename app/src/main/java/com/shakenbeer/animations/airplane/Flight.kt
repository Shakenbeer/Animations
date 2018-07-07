package com.shakenbeer.animations.airplane

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.RESTART
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.shakenbeer.animations.R
import kotlin.math.max

class Flight @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val h = HEIGHT_DP.toPx
    private val color: Int
    private val flightDuration: Int
    private val linePaint = Paint()
    private val planeBitmap: Bitmap
    private val planePaint = Paint()
    private var planeX: Float
    private var anim: ValueAnimator? = null
    private val planeInterpolator = AccelerateDecelerateInterpolator()

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.Flight, 0, 0)
        try {
            color = a.getColor(R.styleable.Flight_color, Color.BLACK)
            flightDuration = a.getInt(R.styleable.Flight_duration, DEFAULT_DURATION_MILLIS).let {
                when {
                    it < MIN_DURATION_MILLIS -> MIN_DURATION_MILLIS
                    it > MAX_DURATION_MILLIS -> MAX_DURATION_MILLIS
                    else -> it
                }
            }
        } finally {
            a.recycle()
        }
        linePaint.color = color
        linePaint.strokeWidth = 2.toPx.toFloat()

        planeBitmap = BitmapFactory.decodeResource(resources, R.drawable.airplane_white_48dp)
        planePaint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        planePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        planeX = -planeBitmap.width.toFloat()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(max(widthMeasureSpec, MIN_WIDTH_DP.toPx), h)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        anim = ValueAnimator.ofFloat(-planeBitmap.width.toFloat(),
                (width + planeBitmap.width).toFloat()).apply {
            duration = flightDuration.toLong()
            interpolator = planeInterpolator
            repeatMode = RESTART
            repeatCount = INFINITE
            addUpdateListener { animation ->
                planeX = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(0f, h.toFloat() / 2, width.toFloat(), h.toFloat() / 2, linePaint)
        canvas.drawBitmap(planeBitmap, planeX, 0f, planePaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        anim?.cancel()
        planeBitmap.recycle()
    }

    companion object {
        private const val HEIGHT_DP = 48
        private const val MIN_WIDTH_DP = 48
        private const val MIN_DURATION_MILLIS = 300
        private const val DEFAULT_DURATION_MILLIS = 2000
        private const val MAX_DURATION_MILLIS = 4000

        private val Int.toPx: Int
            get() = (this * Resources.getSystem().displayMetrics.density).toInt()
    }
}