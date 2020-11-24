package uz.xdevelop.exammonth3.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.data.models.IntoData
import uz.xdevelop.exammonth3.databinding.ItemPagesBinding

@Suppress("DEPRECATION")
class IntoFragment : Fragment() {
    private lateinit var binding: ItemPagesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_pages, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = requireArguments()
        val data = bundle.getSerializable("DATA") as IntoData

        binding.apply {
            imageBackground.setImageDrawable(resources.getDrawable(data.image))
            textHeader.text = data.header
            textInfo.text = data.info
        }

    }
}