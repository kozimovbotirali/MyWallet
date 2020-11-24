package uz.xdevelop.exammonth3.presentation.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.xdevelop.exammonth3.data.models.IntoData
import uz.xdevelop.exammonth3.presentation.ui.fragments.IntoFragment
import uz.xdevelop.exammonth3.utils.putArguments

class IntoPagerAdapter(private val data: List<IntoData>, activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = data.size
    override fun createFragment(position: Int): Fragment = IntoFragment()
        .putArguments {
            putSerializable("DATA", data[position])
        }
}