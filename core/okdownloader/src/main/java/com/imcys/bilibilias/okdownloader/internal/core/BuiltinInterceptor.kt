package com.imcys.bilibilias.okdownloader.internal.core

import com.imcys.bilibilias.okdownloader.Download
import com.imcys.bilibilias.okdownloader.DownloadException
import com.imcys.bilibilias.okdownloader.Downloader
import com.imcys.bilibilias.okdownloader.ErrorCode
import com.imcys.bilibilias.okdownloader.Interceptor
import com.imcys.bilibilias.okdownloader.internal.exception.CancelException
import com.imcys.bilibilias.okdownloader.internal.exception.TerminalException
import com.imcys.bilibilias.okdownloader.internal.util.contentLength
import com.imcys.bilibilias.okdownloader.internal.util.deleteIfExists
import com.imcys.bilibilias.okdownloader.internal.util.makeNewFile
import com.imcys.bilibilias.okdownloader.internal.util.md5
import com.imcys.bilibilias.okdownloader.internal.util.ofRangeStart
import com.imcys.bilibilias.okdownloader.internal.util.renameToTarget
import okhttp3.Headers
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http2.StreamResetException
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.MalformedURLException

internal class RetryInterceptor(private val client: Downloader) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Download.Response {
        var retry = chain.request().retry ?: client.defaultMaxRetry
        var retryCount = 0
        while (true) {
            val response = chain.proceed(chain.request())
            if (response.isSuccessful() || retry-- < 1 || chain.call().isCanceled()) {
                return response.newBuilder()
                    .retryCount(retryCount)
                    .build()
            }
            try {
                Thread.sleep(3000)
            } catch (ex: InterruptedException) {
                // ignore
            }
            chain.callback().onRetrying(chain.call())
            retryCount++
        }
    }
}

internal fun Interceptor.Chain.checkTerminal() {
    val call = this.call()
    if (call is InternalCall && call.isCancelSafely()) {
        throw TerminalException("Call terminal!")
    }
    if (call.isCanceled()) {
        throw CancelException("Call canceled!")
    }
}

internal class VerifierInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Download.Response {
        val request = chain.request()
        val destFile = request.destFile()
        val response = chain.proceed(request)
        if (!response.isSuccessful()) return response
        chain.callback().onChecking(chain.call())
        if (destFile.exists().not()) {
            throw DownloadException(ErrorCode.VERIFY_FILE_NOT_EXISTS, "File not exists")
        }
        if (destFile.isFile.not()) {
            throw DownloadException(ErrorCode.VERIFY_FILE_NOT_FILE, "Not file")
        }
        if (!request.md5.isNullOrEmpty() && destFile.md5() != request.md5) {
            throw DownloadException(ErrorCode.VERIFY_MD5_NOT_MATCHED, "MD5 not matched")
        }
        if (request.size != null && destFile.length() != request.size) {
            throw DownloadException(ErrorCode.VERIFY_SIZE_NOT_MATCHED, "Size not matched")
        }
        return response
    }
}

internal class SynchronousInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Download.Response {
        val request = chain.request()
        val (resLock, fileLock) = synchronized(SynchronousInterceptor::class.java) {
            sResLocks.getOrPut(request.url) { Any() } to sFileLock.getOrPut(request.path) { Any() }
        }
        return synchronized(fileLock) {
            synchronized(resLock) {
                chain.proceed(chain.request())
            }
        }.also {
            synchronized(SynchronousInterceptor::class.java) {
                sResLocks -= request.url
                sFileLock -= request.path
            }
        }
    }

    companion object {
        private val sResLocks = mutableMapOf<String, Any>()
        private val sFileLock = mutableMapOf<String, Any>()
    }
}

internal class LocalExistsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Download.Response {
        val destFile = chain.request().destFile()
        val md5 = chain.request().md5
        if (md5 != null && destFile.exists() && destFile.md5() == md5) {
            return Download.Response.Builder()
                .code(ErrorCode.EXISTS_SUCCESS)
                .totalSize(destFile.length())
                .message("File exists and MD5 matched, don`t need download!")
                .build()
        }
        return chain.proceed(chain.request())
    }
}

internal class ExceptionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Download.Response {
        return try {
            try {
                val response = chain.proceed(chain.request())
                if (response.isSuccessful().not()) chain.checkTerminal()
                response
            } catch (t: Throwable) {
                chain.checkTerminal()
                throw t
            }
        } catch (ex: CancelException) {
            chain.callback().onCancel(chain.call())
            Download.Response.Builder().code(ErrorCode.CANCEL)
                .messageWith(ex)
                .build()
        } catch (ex: TerminalException) {
            chain.callback().onCancel(chain.call())
            chain.request().sourceFile().deleteIfExists()
            Download.Response.Builder().code(ErrorCode.CANCEL)
                .messageWith(ex)
                .build()
        } catch (ex: StreamResetException) {
            Download.Response.Builder().code(ErrorCode.NET_STREAM_RESET)
                .messageWith(ex)
                .build()
        } catch (ex: InterruptedIOException) {
            Download.Response.Builder().code(ErrorCode.IO_INTERRUPTED)
                .messageWith(ex)
                .build()
        } catch (ex: InterruptedException) {
            Download.Response.Builder().code(ErrorCode.INTERRUPTED)
                .messageWith(ex)
                .build()
        } catch (ex: IllegalArgumentException) {
            Download.Response.Builder().code(ErrorCode.ARGUMENT_EXCEPTION)
                .messageWith(ex)
                .build()
        } catch (ex: MalformedURLException) {
            Download.Response.Builder().code(ErrorCode.MALFORMED_URL)
                .messageWith(ex)
                .build()
        } catch (ex: FileNotFoundException) {
            Download.Response.Builder().code(ErrorCode.FILE_NOT_FOUND)
                .messageWith(ex)
                .build()
        } catch (ex: IOException) {
            Download.Response.Builder().code(ErrorCode.IO_EXCEPTION)
                .messageWith(ex)
                .build()
        } catch (ex: DownloadException) {
            Download.Response.Builder().code(ex.code)
                .messageWith(ex)
                .build()
        } catch (t: Throwable) {
            Download.Response.Builder().code(ErrorCode.UNKNOWN)
                .messageWith(t)
                .build()
        }
    }

    private fun Download.Response.Builder.messageWith(t: Throwable): Download.Response.Builder {
        return message(t.toString())
    }
}

internal class ExchangeInterceptor(private val client: Downloader) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Download.Response {
        val downloadRequest = chain.request()
        val sourceFile = downloadRequest.sourceFile()
        if (!sourceFile.exists()) sourceFile.makeNewFile()
        chain.checkTerminal()
        val httpResponse = getHttpResponse(chain, downloadRequest)
        chain.checkTerminal()
        if (!httpResponse.isSuccessful) throw DownloadException(
            ErrorCode.REMOTE_CONNECT_ERROR,
            "Http connection failed, message = ${httpResponse.message}, httpCode = ${httpResponse.code}"
        )
        val body = httpResponse.body
            ?: throw DownloadException(ErrorCode.REMOTE_CONTENT_EMPTY, "Remote source body is null")
        var downloadLength = 0L
        val startLength = sourceFile.length()
        val contentLength = startLength + httpResponse.contentLength()
        try {
            IOExchange().exchange(sourceFile, body.source()) {
                if (it > 0) {
                    downloadLength += it
                }
                chain.callback()
                    .onLoading(chain.call(), startLength + downloadLength, contentLength)
            }
        } catch (e: IOException) {
            chain.checkTerminal()
            throw e
        }
        sourceFile.renameToTarget(downloadRequest.destFile())
        return Download.Response.Builder()
            .code(if (startLength > 0) ErrorCode.APPEND_SUCCESS else ErrorCode.SUCCESS)
            .downloadLength(downloadLength)
            .totalSize(contentLength)
            .output(downloadRequest.destFile())
            .message("Success")
            .build()
    }

    private fun getHttpResponse(chain: Interceptor.Chain, request: Download.Request): Response {
        val httpRequest = Request.Builder()
            .url(request.url)
            .headers(Headers.headersOf(*request.headers()))
            .ofRangeStart(request.sourceFile().length())
            .get()
            .build()
        try {
            val httpCall = client.okhttpClient.newCall(httpRequest)
            val call = chain.call()
            if (call is InternalCall) {
                call.httpCall = httpCall
            }
            return httpCall.execute()
        } catch (e: IOException) {
            chain.checkTerminal()
            throw DownloadException(ErrorCode.REMOTE_CONNECT_ERROR, "Connection failed: $e", e)
        }
    }
}
