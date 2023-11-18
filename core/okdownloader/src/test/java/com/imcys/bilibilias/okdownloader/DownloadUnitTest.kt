package com.imcys.bilibilias.okdownloader

import com.imcys.bilibilias.okdownloader.internal.util.md5
import okhttp3.internal.notifyAll
import okhttp3.internal.wait
import org.junit.Assert
import org.junit.Test
import java.nio.file.Files
import java.util.UUID

class DownloadUnitTest {

    private val downloader by lazy { Downloader.Builder().build() }
    private val downloader2 by lazy { downloader.newBuilder().build() }

    @Test
    fun argument_is_correct() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            Download.Request.Builder().build()
        }
        Assert.assertThrows(IllegalArgumentException::class.java) {
            Download.Request.Builder().url("")
                .build()
        }
        Assert.assertThrows(IllegalArgumentException::class.java) {
            Download.Request.Builder().url("xxx")
                .build()
        }
        Assert.assertThrows(IllegalArgumentException::class.java) {
            Download.Request.Builder().into("").build()
        }
        val request = Download.Request.Builder()
            .url("xxx")
            .into("xxx")
            .build()
        val response = downloader.newCall(request).execute()
        Assert.assertFalse(response.isSuccessful())
    }

    @Test
    fun execute_is_correct() {
        val request = buildRequest()
        val call = downloader.newCall(request)
        call.execute()
        Assert.assertThrows(IllegalStateException::class.java) {
            call.execute()
        }
    }

    private fun buildRequest(): Download.Request {
        return Download.Request.Builder()
            .url(FakeData.resources[0].url)
            .into(Files.createTempFile(UUID.randomUUID().toString(), ".apk").toFile())
            .build()
    }

    @Test
    fun source_file_exists() {
        val request = buildRequest()
        val call = downloader.newCall(request)
        call.enqueue(object : Download.Callback {
            override fun onLoading(tmp: Download.Call, current: Long, total: Long) {
                super.onLoading(tmp, current, total)
                synchronized(call) { call.notifyAll() }
            }
        })
        synchronized(call) { call.wait() }
        Assert.assertTrue(request.sourceFile().exists())
    }

    @Test
    fun cancel_is_correct() {
        val request = buildRequest()
        val call = downloader.newCall(request)
        call.enqueue(object : Download.Callback {
            override fun onLoading(tmp: Download.Call, current: Long, total: Long) {
                super.onLoading(tmp, current, total)
                synchronized(call) { call.notifyAll() }
            }
        })
        synchronized(call) { call.wait() }
        call.cancel()
        Thread.sleep(1000)
        Assert.assertTrue(request.sourceFile().exists())
    }

    @Test
    fun cancelSafely_is_correct() {
        val request = buildRequest()
        val call = downloader.newCall(request)
        call.enqueue(object : Download.Callback {
            override fun onLoading(tmp: Download.Call, current: Long, total: Long) {
                super.onLoading(tmp, current, total)
                synchronized(call) { call.notifyAll() }
            }
        })
        synchronized(call) { call.wait() }
        call.cancelSafely()
        Assert.assertTrue(call.isCanceled())
        Thread.sleep(1000)
        Assert.assertFalse(request.sourceFile().exists())
    }

    @Test
    fun cancelAll_is_correct() {
        val request = buildRequest()
        val call = downloader.newCall(request)
        call.enqueue(object : Download.Callback {
            override fun onLoading(tmp: Download.Call, current: Long, total: Long) {
                super.onLoading(tmp, current, total)
                synchronized(call) { call.notifyAll() }
            }
        })
        synchronized(call) { call.wait() }
        downloader.cancelAll()
        Thread.sleep(1000)
        Assert.assertTrue(call.isCanceled())
        Assert.assertTrue(request.sourceFile().exists())
    }

    @Test
    fun cancelAllSafely_is_correct() {
        val request = buildRequest()
        val call = downloader.newCall(request)
        call.enqueue(object : Download.Callback {
            override fun onLoading(tmp: Download.Call, current: Long, total: Long) {
                super.onLoading(tmp, current, total)
                synchronized(call) { call.notifyAll() }
            }
        })
        synchronized(call) { call.wait() }
        downloader.cancelAllSafely()
        Thread.sleep(1000)
        Assert.assertTrue(call.isCanceled())
        Assert.assertFalse(request.sourceFile().exists())
    }

    @Test
    fun downloadPool_is_correct() {
        val request = buildRequest()
        val call = downloader.newCall(request)
        call.enqueue(object : Download.Callback {
            override fun onLoading(tmp: Download.Call, current: Long, total: Long) {
                super.onLoading(tmp, current, total)
                synchronized(call) { call.notifyAll() }
            }
        })
        synchronized(call) { call.wait() }
        downloader2.cancelAll()
        Thread.sleep(1000)
        Assert.assertFalse(call.isCanceled())
        Assert.assertTrue(request.sourceFile().exists())
    }

    @Test
    fun callback_is_correct() {
        val request = buildRequest()
        val call = downloader.newCall(request)
        val methodCount = mutableMapOf<String, Int>()
        call.execute(object : Download.Callback {
            override fun onStart(call: Download.Call) {
                super.onStart(call)
                methodCount["onStart"] = methodCount["onStart"] ?: 0 + 1
            }

            override fun onCancel(call: Download.Call) {
                super.onCancel(call)
                methodCount["onCancel"] = methodCount["onCancel"] ?: 0 + 1
            }

            override fun onChecking(call: Download.Call) {
                super.onChecking(call)
                methodCount["onChecking"] = methodCount["onChecking"] ?: 0 + 1
            }

            override fun onRetrying(call: Download.Call) {
                super.onRetrying(call)
                methodCount["onRetrying"] = methodCount["onRetrying"] ?: 0 + 1
            }

            override fun onSuccess(call: Download.Call, response: Download.Response) {
                super.onSuccess(call, response)
                println("onSuccess")
                methodCount["onSuccess"] = methodCount["onSuccess"] ?: 0 + 1
            }

            override fun onFailure(call: Download.Call, response: Download.Response) {
                super.onFailure(call, response)
                println("onFailure")
                methodCount["onFailure"] = methodCount["onFailure"] ?: 0 + 1
            }

            override fun onLoading(tmp: Download.Call, current: Long, total: Long) {
            }
        })
        Assert.assertEquals(1, methodCount["onStart"])
        Assert.assertEquals(1, methodCount["onSuccess"])
    }

    @Test
    fun download_is_correct() {
        val request = Download.Request.Builder()
            .url(FakeData.resources[0].url)
            .into(Files.createTempFile(UUID.randomUUID().toString(), ".apk").toFile())
            .build()
        val response = downloader.newCall(request).execute()
        Assert.assertTrue(response.isSuccessful())
        Assert.assertFalse(response.isBreakpoint())
        Assert.assertTrue(response.output?.exists() == true)
        Assert.assertEquals(FakeData.resources[0].size, response.downloadLength)
        Assert.assertEquals(FakeData.resources[0].size, response.totalSize)
        Assert.assertEquals(FakeData.resources[0].md5, response.output?.md5())
    }

    @Test
    fun retry_count_is_correct() {
        val request = Download.Request.Builder()
            .url("https://xxx")
            .into(Files.createTempFile(UUID.randomUUID().toString(), ".apk").toFile())
            .build()
        val response = downloader.newCall(request).execute()
        Assert.assertFalse(response.isSuccessful())
        Assert.assertFalse(response.output?.exists() == true)
        Assert.assertEquals(3, response.retryCount)
        Assert.assertEquals(0, response.downloadLength)
        Assert.assertEquals(0, response.totalSize)
    }

    @Test
    fun download_pool_is_correct() {
        val downloadPool = DownloadPool()
        val downloader = Downloader.Builder().downloadPool(downloadPool).build()
        val downloader2 = downloader.newBuilder().build()
        val downloader3 = downloader.newBuilder().downloadPool(downloadPool).build()
        val downloader4 = Downloader.Builder().downloadPool(downloadPool).build()
        val downloader5 = Downloader.Builder().build()
        Assert.assertFalse(downloader.downloadPool === downloader2.downloadPool)
        Assert.assertSame(downloader.downloadPool, downloader3.downloadPool)
        Assert.assertSame(downloader.downloadPool, downloader4.downloadPool)
        Assert.assertFalse(downloader.downloadPool === downloader5.downloadPool)
    }
}
