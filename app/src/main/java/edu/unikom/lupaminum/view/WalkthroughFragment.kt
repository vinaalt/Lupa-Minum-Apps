package edu.unikom.lupaminum.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import edu.unikom.lupaminum.R

class WalkthroughFragment : Fragment(R.layout.fragment_walkthrough) {

    companion object {
        fun newInstance(imageResId: Int): WalkthroughFragment {
            val fragment = WalkthroughFragment()
            val args = Bundle()
            args.putInt("imageRes", imageResId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val imageRes = arguments?.getInt("imageRes") ?: R.drawable.img_splashscreen
        imageView.setImageResource(imageRes)
    }
}