package com.fd.fakestore.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fd.fakestore.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Query("SELECT * FROM t_product")
    fun getProducts(): Flow<List<Product>>

    @Query("SELECT * FROM t_product WHERE id = :productId")
    fun getProductById(productId: Int): Flow<Product?>?

    @Query("SELECT * FROM t_product WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<Product>>

}