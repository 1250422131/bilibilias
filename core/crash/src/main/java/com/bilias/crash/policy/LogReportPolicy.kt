package com.bilias.crash.policy

import com.bilias.crash.LogInfo
import com.bilias.crash.group.Group

/**
 * 发送日志信息的策略
 */
abstract class LogReportPolicy protected constructor(group: Group, policy: LogReportPolicy?) :
    PolicyWrapper<LogInfo, LogReportPolicy>(group, policy)