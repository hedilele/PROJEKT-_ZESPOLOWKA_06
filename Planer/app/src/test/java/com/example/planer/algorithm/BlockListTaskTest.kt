package com.example.planer.algorithm

import com.example.planer.entities.Tasks
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class BlockListTaskTest{

    @Test
    fun funTest() = runTest {
        var blt = BlockListTask(listOf(Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1), Tasks(1, "string", 0, 0, "24-03-2023", 1, 1, 1, 1)))
        blt.planner()
    }

}