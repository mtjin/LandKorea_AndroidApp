package com.mtjin.mapogreen

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mtjin.mapogreen.activities.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity
import java.lang.Exception



class SplashActivity : AppCompatActivity() {
    private var fadeIn : Animation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //서서히보이는 페이드인 애니메이션
        fadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in);
        fadeIn(splash_one, 0)
        fadeIn(splash_two, 1000)
        fadeIn(splash_three, 2000)
        fadeIn(splash_four, 3000)
        try {
            Handler().postDelayed({
                //method
                startActivity<LoginActivity>()
                finish()
            }, 4000)

        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun fadeIn(view : TextView, duration: Int){
        Handler().postDelayed({
            view.visibility = View.VISIBLE
            view.startAnimation(fadeIn)
        }, duration.toLong())
    }

}
