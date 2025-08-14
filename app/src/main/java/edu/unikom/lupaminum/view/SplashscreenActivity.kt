package edu.unikom.lupaminum.view

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.unikom.lupaminum.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import edu.unikom.lupaminum.databinding.ActivitySplashscreenBinding

class SplashscreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val anim = AnimationUtils.loadAnimation(this, R.anim.slide_in)
        binding.imgSplashscreen.startAnimation(anim)

        lifecycleScope.launch {
            delay(3000)

            val intent = Intent(this@SplashscreenActivity, WalkthroughActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}