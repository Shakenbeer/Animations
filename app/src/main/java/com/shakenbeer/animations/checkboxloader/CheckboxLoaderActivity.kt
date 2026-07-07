package com.shakenbeer.animations.checkboxloader

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.text.util.Linkify
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.shakenbeer.animations.R

class CheckboxLoaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkbox_loader)
        val textView = findViewById<TextView>(R.id.textView)
        Linkify.addLinks(textView, Linkify.WEB_URLS)
    }

    override fun onResume() {
        super.onResume()
        val loaderImageView = findViewById<ImageView>(R.id.loaderImageView)
        val vDrawable = loaderImageView.drawable
        if (vDrawable is AnimatedVectorDrawable) {
            vDrawable.start()
        }
    }
}
