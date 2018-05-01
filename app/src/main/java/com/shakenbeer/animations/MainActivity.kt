package com.shakenbeer.animations

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shakenbeer.animations.biathlon.BiathlonActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        biathlonTextView.setOnClickListener {
            startActivity(Intent(this@MainActivity, BiathlonActivity::class.java))
        }
    }
}
