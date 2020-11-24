package uz.xdevelop.exammonth3.data.models.network_models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class CardData(
    var id: Int = 0,
    val phoneNumber: String? = null,
    val pan: String?,//8600 1234 1234 1234 -> 8600123412341234
    val expiredAt: String?,//05/21
    val name: String?,// mening kartam
    val color: Int? = null,//0-6
    val amount: Long = 0L//Balance
) : Serializable {
    companion object {
        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<CardData>() {
            override fun areItemsTheSame(oldItem: CardData, newItem: CardData) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CardData, newItem: CardData) =
                oldItem.pan == newItem.pan && oldItem.expiredAt == newItem.expiredAt &&
                        oldItem.name == newItem.name && oldItem.color == newItem.color
                        && oldItem.amount == newItem.amount
        }
    }
}