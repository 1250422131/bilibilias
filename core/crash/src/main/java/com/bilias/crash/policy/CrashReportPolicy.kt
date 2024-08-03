package com.bilias.crash.policy

import com.bilias.crash.Info
import com.bilias.crash.group.Group

/**
 * 崩溃信息通道
 */
abstract class CrashReportPolicy
protected constructor(group: Group, policy: CrashReportPolicy?) :
    PolicyWrapper<Info, CrashReportPolicy>(group, policy)