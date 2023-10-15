import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
    alias(libs.plugins.protobuf)
}

apply {
    from("/../config.gradle")
}

ksp {
    arg("ModuleName", project.name)
}

android {
    namespace = "com.imcys.bilibilias.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    buildFeatures {
        dataBinding = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/ASL2.0"
            excludes += "META-INF/*.kotlin_module"
            excludes += "META-INF/versions/9/previous-compilation-data.bin"
        }
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
    // main.java.srcDirs += "src/main/kotlin/"
    // main.java.srcDirs += "build/generated/source/protos/main/java"
    // test.java.srcDirs += "src/test/kotlin/"
    // test.java.srcDirs += "build/generated/source/protos/main/java"
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

kotlin {
    jvmToolchain(17)
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
    // hilt库，实现控制反转
    api(libs.hilt.android)
    ksp(libs.hilt.compiler)
    api(libs.androidx.hilt.navigation.compose)

    /**
     * SmoothRefreshLayout支持
     */
    api(libs.srl.core)
    api(libs.srl.ext.material) // md刷新头
    api(libs.srl.ext.classics)

    /**
     * MMKV 储存框架
     */
    api(libs.mmkv)
    api(libs.fastkv)

    /**
     * 伸缩布局
     */
    api(libs.flexbox)

    /**
     * jxl库
     * 直接生成excel文件时采用
     */
    api(libs.jxl)

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
    api(libs.kotlinx.serialization.json)
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
     * room
     * 本地化数据库
     */
    api(libs.room.runtime)
    api(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

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
    api(libs.gson)

    api(libs.constraintlayout)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.preference.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)

    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.material3)
    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime.ktx)
    debugImplementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)

    api(libs.timber)

    api(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.coil)

    implementation(libs.okio)
    api(libs.okhttp)

    implementation(libs.grpc.kotlin.stub)
    implementation(libs.grpc.protobuf)

    implementation(libs.protobuf.kotlin)
    implementation(libs.protobuf.java.util)
}
