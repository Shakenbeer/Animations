package com.shakenbeer.animations.checkboxloader

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.util.Linkify
import com.shakenbeer.animations.R
import kotlinx.android.synthetic.main.activity_checkbox_loader.*

class CheckboxLoaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkbox_loader)
        Linkify.addLinks(textView, Linkify.WEB_URLS)
    }

    override fun onResume() {
        super.onResume()
        val vDrawable = loaderImageView.drawable
        if (vDrawable is AnimatedVectorDrawable) {
            vDrawable.start()
        }
    }
}
