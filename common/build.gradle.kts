@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibiliAs.android.library)
    alias(libs.plugins.bilibiliAs.android.library.compose)
    alias(libs.plugins.bilibiliAs.android.hilt)
    alias(libs.plugins.bilibiliAs.android.room)
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

    // sourceSets {
    //     getByName("main") {
    //         proto {
    //             srcDir("src/main/proto")
    //         }
    //     }
    //     getByName("test") {
    //         proto {
    //             srcDir("src/test/proto")
    //         }
    //     }
    //     getByName("androidTest") {
    //         proto {
    //             srcDir("src/androidTest/proto")
    //         }
    //     }
    // }
}

// https://github.com/wilsoncastiblanco/notes-grpc/blob/master/app/build.gradle.kts
// https://stackoverflow.com/questions/75384020/setting-up-protobuf-kotlin-in-android-studio-2023
// protobuf {
//     protoc {
//         artifact = "com.google.protobuf:protoc:3.24.4"
//     }
//     plugins {
//         id("java") {
//             artifact = "io.grpc:protoc-gen-grpc-java:1.59.0"
//         }
//         id("grpc") {
//             artifact = "io.grpc:protoc-gen-grpc-java:1.59.0"
//         }
//         id("grpckt") {
//             artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.0:jdk8@jar"
//         }
//     }
//     generateProtoTasks {
//         all().forEach {
//             it.plugins {
//                 id("java") {
//                     option("lite")
//                 }
//                 id("grpc") {
//                     option("lite")
//                 }
//                 id("grpckt") {
//                     option("lite")
//                 }
//             }
//             it.builtins {
//                 id("kotlin") {
//                     option("lite")
//                 }
//             }
//         }
//     }
// }
dependencies {
    api(libs.androidx.hilt.navigation.compose)

    /**
     * 伸缩布局
     */
    api(libs.flexbox)

    /**
     * 底部对话框库
     * 为AS专门打造适配的对话框库
     */
    api(libs.asBottomDialog)

    // kotlinx
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    /**
     * DanmakuFlameMaster
     * 烈焰弹幕使
     */
    api(libs.dfm)

    // 饺子播放器
    api(libs.jiaozivideoplayer)

    // lottie动画库
    api(libs.lottie)

    api(libs.constraintlayout)

    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.paging.compose)

    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

    api(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(libs.okio)
    implementation(libs.android.startup)
}
