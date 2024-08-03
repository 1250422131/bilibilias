package com.bilias.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.measureTime

/**
 * Example local unit test, which will execute on the development machine
 * (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_flow() {
        val t = measureTime {
            combine(requestElectricCost(), requestWaterCost(), requestInternetCost()) { a, b, c ->
                println(a.info())
                println(b.info())
                println(c.info())
            }
        }
        println(t)
    }

    fun requestElectricCost(): Flow<ExpendModel> =
        flow {
            delay(500)
            emit(ExpendModel("电费", 10f, 500))
        }.flowOn(Dispatchers.IO)

    fun requestWaterCost(): Flow<ExpendModel> =
        flow {
            delay(1000)
            emit(ExpendModel("水费", 20f, 1000))
        }.flowOn(Dispatchers.IO)

    fun requestInternetCost(): Flow<ExpendModel> =
        flow {
            delay(2000)
            emit(ExpendModel("网费", 30f, 2000))
        }.flowOn(Dispatchers.IO)

    data class ExpendModel(val type: String, val cost: Float, val apiTime: Int) {
        fun info(): String {
            return "$type: $cost, 接口请求耗时约$apiTime ms"
        }
    }
}
