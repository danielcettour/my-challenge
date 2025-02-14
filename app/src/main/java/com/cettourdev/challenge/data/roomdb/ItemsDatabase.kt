package com.cettourdev.challenge.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cettourdev.challenge.model.ItemResponse

@Database(entities = [ItemResponse::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ItemsDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemsDatabase? = null

        fun getDatabase(context: Context): ItemsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ItemsDatabase::class.java,
                    "app_database"
                )
                    .addTypeConverter(Converter())
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}