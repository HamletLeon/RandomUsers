package com.hamletleon.randomusers.utils

import java.text.SimpleDateFormat
import java.util.*

private val m_ISO8601Withms = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale("es", "ES"))
private val m_ISO8601Withoutms = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("es", "ES"))
private val m_CustomFormat = SimpleDateFormat("yyyy-MM-dd", Locale("es", "ES"))

fun String?.toDate(): Date? = this?.let {
    return try {
        m_ISO8601Withms.parse(it)
    } catch (e: Exception){
        try {
            m_ISO8601Withoutms.parse(it)
        } catch (e2: Exception) {
            try{
                m_CustomFormat.parse(it)
            } catch (e3: Exception) {
                null
            }
        }
    }
}

fun Date?.fromDate(): String? = this?.let {
    return try {
        m_ISO8601Withms.format(it)
    } catch (e: Exception){
        try {
            m_ISO8601Withoutms.format(it)
        } catch (e2: Exception) {
            null
        }
    }
}