package com.shakenbeer.animations

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.shakenbeer.animations.airplane.FlightActivity
import com.shakenbeer.animations.biathlon.BiathlonActivity
import com.shakenbeer.animations.checkboxloader.CheckboxLoaderActivity
import com.shakenbeer.animations.composeowls.BabalexActivity
import com.shakenbeer.animations.progressring.ProgressRingActivity
import com.shakenbeer.animations.strikethru.StrikethruActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        findViewById<TextView>(R.id.biathlonTextView).setOnClickListener {
            startActivity(Intent(this@MainActivity, BiathlonActivity::class.java))
        }
        findViewById<TextView>(R.id.flightTextView).setOnClickListener {
            startActivity(Intent(this@MainActivity, FlightActivity::class.java))
        }
        findViewById<TextView>(R.id.checkboxLoaderTextView).setOnClickListener {
            startActivity(Intent(this@MainActivity, CheckboxLoaderActivity::class.java))
        }
        findViewById<TextView>(R.id.progressRingTextView).setOnClickListener {
            startActivity(Intent(this@MainActivity, ProgressRingActivity::class.java))
        }
        findViewById<TextView>(R.id.composePagerTextView).setOnClickListener {
            startActivity(Intent(this@MainActivity, BabalexActivity::class.java))
        }
        findViewById<TextView>(R.id.composeStrikethruTextView).setOnClickListener {
            startActivity(Intent(this@MainActivity, StrikethruActivity::class.java))
        }
    }
}
