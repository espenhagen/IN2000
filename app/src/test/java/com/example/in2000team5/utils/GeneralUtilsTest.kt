package com.example.in2000team5.utils

import com.example.in2000team5.utils.GeneralUtils.Companion.round
import org.junit.Assert.*
import org.junit.Test
import java.lang.ArithmeticException
import java.lang.IllegalArgumentException

class GeneralUtilsTest  {

    @Test
    fun doubleRoundTest()   {
        assertEquals(2.55000,2.55389.round(2),0.00001)
        assertNotEquals(2.00000,1.989.round(2),0.0001)
    }

    @Test
    fun zeroDecimalsReturnsSame() {
        assertTrue(2.401.round(0).equals(2.0))
    }

}
