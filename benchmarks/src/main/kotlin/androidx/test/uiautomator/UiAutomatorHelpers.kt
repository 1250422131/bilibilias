package androidx.test.uiautomator

import androidx.test.uiautomator.HasChildrenOp.AT_LEAST
import androidx.test.uiautomator.HasChildrenOp.AT_MOST
import androidx.test.uiautomator.HasChildrenOp.EXACTLY

// These helpers need to be in the androidx.test.uiautomator package,
// because the abstract class has package local method that needs to be implemented.

/**
 * Condition will be satisfied if given element has specified count of children
 */
fun untilHasChildren(
    childCount: Int = 1,
    op: HasChildrenOp = AT_LEAST,
): UiObject2Condition<Boolean> = object : UiObject2Condition<Boolean>() {
    override fun apply(element: UiObject2): Boolean = when (op) {
        HasChildrenOp.AT_LEAST -> element.childCount >= childCount
        EXACTLY -> element.childCount == childCount
        AT_MOST -> element.childCount <= childCount
    }
}

enum class HasChildrenOp {
    AT_LEAST,
    EXACTLY,
    AT_MOST,
}
