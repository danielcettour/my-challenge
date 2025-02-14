package com.cettourdev.challenge.data.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cettourdev.challenge.model.ItemResponse

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemResponse)

    @Delete
    suspend fun deleteItem(item: ItemResponse)

    @Query("SELECT * FROM items")
    fun observeItems(): LiveData<List<ItemResponse>>
}