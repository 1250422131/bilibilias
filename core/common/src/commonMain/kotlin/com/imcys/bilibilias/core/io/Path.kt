package com.imcys.bilibilias.core.io

import kotlinx.io.files.Path

/**
 * 获取子目录路径
 *
 * See Java `File.resolve`
 */
fun Path.resolve(vararg parts: String): Path = Path(this, parts = parts)

/**
 * 获取子目录路径
 *
 * See Java `File.resolve`
 */
fun Path.resolve(part: String): Path = Path(this, part)

/**
 * 将该路径放在 [SystemFileSystem] 中, 以便使用 [exists], [list] 等.
 */
inline val Path.inSystem get() = SystemPath(this)

/**
 * 由于 [Path] 是文件系统无关的, 我们需要显式表示将它于做系统文件系统.
 */
@JvmInline // Path 在绝大部分情况下都是直接使用的, 只有放入 collection 才会 box, 所以这里性能上是有保证的
value class SystemPath @PublishedApi internal constructor(
    val path: Path
) {
    override fun toString(): String = path.toString()
}

/**
 * @see Path.resolve
 */
fun SystemPath.resolve(part: String) = path.resolve(part).inSystem

/**
 * @see Path.resolve
 */
fun SystemPath.resolve(vararg parts: String) = path.resolve(*parts).inSystem