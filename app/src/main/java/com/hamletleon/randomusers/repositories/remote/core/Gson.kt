package com.hamletleon.randomusers.repositories.remote.core

import com.google.gson.Gson as GsonC

object Gson {
    var gson: GsonC? = null
        get() {
            if (field == null) { field = GsonC() }
            return field
        }
}

fun <T> String?.parseStringToObject(clazz: Class<T>): T? = Gson.gson?.getAdapter(clazz)?.fromJson(this)
fun <T> T?.parseStringToObject(clazz: Class<T>): String? = Gson.gson?.getAdapter(clazz)?.toJson(this)