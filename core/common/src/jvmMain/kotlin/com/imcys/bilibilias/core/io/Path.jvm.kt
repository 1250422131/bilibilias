package com.imcys.bilibilias.core.io

import kotlinx.io.files.Path
import java.io.File

fun Path.toFile(): File = File(this.toString())
fun SystemPath.toFile(): File = path.toFile()