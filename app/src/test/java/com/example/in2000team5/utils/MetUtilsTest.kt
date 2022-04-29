package com.example.in2000team5.utils

import com.example.in2000team5.utils.MetUtils.Companion.isNowTime
import com.example.in2000team5.utils.MetUtils.Companion.getDateAndHour
import org.junit.Assert.*
import org.junit.Test

class MetUtilsTest {

    private val lastChristmasEve = "2021-12-24T17:00:00Z"

    @Test
    fun `Some time last year should not be the same as Now`() {
        assertFalse(isNowTime(lastChristmasEve))
    }

    @Test
    fun `Last christmas eve was at 5pm`() {
        val fiveOClock = getDateAndHour(lastChristmasEve)
        assertEquals("kl: 17", fiveOClock)
    }

}