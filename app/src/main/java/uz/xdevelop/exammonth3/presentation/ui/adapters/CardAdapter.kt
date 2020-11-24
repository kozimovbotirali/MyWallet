package uz.xdevelop.exammonth3.presentation.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_item.view.*
import uz.xdevelop.exammonth3.R
import uz.xdevelop.exammonth3.data.models.network_models.CardData
import uz.xdevelop.exammonth3.utils.bindItem
import uz.xdevelop.exammonth3.utils.inflate
import uz.xdevelop.exammonth3.utils.setReadOnly
import java.text.NumberFormat
import java.util.*

class CardAdapter : ListAdapter<CardData, CardAdapter.VH>(CardData.ITEM_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(parent.inflate(R.layout.card_item))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = currentList.size

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.apply {
                text_pan.setReadOnly(true)
                text_expired_at.setReadOnly(true)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind() = bindItem {
            val d = getItem(adapterPosition)

            itemView.apply {
                Log.d("TTTT", d.toString())
                text_name.text = if (d.name == "") "Credit" else d.name

                val str = (d.expiredAt ?: "").replace("/", "")
                text_expired_at.setMaskedText(str)

                var bg = R.drawable.color5
                if (d.color != null) {
                    bg = when (d.color) {
                        0 -> R.drawable.color1
                        1 -> R.drawable.color2
                        2 -> R.drawable.color3
                        3 -> R.drawable.color4
                        4 -> R.drawable.color5
                        5 -> R.drawable.color6
                        else -> R.drawable.color5
                    }
                }
                cardBg.setImageResource(bg)

                text_amount.visibility = View.VISIBLE

                val format = NumberFormat.getCurrencyInstance()
                format.maximumFractionDigits = 1
                text_amount.text =
                    format.format(d.amount).replace("$", "")
                        .replace(",", " ") + " so'm"

                d.pan?.apply {
                    val str = this.replaceRange(6, 12, "******")
                    text_pan.setMaskedText(str)
                }
            }
        }
    }
}