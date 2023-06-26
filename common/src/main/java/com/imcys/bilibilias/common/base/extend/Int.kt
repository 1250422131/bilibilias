package com.imcys.bilibilias.common.base.extend


fun Int.digitalConversion(): String {

    fun lengthNum(num: Int): Int {
        var mNum = num
        var count = 0 //计数
        while (mNum >= 1) {
            mNum /= 10
            count++
        }
        return count
    }

    val originallyNum: String = this.toString() + ""
    val result: String
    return if (this >= 10000) {
        when (lengthNum(this)) {
            5 -> {
                result = originallyNum[0].toString() + "." + originallyNum[1] + "万"
                result
            }

            6 -> {
                result = originallyNum.substring(0, 2) + "." + originallyNum[2] + "万"
                result
            }

            7 -> {

                result = originallyNum.substring(0, 3) + "." + originallyNum[3] + "万"
                result
            }

            8 -> {
                result = originallyNum.substring(0, 4) + "." + originallyNum[4] + "万"
                result
            }

            else -> {
                result = originallyNum[0] + "." + originallyNum[1] + "亿"
                result
            }
        }
    } else {
        this.toString() + ""
    }


}

