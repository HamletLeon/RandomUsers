package com.hamletleon.randomusers.dtos

import com.hamletleon.randomusers.models.Coordinate

class UserDto {
    var gender: String? = null
    var name: Name? = null
    var location: Location? = null
    var email: String? = null
    var registered: Registered? = null
    var phone: String? = null
    var picture: Picture? = null

    fun getTinyPicture() = picture?.thumbnail ?: ""
    fun getLargePicture() = picture?.large ?: ""
    fun getAddress() = location?.getAddress() ?: ""
    fun getCoordinates() = location?.coordinates ?: Coordinate()
    fun getRegisteredAt() = registered?.date ?: "2000-01-01T00:00:00Z"
    fun getAge() = registered?.age ?: 0
}