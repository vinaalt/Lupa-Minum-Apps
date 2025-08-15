package edu.unikom.lupaminum.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import edu.unikom.lupaminum.R
import edu.unikom.lupaminum.adapter.WalkthroughAdapter
import edu.unikom.lupaminum.databinding.ActivityWalkthroughBinding

class WalkthroughActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalkthroughBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWalkthroughBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageList = listOf(
            R.drawable.img_wk_1,
            R.drawable.img_wk_2,
            R.drawable.img_wk_3
        )

        val adapter = WalkthroughAdapter(this, imageList)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = adapter

        val dots = listOf(
            findViewById<View>(R.id.dot1),
            findViewById<View>(R.id.dot2),
            findViewById<View>(R.id.dot3)
        )

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in dots.indices) {
                    dots[i].background = ContextCompat.getDrawable(
                        this@WalkthroughActivity,
                        if (i == position) R.drawable.dot_active else R.drawable.dot_inactive
                    )
                }
            }
        })

        val btnNext = findViewById<ImageButton>(R.id.btnNext)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in dots.indices) {
                    dots[i].background = ContextCompat.getDrawable(
                        this@WalkthroughActivity,
                        if (i == position) R.drawable.dot_active else R.drawable.dot_inactive
                    )
                }

                // Tampilkan tombol Next hanya di halaman terakhir
                btnNext.visibility = if (position == imageList.size - 1) View.VISIBLE else View.GONE
            }
        })

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, CollectIdentityTemp::class.java)
            startActivity(intent)
            finish()
        }
    }
}