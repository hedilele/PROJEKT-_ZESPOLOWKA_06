package com.example.planer.algorithm

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EasyDateTest{

    @Test
    fun construcktorTest1() = runTest {
        var d = EasyDate(LocalDate.now())
    }

    @Test
    fun to_StringTest() = runTest {
        var d = EasyDate(LocalDate.now())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.now().format(formatter)
        assertThat(d.to_String()).isEqualTo(date)
    }
}