# detekt

## Metrics

* 210 number of properties

* 113 number of functions

* 24 number of classes

* 19 number of packages

* 36 number of kt files

## Complexity Report

* 2,337 lines of code (loc)

* 1,543 source lines of code (sloc)

* 1,074 logical lines of code (lloc)

* 362 comment lines of code (cloc)

* 229 cyclomatic complexity (mcc)

* 94 cognitive complexity

* 87 number of total code smells

* 23% comment source ratio

* 213 mcc per 1,000 lloc

* 81 code smells per 1,000 lloc

## Findings (87)

### complexity, CyclomaticComplexMethod (1)

Prefer splitting up complex methods into smaller, easier to test methods.

[Documentation](https://detekt.dev/docs/rules/complexity#cyclomaticcomplexmethod)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/file/VideoMergerUtils.kt:15:9
```
The function merge appears to be too complex based on Cyclomatic Complexity (complexity: 19). Defined complexity threshold for methods is set to '15'
```
```kotlin
12     private const val timeoutUs = 5000000L
13 
14 
15     fun merge(videoFilePath: String, audioFilePath: String, outputFilePath: String) {
!!         ^ error
16         val videoExtractor = MediaExtractor()
17         val audioExtractor = MediaExtractor()
18 

```

### complexity, LongMethod (1)

One method should have one responsibility. Long methods tend to handle many things at once. Prefer smaller methods to make them easier to understand.

[Documentation](https://detekt.dev/docs/rules/complexity#longmethod)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/file/VideoMergerUtils.kt:15:9
```
The function merge is too long (147). The maximum length is 60.
```
```kotlin
12     private const val timeoutUs = 5000000L
13 
14 
15     fun merge(videoFilePath: String, audioFilePath: String, outputFilePath: String) {
!!         ^ error
16         val videoExtractor = MediaExtractor()
17         val audioExtractor = MediaExtractor()
18 

```

### complexity, LongParameterList (1)

The more parameters a function has the more complex it is. Long parameter lists are often used to control complex algorithms and violate the Single Responsibility Principle. Prefer functions with short parameter lists.

[Documentation](https://detekt.dev/docs/rules/complexity#longparameterlist)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/String.kt:55:32
```
The function toAsDownloadSavePath(context: Context, avid: String, bvid: String, pTitle: String, cid: String, fileType: String, p: String, title: String, type: String) has too many parameters. The current threshold is set to 6.
```
```kotlin
52  * @param type String
53  * @return String
54  */
55 fun String.toAsDownloadSavePath(
!!                                ^ error
56     context: Context,
57     avid: String = "",
58     bvid: String = "",

```

### complexity, TooManyFunctions (4)

Too many functions inside a/an file/class/object/interface always indicate a violation of the single responsibility principle. Maybe the file/class/object/interface wants to manage too many things at once. Extract functionality which clearly belongs together.

[Documentation](https://detekt.dev/docs/rules/complexity#toomanyfunctions)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/AbsActivity.kt:28:12
```
Class 'AbsActivity' with '12' functions detected. Defined threshold inside classes is set to '11'
```
```kotlin
25 import java.util.Locale
26 
27 
28 open class AbsActivity : AppCompatActivity() {
!!            ^ error
29 
30     private val mThemeChangedBroadcast by lazy {
31         ThemeChangedBroadcast()

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/RoutineUtils.kt:1:1
```
File 'E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\utils\RoutineUtils.kt' with '11' functions detected. Defined threshold inside files is set to '11'
```
```kotlin
1 package com.imcys.bilibilias.base.utils
! ^ error
2 
3 import android.content.Context
4 import android.util.Log

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:27:5
```
Object 'Companion' with '13' functions detected. Defined threshold inside objects is set to '11'
```
```kotlin
24 open class HttpUtils {
25 
26 
27     companion object {
!!     ^ error
28 
29         private val okHttpClient = OkHttpClient()
30 

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/view/AsJzvdStd.kt:40:12
```
Class 'AsJzvdStd' with '14' functions detected. Defined threshold inside classes is set to '11'
```
```kotlin
37  * 继承饺子播放器，这里需要定义一些东西
38  * @constructor
39  */
40 open class AsJzvdStd : JzvdStd {
!!            ^ error
41 
42 
43     private lateinit var jzbdStdInfo: JzbdStdInfo

```

### empty-blocks, EmptyFunctionBlock (3)

Empty block of code detected. As they serve no purpose they should be removed.

[Documentation](https://detekt.dev/docs/rules/empty-blocks#emptyfunctionblock)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/AbsActivity.kt:88:30
```
This empty block of code can be removed.
```
```kotlin
85         resources.updateConfiguration(configuration, resources.displayMetrics)
86     }
87 
88     private fun initAsUser() {
!!                              ^ error
89 
90     }
91 

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/OkHttp.kt:20:72
```
This empty block of code can be removed.
```
```kotlin
17         }
18 
19         enqueue(object : Callback {
20             override fun onFailure(call: Call, e: java.io.IOException) {
!!                                                                        ^ error
21             }
22 
23             override fun onResponse(call: Call, response: Response) {

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpCallback.kt:9:56
```
This empty block of code can be removed.
```
```kotlin
6  import java.io.IOException
7  
8  class HttpCallback(val method: (data: String) -> Unit) : Callback {
9      override fun onFailure(call: Call, e: IOException) {
!                                                         ^ error
10 
11     }
12 

```

### exceptions, PrintStackTrace (3)

Do not print a stack trace. These debug statements should be removed or replaced with a logger.

[Documentation](https://detekt.dev/docs/rules/exceptions#printstacktrace)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/file/FileUtils.kt:97:13
```
Do not print a stack trace. These debug statements should be removed or replaced with a logger.
```
```kotlin
94          try {
95              file.createNewFile()
96          } catch (e: IOException) {
97              e.printStackTrace()
!!              ^ error
98          }
99          val fw = FileWriter(file)
100         val bw = BufferedWriter(fw)

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:372:17
```
Do not print a stack trace. These debug statements should be removed or replaced with a logger.
```
```kotlin
369                     result += line
370                 }
371             } catch (e: Exception) {
372                 e.printStackTrace()
!!!                 ^ error
373             } // 使用finally块来关闭输出流、输入流
374             finally {
375                 try {

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:379:21
```
Do not print a stack trace. These debug statements should be removed or replaced with a logger.
```
```kotlin
376                     out?.close()
377                     `in`?.close()
378                 } catch (ex: IOException) {
379                     ex.printStackTrace()
!!!                     ^ error
380                 }
381             }
382             return result

```

### exceptions, TooGenericExceptionCaught (1)

The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.

[Documentation](https://detekt.dev/docs/rules/exceptions#toogenericexceptioncaught)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:371:22
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
368                 while (`in`.readLine().also { line = it } != null) {
369                     result += line
370                 }
371             } catch (e: Exception) {
!!!                      ^ error
372                 e.printStackTrace()
373             } // 使用finally块来关闭输出流、输入流
374             finally {

```

### naming, FunctionParameterNaming (2)

Function parameter names should follow the naming convention set in the projects configuration.

[Documentation](https://detekt.dev/docs/rules/naming#functionparameternaming)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/file/FileUtils.kt:89:34
```
Function parameter names should match the pattern: [a-z][A-Za-z0-9]*
```
```kotlin
86     }
87 
88 
89     fun fileWrite(path: String?, Str: String?) {
!!                                  ^ error
90         val file = File(path.toString())
91         if (!file.exists()) {
92             file.parentFile?.mkdirs()

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:325:54
```
Function parameter names should match the pattern: [a-z][A-Za-z0-9]*
```
```kotlin
322          * @return 所代表远程资源的响应结果
323          * @throws Exception
324          */
325         fun doCardPost(url: String?, param: String?, Cookie: String?): String? {
!!!                                                      ^ error
326             var out: PrintWriter? = null
327             var `in`: BufferedReader? = null
328             var result: String? = ""

```

### naming, InvalidPackageDeclaration (1)

Kotlin source files should be stored in the directory corresponding to its package statement.

[Documentation](https://detekt.dev/docs/rules/naming#invalidpackagedeclaration)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/RoutineUtils.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.imcys.bilibilias.base.utils
! ^ error
2 
3 import android.content.Context
4 import android.util.Log

```

### style, MagicNumber (20)

Report magic numbers. Magic number is a numeric literal that is not defined as a constant and hence it's unclear what the purpose of this number is. It's better to declare such numbers as constants and give them a proper name. By default, -1, 0, 1, and 2 are not considered to be magic numbers.

[Documentation](https://detekt.dev/docs/rules/style#magicnumber)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/Int.kt:10:21
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
7          var mNum = num
8          var count = 0 //计数
9          while (mNum >= 1) {
10             mNum /= 10
!!                     ^ error
11             count++
12         }
13         return count

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/Int.kt:18:24
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
15 
16     val originallyNum: String = this.toString() + ""
17     val result: String
18     return if (this >= 10000) {
!!                        ^ error
19         when (lengthNum(this)) {
20             5 -> {
21                 result = originallyNum[0].toString() + "." + originallyNum[1] + "万"

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/Int.kt:20:13
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
17     val result: String
18     return if (this >= 10000) {
19         when (lengthNum(this)) {
20             5 -> {
!!             ^ error
21                 result = originallyNum[0].toString() + "." + originallyNum[1] + "万"
22                 result
23             }

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/Int.kt:25:13
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
22                 result
23             }
24 
25             6 -> {
!!             ^ error
26                 result = originallyNum.substring(0, 2) + "." + originallyNum[2] + "万"
27                 result
28             }

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/Int.kt:30:13
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
27                 result
28             }
29 
30             7 -> {
!!             ^ error
31 
32                 result = originallyNum.substring(0, 3) + "." + originallyNum[3] + "万"
33                 result

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/Int.kt:32:53
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
29 
30             7 -> {
31 
32                 result = originallyNum.substring(0, 3) + "." + originallyNum[3] + "万"
!!                                                     ^ error
33                 result
34             }
35 

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/Int.kt:32:78
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
29 
30             7 -> {
31 
32                 result = originallyNum.substring(0, 3) + "." + originallyNum[3] + "万"
!!                                                                              ^ error
33                 result
34             }
35 

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/Int.kt:36:13
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
33                 result
34             }
35 
36             8 -> {
!!             ^ error
37                 result = originallyNum.substring(0, 4) + "." + originallyNum[4] + "万"
38                 result
39             }

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/Int.kt:37:53
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
34             }
35 
36             8 -> {
37                 result = originallyNum.substring(0, 4) + "." + originallyNum[4] + "万"
!!                                                     ^ error
38                 result
39             }
40 

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/Int.kt:37:78
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
34             }
35 
36             8 -> {
37                 result = originallyNum.substring(0, 4) + "." + originallyNum[4] + "万"
!!                                                                              ^ error
38                 result
39             }
40 

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/Long.kt:14:28
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
11  */
12 @SuppressLint("SimpleDateFormat")
13 fun Long.timeStampDateStr(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
14     val date = Date(this * 1000)
!!                            ^ error
15     val format = SimpleDateFormat(pattern)
16     return format.format(date)
17 }

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/AsVideoNumUtils.kt:29:13
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
26 
27     fun getQualityName(code: Int): String {
28         return when (code) {
29             30216 -> "64K"
!!             ^ error
30             30232 -> "132K"
31             30250 -> "杜比全景声"
32             30251 -> "Hi-Res无损"

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/AsVideoNumUtils.kt:30:13
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
27     fun getQualityName(code: Int): String {
28         return when (code) {
29             30216 -> "64K"
30             30232 -> "132K"
!!             ^ error
31             30250 -> "杜比全景声"
32             30251 -> "Hi-Res无损"
33             30280 -> "192K"

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/AsVideoNumUtils.kt:31:13
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
28         return when (code) {
29             30216 -> "64K"
30             30232 -> "132K"
31             30250 -> "杜比全景声"
!!             ^ error
32             30251 -> "Hi-Res无损"
33             30280 -> "192K"
34             else -> {

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/AsVideoNumUtils.kt:32:13
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
29             30216 -> "64K"
30             30232 -> "132K"
31             30250 -> "杜比全景声"
32             30251 -> "Hi-Res无损"
!!             ^ error
33             30280 -> "192K"
34             else -> {
35                 "192K"

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/AsVideoNumUtils.kt:33:13
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
30             30232 -> "132K"
31             30250 -> "杜比全景声"
32             30251 -> "Hi-Res无损"
33             30280 -> "192K"
!!             ^ error
34             else -> {
35                 "192K"
36             }

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:349:36
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
346                 // 发送POST请求必须设置如下两行
347                 conn.doOutput = true
348                 conn.doInput = true
349                 conn.readTimeout = 5000
!!!                                    ^ error
350                 conn.connectTimeout = 5000
351                 val sessionId = ""
352                 val cookieVal = ""

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:350:39
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
347                 conn.doOutput = true
348                 conn.doInput = true
349                 conn.readTimeout = 5000
350                 conn.connectTimeout = 5000
!!!                                       ^ error
351                 val sessionId = ""
352                 val cookieVal = ""
353                 val key: String? = null

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/view/AsJzvdStd.kt:93:38
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
90 
91         changeUiToComplete()
92         cancelDismissControlViewTimer()
93         bottomProgressBar.progress = 100
!!                                      ^ error
94         asJzvdstdPosterFL.isVisible = true
95 
96         //通知播放完成

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/view/AsJzvdStd.kt:150:71
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
147                     val photo = File(photoDir, fileName)
148                     try {
149                         val fos = FileOutputStream(photo)
150                         resource.compress(Bitmap.CompressFormat.JPEG, 100, fos)
!!!                                                                       ^ error
151                         fos.flush()
152                         fos.close()
153                     } catch (e: FileNotFoundException) {

```

### style, MaxLineLength (5)

Line detected, which is longer than the defined maximum line length in the code style.

[Documentation](https://detekt.dev/docs/rules/style#maxlinelength)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/RecyclerViewUtils.kt:9:1
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
6  
7      fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
8          if (recyclerView == null) return false
9          return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
!  ^ error
10     }
11 }

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/file/VideoMergerUtils.kt:183:1
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
180             }
181 
182 // 判断是否完成合并
183             if ((videoBufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0 && (audioBufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
!!! ^ error
184                 break
185             }
186 

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:341:1
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
338                 conn.setRequestProperty("referer", " https://www.bilibili.com/")
339                 conn.setRequestProperty(
340                     "User-Agent",
341                     "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
!!! ^ error
342                 )
343                 conn.setRequestProperty("cookie", Cookie)
344                 conn.setRequestProperty("charset", "utf-8")

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:389:1
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
386             headers["user-agent"] = if (url.contains("misakamoe.com")) {
387                 misakaMoeUa + " BILIBILIAS/${BiliBiliAsApi.version}"
388             } else {
389                 "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54"
!!! ^ error
390             }
391         }
392 

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/KtHttpUtils.kt:164:1
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
161         headers["user-agent"] = if (url in "misakamoe") {
162             SystemUtil.getUserAgent() + " BILIBILIAS/${BiliBiliAsApi.version}"
163         } else {
164             "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54"
!!! ^ error
165         }
166     }
167 }

```

### style, NewLineAtEndOfFile (27)

Checks whether files end with a line separator.

[Documentation](https://detekt.dev/docs/rules/style#newlineatendoffile)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/AbsActivity.kt:199:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\AbsActivity.kt is not ending with a new line.
```
```kotlin
196     }
197 
198 
199 }
!!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/BaseFragment.kt:6:37
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\BaseFragment.kt is not ending with a new line.
```
```kotlin
3  import androidx.fragment.app.Fragment
4  
5  
6  open class BaseFragment : Fragment()
!                                      ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/api/BiliBiliAsApi.kt:16:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\api\BiliBiliAsApi.kt is not ending with a new line.
```
```kotlin
13     const val appVersionDataPath = serviceTestApi + "versions/$version"
14 
15 
16 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/app/BaseApplication.kt:96:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\app\BaseApplication.kt is not ending with a new line.
```
```kotlin
93  
94  
95      }
96  }
!!   ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/arouter/ARouterAddress.kt:24:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\arouter\ARouterAddress.kt is not ending with a new line.
```
```kotlin
21 
22     const val LogExportActivity = "tool_log_export/LiveStreamActivity"
23 
24 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/exception/BaseCoroutineExceptionHandler.kt:15:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\exception\BaseCoroutineExceptionHandler.kt is not ending with a new line.
```
```kotlin
12     }
13 
14 
15 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/extend/OkHttp.kt:30:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\extend\OkHttp.kt is not ending with a new line.
```
```kotlin
27 
28         })
29     }
30 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/model/common/IPostBody.kt:3:20
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\model\common\IPostBody.kt is not ending with a new line.
```
```kotlin
1 package com.imcys.bilibilias.common.base.model.common
2 
3 interface IPostBody
!                    ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/model/user/AsUser.kt:12:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\model\user\AsUser.kt is not ending with a new line.
```
```kotlin
9      var asCookie: String = ""
10 
11 
12 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/model/user/AsUserLoginModel.kt:18:17
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\model\user\AsUserLoginModel.kt is not ending with a new line.
```
```kotlin
15     val code: Int, // 0
16     @SerializedName("msg")
17     val msg: String, // 登录成功
18 ) : Serializable
!!                 ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/model/user/ResponseResult.kt:17:17
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\model\user\ResponseResult.kt is not ending with a new line.
```
```kotlin
14     open val code: Int, // 0
15     @SerializedName("msg")
16     open val msg: String, // 登录成功
17 ) : Serializable
!!                 ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/model/user/UserBiliBiliCookieModel.kt:28:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\model\user\UserBiliBiliCookieModel.kt is not ending with a new line.
```
```kotlin
25         @SerializedName("type")
26         val type: Int, // 1
27     ) : Serializable, IPostBody
28 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/AsVideoNumUtils.kt:42:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\utils\AsVideoNumUtils.kt is not ending with a new line.
```
```kotlin
39     }
40 
41 
42 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/RecyclerViewUtils.kt:11:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\utils\RecyclerViewUtils.kt is not ending with a new line.
```
```kotlin
8          if (recyclerView == null) return false
9          return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
10     }
11 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/RoutineUtils.kt:30:64
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\utils\RoutineUtils.kt is not ending with a new line.
```
```kotlin
27 fun asLoW(context: Context, content: String) = Log.w(context::class.java.simpleName, content)
28 
29 fun asToast(context: Context, content: String) =
30     Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
!!                                                                ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/file/FileUtils.kt:117:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\utils\file\FileUtils.kt is not ending with a new line.
```
```kotlin
114     }
115 
116 
117 }
!!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/file/VideoMergerUtils.kt:190:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\utils\file\VideoMergerUtils.kt is not ending with a new line.
```
```kotlin
187 
188         }
189     }
190 }
!!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpCallback.kt:18:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\utils\http\HttpCallback.kt is not ending with a new line.
```
```kotlin
15             method(response.body?.string() ?: "")
16         }
17     }
18 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:396:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\utils\http\HttpUtils.kt is not ending with a new line.
```
```kotlin
393     }
394 
395 
396 }
!!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/view/AsJzvdStd.kt:269:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\view\AsJzvdStd.kt is not ending with a new line.
```
```kotlin
266     }
267 
268 
269 }
!!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/view/AsTopNavigationLayout.kt:37:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\base\view\AsTopNavigationLayout.kt is not ending with a new line.
```
```kotlin
34 
35     }
36 
37 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/broadcast/ThemeChangedBroadcast.kt:22:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\broadcast\ThemeChangedBroadcast.kt is not ending with a new line.
```
```kotlin
19     override fun onReceive(context: Context, intent: Intent) {
20         (context as AbsActivity).updateTheme()
21     }
22 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/data/AppDatabase.kt:56:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\data\AppDatabase.kt is not ending with a new line.
```
```kotlin
53 
54     }
55 
56 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/data/dao/RoamDao.kt:41:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\data\dao\RoamDao.kt is not ending with a new line.
```
```kotlin
38     @Query("SELECT * from as_roam_data WHERE name = :roamName LIMIT 1")
39     suspend fun isNameExist(roamName: String): RoamInfo?
40 
41 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/data/repository/DownloadFinishTaskRepository.kt:26:2
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\data\repository\DownloadFinishTaskRepository.kt is not ending with a new line.
```
```kotlin
23         dao.delete(todo)
24     }
25 
26 }
!!  ^ error

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/data/repository/RoamRepository.kt:5:47
```
The file E:\Github-learning\bilibilias\common\src\main\java\com\imcys\bilibilias\common\data\repository\RoamRepository.kt is not ending with a new line.
```
```kotlin
2 
3 import com.imcys.bilibilias.common.data.dao.RoamDao
4 
5 class RoamRepository(private val dao: RoamDao)
!                                               ^ error

```

* E:/Github-learning/bilibilias/common/src/test/java/com/imcys/bilibilias/common/ExampleUnitTest.kt:16:2
```
The file E:\Github-learning\bilibilias\common\src\test\java\com\imcys\bilibilias\common\ExampleUnitTest.kt is not ending with a new line.
```
```kotlin
13     fun addition_isCorrect() {
14         assertEquals(4, 2 + 2)
15     }
16 }
!!  ^ error

```

### style, ReturnCount (1)

Restrict the number of return statements in methods.

[Documentation](https://detekt.dev/docs/rules/style#returncount)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/AsVideoNumUtils.kt:6:9
```
Function getBvid has 3 return statements which exceeds the limit of 2.
```
```kotlin
3  
4  object AsVideoNumUtils {
5  
6      fun getBvid(string: String): String {
!          ^ error
7  
8          //bv过滤
9          var epRegex =

```

### style, SerialVersionUIDInSerializableClass (6)

A class which implements the Serializable interface does not define a correct serialVersionUID field. The serialVersionUID field should be a private constant long value inside a companion object.

[Documentation](https://detekt.dev/docs/rules/style#serialversionuidinserializableclass)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/arouter/ARouterAddress.kt:6:8
```
The object ARouterAddress implements the `Serializable` interface and should thus define a `serialVersionUID`.
```
```kotlin
3  import java.io.Serializable
4  
5  
6  object ARouterAddress : Serializable {
!         ^ error
7  
8      const val AppHomeActivity = "app/HomeActivity"
9  

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/model/user/AsUserLoginModel.kt:13:12
```
The class AsUserLoginModel implements the `Serializable` interface and should thus define a `serialVersionUID`.
```
```kotlin
10  * @constructor
11  */
12 
13 data class AsUserLoginModel(
!!            ^ error
14     @SerializedName("code")
15     val code: Int, // 0
16     @SerializedName("msg")

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/model/user/BiLiCookieResponseModel.kt:6:12
```
The class BiLiCookieResponseModel implements the `Serializable` interface and should thus define a `serialVersionUID`.
```
```kotlin
3  import com.google.gson.annotations.SerializedName
4  import java.io.Serializable
5  
6  data class BiLiCookieResponseModel(
!             ^ error
7      @SerializedName("code")
8      val code: Int, // 0
9      @SerializedName("msg")

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/model/user/ResponseResult.kt:12:12
```
The class ResponseResult implements the `Serializable` interface and should thus define a `serialVersionUID`.
```
```kotlin
9   * @property msg String
10  * @constructor
11  */
12 open class ResponseResult(
!!            ^ error
13     @SerializedName("code")
14     open val code: Int, // 0
15     @SerializedName("msg")

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/model/user/UserBiliBiliCookieModel.kt:8:12
```
The class UserBiliBiliCookieModel implements the `Serializable` interface and should thus define a `serialVersionUID`.
```
```kotlin
5  import java.io.Serializable
6  
7  
8  data class UserBiliBiliCookieModel(
!             ^ error
9      @SerializedName("code")
10     val code: Int, // 0
11     @SerializedName("data")

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/model/user/UserBiliBiliCookieModel.kt:16:16
```
The class Data implements the `Serializable` interface and should thus define a `serialVersionUID`.
```
```kotlin
13     @SerializedName("msg")
14     val msg: String, // 获取成功啦
15 ) : Serializable {
16     data class Data(
!!                ^ error
17         @SerializedName("cookie")
18         val cookie: String, // adda
19         @SerializedName("face")

```

### style, UnusedPrivateMember (1)

Private function is unused and should be removed.

[Documentation](https://detekt.dev/docs/rules/style#unusedprivatemember)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/AbsActivity.kt:88:17
```
Private function `initAsUser` is unused.
```
```kotlin
85         resources.updateConfiguration(configuration, resources.displayMetrics)
86     }
87 
88     private fun initAsUser() {
!!                 ^ error
89 
90     }
91 

```

### style, UnusedPrivateProperty (5)

Property is unused and should be removed.

[Documentation](https://detekt.dev/docs/rules/style#unusedprivateproperty)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:351:21
```
Private property `sessionId` is unused.
```
```kotlin
348                 conn.doInput = true
349                 conn.readTimeout = 5000
350                 conn.connectTimeout = 5000
351                 val sessionId = ""
!!!                     ^ error
352                 val cookieVal = ""
353                 val key: String? = null
354                 if (param != null && param.trim { it <= ' ' } != "") {

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:352:21
```
Private property `cookieVal` is unused.
```
```kotlin
349                 conn.readTimeout = 5000
350                 conn.connectTimeout = 5000
351                 val sessionId = ""
352                 val cookieVal = ""
!!!                     ^ error
353                 val key: String? = null
354                 if (param != null && param.trim { it <= ' ' } != "") {
355                     // 获取URLConnection对象对应的输出流

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/view/AsJzvdStd.kt:236:13
```
Private property `imageWidth` is unused.
```
```kotlin
233 
234     private fun isHorizontalAsVideo(): Boolean {
235         val picImage = findViewById<ImageView>(R.id.poster)
236         val imageWidth = picImage.width
!!!             ^ error
237         val imageHeight = picImage.height
238 
239         return width > height

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/view/AsJzvdStd.kt:237:13
```
Private property `imageHeight` is unused.
```
```kotlin
234     private fun isHorizontalAsVideo(): Boolean {
235         val picImage = findViewById<ImageView>(R.id.poster)
236         val imageWidth = picImage.width
237         val imageHeight = picImage.height
!!!             ^ error
238 
239         return width > height
240 

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/data/repository/RoamRepository.kt:5:34
```
Private property `dao` is unused.
```
```kotlin
2 
3 import com.imcys.bilibilias.common.data.dao.RoamDao
4 
5 class RoamRepository(private val dao: RoamDao)
!                                  ^ error

```

### style, UtilityClassWithPublicConstructor (1)

The class declaration is unnecessary because it only contains utility functions. An object declaration should be used instead.

[Documentation](https://detekt.dev/docs/rules/style#utilityclasswithpublicconstructor)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:17:1
```
The class HttpUtils only contains utility functions. Consider defining it as an object.
```
```kotlin
14 import java.net.URL
15 
16 
17 /**
!! ^ error
18  * @author imcys
19  *
20  * 此类为okhttp3的封装类

```

### style, WildcardImport (4)

Wildcard imports should be replaced with imports using fully qualified class names. Wildcard imports can lead to naming conflicts. A library update can introduce naming clashes with your classes which results in compilation errors.

[Documentation](https://detekt.dev/docs/rules/style#wildcardimport)

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:8:1
```
kotlinx.coroutines.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
5  import com.imcys.bilibilias.common.base.app.BaseApplication
6  import com.imcys.bilibilias.common.base.extend.awaitResponse
7  import com.imcys.bilibilias.common.base.utils.file.SystemUtil
8  import kotlinx.coroutines.*
!  ^ error
9  import okhttp3.*
10 import okhttp3.MediaType.Companion.toMediaType
11 import okhttp3.RequestBody.Companion.toRequestBody

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:9:1
```
okhttp3.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
6  import com.imcys.bilibilias.common.base.extend.awaitResponse
7  import com.imcys.bilibilias.common.base.utils.file.SystemUtil
8  import kotlinx.coroutines.*
9  import okhttp3.*
!  ^ error
10 import okhttp3.MediaType.Companion.toMediaType
11 import okhttp3.RequestBody.Companion.toRequestBody
12 import java.io.*

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/utils/http/HttpUtils.kt:12:1
```
java.io.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
9  import okhttp3.*
10 import okhttp3.MediaType.Companion.toMediaType
11 import okhttp3.RequestBody.Companion.toRequestBody
12 import java.io.*
!! ^ error
13 import java.net.HttpURLConnection
14 import java.net.URL
15 

```

* E:/Github-learning/bilibilias/common/src/main/java/com/imcys/bilibilias/common/base/view/AsJzvdStd.kt:12:1
```
android.widget.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
9  import android.util.AttributeSet
10 import android.view.View
11 import android.view.ViewGroup
12 import android.widget.*
!! ^ error
13 import androidx.core.view.isVisible
14 import cn.jzvd.JZUtils
15 import cn.jzvd.JzvdStd

```

generated with [detekt version 1.23.1](https://detekt.dev/) on 2023-08-26 13:38:14 UTC
