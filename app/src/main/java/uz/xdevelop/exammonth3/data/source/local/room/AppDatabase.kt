package uz.xdevelop.exammonth3.data.source.local.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.xdevelop.exammonth3.app.MyApp
import uz.xdevelop.exammonth3.data.source.local.room.dao.ContactDao
import uz.xdevelop.exammonth3.data.source.local.room.entities.ContactData

@Database(entities = [ContactData::class], version = 1)
//@TypeConverters(CustomTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    MyApp.instance,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}