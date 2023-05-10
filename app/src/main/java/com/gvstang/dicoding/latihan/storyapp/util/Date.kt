package com.gvstang.dicoding.latihan.storyapp.util

import android.content.res.Resources
import com.gvstang.dicoding.latihan.storyapp.R
import java.text.SimpleDateFormat
import java.util.Locale

class Date(private var dateString: String = "", private val resources: Resources? = null) {

    private fun getDay(): String {
        return if(dateString[8].toString() == "0") {
            dateString[9].toString()
        } else {
            dateString.substring(8, 10)
        }
    }

    private fun getMonth(): String {
        val month = resources!!.getStringArray(R.array.month)
        return month[dateString.substring(5, 7).toInt() - 1]
    }

    private fun getYear(): String = dateString.substring(0, 4)

    fun format(): String {
        return "${getDay()} ${getMonth()} ${getYear()}"
    }

    fun getNow(): String {
        dateString = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        ).format(System.currentTimeMillis())
        return format()
    }

}