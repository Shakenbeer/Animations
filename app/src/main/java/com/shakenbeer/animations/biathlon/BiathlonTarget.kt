package com.shakenbeer.animations.biathlon

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

@SuppressLint("Recycle")
class BiathlonTarget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val coef = 0.15
    private val closingMillis = 500L
    private val openingMillis = 1000L
    private val delayMillis = 200L

    private val w: Int = (WIDTH_MM * coef).toInt().toPx
    private val h: Int = (HEIGHT_MM * coef).toInt().toPx
    private val ds: Int = (DIAMETER_SMALL_MM * coef).toInt().toPx
    private val d: Int = (DIAMETER_MM * coef).toInt().toPx
    private val gap: Int = (GAP_MM * coef).toInt().toPx

    private val targetRect = RectF(0f, 0f, w.toFloat(), h.toFloat())
    private val targetPaint = Paint()

    private val centerY = (h / 2).toFloat()
    private val centers by lazy {
        (1..5).map { Pair(((gap + d / 2) * it + (d / 2) * (it - 1)).toFloat(), centerY) }.toTypedArray()
    }
    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val diskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val smallDiskPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val lidCenterY = h.toFloat() + 1.toPx

    private val lidCenters by lazy {
        (1..5).map { Pair(((gap + d / 2) * it + (d / 2) * (it - 1)).toFloat(), lidCenterY) }.toTypedArray()
    }
    private val lidPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var isAnimating = false

    private val animations = mutableListOf<ValueAnimator>()
    private val animatorSet = AnimatorSet()

    init {
        targetPaint.style = Paint.Style.FILL
        targetPaint.color = Color.WHITE

        ringPaint.style = Paint.Style.STROKE
        ringPaint.color = Color.BLACK
        ringPaint.strokeWidth = 1.toPx.toFloat()

        diskPaint.style = Paint.Style.FILL
        diskPaint.color = 0xFF666666.toInt()

        smallDiskPaint.style = Paint.Style.FILL
        smallDiskPaint.color = Color.BLACK

        lidPaint.style = Paint.Style.FILL
        lidPaint.color = Color.WHITE

        for (i in 0..5) {
            animations.add(run {
                if (i < 5) {
                    val anim = ValueAnimator.ofFloat(lidCenterY, centerY)
                    anim.duration = closingMillis
                    anim.addUpdateListener {
                        lidCenters[i] = lidCenters[i].copy(second = it.animatedValue as Float)
                        invalidate()
                    }
                    anim
                } else {
                    val anim = ValueAnimator.ofFloat(centerY, lidCenterY)
                    anim.startDelay = delayMillis
                    anim.duration = openingMillis
                    anim.interpolator = LinearInterpolator()
                    anim.addUpdateListener {
                        for (j in lidCenters.indices) {
                            lidCenters[j] = lidCenters[j].copy(second = it.animatedValue as Float)
                        }
                        invalidate()
                    }
                    anim
                }
            })
        }

        animatorSet.startDelay = delayMillis
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (isAnimating) {
                    animatorSet.start()
                }
            }
        })
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(targetRect, targetPaint)
        centers.forEach {
            canvas.drawCircle(it.first, it.second, d.toFloat() / 2, ringPaint)
            canvas.drawCircle(it.first, it.second, d.toFloat() / 2, diskPaint)
            canvas.drawCircle(it.first, it.second, ds.toFloat() / 2, smallDiskPaint)
        }
        if (isAnimating) {
            lidCenters.forEach { canvas.drawCircle(it.first, it.second, d.toFloat() / 2, lidPaint) }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    private fun startAnimation() {
        isAnimating = true
        for (i in lidCenters.indices) {
            lidCenters[i] = Pair(((gap + d / 2) * (i + 1) + (d / 2) * i).toFloat(), lidCenterY)
        }
        val list = animations as List<Animator>?
        animatorSet.playSequentially(list)
        animatorSet.start()
    }

    private fun stopAnimation() {
        isAnimating = false
        animatorSet.end()
    }

    companion object {
        private const val WIDTH_MM = 1175
        private const val HEIGHT_MM = 310
        private const val DIAMETER_SMALL_MM = 45
        private const val DIAMETER_MM = 115
        private const val GAP_MM = 100

        val Int.toPx: Int
            get() = (this * Resources.getSystem().displayMetrics.density).toInt()
    }
}