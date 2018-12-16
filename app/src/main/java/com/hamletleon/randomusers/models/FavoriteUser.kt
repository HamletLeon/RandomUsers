package com.hamletleon.randomusers.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteUsers")
class FavoriteUser {
    @PrimaryKey var id: Int = 0
    var userId: Int = 0
    var registeredAt: String = ""
}