package com.kong.fsk.textgame.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kong.fsk.textgame.R
import kotlinx.android.synthetic.main.a_intro.*

class IntroActivity : AppCompatActivity() {
    val visibilityHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_intro)
        setTextVisibility(true, false)
        a_intro_root.setOnClickListener {
            setTextVisibility(true, true)
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        }
    }

    fun setTextVisibility(isShow : Boolean, isStop : Boolean) {
        a_intro_text.visibility = if(isShow) View.VISIBLE else View.GONE

        if(!isStop) {
            visibilityHandler.postDelayed({
                setTextVisibility(!isShow, false)
            }, 500)
        }
    }
}