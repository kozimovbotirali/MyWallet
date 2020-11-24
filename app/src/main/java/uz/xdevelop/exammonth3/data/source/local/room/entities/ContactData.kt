package uz.xdevelop.exammonth3.data.source.local.room.entities

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactData(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var phoneNumber: String = "",
    var lastName: String = "",
    var firstName: String = ""
) {
    companion object {
        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<ContactData>() {
            override fun areItemsTheSame(oldItem: ContactData, newItem: ContactData) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ContactData, newItem: ContactData) =
                oldItem.firstName == newItem.firstName && oldItem.lastName == newItem.lastName &&
                        oldItem.phoneNumber == newItem.phoneNumber
        }
    }
}