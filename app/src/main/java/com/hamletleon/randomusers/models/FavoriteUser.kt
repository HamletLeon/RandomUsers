package com.hamletleon.randomusers.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteUsers",
    foreignKeys = [ ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = CASCADE) ])
class FavoriteUser {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    var userId: Int = 0
    var registeredAt: String = ""
}