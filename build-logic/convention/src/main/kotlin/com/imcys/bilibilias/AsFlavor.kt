package com.imcys.bilibilias

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

@Suppress("EnumEntryName")
enum class FlavorDimension {
    contentType
}

// The content for the app can either come from local static data which is useful for demo
// purposes, or from a production backend server which supplies up-to-date, real content.
// These two product flavors reflect this behaviour.
@Suppress("EnumEntryName")
enum class AsFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    demo(FlavorDimension.contentType, applicationIdSuffix = ".demo"),
    prod(FlavorDimension.contentType),
}

fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: AsFlavor) -> Unit = {},
) {
    commonExtension.apply {
        FlavorDimension.values().forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name
        }

        productFlavors {
            AsFlavor.values().forEach { asFlavor ->
                register(asFlavor.name) {
                    dimension = asFlavor.dimension.name
                    flavorConfigurationBlock(this, asFlavor)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (asFlavor.applicationIdSuffix != null) {
                            applicationIdSuffix = asFlavor.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}
