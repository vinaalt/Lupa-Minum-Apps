package edu.unikom.lupaminum.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import edu.unikom.lupaminum.view.WalkthroughFragment

class WalkthroughAdapter (
    fragmentActivity: FragmentActivity,
    private val imageResIds: List<Int>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = imageResIds.size

    override fun createFragment(position: Int): Fragment {
        return WalkthroughFragment.newInstance(imageResIds[position])
    }
}