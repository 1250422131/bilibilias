import androidx.annotation.VisibleForTesting
import com.imcys.network.fake.FakeAssetManager
import java.io.File
import java.io.InputStream
import java.util.Properties

@VisibleForTesting
internal object JvmUnitTestFakeAssetManager : FakeAssetManager {
    private val config =
        requireNotNull(javaClass.getResource("com/android/tools/test_config.properties")) {
            """
            Missing Android resources properties file.
            Did you forget to enable the feature in the gradle build file?
            android.testOptions.unitTests.isIncludeAndroidResources = true
            """.trimIndent()
        }
    private val properties = Properties().apply { config.openStream().use(::load) }
    private val assets = File(properties["android_merged_assets"].toString())

    override fun open(fileName: String): InputStream = File(assets, fileName).inputStream()
}
