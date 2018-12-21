package com.hamletleon.randomusers.models

import android.os.Build
import android.telephony.PhoneNumberUtils
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hamletleon.randomusers.dtos.UserDto
import java.util.*

@Entity(tableName = "Users")
class User {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    var gender: String? = ""
    var title: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var fullName: String = ""
    var age: Int = 0
    var phone: String = ""
    var email: String = ""
    var address: String = ""
    @Embedded var addressLocation: Coordinate = Coordinate()
    var tinyPicture: String = ""
    var largePicture: String = ""
    var registeredAt: String = ""

    @Embedded var timeZone: TimeZone? = null

    fun getName() = firstName.capitalize()
    fun getTitledName() = "${title.capitalize()} ${firstName.capitalize()}"
    fun getFullTitledName() = "${title.capitalize()} ${fullName.capitalize()}"
    fun getPhoneNumber(): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) PhoneNumberUtils.formatNumber(phone, Locale.getDefault().country) ?: "Not specified"
        else PhoneNumberUtils.formatNumber(phone) ?: "Not specified"

    constructor()
    constructor(userDto: UserDto) {
        gender = userDto.gender ?: "N/D"
        title = userDto.name?.title ?: ""
        firstName = userDto.name?.first ?: ""
        lastName = userDto.name?.last ?: ""
        fullName = userDto.name?.getFullName() ?: ""
        age = userDto.getAge()
        phone = userDto.phone ?: ""
        email = userDto.email ?: ""
        address = userDto.getAddress()
        addressLocation = userDto.getCoordinates()
        tinyPicture = userDto.getTinyPicture()
        largePicture = userDto.getLargePicture()
        registeredAt = userDto.getRegisteredAt()
        timeZone = userDto.location?.timezone
    }
}