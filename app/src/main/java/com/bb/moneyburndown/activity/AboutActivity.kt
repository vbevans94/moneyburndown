package com.bb.moneyburndown.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bb.moneyburndown.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val page = assets.open("about.html").bufferedReader().use { it.readText() }
        web_view.loadDataWithBaseURL(null, page, "text/html", "", "")
    }
}
