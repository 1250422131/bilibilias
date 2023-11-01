import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.library)
    alias(libs.plugins.bilibili.android.compose)
    alias(libs.plugins.bilibili.android.hilt)
    alias(libs.plugins.bilibili.android.room)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.protobuf)
}

ksp {
    arg("ModuleName", project.name)
}

android {
    namespace = "com.imcys.bilibilias.common"


    buildFeatures {
        dataBinding = true
        compose = true
    }

    sourceSets {
        getByName("main") {
            proto {
                srcDir("src/main/proto")
            }
        }
        getByName("test") {
            proto {
                srcDir("src/test/proto")
            }
        }
        getByName("androidTest") {
            proto {
                srcDir("src/androidTest/proto")
            }
        }
    }
}

// https://github.com/wilsoncastiblanco/notes-grpc/blob/master/app/build.gradle.kts
// https://stackoverflow.com/questions/75384020/setting-up-protobuf-kotlin-in-android-studio-2023
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.4"
    }
    plugins {
        id("java") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.58.0"
        }
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.58.0"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.0:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("java") {
                    option("lite")
                }
                id("grpc") {
                    option("lite")
                }
                id("grpckt") {
                    option("lite")
                }
            }
            it.builtins {
                id("kotlin") {
                    option("lite")
                }
            }
        }
    }
}
dependencies {
    api(libs.androidx.hilt.navigation.compose)

    /**
     * SmoothRefreshLayout支持
     */
    api(libs.srl.core)
    api(libs.srl.ext.material) // md刷新头
    api(libs.srl.ext.classics)

    /**
     * 伸缩布局
     */
    api(libs.flexbox)

    /**
     * rv框架
     * 实现RV的动画效果
     */
    api(libs.brv)

    /**
     * 底部对话框库
     * 为AS专门打造适配的对话框库
     */
    api(libs.asBottomDialog)

    // kotlinx
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.collections.immutable)

    api(libs.kotlinx.datetime)

    /**
     * RxFFmpeg
     * 支持视频合并等操作
     */
    api(libs.rxFFmpeg)

    /**
     * 组件化路由库
     */
    api(libs.kcomponent.rx)
    ksp(libs.kcomponent.compiler)

    // 百度统计
    api(libs.mtj.sdk.circle)

    // 开屏引导
    api(libs.hyy920109.guidePro)

    // 微软分发
    api(libs.appcenter.distribute)
    // 微软统计
    api(libs.appcenter.analytics)
    api(libs.appcenter.crashes)

    /**
     * 文件选择器
     */
    api(libs.androidFilePicker)

    /**
     * xutils
     * 下载文件支持
     */
    api(libs.xutils)

    /**
     * DanmakuFlameMaster
     * 烈焰弹幕使
     */
    api(libs.dfm)

    // 饺子播放器
    api(libs.jiaozivideoplayer)

    // lottie动画库
    api(libs.lottie)

    api(libs.banner)

    api(libs.glide)

    /**
     * 沉浸式布局库
     */
    api(libs.uiTimateBarX)

    /**
     * ktor全局支持
     */
    api(libs.ktor.client.android)
    implementation(libs.ktor.client.okhttp)
    // 日志
    implementation(libs.ktor.client.logging)
    debugImplementation(libs.monitor)
    releaseImplementation(libs.monitor.no.op)

    // json解析支持
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.encoding)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.serialization.kotlinx.protobuf)

    api(libs.constraintlayout)
    // api(libs.androidx.lifecycle.viewmodel.ktx)
    // api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.preference.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime.ktx)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)


    api(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    // implementation(libs.coil)

    implementation(libs.okio)
    api(libs.okhttp)

    implementation(libs.grpc.kotlin.stub)
    implementation(libs.grpc.protobuf)

    implementation(libs.protobuf.kotlin)
    implementation(libs.protobuf.java.util)
}
