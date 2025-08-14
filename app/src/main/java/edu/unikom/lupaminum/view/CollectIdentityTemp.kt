package edu.unikom.lupaminum.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import edu.unikom.lupaminum.R

@AndroidEntryPoint
class CollectIdentityTemp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect_identity_temp)

        val bottomSheet = CollectIdentityTempFragment()
        bottomSheet.show(supportFragmentManager, "Collect Identity")
    }
}