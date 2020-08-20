package com.shakenbeer.animations.progressring

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.content.res.use
import com.shakenbeer.animations.R
import kotlin.math.min
import kotlin.math.roundToLong

/**
 * https://stackoverflow.com/a/53830379/276074
 */
@Suppress("MemberVisibilityCanBePrivate")
class ProgressRing @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val progressPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val bitmapPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    private val progressRect = RectF()
    private val fullRect = RectF()
    private val startAngle = -90f
    private val maxAngle = 360f
    private val minProgress = 0f
    private var currentProgress = 0f
    private val maxProgress = 100f
    private val minWidth = 1f.toPx
    private val maxWidth = 32f.toPx

    private var inset: Float = 0f
    @ColorInt
    var glowColor: Int = Color.WHITE

    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private var diameter = 0f
    private var angle = 0f

    private var progressBitmap: Bitmap? = null

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ProgressRing, 0, 0).use {
            setProgress(it.getFloat(R.styleable.ProgressRing_progress, 0f))
            setProgressColor(it.getColor(R.styleable.ProgressRing_progressColor, Color.MAGENTA))
            setProgressBackgroundColor(it.getColor(R.styleable.ProgressRing_progressBackgroundColor, Color.LTGRAY))
            setProgressWidth(it.getDimension(R.styleable.ProgressRing_progressWidth, 16f.toPx))
            setRounded(it.getBoolean(R.styleable.ProgressRing_rounded, false))
            val progressDrawable = it.getDrawable(R.styleable.ProgressRing_progressDrawable)
            progressBitmap = progressDrawable?.toBitmap()
            setLayerType(LAYER_TYPE_SOFTWARE, null)
            glowColor = it.getColor(R.styleable.ProgressRing_glowColor, Color.WHITE)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (currentProgress >= maxProgress) {
            val f = (currentProgress - maxProgress) / (maxProgress * 0.25f)
            glowPaint.setShadowLayer(
                    inset * f.coerceAtMost(1f),
                    0f,
                    0f,
                    glowColor
            )
            canvas.drawArc(progressRect, 0f, angle, false, glowPaint)
        }

        canvas.drawArc(progressRect, startAngle, maxAngle, false, backgroundPaint)
        canvas.saveLayer(null, null)

        canvas.drawArc(progressRect, startAngle, angle, false, progressPaint)
        progressBitmap?.let {
            canvas.drawBitmap(it, null, fullRect, bitmapPaint)
        }

        canvas.restore()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        diameter = min(width, height).toFloat()
        updateRect()
    }

    private fun updateRect() {
        val strokeWidth = backgroundPaint.strokeWidth
        progressRect.set(2 * strokeWidth, 2 * strokeWidth, diameter - 2 * strokeWidth, diameter - 2 * strokeWidth)
        fullRect.set(0f, 0f, diameter, diameter)
    }

    private fun calculateAngle(progress: Float) = maxAngle / maxProgress * progress

    fun setProgress(@FloatRange(from = 0.0, to = 100.0) progress: Float) {
        currentProgress = progress
        angle = calculateAngle(progress.coerceIn(minProgress, maxProgress))
        invalidate()
    }

    fun setProgressColor(@ColorInt color: Int) {
        progressPaint.color = color
        invalidate()
    }

    fun setProgressBackgroundColor(@ColorInt color: Int) {
        backgroundPaint.color = color
        invalidate()
    }

    fun setProgressWidth(width: Float) {
        width.coerceIn(minWidth, maxWidth)
        progressPaint.strokeWidth = width
        backgroundPaint.strokeWidth = width
        glowPaint.strokeWidth = width
        inset = width
        updateRect()
        invalidate()
    }

    fun setRounded(rounded: Boolean) {
        progressPaint.strokeCap = if (rounded) Paint.Cap.ROUND else Paint.Cap.BUTT
        invalidate()
    }

    private val Float.toPx: Float
        get() = this * Resources.getSystem().displayMetrics.density

    private val Int.toPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun Drawable.toBitmap(): Bitmap {
        if (this is BitmapDrawable) {
            return this.bitmap
        }

        val bitmap = Bitmap.createBitmap(this.intrinsicWidth.coerceAtLeast(32.toPx), this.intrinsicHeight.coerceAtLeast(32.toPx), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.setBounds(0, 0, canvas.width, canvas.height)
        this.draw(canvas)

        return bitmap
    }
}