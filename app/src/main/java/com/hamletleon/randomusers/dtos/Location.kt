package com.hamletleon.randomusers.dtos

import com.hamletleon.randomusers.models.Coordinate
import com.hamletleon.randomusers.models.TimeZone

class Location{
    var street: String = ""
    var city: String = ""
    var state: String = ""
    var postcode: String = ""
    var coordinates : Coordinate? = null
    var timeZone: TimeZone? = null

    fun getAddress() = "${street.capitalize()}, ${city.capitalize()}, ${state.capitalize()}."
}