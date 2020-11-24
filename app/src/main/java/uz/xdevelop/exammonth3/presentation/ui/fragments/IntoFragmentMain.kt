package uz.xdevelop.exammonth3.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import uz.xdevelop.exammonth3.data.models.IntoData
import uz.xdevelop.exammonth3.data.source.local.storage.LocalStorage
import uz.xdevelop.exammonth3.presentation.ui.adapters.IntoPagerAdapter
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.databinding.FragmentIntoBinding
import uz.xdevelop.exammonth3.utils.navigate

@Suppress("NAME_SHADOWING", "SameParameterValue")
class IntoFragmentMain : BaseFragment<FragmentIntoBinding>() {
    private lateinit var adapter: IntoPagerAdapter

    private val data = arrayListOf(
        IntoData(
            R.drawable.into1,
            "Listen to Unlimited music",
            "Listen to 40000+ song and 1600+ artist"
        ),
        IntoData(
            R.drawable.into2,
            "Unlimited Download",
            "Download Unlimited  song anytime for free"
        ),
        IntoData(
            R.drawable.into3,
            "Search With Voice",
            "Service will identify any music playing around you"
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadViews()
    }

    private fun loadViews() {
        adapter = IntoPagerAdapter(
            data,
            activity!!
        )
        binding.apply {
            pager.adapter = adapter
            indicator.setViewPager2(pager)
            buttonDalee.setOnClickListener {
                if (pager.currentItem != data.size - 1) {
                    pager.setCurrentItem(pager.currentItem + 1, true)
                } else {
                    openLoginFragment()
                }
                if (pager.currentItem != data.size - 2) {
                    buttonDalee.text = getString(R.string.get_started)
                    buttonSkip.visibility = View.INVISIBLE
                }
            }

            buttonSkip.setOnClickListener {
                openLoginFragment()
            }

            pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 2) {
                        buttonDalee.text = getString(R.string.get_started)
                        buttonSkip.visibility = View.INVISIBLE
                    } else {
                        buttonDalee.text = getString(R.string.continue1)
                        buttonSkip.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

    private fun openLoginFragment() {
        /*fragmentManager?.beginTransaction()
            ?.replace(R.id.layoutFragment, LoginFragment())
            ?.addToBackStack(this.javaClass.simpleName)
            ?.commit()*/
        navigate(LoginFragment())
        LocalStorage.instance.isFirst = false
    }

    override fun getLayoutId(): Int = R.layout.fragment_into
}
