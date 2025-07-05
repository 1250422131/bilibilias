plugins {
    alias(libs.plugins.bilibilias.android.library)
    alias(libs.plugins.bilibilias.android.koin)

}
android {
    namespace = "com.imcys.bilibilias.data"
}
dependencies {
    implementation(project(":core:common"))
    api(project(":core:database"))
    api(project(":core:datastore"))
    api(project(":core:network"))

}