package com.shakenbeer.animations

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shakenbeer.animations.airplane.FlightActivity
import com.shakenbeer.animations.biathlon.BiathlonActivity
import com.shakenbeer.animations.checkboxloader.CheckboxLoaderActivity
import com.shakenbeer.animations.progressring.ProgressRingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        biathlonTextView.setOnClickListener {
            startActivity(Intent(this@MainActivity, BiathlonActivity::class.java))
        }
        flightTextView.setOnClickListener {
            startActivity(Intent(this@MainActivity, FlightActivity::class.java))
        }
        checkboxLoaderTextView.setOnClickListener {
            startActivity(Intent(this@MainActivity, CheckboxLoaderActivity::class.java))
        }
        progressRingTextView.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProgressRingActivity::class.java))
        }
    }
}
