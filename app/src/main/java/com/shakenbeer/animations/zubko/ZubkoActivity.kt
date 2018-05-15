package com.shakenbeer.animations.zubko

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import com.shakenbeer.animations.R
import kotlinx.android.synthetic.main.activity_zubko.*

class ZubkoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zubko)

        verticalRecyclerView.run {
            layoutManager = LinearLayoutManager(this@ZubkoActivity)
            PagerSnapHelper().attachToRecyclerView(this)
        }
    }
}
