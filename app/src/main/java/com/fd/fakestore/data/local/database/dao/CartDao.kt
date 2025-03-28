package com.fd.fakestore.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.fd.fakestore.data.model.Cart
import com.fd.fakestore.data.model.CartWithProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: Cart)

    @Update
    suspend fun updateCartItem(cartItem: Cart)

    @Query("SELECT * FROM t_cart WHERE cartId IN (:cartIds) AND userId = :userId")
    fun getCartItemsByCartIds(cartIds: List<Int>, userId: Int): Flow<List<Cart>>

    @Query("SELECT * FROM t_cart WHERE productId = :productId AND userId = :userId")
    fun getCartItemByProductId(productId: Int, userId: Int): Flow<Cart?>

    @Query("DELETE FROM t_cart WHERE productId = :productId AND userId = :userId")
    suspend fun deleteCartByProductId(productId: Int, userId: Int)

    @Transaction
    @Query("SELECT * FROM t_cart WHERE userId = :userId")
    fun getCartItemsWithProducts(userId: Int): Flow<List<CartWithProduct>>

}