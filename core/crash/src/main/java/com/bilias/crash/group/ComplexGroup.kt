package com.bilias.crash.group

/**
 * 混合分组
 */
class ComplexGroup internal constructor(
    private val mSymbol: String,
    private val mLeft: Group,
    private val mRight: Group
) : Group {
    override fun counts(): Boolean {
        return when (mSymbol) {
            SYMBOL_INTERSECTION -> mLeft.counts() && mRight.counts()
            SYMBOL_UNION_SET -> mLeft.counts() || mRight.counts()
            SYMBOL_SUPPLEMENTARY_SET -> !(mLeft.counts() || mRight.counts())
            else -> false
        }
    }

    override val name: String =
        if (mSymbol == SYMBOL_SUPPLEMENTARY_SET)
            "$mSymbol($mLeft $mRight)"
        else "($mLeft$mSymbol$mRight)"

    companion object {
        /** 交集 */
        const val SYMBOL_INTERSECTION = "∩"

        /** 并集 */
        const val SYMBOL_UNION_SET = "∪"

        /** 补集 */
        const val SYMBOL_SUPPLEMENTARY_SET = "^"
        fun and(left: Group, right: Group): ComplexGroup {
            return ComplexGroup(SYMBOL_INTERSECTION, left, right)
        }

        fun or(left: Group, right: Group): ComplexGroup {
            return ComplexGroup(SYMBOL_UNION_SET, left, right)
        }

        fun not(left: Group, right: Group): ComplexGroup {
            return ComplexGroup(SYMBOL_SUPPLEMENTARY_SET, left, right)
        }
    }
}