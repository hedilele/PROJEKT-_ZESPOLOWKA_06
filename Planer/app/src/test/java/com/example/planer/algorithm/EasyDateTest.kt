package com.example.planer.algorithm

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ExperimentalCoroutinesApi
class EasyDateTest{

    @Test
    fun constructorSingleIntTest() = runTest {
        var d = EasyDate(3)
    }

    @Test
    fun constructorThreeIntsTest() = runTest {
        var d = EasyDate(3, 5, 2000)
    }

    @Test
    fun constructorLocalDateTest() = runTest {
        var d = EasyDate(LocalDate.now())
    }

    @Test
    fun constructorStringTest() = runTest {
        var d = EasyDate("2000-05-03")
    }

    @Test
    fun getTest() = runTest {
        val d = EasyDate(3, 5, 2000)
        val e = d
        assertThat(e).isEqualTo(d)
    }

    @Test
    fun getDayTest() = runTest {
        val d = EasyDate("2020-01-13")
        assertThat(d.getDay()).isEqualTo(13)
    }

    @Test
    fun getMonthTest() = runTest {
        val d = EasyDate("2020-01-13")
        assertThat(d.getMonth()).isEqualTo(1)
    }

    @Test
    fun getYearTest() = runTest {
        val d = EasyDate("2020-01-13")
        assertThat(d.getYear()).isEqualTo(2020)
    }

    @Test
    fun plusOperatorTest() = runTest {
        var d1 = EasyDate(3, 5, 2000)
        var d2 = EasyDate(31, 12, 2000)
        var d3 = EasyDate(29, 2, 2000)
        var d4 = EasyDate(3, 5, 2000)


        d1 += 1
        d2 += 1
        d3 += 1
        d4 += 60

        val t1 = EasyDate(4, 5, 2000)
        val t2 = EasyDate(1, 1, 2001)
        val t3 = EasyDate(1, 3, 2000)
        val t4 = EasyDate(2, 7, 2000)

        assertThat(d1.date).isEqualTo(t1.date)
        assertThat(d2.date).isEqualTo(t2.date)
        assertThat(d3.date).isEqualTo(t3.date)
        assertThat(d4.date).isEqualTo(t4.date)

    }


    @Test
    fun convertTest() = runTest {
        val d = EasyDate("2020-01-13")
        val i = d.convert("2020-01-01")
        assertThat(i).isEqualTo(1012020)
    }

    @Test
    fun convertTestWithReverseDate() = runTest {
        val d = EasyDate("13-01-2020")
        val i = d.convert("01-01-2020")
        assertThat(i).isEqualTo(20200101)
    }

    @Test
    fun to_StringTest() = runTest {
        val d = EasyDate(LocalDate.now())
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.now().format(formatter)
        assertThat(d.to_String()).isEqualTo(date)
    }
}