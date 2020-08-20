package com.shakenbeer.animations.progressring

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import com.shakenbeer.animations.R
import kotlinx.android.synthetic.main.activity_progress_ring.*

class ProgressRingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_ring)
    }

    override fun onResume() {
        super.onResume()
        with(progressRing) {
            setProgress(0f)
            ValueAnimator.ofFloat(0f, 200f).apply {
                duration = 2000
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener {
                    setProgress(it.animatedValue as Float)
                    invalidate()
                }
                repeatCount = INFINITE
                start()
            }
        }

        with(progressRing2) {
            setProgress(0f)
            ValueAnimator.ofFloat(75f, 150f).apply {
                duration = 1000
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener {
                    setProgress(it.animatedValue as Float)
                    invalidate()
                }
                repeatCount = INFINITE
                start()
            }
        }
    }
}