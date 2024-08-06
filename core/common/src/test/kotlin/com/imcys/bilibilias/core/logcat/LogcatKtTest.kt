package com.imcys.bilibilias.core.logcat

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class LogcatTest {

    @AfterTest
    fun tearDown() {
        LogcatLogger.uninstall()
    }

    @Test
    fun `when no logger set, calling logcat() does not crash`() {
        logcat { "Yo" }
    }

    @Test
    fun `when no logger set, the message lambda isn't invoked`() {
        var count = 0

        logcat { "Yo${++count}" }

        assertEquals(0, count)
    }

    @Test
    fun `logcat() logs message from lambda`() {
        val logger = TestLogcatLogger().apply { LogcatLogger.install(this); latestLog = null }

        logcat { "Hi" }
        assertEquals("Hi", logger.latestLog!!.message)
    }

    @Test
    fun `logcat() captures tag from outer context class name`() {
        val logger = TestLogcatLogger().apply { LogcatLogger.install(this); latestLog = null }

        logcat { "Hi" }

        assertEquals(LogcatTest::class.java.simpleName, logger.latestLog!!.tag)
    }

    @Test
    fun `logcat() tag overriding passes tag to logger`() {
        val logger = TestLogcatLogger().apply { LogcatLogger.install(this); latestLog = null }

        logcat(tag = "Bonjour") { "Hi" }

        assertEquals("Bonjour", logger.latestLog!!.tag)
    }

    @Test
    fun `logcat() passes priority to logger`() {
        val logger = TestLogcatLogger().apply { LogcatLogger.install(this); latestLog = null }

        logcat(LogPriority.INFO) { "Hi" }

        assertEquals(LogPriority.INFO, logger.latestLog!!.priority)
    }

    @Test
    fun `logcat() passes priority to isLoggable check`() {
        var receivedPriority: LogPriority? = null
        TestLogcatLogger(isLoggable = { receivedPriority = it; true })
            .apply { LogcatLogger.install(this); latestLog = null }

        logcat(LogPriority.INFO) { "Hi" }

        assertEquals(LogPriority.INFO, receivedPriority)
    }

    @Test
    fun `when not loggable, the message lambda isn't invoked`() {
        TestLogcatLogger(isLoggable = { false }).apply {
            LogcatLogger.install(this); latestLog = null
        }
        var count = 0

        logcat { "Yo${++count}" }

        assertEquals(0, count)
    }

    @Test
    fun `Throwable asLogMessage() has stacktrace logged`() {
        val logger = TestLogcatLogger().apply { LogcatLogger.install(this); latestLog = null }
        val exception = RuntimeException("damn")

        logcat { exception.asLog() }

        assertContains(
            logger.latestLog!!.message,
            """
      |java.lang.RuntimeException: damn
    	|	at logcat.LogcatTest.Throwable asLogMessage() has stacktrace logged(LogcatTest.kt:
      """.trimMargin(),
        )
    }

    @Test
    fun `standalone function can log with tag`() {
        val logger = TestLogcatLogger().apply { LogcatLogger.install(this); latestLog = null }

        standaloneFunctionLog(tag = "Bonjour", message = { "Hi" })

        with(logger.latestLog!!) {
            assertEquals("Bonjour", tag)
            assertEquals("Hi", message)
        }
    }

    @Test
    fun `logcat() captures outer this tag from lambda`() {
        val logger = TestLogcatLogger().apply { LogcatLogger.install(this); latestLog = null }

        val lambda = {
            logcat { "Hi" }
        }
        lambda()

        assertEquals(LogcatTest::class.java.simpleName, logger.latestLog!!.tag)
    }

    @Test
    fun `logcat() captures outer this tag from nested lambda`() {
        val logger = TestLogcatLogger().apply { LogcatLogger.install(this); latestLog = null }

        val lambda = {
            val lambda = {
                logcat { "Hi" }
            }
            lambda()
        }
        lambda()

        assertEquals(LogcatTest::class.java.simpleName, logger.latestLog!!.tag)
    }

    @Test
    fun `logcat() captures outer this tag from anonymous object`() {
        val logger = TestLogcatLogger().apply { LogcatLogger.install(this); latestLog = null }

        val anonymousRunnable = object : Runnable {
            override fun run() {
                logcat { "Hi" }
            }
        }
        anonymousRunnable.run()

        assertEquals(LogcatTest::class.java.simpleName, logger.latestLog!!.tag)
    }

    @Test
    fun `logcat() captures tag from companion function`() {
        val logger = TestLogcatLogger().apply { LogcatLogger.install(this); latestLog = null }

        companionFunctionLog { "Hi" }

        assertEquals(LogcatTest::class.java.simpleName, logger.latestLog!!.tag)
    }

    companion object {
        fun companionFunctionLog(
            message: () -> String,
        ) {
            logcat(message = message)
        }
    }
}

fun standaloneFunctionLog(
    tag: String,
    message: () -> String,
) {
    logcat(tag, message = message)
}