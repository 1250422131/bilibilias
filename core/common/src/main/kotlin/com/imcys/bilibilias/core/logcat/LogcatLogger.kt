package com.imcys.bilibilias.core.logcat

/**
 * Logger that [logcat] delegates to. Call [install] to install a new logger, the default is a
 * no-op logger. Calling [uninstall] falls back to the default no-op logger.
 *
 * You should install [AndroidLogcatLogger] on Android and [PrintLogger] on a JVM.
 */
interface LogcatLogger {

  /**
   * Whether a log with the provided priority should be logged and the corresponding message
   * providing lambda evaluated. Called by [logcat].
   */
  fun isLoggable(priority: LogPriority) = true

  /**
   * Write a log to its destination. Called by [logcat].
   */
  fun log(
    priority: LogPriority,
    tag: String,
    message: String
  )

  companion object {
    @Volatile
    @PublishedApi
    internal var logger: LogcatLogger = NoLog
      private set

    @Volatile
    private var installedThrowable: Throwable? = null

    val isInstalled: Boolean
      get() = installedThrowable != null

    /**
     * Installs a [LogcatLogger].
     *
     * It is an error to call [install] more than once without calling [uninstall] in between,
     * however doing this won't throw, it'll log an error to the newly provided logger.
     */
    fun install(logger: LogcatLogger) {
      synchronized(this) {
        if (isInstalled) {
            logger.log(
              LogPriority.ERROR,
            "LogcatLogger",
            "Installing $logger even though a logger was previously installed here: " +
              installedThrowable!!.asLog()
          )
        }
        installedThrowable = RuntimeException("Previous logger installed here")
        Companion.logger = logger
      }
    }

    /**
     * Replaces the current logger (if any) with a no-op logger.
     */
    fun uninstall() {
      synchronized(this) {
        installedThrowable = null
        logger = NoLog
      }
    }
  }

  private object NoLog : LogcatLogger {
    override fun isLoggable(priority: LogPriority) = false

    override fun log(
      priority: LogPriority,
      tag: String,
      message: String
    ) = error("Should never receive any log")
  }
}