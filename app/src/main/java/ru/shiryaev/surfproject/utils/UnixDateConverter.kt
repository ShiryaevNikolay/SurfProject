package ru.shiryaev.surfproject.utils

import java.text.SimpleDateFormat
import java.util.*

object UnixDateConverter {
    private const val SECOND_IN_MSEC = 1000
    private const val MINUTE_IN_MSEC = 60000
    private const val HOURS_IN_MSEC = 3600000
    private const val DAY_IN_MSEC = 86400000
    private const val MONTH_IN_MSEC = 2592000000

    fun convertCreatedDate(timestamp: Long) : String {
        val currentTimeMs = System.currentTimeMillis()
        val datePublic = Date(currentTimeMs - timestamp)

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val calendarPublic = Calendar.getInstance()
        calendarPublic.time = datePublic

        if (Date(currentTimeMs).before(datePublic)) {
            return "Только что"
        }
        return when {
            timestamp <= SECOND_IN_MSEC -> "Только что"
            timestamp < MINUTE_IN_MSEC -> {
                val secondPublished = calendar.get(Calendar.SECOND) - calendarPublic.get(Calendar.SECOND)
                "$secondPublished секунд назад"
            }
            timestamp < HOURS_IN_MSEC -> {
                val secondPublished = calendar.get(Calendar.MINUTE) - calendarPublic.get(Calendar.MINUTE)
                "$secondPublished секунд назад"
            }
            timestamp < DAY_IN_MSEC -> {
                val secondPublished = calendar.get(Calendar.HOUR_OF_DAY) - calendarPublic.get(Calendar.HOUR_OF_DAY)
                "$secondPublished секунд назад"
            }
            timestamp < MONTH_IN_MSEC -> {
                val secondPublished = calendar.get(Calendar.DAY_OF_YEAR) - calendarPublic.get(Calendar.DAY_OF_YEAR)
                "$secondPublished секунд назад"
            }
            else -> {
                val date = SimpleDateFormat("yyyy:MM:dd в HH:mm").format(datePublic)
                "Создано $date"
            }
        }
    }
}