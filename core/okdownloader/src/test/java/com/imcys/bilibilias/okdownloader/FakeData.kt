package com.imcys.bilibilias.okdownloader

object FakeData {

    val resources: List<ResourceBean> = listOf(
        ResourceBean(
            url = "http://cdn.billbook.net.cn/apk/Threads%2C%20an%20Instagram%20app_291.0.0.31.111_Apkpure.apk",
            name = "Threads",
            icon = "https://image.winudf.com/v2/image1/Y29tLmluc3RhZ3JhbS5iYXJjZWxvbmFfaWNvbl8xNjg4MjYzMjE4XzAyMg/icon.webp?w=280&fakeurl=1&type=.webp",
            size = 76639442,
            md5 = "9631fff7a586b9870fb0116b136cbfef"
        ),
        ResourceBean(
            url = "http://cdn.billbook.net.cn/apk/Twitter_9.96.0-release.0_Apkpure.apk",
            name = "Twitter",
            icon = "https://image.winudf.com/v2/image1/Y29tLnR3aXR0ZXIuYW5kcm9pZF9pY29uXzE1NTU0NjI4MTJfMDI2/icon.webp?w=280&fakeurl=1&type=.webp",
            size = 113837675,
            md5 = "25eb790c62edf3363ffd899ed8ea8a7a"
        ),
        ResourceBean(
            url = "http://cdn.billbook.net.cn/apk/Cash%E2%80%99em%20All_%20Play%20%26%20Win_4.8.1-CashemAll_Apkpure.apk",
            name = "Cash’em All: Play & Win",
            icon = "https://image.winudf.com/v2/image1/b25saW5lLmNhc2hlbWFsbC5hcHBfaWNvbl8xNTk0NDA0NDY5XzAwOA/icon.webp?w=280&fakeurl=1&type=.webp",
            size = 56572333,
            md5 = "1627a6317e820347ebeeec1aef6e6df3"
        ),
        ResourceBean(
            url = "http://cdn.billbook.net.cn/apk/TikTok_30.3.4_Apkpure.xapk",
            name = "TikTok",
            icon = "https://image.winudf.com/v2/image1/Y29tLnpoaWxpYW9hcHAubXVzaWNhbGx5X2ljb25fMTY2NjcyMjU0MF8wOTY/icon.webp?w=280&fakeurl=1&type=.webp",
            size = 129126005,
            md5 = "067095681c5fa97def29f2b83b7e6803"
        ),
        ResourceBean(
            url = "http://cdn.billbook.net.cn/apk/Facebook_422.0.0.26.76_Apkpure.xapk",
            name = "Facebook",
            icon = "https://image.winudf.com/v2/image1/Y29tLmZhY2Vib29rLmthdGFuYV9pY29uXzE1NTc5OTAwMzBfMDIz/icon.webp?w=280&fakeurl=1&type=.webp",
            size = 54846526,
            md5 = "dc050e289d9fe0f10ba2321740301f6d"
        ),
        ResourceBean(
            url = "http://cdn.billbook.net.cn/apk/CapCut%20-%20Video%20Editor_8.7.0_Apkpure.apk",
            name = "CapCut ",
            icon = "https://image.winudf.com/v2/image1/Y29tLmxlbW9uLmx2b3ZlcnNlYXNfaWNvbl8xNjYwMjE4OTc4XzA1NA/icon.webp?w=280&fakeurl=1&type=.webp",
            size = 170715905,
            md5 = "f34dcf22ec82dfd82d4ab2f110a2c2de"
        ),
        ResourceBean(
            url = "http://cdn.billbook.net.cn/apk/Snapchat_12.42.0.58_Apkpure.xapk",
            name = "Snapchat",
            icon = "https://image.winudf.com/v2/image1/Y29tLnNuYXBjaGF0LmFuZHJvaWRfaWNvbl8xNTY2ODQ0NzEzXzA4Nw/icon.webp?w=280&fakeurl=1&type=.webp",
            size = 97595724,
            md5 = "99a6e36d5523d223a651e62d9a2cc052"
        ),
        ResourceBean(
            url = "http://cdn.billbook.net.cn/apk/WhatsApp%20Messenger_2.23.15.3_Apkpure.apk",
            name = "WhatsApp Messenger",
            icon = "https://image.winudf.com/v2/image1/Y29tLndoYXRzYXBwX2ljb25fMTU1OTg1MDA2NF8wNjI/icon.webp?w=280&fakeurl=1&type=.webp",
            size = 54225683,
            md5 = "57dfd00e1a5319cf8d5fa9b1f9e1a28e"
        ),
        ResourceBean(
            url = "http://cdn.billbook.net.cn/apk/Instagram_290.0.0.13.76_Apkpure.apk",
            name = "Instagram",
            icon = "https://image.winudf.com/v2/image1/Y29tLmluc3RhZ3JhbS5hbmRyb2lkX2ljb25fMTY3NjM0ODUzN18wMzI/icon.webp?w=280&fakeurl=1&type=.webp",
            size = 53887484,
            md5 = "43e780faac8092ec5f294fa3a67bc719"
        )
    )
}

data class ResourceBean(
    val url: String,
    val icon: String,
    val name: String,
    val size: Long,
    val md5: String
)
