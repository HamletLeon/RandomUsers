package com.hamletleon.randomusers.dtos

class Name {
    var title: String = ""
    var first: String = ""
    var last: String = ""

    fun getFullName() = "${first.capitalize()} ${last.capitalize()}"
}