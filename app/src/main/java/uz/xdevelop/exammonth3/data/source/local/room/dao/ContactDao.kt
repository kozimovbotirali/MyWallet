package uz.xdevelop.exammonth3.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import uz.xdevelop.exammonth3.data.source.local.room.entities.ContactData

@Dao
interface ContactDao : BaseDao<ContactData> {
    @Query("SELECT * FROM contacts")
    fun getAll(): List<ContactData>

    @Query("DELETE FROM contacts")
    fun deleteAll()
}