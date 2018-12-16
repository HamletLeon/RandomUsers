package com.hamletleon.randomusers.repositories.local.services

import androidx.room.Dao
import androidx.room.Query
import com.hamletleon.randomusers.models.User
import com.hamletleon.randomusers.repositories.local.core.BaseDAO

@Dao
abstract class RandomUsersLocalService : BaseDAO<User>() {
    @Query("SELECT Count(*) FROM Users AS u LIMIT :limit OFFSET IFNULL(:offset, 0)")
    abstract fun getUsersCount(limit: Int = 50, offset: Int? = null) : Int

    @Query("SELECT u.* FROM Users AS u LEFT JOIN FavoriteUsers AS f ON u.id = f.userId WHERE f.id IS NULL LIMIT :limit OFFSET IFNULL(:offset, 0)")
    abstract fun getUsers(limit: Int = 50, offset: Int? = null) : List<User>

    @Query("SELECT u.* FROM Users AS u, FavoriteUsers AS f WHERE u.id = f.userId LIMIT :limit OFFSET IFNULL(:offset, 0)")
    abstract fun getAllFavoriteUsers(limit: Int = 50, offset: Int? = null): List<User>
}