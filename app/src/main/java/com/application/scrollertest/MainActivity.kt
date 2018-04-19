package com.application.scrollertest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val value = arrayListOf(0f, 20f, 40f, 60f, 80f, 100f)
        city_line_view.setValueString(value)
        city_line_view.setItemListener(object : RatioLineView.OnDataClickListener {

            override fun getData(position: Int) = arrayListOf(RatioLineView.RatioData("北京", "11")
                    , RatioLineView.RatioData("上海", "51")
                    , RatioLineView.RatioData("南京", "852"))

            override fun getTime() = "4.19 14"

        })
    }
}