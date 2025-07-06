package com.imcys.bilibilias.ui.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AsCardGroups(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(4.dp, Alignment.Start),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: AsCardGroupsScope.() -> Unit
) {
    val scope = AsCardGroupsScopeImpl()
    scope.content()

    Row(
        modifier = modifier,
        horizontalArrangement, verticalAlignment
    ) {
        scope.items.forEachIndexed { index, item ->
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.weight(1f),
                shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStart = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 8.dp,
                        topEnd = 8.dp
                    )

                    scope.items.size - 1 -> RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp,
                        bottomEnd = 16.dp,
                        topEnd = 16.dp
                    )

                    else -> RoundedCornerShape(8.dp)
                }
            ) {
                item.content()
            }
        }
    }
}

// DSL 作用域接口
interface AsCardGroupsScope {
    fun item(
        key: Any? = null,
        content: @Composable () -> Unit
    )
}

// DSL 作用域实现
private class AsCardGroupsScopeImpl : AsCardGroupsScope {
    private val _items = mutableListOf<AsCardItem>()
    val items: List<AsCardItem> get() = _items

    override fun item(
        key: Any?,
        content: @Composable () -> Unit
    ) {
        _items.add(AsCardItem(key, content))
    }
}

// 内部数据类
private data class AsCardItem(
    val key: Any?,
    val content: @Composable () -> Unit
)