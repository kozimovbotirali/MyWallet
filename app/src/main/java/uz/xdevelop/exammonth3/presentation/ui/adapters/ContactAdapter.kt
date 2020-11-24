package uz.xdevelop.exammonth3.presentation.ui.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import uz.xdevelop.exammonth3.data.source.local.room.entities.ContactData
import uz.xdevelop.exammonth3.utils.SingleBlock
import uz.xdevelop.exammonth3.utils.bindItem
import uz.xdevelop.exammonth3.utils.inflate
import uz.xdevelop.exammonth3.R

class ContactAdapter : ListAdapter<ContactData, ContactAdapter.VH>(ContactData.ITEM_CALLBACK) {
    private var listenerEdit: SingleBlock<ContactData>? = null
    private var listenerDelete: SingleBlock<ContactData>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(parent.inflate(R.layout.list_item))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    fun setOnItemEditListener(block: SingleBlock<ContactData>) {
        listenerEdit = block
    }

    fun setOnItemDeleteListener(block: SingleBlock<ContactData>) {
        listenerDelete = block
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        init {

            itemView.apply {
                buttonMore.setOnClickListener { moreBtn ->
                    val menu = PopupMenu(context, moreBtn)
                    menu.inflate(R.menu.item_menu)
                    menu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.menu_edit -> listenerEdit?.invoke(getItem(adapterPosition))
                            R.id.menu_remove -> listenerDelete?.invoke(getItem(adapterPosition))
                        }
                        true
                    }
                    menu.show()
                }
                card.setOnClickListener { listenerEdit?.invoke(getItem(adapterPosition)) }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind() = bindItem {
            val d = getItem(adapterPosition)
            textName.text = "${d.firstName} ${d.lastName}"
            textNumber.text = d.phoneNumber
        }
    }
}