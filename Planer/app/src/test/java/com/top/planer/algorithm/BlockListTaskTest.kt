package com.top.planer.algorithm

import com.top.planer.entities.Tasks
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class BlockListTaskTest{

    @Test
    fun funTest() = runTest {
        val blt = BlockListTask(listOf(Tasks(1, "string", 0, 0, "07.04.2023  15:05", 1, 1, 1, 1), Tasks(1, "string", 0, 0, "24-03-2020", 1, 1, 1, 1)))
        println(blt.list)
        blt.planner()
        assertThat(blt.today_list).isNotEmpty()
        assertThat(blt.tomorrow_list).isEmpty()
        assertThat(blt.week_list).isEmpty()
        assertThat(blt.month_list).isEmpty()
        assertThat(blt.rest_list).isNotEmpty()
    }

    @Test
    fun plannerTest() = runTest {
        val today = EasyDate(LocalDate.now())
        val easy = EasyDate("07.04.2023  15:05")
        throw Exception(
            "${today.date}\n" +
                    "${(today+1).date}\n" +
                "${(today+7).date}\n"+
            "${(today+31).date}\n" +
            "${easy.date}\n"
        )
    }

    @Test
    fun KotlinTest() = runTest {

    }

}