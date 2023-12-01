package com.imcys.bilibilias.okdownloader

import com.imcys.bilibilias.okdownloader.internal.util.requireNotNullOrEmpty
import java.io.File

interface Download {

    class Request private constructor(
        val url: String,
        val path: String,
        val md5: String?,
        val tag: String?,
        val size: Long?,
        val retry: Int?,
        val priority: Priority,
        val headers: Map<String, String>,
        val groupId: Long = DEFAULT_GROUP_ID
    ) {
        fun newBuilder(): Builder = Builder(this)

        fun sourceFile(): File = File("$path.tmp")

        fun destFile(): File = File(path)
        fun headers(): Array<String> {
            val header = mutableListOf<String>()
            headers.forEach { (name, value) ->
                header.add(name)
                header.add(value)
            }
            return header.toTypedArray()
        }

        override fun toString(): String {
            return "Request(url='$url', path='$path', md5=$md5, tag=$tag, size=$size, retry=$retry, priority=$priority)"
        }

        class Builder {
            private var url: String? = null
            private var path: String? = null
            private var md5: String? = null
            private var tag: String? = null
            private var size: Long? = null
            private var retry: Int = DEFAULT_RETRY
            private var groupId: Long = DEFAULT_GROUP_ID
            private var priority: Priority = Priority.MIDDLE
            private var headers = mutableMapOf<String, String>()

            constructor()

            constructor(request: Request) {
                this.url = request.url
                this.path = request.path
                this.md5 = request.md5
                this.tag = request.tag
                this.size = request.size
                this.priority = request.priority
                this.groupId = request.groupId
                this.headers = request.headers.toMutableMap()
            }

            fun url(url: String): Builder = apply {
                this.url = url
            }

            fun md5(md5: String): Builder = apply {
                this.md5 = md5
            }

            fun tag(tag: String): Builder = apply {
                this.tag = tag
            }

            fun size(size: Long): Builder = apply {
                this.size = size
            }

            fun into(path: String): Builder = apply {
                this.path = path
            }

            fun into(file: File): Builder = apply {
                this.path = file.absolutePath
            }

            fun retry(retry: Int): Builder = apply {
                this.retry = retry
            }

            fun priority(priority: Priority): Builder = apply {
                this.priority = priority
            }

            fun header(name: String, value: String): Builder = apply {
                this.headers[name] = value
            }

            fun groupId(group: Long): Builder = apply {
                groupId = group
            }

            fun build(): Request {
                return Request(
                    url = requireNotNullOrEmpty(url) { "Missing url!" },
                    path = requireNotNullOrEmpty(path) { "Missing path!" },
                    md5 = md5,
                    size = size,
                    retry = retry,
                    tag = tag,
                    priority = priority,
                    headers = headers
                )
            }
        }
    }

    class Response internal constructor(
        val code: Int,
        val message: String?,
        val output: File?,
        val retryCount: Int,
        val downloadLength: Long,
        val totalSize: Long,
    ) {
        fun isSuccessful(): Boolean = this.code in ErrorCode.SUCCESS..ErrorCode.EXISTS_SUCCESS

        fun newBuilder(): Builder = Builder(this)

        fun isBreakpoint(): Boolean = this.code == ErrorCode.APPEND_SUCCESS

        override fun toString(): String {
            return "Response(code=$code, message=$message, output=$output, retryCount=$retryCount, downloadLength=$downloadLength, totalSize=$totalSize)"
        }

        class Builder {
            private var code: Int = ErrorCode.SUCCESS
            private var message: String? = null
            private var output: File? = null
            private var downloadLength: Long = 0L
            private var retryCount: Int = 0
            private var totalSize: Long = 0

            constructor()

            internal constructor(response: Response) {
                this.code = response.code
                this.message = response.message
                this.output = response.output
                this.downloadLength = response.downloadLength
                this.retryCount = response.retryCount
                this.totalSize = response.totalSize
            }

            fun code(code: Int): Builder = apply {
                this.code = code
            }

            fun message(message: String): Builder = apply {
                this.message = message
            }

            fun output(output: File): Builder = apply {
                this.output = output
            }

            fun downloadLength(downloadLength: Long): Builder = apply {
                this.downloadLength = downloadLength
            }

            fun retryCount(retryCount: Int): Builder = apply {
                this.retryCount = retryCount
            }

            fun totalSize(totalSize: Long): Builder = apply {
                this.totalSize = totalSize
            }

            fun build(): Response {
                return Response(
                    code = this.code,
                    message = this.message,
                    output = this.output,
                    downloadLength = this.downloadLength,
                    retryCount = this.retryCount,
                    totalSize = this.totalSize,
                )
            }
        }
    }

    interface Call {
        val request: Request

        fun execute(): Response

        fun execute(callback: Callback): Response

        fun enqueue()

        fun enqueue(callback: Callback)

        fun cancel()

        fun cancelSafely()

        fun isExecuted(): Boolean

        fun isCanceled(): Boolean

        fun interface Factory {
            fun newCall(request: Request): Call
        }
    }

    interface Callback {
        fun onStart(call: Call) {}
        fun onLoading(call: Call, current: Long, total: Long) {}
        fun onCancel(call: Call) {}
        fun onChecking(call: Call) {}
        fun onRetrying(call: Call) {}
        fun onSuccess(call: Call, response: Response) {}
        fun onFailure(call: Call, response: Response) {}

        companion object {
            @JvmField
            val NOOP: Callback = object : Callback {}
        }
    }

    enum class Priority {
        LOW_LOW, LOW, MIDDLE, HIGH, HIGH_HIGH
    }

    interface Subjection {
        fun subscribe(subscriber: Subscriber)
        fun unsubscribe(subscriber: Subscriber)
        fun subscribe(url: String, subscriber: Subscriber)
        fun unsubscribe(url: String, subscriber: Subscriber)
    }

    interface Subscriber {
        fun onSuccess(call: Call, response: Response) {}
        fun onFailure(call: Call, response: Response) {}
    }
}
