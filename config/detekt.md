# detekt

## Metrics

* 23 number of properties

* 21 number of functions

* 0 number of classes

* 1 number of packages

* 1 number of kt files

## Complexity Report

* 418 lines of code (loc)

* 199 source lines of code (sloc)

* 171 logical lines of code (lloc)

* 203 comment lines of code (cloc)

* 22 cyclomatic complexity (mcc)

* 1 cognitive complexity

* 109 number of total code smells

* 102% comment source ratio

* 128 mcc per 1,000 lloc

* 637 code smells per 1,000 lloc

## Findings (109)

### HbmartinRuleSet, AvoidVarsExceptWithDelegate (21)

Variables shouldn't be used except with delegates

[Documentation](https://detekt.dev/docs/rules/hbmartinruleset#avoidvarsexceptwithdelegate)

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:23:9
```
Property transformAudioCmd is a `var`iable, please make it a val.
```
```kotlin
20      * @return 转码后的文件
21      */
22     fun transformAudio(srcFile: String?, targetFile: String?): Array<String?> {
23         var transformAudioCmd = "-i %s %s"
!!         ^ error
24         transformAudioCmd = String.format(transformAudioCmd, srcFile, targetFile)
25         return transformAudioCmd.split(" ") //以空格分割为字符串数组
26             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:46:9
```
Property cutAudioCmd is a `var`iable, please make it a val.
```
```kotlin
43         duration: Int,
44         targetFile: String,
45     ): Array<String> {
46         var cutAudioCmd = "-i %s -ss %d -t %d %s"
!!         ^ error
47         cutAudioCmd = String.format(cutAudioCmd, srcFile, startTime, duration, targetFile)
48         return cutAudioCmd.split(" ") //以空格分割为字符串数组
49             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:61:9
```
Property concatAudioCmd is a `var`iable, please make it a val.
```
```kotlin
58      * @return 合并后的文件
59      */
60     fun concatAudio(srcFile: String?, appendFile: String?, targetFile: String?): Array<String?> {
61         var concatAudioCmd = "-i concat:%s|%s -acodec copy %s"
!!         ^ error
62         concatAudioCmd = String.format(concatAudioCmd, srcFile, appendFile, targetFile)
63         return concatAudioCmd.split(" ") //以空格分割为字符串数组
64             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:76:9
```
Property mixAudioCmd is a `var`iable, please make it a val.
```
```kotlin
73      * @return 混合后的文件
74      */
75     fun mixAudio(srcFile: String?, mixFile: String?, targetFile: String?): Array<String?> {
76         var mixAudioCmd = "-i %s -i %s -filter_complex amix=inputs=2:duration=first -strict -2 %s"
!!         ^ error
77         mixAudioCmd = String.format(mixAudioCmd, srcFile, mixFile, targetFile)
78         return mixAudioCmd.split(" ") //以空格分割为字符串数组
79             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:103:9
```
Property mixAudioCmd is a `var`iable, please make it a val.
```
```kotlin
100     ): Array<String?> {
101 
102         //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
103         var mixAudioCmd = "ffmpeg -i %s -i %s -t %d %s"
!!!         ^ error
104         mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, duration, muxFile)
105         return mixAudioCmd.split(" ") //以空格分割为字符串数组
106             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:120:9
```
Property mixAudioCmd is a `var`iable, please make it a val.
```
```kotlin
117         //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
118         // "-i %s -i %s -c:v copy -c:a aac -strict experimental %s"
119 
120         var mixAudioCmd = "ffmpeg -i %s -i %s -c copy %s"
!!!         ^ error
121         mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, muxFile)
122         return mixAudioCmd.split(" ") //以空格分割为字符串数组
123             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:135:9
```
Property mixAudioCmd is a `var`iable, please make it a val.
```
```kotlin
132      */
133     fun extractAudio(srcFile: String?, targetFile: String?): Array<String?> {
134         //-vn:video not
135         var mixAudioCmd = "-i %s -acodec copy -vn %s"
!!!         ^ error
136         mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile)
137         return mixAudioCmd.split(" ") //以空格分割为字符串数组
138             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:150:9
```
Property mixAudioCmd is a `var`iable, please make it a val.
```
```kotlin
147      */
148     fun extractVideo(srcFile: String?, targetFile: String?): Array<String?> {
149         //-an audio not
150         var mixAudioCmd = "-i %s -vcodec copy -an %s"
!!!         ^ error
151         mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile)
152         return mixAudioCmd.split(" ") //以空格分割为字符串数组
153             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:167:9
```
Property transformVideoCmd is a `var`iable, please make it a val.
```
```kotlin
164     fun transformVideo(srcFile: String?, targetFile: String?): Array<String?> {
165         //指定目标视频的帧率、码率、分辨率
166 //        String transformVideoCmd = "-i %s -r 25 -b 200 -s 1080x720 %s";
167         var transformVideoCmd = "-i %s -vcodec copy -acodec copy %s"
!!!         ^ error
168         transformVideoCmd = String.format(transformVideoCmd, srcFile, targetFile)
169         return transformVideoCmd.split(" ") //以空格分割为字符串数组
170             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:189:9
```
Property cutVideoCmd is a `var`iable, please make it a val.
```
```kotlin
186         endTime: String?,
187         targetFile: String?,
188     ): Array<String?> {
189         var cutVideoCmd =
!!!         ^ error
190             "-ss %s -t %s -i %s -c:v libx264 -c:a aac -strict experimental -b:a 98k %s"
191         cutVideoCmd = String.format(cutVideoCmd, startTime, endTime, srcFile, targetFile)
192         return cutVideoCmd.split(" ") //以空格分割为字符串数组

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:205:9
```
Property screenShotCmd is a `var`iable, please make it a val.
```
```kotlin
202      * @return 需要执行命令行
203      */
204     fun screenShot(srcFile: String?, size: String?, targetFile: String?): Array<String?> {
205         var screenShotCmd = "-i %s -f image2 -t 0.001 -s %s %s"
!!!         ^ error
206         screenShotCmd = String.format(screenShotCmd, srcFile, size, targetFile)
207         return screenShotCmd.split(" ") //以空格分割为字符串数组
208             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:220:9
```
Property waterMarkCmd is a `var`iable, please make it a val.
```
```kotlin
217      * @return 需要执行命令行
218      */
219     fun addWaterMark(srcFile: String?, waterMark: String?, targetFile: String?): Array<String?> {
220         var waterMarkCmd = "-i %s -i %s -filter_complex overlay=0:0 %s"
!!!         ^ error
221         waterMarkCmd = String.format(waterMarkCmd, srcFile, waterMark, targetFile)
222         return waterMarkCmd.split(" ") //以空格分割为字符串数组
223             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:243:9
```
Property screenShotCmd is a `var`iable, please make it a val.
```
```kotlin
240         targetFile: String?,
241     ): Array<String?> {
242         //String screenShotCmd = "-i %s -vframes %d -f gif %s";
243         var screenShotCmd = "-i %s -ss %d -t %d -s 320x240 -f gif %s"
!!!         ^ error
244         screenShotCmd = String.format(screenShotCmd, srcFile, startTime, duration, targetFile)
245         return screenShotCmd.split(" ") //以空格分割为字符串数组
246             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:261:9
```
Property screenRecordCmd is a `var`iable, please make it a val.
```
```kotlin
258     fun screenRecord(size: String?, recordTime: Int, targetFile: String?): Array<String?> {
259         //-vd x11:0,0 指录制所使用的偏移为 x=0 和 y=0
260         //String screenRecordCmd = "-vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s %s";
261         var screenRecordCmd = "-vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s -t %d %s"
!!!         ^ error
262         screenRecordCmd = String.format(screenRecordCmd, size, recordTime, targetFile)
263         Log.i("VideoHandleActivity", "screenRecordCmd=$screenRecordCmd")
264         return screenRecordCmd.split(" ") //以空格分割为字符串数组

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:278:9
```
Property combineVideo is a `var`iable, please make it a val.
```
```kotlin
275     @SuppressLint("DefaultLocale")
276     fun pictureToVideo(srcFile: String?, targetFile: String?): Array<String?> {
277         //-f image2：代表使用image2格式，需要放在输入文件前面
278         var combineVideo = "-f image2 -r 1 -i %simg#d.jpg -vcodec mpeg4 %s"
!!!         ^ error
279         combineVideo = String.format(combineVideo, srcFile, targetFile)
280         combineVideo = combineVideo.replace("#", "%")
281         Log.i("FFmpegUtil", "combineVideo=$combineVideo")

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:302:9
```
Property combineVideo is a `var`iable, please make it a val.
```
```kotlin
299         sampleRate: Int,
300         channel: Int,
301     ): Array<String?> {
302         var combineVideo = "-f s16le -ar %d -ac %d -i %s %s"
!!!         ^ error
303         combineVideo = String.format(combineVideo, sampleRate, channel, srcFile, targetFile)
304         return combineVideo.split(" ").toTypedArray()
305     }

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:324:9
```
Property multiVideo is a `var`iable, please make it a val.
```
```kotlin
321     ): Array<String?> {
322 //        String multiVideo = "-i %s -i %s -i %s -i %s -filter_complex " +
323 //                "\"[0:v]pad=iw*2:ih*2[a];[a][1:v]overlay=w[b];[b][2:v]overlay=0:h[c];[c][3:v]overlay=w:h\" %s";
324         var multiVideo = "-i %s -i %s -filter_complex hstack %s" //hstack:水平拼接，默认
!!!         ^ error
325         if (videoLayout == LAYOUT_VERTICAL) { //vstack:垂直拼接
326             multiVideo = multiVideo.replace("hstack", "vstack")
327         }

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:342:9
```
Property reverseVideo is a `var`iable, please make it a val.
```
```kotlin
339     fun reverseVideo(inputFile: String?, targetFile: String?): Array<String?> {
340         //FIXME 音频也反序
341 //        String reverseVideo = "-i %s -filter_complex [0:v]reverse[v];[0:a]areverse[a] -map [v] -map [a] %s";
342         var reverseVideo = "-i %s -filter_complex [0:v]reverse[v] -map [v] %s" //单纯视频反序
!!!         ^ error
343         reverseVideo = String.format(reverseVideo, inputFile, targetFile)
344         return reverseVideo.split(" ").toTypedArray()
345     }

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:355:9
```
Property reverseVideo is a `var`iable, please make it a val.
```
```kotlin
352      * @return 视频降噪的命令行
353      */
354     fun denoiseVideo(inputFile: String?, targetFile: String?): Array<String?> {
355         var reverseVideo = "-i %s -nr 500 %s"
!!!         ^ error
356         reverseVideo = String.format(reverseVideo, inputFile, targetFile)
357         return reverseVideo.split(" ").toTypedArray()
358     }

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:380:9
```
Property toImage is a `var`iable, please make it a val.
```
```kotlin
377         //-ss：开始时间，单位为秒
378         //-t：持续时间，单位为秒
379         //-r：帧率，每秒抽多少帧
380         var toImage = "-i %s -ss %s -t %s -r %s %s"
!!!         ^ error
381         toImage = java.lang.String.format(
382             Locale.CHINESE,
383             toImage,

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:412:9
```
Property reverseVideo is a `var`iable, please make it a val.
```
```kotlin
409         y: Int,
410         targetFile: String?,
411     ): Array<String?> {
412         var reverseVideo = "-i %s -i %s -filter_complex overlay=%d:%d %s"
!!!         ^ error
413         reverseVideo = String.format(reverseVideo, inputFile1, inputFile2, x, y, targetFile)
414         return reverseVideo.split(" ").toTypedArray()
415     }

```

### complexity, TooManyFunctions (1)

Too many functions inside a/an file/class/object/interface always indicate a violation of the single responsibility principle. Maybe the file/class/object/interface wants to manage too many things at once. Extract functionality which clearly belongs together.

[Documentation](https://detekt.dev/docs/rules/complexity#toomanyfunctions)

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:7:8
```
Object 'FFmpegUtil' with '21' functions detected. Defined threshold inside objects is set to '11'
```
```kotlin
4  import android.util.Log
5  import java.util.*
6  
7  object FFmpegUtil {
!         ^ error
8  
9      //水平拼接
10     const val LAYOUT_HORIZONTAL = 1

```

### formatting, CommentSpacing (35)

Checks if comments have the right spacing

[Documentation](https://detekt.dev/docs/rules/formatting#commentspacing)

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:9:5
```
Missing space after //
```
```kotlin
6  
7  object FFmpegUtil {
8  
9      //水平拼接
!      ^ error
10     const val LAYOUT_HORIZONTAL = 1
11 
12     //垂直拼接

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:12:5
```
Missing space after //
```
```kotlin
9      //水平拼接
10     const val LAYOUT_HORIZONTAL = 1
11 
12     //垂直拼接
!!     ^ error
13     const val LAYOUT_VERTICAL = 2
14 
15     /**

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:25:45
```
Missing space after //
```
```kotlin
22     fun transformAudio(srcFile: String?, targetFile: String?): Array<String?> {
23         var transformAudioCmd = "-i %s %s"
24         transformAudioCmd = String.format(transformAudioCmd, srcFile, targetFile)
25         return transformAudioCmd.split(" ") //以空格分割为字符串数组
!!                                             ^ error
26             .toTypedArray()
27     }
28 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:48:39
```
Missing space after //
```
```kotlin
45     ): Array<String> {
46         var cutAudioCmd = "-i %s -ss %d -t %d %s"
47         cutAudioCmd = String.format(cutAudioCmd, srcFile, startTime, duration, targetFile)
48         return cutAudioCmd.split(" ") //以空格分割为字符串数组
!!                                       ^ error
49             .toTypedArray()
50     }
51 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:63:42
```
Missing space after //
```
```kotlin
60     fun concatAudio(srcFile: String?, appendFile: String?, targetFile: String?): Array<String?> {
61         var concatAudioCmd = "-i concat:%s|%s -acodec copy %s"
62         concatAudioCmd = String.format(concatAudioCmd, srcFile, appendFile, targetFile)
63         return concatAudioCmd.split(" ") //以空格分割为字符串数组
!!                                          ^ error
64             .toTypedArray()
65     }
66 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:78:39
```
Missing space after //
```
```kotlin
75     fun mixAudio(srcFile: String?, mixFile: String?, targetFile: String?): Array<String?> {
76         var mixAudioCmd = "-i %s -i %s -filter_complex amix=inputs=2:duration=first -strict -2 %s"
77         mixAudioCmd = String.format(mixAudioCmd, srcFile, mixFile, targetFile)
78         return mixAudioCmd.split(" ") //以空格分割为字符串数组
!!                                       ^ error
79             .toTypedArray()
80     }
81     //混音公式：value = sample1 + sample2 - (sample1 * sample2 / (pow(2, 16-1) - 1))

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:81:5
```
Missing space after //
```
```kotlin
78         return mixAudioCmd.split(" ") //以空格分割为字符串数组
79             .toTypedArray()
80     }
81     //混音公式：value = sample1 + sample2 - (sample1 * sample2 / (pow(2, 16-1) - 1))
!!     ^ error
82 
83 
84     //混音公式：value = sample1 + sample2 - (sample1 * sample2 / (pow(2, 16-1) - 1))

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:84:5
```
Missing space after //
```
```kotlin
81     //混音公式：value = sample1 + sample2 - (sample1 * sample2 / (pow(2, 16-1) - 1))
82 
83 
84     //混音公式：value = sample1 + sample2 - (sample1 * sample2 / (pow(2, 16-1) - 1))
!!     ^ error
85     /**
86      * 使用ffmpeg命令行进行音视频合成
87      *

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:102:9
```
Missing space after //
```
```kotlin
99          muxFile: String?,
100     ): Array<String?> {
101 
102         //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
!!!         ^ error
103         var mixAudioCmd = "ffmpeg -i %s -i %s -t %d %s"
104         mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, duration, muxFile)
105         return mixAudioCmd.split(" ") //以空格分割为字符串数组

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:105:39
```
Missing space after //
```
```kotlin
102         //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
103         var mixAudioCmd = "ffmpeg -i %s -i %s -t %d %s"
104         mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, duration, muxFile)
105         return mixAudioCmd.split(" ") //以空格分割为字符串数组
!!!                                       ^ error
106             .toTypedArray()
107     }
108 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:117:9
```
Missing space after //
```
```kotlin
114         muxFile: String?,
115     ): Array<String?> {
116 
117         //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
!!!         ^ error
118         // "-i %s -i %s -c:v copy -c:a aac -strict experimental %s"
119 
120         var mixAudioCmd = "ffmpeg -i %s -i %s -c copy %s"

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:122:39
```
Missing space after //
```
```kotlin
119 
120         var mixAudioCmd = "ffmpeg -i %s -i %s -c copy %s"
121         mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, muxFile)
122         return mixAudioCmd.split(" ") //以空格分割为字符串数组
!!!                                       ^ error
123             .toTypedArray()
124     }
125 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:134:9
```
Missing space after //
```
```kotlin
131      * @return 需要执行命令行
132      */
133     fun extractAudio(srcFile: String?, targetFile: String?): Array<String?> {
134         //-vn:video not
!!!         ^ error
135         var mixAudioCmd = "-i %s -acodec copy -vn %s"
136         mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile)
137         return mixAudioCmd.split(" ") //以空格分割为字符串数组

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:137:39
```
Missing space after //
```
```kotlin
134         //-vn:video not
135         var mixAudioCmd = "-i %s -acodec copy -vn %s"
136         mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile)
137         return mixAudioCmd.split(" ") //以空格分割为字符串数组
!!!                                       ^ error
138             .toTypedArray()
139     }
140 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:149:9
```
Missing space after //
```
```kotlin
146      * @return 需要执行命令行
147      */
148     fun extractVideo(srcFile: String?, targetFile: String?): Array<String?> {
149         //-an audio not
!!!         ^ error
150         var mixAudioCmd = "-i %s -vcodec copy -an %s"
151         mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile)
152         return mixAudioCmd.split(" ") //以空格分割为字符串数组

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:152:39
```
Missing space after //
```
```kotlin
149         //-an audio not
150         var mixAudioCmd = "-i %s -vcodec copy -an %s"
151         mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile)
152         return mixAudioCmd.split(" ") //以空格分割为字符串数组
!!!                                       ^ error
153             .toTypedArray()
154     }
155 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:165:9
```
Missing space after //
```
```kotlin
162      * @return 需要执行命令行
163      */
164     fun transformVideo(srcFile: String?, targetFile: String?): Array<String?> {
165         //指定目标视频的帧率、码率、分辨率
!!!         ^ error
166 //        String transformVideoCmd = "-i %s -r 25 -b 200 -s 1080x720 %s";
167         var transformVideoCmd = "-i %s -vcodec copy -acodec copy %s"
168         transformVideoCmd = String.format(transformVideoCmd, srcFile, targetFile)

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:169:45
```
Missing space after //
```
```kotlin
166 //        String transformVideoCmd = "-i %s -r 25 -b 200 -s 1080x720 %s";
167         var transformVideoCmd = "-i %s -vcodec copy -acodec copy %s"
168         transformVideoCmd = String.format(transformVideoCmd, srcFile, targetFile)
169         return transformVideoCmd.split(" ") //以空格分割为字符串数组
!!!                                             ^ error
170             .toTypedArray()
171     }
172 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:192:39
```
Missing space after //
```
```kotlin
189         var cutVideoCmd =
190             "-ss %s -t %s -i %s -c:v libx264 -c:a aac -strict experimental -b:a 98k %s"
191         cutVideoCmd = String.format(cutVideoCmd, startTime, endTime, srcFile, targetFile)
192         return cutVideoCmd.split(" ") //以空格分割为字符串数组
!!!                                       ^ error
193             .toTypedArray()
194     }
195 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:207:41
```
Missing space after //
```
```kotlin
204     fun screenShot(srcFile: String?, size: String?, targetFile: String?): Array<String?> {
205         var screenShotCmd = "-i %s -f image2 -t 0.001 -s %s %s"
206         screenShotCmd = String.format(screenShotCmd, srcFile, size, targetFile)
207         return screenShotCmd.split(" ") //以空格分割为字符串数组
!!!                                         ^ error
208             .toTypedArray()
209     }
210 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:222:40
```
Missing space after //
```
```kotlin
219     fun addWaterMark(srcFile: String?, waterMark: String?, targetFile: String?): Array<String?> {
220         var waterMarkCmd = "-i %s -i %s -filter_complex overlay=0:0 %s"
221         waterMarkCmd = String.format(waterMarkCmd, srcFile, waterMark, targetFile)
222         return waterMarkCmd.split(" ") //以空格分割为字符串数组
!!!                                        ^ error
223             .toTypedArray()
224     }
225 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:242:9
```
Missing space after //
```
```kotlin
239         duration: Int,
240         targetFile: String?,
241     ): Array<String?> {
242         //String screenShotCmd = "-i %s -vframes %d -f gif %s";
!!!         ^ error
243         var screenShotCmd = "-i %s -ss %d -t %d -s 320x240 -f gif %s"
244         screenShotCmd = String.format(screenShotCmd, srcFile, startTime, duration, targetFile)
245         return screenShotCmd.split(" ") //以空格分割为字符串数组

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:245:41
```
Missing space after //
```
```kotlin
242         //String screenShotCmd = "-i %s -vframes %d -f gif %s";
243         var screenShotCmd = "-i %s -ss %d -t %d -s 320x240 -f gif %s"
244         screenShotCmd = String.format(screenShotCmd, srcFile, startTime, duration, targetFile)
245         return screenShotCmd.split(" ") //以空格分割为字符串数组
!!!                                         ^ error
246             .toTypedArray()
247     }
248 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:259:9
```
Missing space after //
```
```kotlin
256      */
257     @SuppressLint("DefaultLocale")
258     fun screenRecord(size: String?, recordTime: Int, targetFile: String?): Array<String?> {
259         //-vd x11:0,0 指录制所使用的偏移为 x=0 和 y=0
!!!         ^ error
260         //String screenRecordCmd = "-vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s %s";
261         var screenRecordCmd = "-vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s -t %d %s"
262         screenRecordCmd = String.format(screenRecordCmd, size, recordTime, targetFile)

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:260:9
```
Missing space after //
```
```kotlin
257     @SuppressLint("DefaultLocale")
258     fun screenRecord(size: String?, recordTime: Int, targetFile: String?): Array<String?> {
259         //-vd x11:0,0 指录制所使用的偏移为 x=0 和 y=0
260         //String screenRecordCmd = "-vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s %s";
!!!         ^ error
261         var screenRecordCmd = "-vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s -t %d %s"
262         screenRecordCmd = String.format(screenRecordCmd, size, recordTime, targetFile)
263         Log.i("VideoHandleActivity", "screenRecordCmd=$screenRecordCmd")

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:264:43
```
Missing space after //
```
```kotlin
261         var screenRecordCmd = "-vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s -t %d %s"
262         screenRecordCmd = String.format(screenRecordCmd, size, recordTime, targetFile)
263         Log.i("VideoHandleActivity", "screenRecordCmd=$screenRecordCmd")
264         return screenRecordCmd.split(" ") //以空格分割为字符串数组
!!!                                           ^ error
265             .toTypedArray()
266     }
267 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:277:9
```
Missing space after //
```
```kotlin
274      */
275     @SuppressLint("DefaultLocale")
276     fun pictureToVideo(srcFile: String?, targetFile: String?): Array<String?> {
277         //-f image2：代表使用image2格式，需要放在输入文件前面
!!!         ^ error
278         var combineVideo = "-f image2 -r 1 -i %simg#d.jpg -vcodec mpeg4 %s"
279         combineVideo = String.format(combineVideo, srcFile, targetFile)
280         combineVideo = combineVideo.replace("#", "%")

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:282:40
```
Missing space after //
```
```kotlin
279         combineVideo = String.format(combineVideo, srcFile, targetFile)
280         combineVideo = combineVideo.replace("#", "%")
281         Log.i("FFmpegUtil", "combineVideo=$combineVideo")
282         return combineVideo.split(" ") //以空格分割为字符串数组
!!!                                        ^ error
283             .toTypedArray()
284     }
285 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:324:66
```
Missing space after //
```
```kotlin
321     ): Array<String?> {
322 //        String multiVideo = "-i %s -i %s -i %s -i %s -filter_complex " +
323 //                "\"[0:v]pad=iw*2:ih*2[a];[a][1:v]overlay=w[b];[b][2:v]overlay=0:h[c];[c][3:v]overlay=w:h\" %s";
324         var multiVideo = "-i %s -i %s -filter_complex hstack %s" //hstack:水平拼接，默认
!!!                                                                  ^ error
325         if (videoLayout == LAYOUT_VERTICAL) { //vstack:垂直拼接
326             multiVideo = multiVideo.replace("hstack", "vstack")
327         }

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:325:47
```
Missing space after //
```
```kotlin
322 //        String multiVideo = "-i %s -i %s -i %s -i %s -filter_complex " +
323 //                "\"[0:v]pad=iw*2:ih*2[a];[a][1:v]overlay=w[b];[b][2:v]overlay=0:h[c];[c][3:v]overlay=w:h\" %s";
324         var multiVideo = "-i %s -i %s -filter_complex hstack %s" //hstack:水平拼接，默认
325         if (videoLayout == LAYOUT_VERTICAL) { //vstack:垂直拼接
!!!                                               ^ error
326             multiVideo = multiVideo.replace("hstack", "vstack")
327         }
328         multiVideo = String.format(multiVideo, input1, input2, targetFile)

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:340:9
```
Missing space after //
```
```kotlin
337      * @return 视频反序的命令行
338      */
339     fun reverseVideo(inputFile: String?, targetFile: String?): Array<String?> {
340         //FIXME 音频也反序
!!!         ^ error
341 //        String reverseVideo = "-i %s -filter_complex [0:v]reverse[v];[0:a]areverse[a] -map [v] -map [a] %s";
342         var reverseVideo = "-i %s -filter_complex [0:v]reverse[v] -map [v] %s" //单纯视频反序
343         reverseVideo = String.format(reverseVideo, inputFile, targetFile)

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:342:80
```
Missing space after //
```
```kotlin
339     fun reverseVideo(inputFile: String?, targetFile: String?): Array<String?> {
340         //FIXME 音频也反序
341 //        String reverseVideo = "-i %s -filter_complex [0:v]reverse[v];[0:a]areverse[a] -map [v] -map [a] %s";
342         var reverseVideo = "-i %s -filter_complex [0:v]reverse[v] -map [v] %s" //单纯视频反序
!!!                                                                                ^ error
343         reverseVideo = String.format(reverseVideo, inputFile, targetFile)
344         return reverseVideo.split(" ").toTypedArray()
345     }

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:377:9
```
Missing space after //
```
```kotlin
374         frameRate: Int,
375         targetFile: String?,
376     ): Array<String?> {
377         //-ss：开始时间，单位为秒
!!!         ^ error
378         //-t：持续时间，单位为秒
379         //-r：帧率，每秒抽多少帧
380         var toImage = "-i %s -ss %s -t %s -r %s %s"

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:378:9
```
Missing space after //
```
```kotlin
375         targetFile: String?,
376     ): Array<String?> {
377         //-ss：开始时间，单位为秒
378         //-t：持续时间，单位为秒
!!!         ^ error
379         //-r：帧率，每秒抽多少帧
380         var toImage = "-i %s -ss %s -t %s -r %s %s"
381         toImage = java.lang.String.format(

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:379:9
```
Missing space after //
```
```kotlin
376     ): Array<String?> {
377         //-ss：开始时间，单位为秒
378         //-t：持续时间，单位为秒
379         //-r：帧率，每秒抽多少帧
!!!         ^ error
380         var toImage = "-i %s -ss %s -t %s -r %s %s"
381         toImage = java.lang.String.format(
382             Locale.CHINESE,

```

### formatting, FinalNewline (1)

Detects missing final newlines

[Documentation](https://detekt.dev/docs/rules/formatting#finalnewline)

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:1:1
```
File must end with a newline (\n)
```
```kotlin
1 package com.imcys.bilibilias.ffmpeg.utils
! ^ error
2 
3 import android.annotation.SuppressLint
4 import android.util.Log

```

### formatting, NoBlankLineBeforeRbrace (1)

Detects blank lines before rbraces

[Documentation](https://detekt.dev/docs/rules/formatting#noblanklinebeforerbrace)

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:415:6
```
Unexpected blank line(s) before "}"
```
```kotlin
412         var reverseVideo = "-i %s -i %s -filter_complex overlay=%d:%d %s"
413         reverseVideo = String.format(reverseVideo, inputFile1, inputFile2, x, y, targetFile)
414         return reverseVideo.split(" ").toTypedArray()
415     }
!!!      ^ error
416 
417 
418 }

```

### formatting, NoConsecutiveBlankLines (5)

Reports consecutive blank lines

[Documentation](https://detekt.dev/docs/rules/formatting#noconsecutiveblanklines)

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:27:6
```
Needless blank line(s)
```
```kotlin
24         transformAudioCmd = String.format(transformAudioCmd, srcFile, targetFile)
25         return transformAudioCmd.split(" ") //以空格分割为字符串数组
26             .toTypedArray()
27     }
!!      ^ error
28 
29 
30     /**

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:81:80
```
Needless blank line(s)
```
```kotlin
78         return mixAudioCmd.split(" ") //以空格分割为字符串数组
79             .toTypedArray()
80     }
81     //混音公式：value = sample1 + sample2 - (sample1 * sample2 / (pow(2, 16-1) - 1))
!!                                                                                ^ error
82 
83 
84     //混音公式：value = sample1 + sample2 - (sample1 * sample2 / (pow(2, 16-1) - 1))

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:107:6
```
Needless blank line(s)
```
```kotlin
104         mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, duration, muxFile)
105         return mixAudioCmd.split(" ") //以空格分割为字符串数组
106             .toTypedArray()
107     }
!!!      ^ error
108 
109 
110     @SuppressLint("DefaultLocale")

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:154:6
```
Needless blank line(s)
```
```kotlin
151         mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile)
152         return mixAudioCmd.split(" ") //以空格分割为字符串数组
153             .toTypedArray()
154     }
!!!      ^ error
155 
156 
157     /**

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:415:6
```
Needless blank line(s)
```
```kotlin
412         var reverseVideo = "-i %s -i %s -filter_complex overlay=%d:%d %s"
413         reverseVideo = String.format(reverseVideo, inputFile1, inputFile2, x, y, targetFile)
414         return reverseVideo.split(" ").toTypedArray()
415     }
!!!      ^ error
416 
417 
418 }

```

### formatting, NoEmptyFirstLineInMethodBlock (2)

Reports methods that have an empty first line.

[Documentation](https://detekt.dev/docs/rules/formatting#noemptyfirstlineinmethodblock)

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:100:24
```
First line in a method block should not be empty
```
```kotlin
97          audioFile: String?,
98          duration: Int,
99          muxFile: String?,
100     ): Array<String?> {
!!!                        ^ error
101 
102         //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
103         var mixAudioCmd = "ffmpeg -i %s -i %s -t %d %s"

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:115:24
```
First line in a method block should not be empty
```
```kotlin
112         videoFile: String?,
113         audioFile: String?,
114         muxFile: String?,
115     ): Array<String?> {
!!!                        ^ error
116 
117         //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
118         // "-i %s -i %s -c:v copy -c:a aac -strict experimental %s"

```

### impure, ReturnStatement (21)

Pure function shouldn't have return statement

[Documentation](https://detekt.dev/docs/rules/impure#returnstatement)

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:25:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
22     fun transformAudio(srcFile: String?, targetFile: String?): Array<String?> {
23         var transformAudioCmd = "-i %s %s"
24         transformAudioCmd = String.format(transformAudioCmd, srcFile, targetFile)
25         return transformAudioCmd.split(" ") //以空格分割为字符串数组
!!         ^ error
26             .toTypedArray()
27     }
28 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:48:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
45     ): Array<String> {
46         var cutAudioCmd = "-i %s -ss %d -t %d %s"
47         cutAudioCmd = String.format(cutAudioCmd, srcFile, startTime, duration, targetFile)
48         return cutAudioCmd.split(" ") //以空格分割为字符串数组
!!         ^ error
49             .toTypedArray()
50     }
51 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:63:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
60     fun concatAudio(srcFile: String?, appendFile: String?, targetFile: String?): Array<String?> {
61         var concatAudioCmd = "-i concat:%s|%s -acodec copy %s"
62         concatAudioCmd = String.format(concatAudioCmd, srcFile, appendFile, targetFile)
63         return concatAudioCmd.split(" ") //以空格分割为字符串数组
!!         ^ error
64             .toTypedArray()
65     }
66 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:78:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
75     fun mixAudio(srcFile: String?, mixFile: String?, targetFile: String?): Array<String?> {
76         var mixAudioCmd = "-i %s -i %s -filter_complex amix=inputs=2:duration=first -strict -2 %s"
77         mixAudioCmd = String.format(mixAudioCmd, srcFile, mixFile, targetFile)
78         return mixAudioCmd.split(" ") //以空格分割为字符串数组
!!         ^ error
79             .toTypedArray()
80     }
81     //混音公式：value = sample1 + sample2 - (sample1 * sample2 / (pow(2, 16-1) - 1))

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:105:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
102         //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
103         var mixAudioCmd = "ffmpeg -i %s -i %s -t %d %s"
104         mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, duration, muxFile)
105         return mixAudioCmd.split(" ") //以空格分割为字符串数组
!!!         ^ error
106             .toTypedArray()
107     }
108 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:122:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
119 
120         var mixAudioCmd = "ffmpeg -i %s -i %s -c copy %s"
121         mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, muxFile)
122         return mixAudioCmd.split(" ") //以空格分割为字符串数组
!!!         ^ error
123             .toTypedArray()
124     }
125 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:137:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
134         //-vn:video not
135         var mixAudioCmd = "-i %s -acodec copy -vn %s"
136         mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile)
137         return mixAudioCmd.split(" ") //以空格分割为字符串数组
!!!         ^ error
138             .toTypedArray()
139     }
140 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:152:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
149         //-an audio not
150         var mixAudioCmd = "-i %s -vcodec copy -an %s"
151         mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile)
152         return mixAudioCmd.split(" ") //以空格分割为字符串数组
!!!         ^ error
153             .toTypedArray()
154     }
155 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:169:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
166 //        String transformVideoCmd = "-i %s -r 25 -b 200 -s 1080x720 %s";
167         var transformVideoCmd = "-i %s -vcodec copy -acodec copy %s"
168         transformVideoCmd = String.format(transformVideoCmd, srcFile, targetFile)
169         return transformVideoCmd.split(" ") //以空格分割为字符串数组
!!!         ^ error
170             .toTypedArray()
171     }
172 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:192:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
189         var cutVideoCmd =
190             "-ss %s -t %s -i %s -c:v libx264 -c:a aac -strict experimental -b:a 98k %s"
191         cutVideoCmd = String.format(cutVideoCmd, startTime, endTime, srcFile, targetFile)
192         return cutVideoCmd.split(" ") //以空格分割为字符串数组
!!!         ^ error
193             .toTypedArray()
194     }
195 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:207:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
204     fun screenShot(srcFile: String?, size: String?, targetFile: String?): Array<String?> {
205         var screenShotCmd = "-i %s -f image2 -t 0.001 -s %s %s"
206         screenShotCmd = String.format(screenShotCmd, srcFile, size, targetFile)
207         return screenShotCmd.split(" ") //以空格分割为字符串数组
!!!         ^ error
208             .toTypedArray()
209     }
210 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:222:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
219     fun addWaterMark(srcFile: String?, waterMark: String?, targetFile: String?): Array<String?> {
220         var waterMarkCmd = "-i %s -i %s -filter_complex overlay=0:0 %s"
221         waterMarkCmd = String.format(waterMarkCmd, srcFile, waterMark, targetFile)
222         return waterMarkCmd.split(" ") //以空格分割为字符串数组
!!!         ^ error
223             .toTypedArray()
224     }
225 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:245:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
242         //String screenShotCmd = "-i %s -vframes %d -f gif %s";
243         var screenShotCmd = "-i %s -ss %d -t %d -s 320x240 -f gif %s"
244         screenShotCmd = String.format(screenShotCmd, srcFile, startTime, duration, targetFile)
245         return screenShotCmd.split(" ") //以空格分割为字符串数组
!!!         ^ error
246             .toTypedArray()
247     }
248 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:264:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
261         var screenRecordCmd = "-vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s -t %d %s"
262         screenRecordCmd = String.format(screenRecordCmd, size, recordTime, targetFile)
263         Log.i("VideoHandleActivity", "screenRecordCmd=$screenRecordCmd")
264         return screenRecordCmd.split(" ") //以空格分割为字符串数组
!!!         ^ error
265             .toTypedArray()
266     }
267 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:282:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
279         combineVideo = String.format(combineVideo, srcFile, targetFile)
280         combineVideo = combineVideo.replace("#", "%")
281         Log.i("FFmpegUtil", "combineVideo=$combineVideo")
282         return combineVideo.split(" ") //以空格分割为字符串数组
!!!         ^ error
283             .toTypedArray()
284     }
285 

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:304:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
301     ): Array<String?> {
302         var combineVideo = "-f s16le -ar %d -ac %d -i %s %s"
303         combineVideo = String.format(combineVideo, sampleRate, channel, srcFile, targetFile)
304         return combineVideo.split(" ").toTypedArray()
!!!         ^ error
305     }
306 
307     /**

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:329:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
326             multiVideo = multiVideo.replace("hstack", "vstack")
327         }
328         multiVideo = String.format(multiVideo, input1, input2, targetFile)
329         return multiVideo.split(" ").toTypedArray()
!!!         ^ error
330     }
331 
332     /**

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:344:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
341 //        String reverseVideo = "-i %s -filter_complex [0:v]reverse[v];[0:a]areverse[a] -map [v] -map [a] %s";
342         var reverseVideo = "-i %s -filter_complex [0:v]reverse[v] -map [v] %s" //单纯视频反序
343         reverseVideo = String.format(reverseVideo, inputFile, targetFile)
344         return reverseVideo.split(" ").toTypedArray()
!!!         ^ error
345     }
346 
347     /**

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:357:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
354     fun denoiseVideo(inputFile: String?, targetFile: String?): Array<String?> {
355         var reverseVideo = "-i %s -nr 500 %s"
356         reverseVideo = String.format(reverseVideo, inputFile, targetFile)
357         return reverseVideo.split(" ").toTypedArray()
!!!         ^ error
358     }
359 
360     /**

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:391:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
388             targetFile
389         )
390         toImage = "$toImage%3d.jpg"
391         return toImage.split(" ").toTypedArray()
!!!         ^ error
392     }
393 
394     /**

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:414:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `return` statement.
```
```kotlin
411     ): Array<String?> {
412         var reverseVideo = "-i %s -i %s -filter_complex overlay=%d:%d %s"
413         reverseVideo = String.format(reverseVideo, inputFile1, inputFile2, x, y, targetFile)
414         return reverseVideo.split(" ").toTypedArray()
!!!         ^ error
415     }
416 
417 

```

### impure, VariableDefinition (21)

Variables shouldn't be used in pure code

[Documentation](https://detekt.dev/docs/rules/impure#variabledefinition)

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:23:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
20      * @return 转码后的文件
21      */
22     fun transformAudio(srcFile: String?, targetFile: String?): Array<String?> {
23         var transformAudioCmd = "-i %s %s"
!!         ^ error
24         transformAudioCmd = String.format(transformAudioCmd, srcFile, targetFile)
25         return transformAudioCmd.split(" ") //以空格分割为字符串数组
26             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:46:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
43         duration: Int,
44         targetFile: String,
45     ): Array<String> {
46         var cutAudioCmd = "-i %s -ss %d -t %d %s"
!!         ^ error
47         cutAudioCmd = String.format(cutAudioCmd, srcFile, startTime, duration, targetFile)
48         return cutAudioCmd.split(" ") //以空格分割为字符串数组
49             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:61:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
58      * @return 合并后的文件
59      */
60     fun concatAudio(srcFile: String?, appendFile: String?, targetFile: String?): Array<String?> {
61         var concatAudioCmd = "-i concat:%s|%s -acodec copy %s"
!!         ^ error
62         concatAudioCmd = String.format(concatAudioCmd, srcFile, appendFile, targetFile)
63         return concatAudioCmd.split(" ") //以空格分割为字符串数组
64             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:76:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
73      * @return 混合后的文件
74      */
75     fun mixAudio(srcFile: String?, mixFile: String?, targetFile: String?): Array<String?> {
76         var mixAudioCmd = "-i %s -i %s -filter_complex amix=inputs=2:duration=first -strict -2 %s"
!!         ^ error
77         mixAudioCmd = String.format(mixAudioCmd, srcFile, mixFile, targetFile)
78         return mixAudioCmd.split(" ") //以空格分割为字符串数组
79             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:103:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
100     ): Array<String?> {
101 
102         //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
103         var mixAudioCmd = "ffmpeg -i %s -i %s -t %d %s"
!!!         ^ error
104         mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, duration, muxFile)
105         return mixAudioCmd.split(" ") //以空格分割为字符串数组
106             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:120:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
117         //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
118         // "-i %s -i %s -c:v copy -c:a aac -strict experimental %s"
119 
120         var mixAudioCmd = "ffmpeg -i %s -i %s -c copy %s"
!!!         ^ error
121         mixAudioCmd = String.format(mixAudioCmd, videoFile, audioFile, muxFile)
122         return mixAudioCmd.split(" ") //以空格分割为字符串数组
123             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:135:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
132      */
133     fun extractAudio(srcFile: String?, targetFile: String?): Array<String?> {
134         //-vn:video not
135         var mixAudioCmd = "-i %s -acodec copy -vn %s"
!!!         ^ error
136         mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile)
137         return mixAudioCmd.split(" ") //以空格分割为字符串数组
138             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:150:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
147      */
148     fun extractVideo(srcFile: String?, targetFile: String?): Array<String?> {
149         //-an audio not
150         var mixAudioCmd = "-i %s -vcodec copy -an %s"
!!!         ^ error
151         mixAudioCmd = String.format(mixAudioCmd, srcFile, targetFile)
152         return mixAudioCmd.split(" ") //以空格分割为字符串数组
153             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:167:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
164     fun transformVideo(srcFile: String?, targetFile: String?): Array<String?> {
165         //指定目标视频的帧率、码率、分辨率
166 //        String transformVideoCmd = "-i %s -r 25 -b 200 -s 1080x720 %s";
167         var transformVideoCmd = "-i %s -vcodec copy -acodec copy %s"
!!!         ^ error
168         transformVideoCmd = String.format(transformVideoCmd, srcFile, targetFile)
169         return transformVideoCmd.split(" ") //以空格分割为字符串数组
170             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:189:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
186         endTime: String?,
187         targetFile: String?,
188     ): Array<String?> {
189         var cutVideoCmd =
!!!         ^ error
190             "-ss %s -t %s -i %s -c:v libx264 -c:a aac -strict experimental -b:a 98k %s"
191         cutVideoCmd = String.format(cutVideoCmd, startTime, endTime, srcFile, targetFile)
192         return cutVideoCmd.split(" ") //以空格分割为字符串数组

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:205:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
202      * @return 需要执行命令行
203      */
204     fun screenShot(srcFile: String?, size: String?, targetFile: String?): Array<String?> {
205         var screenShotCmd = "-i %s -f image2 -t 0.001 -s %s %s"
!!!         ^ error
206         screenShotCmd = String.format(screenShotCmd, srcFile, size, targetFile)
207         return screenShotCmd.split(" ") //以空格分割为字符串数组
208             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:220:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
217      * @return 需要执行命令行
218      */
219     fun addWaterMark(srcFile: String?, waterMark: String?, targetFile: String?): Array<String?> {
220         var waterMarkCmd = "-i %s -i %s -filter_complex overlay=0:0 %s"
!!!         ^ error
221         waterMarkCmd = String.format(waterMarkCmd, srcFile, waterMark, targetFile)
222         return waterMarkCmd.split(" ") //以空格分割为字符串数组
223             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:243:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
240         targetFile: String?,
241     ): Array<String?> {
242         //String screenShotCmd = "-i %s -vframes %d -f gif %s";
243         var screenShotCmd = "-i %s -ss %d -t %d -s 320x240 -f gif %s"
!!!         ^ error
244         screenShotCmd = String.format(screenShotCmd, srcFile, startTime, duration, targetFile)
245         return screenShotCmd.split(" ") //以空格分割为字符串数组
246             .toTypedArray()

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:261:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
258     fun screenRecord(size: String?, recordTime: Int, targetFile: String?): Array<String?> {
259         //-vd x11:0,0 指录制所使用的偏移为 x=0 和 y=0
260         //String screenRecordCmd = "-vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s %s";
261         var screenRecordCmd = "-vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s -t %d %s"
!!!         ^ error
262         screenRecordCmd = String.format(screenRecordCmd, size, recordTime, targetFile)
263         Log.i("VideoHandleActivity", "screenRecordCmd=$screenRecordCmd")
264         return screenRecordCmd.split(" ") //以空格分割为字符串数组

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:278:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
275     @SuppressLint("DefaultLocale")
276     fun pictureToVideo(srcFile: String?, targetFile: String?): Array<String?> {
277         //-f image2：代表使用image2格式，需要放在输入文件前面
278         var combineVideo = "-f image2 -r 1 -i %simg#d.jpg -vcodec mpeg4 %s"
!!!         ^ error
279         combineVideo = String.format(combineVideo, srcFile, targetFile)
280         combineVideo = combineVideo.replace("#", "%")
281         Log.i("FFmpegUtil", "combineVideo=$combineVideo")

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:302:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
299         sampleRate: Int,
300         channel: Int,
301     ): Array<String?> {
302         var combineVideo = "-f s16le -ar %d -ac %d -i %s %s"
!!!         ^ error
303         combineVideo = String.format(combineVideo, sampleRate, channel, srcFile, targetFile)
304         return combineVideo.split(" ").toTypedArray()
305     }

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:324:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
321     ): Array<String?> {
322 //        String multiVideo = "-i %s -i %s -i %s -i %s -filter_complex " +
323 //                "\"[0:v]pad=iw*2:ih*2[a];[a][1:v]overlay=w[b];[b][2:v]overlay=0:h[c];[c][3:v]overlay=w:h\" %s";
324         var multiVideo = "-i %s -i %s -filter_complex hstack %s" //hstack:水平拼接，默认
!!!         ^ error
325         if (videoLayout == LAYOUT_VERTICAL) { //vstack:垂直拼接
326             multiVideo = multiVideo.replace("hstack", "vstack")
327         }

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:342:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
339     fun reverseVideo(inputFile: String?, targetFile: String?): Array<String?> {
340         //FIXME 音频也反序
341 //        String reverseVideo = "-i %s -filter_complex [0:v]reverse[v];[0:a]areverse[a] -map [v] -map [a] %s";
342         var reverseVideo = "-i %s -filter_complex [0:v]reverse[v] -map [v] %s" //单纯视频反序
!!!         ^ error
343         reverseVideo = String.format(reverseVideo, inputFile, targetFile)
344         return reverseVideo.split(" ").toTypedArray()
345     }

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:355:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
352      * @return 视频降噪的命令行
353      */
354     fun denoiseVideo(inputFile: String?, targetFile: String?): Array<String?> {
355         var reverseVideo = "-i %s -nr 500 %s"
!!!         ^ error
356         reverseVideo = String.format(reverseVideo, inputFile, targetFile)
357         return reverseVideo.split(" ").toTypedArray()
358     }

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:380:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
377         //-ss：开始时间，单位为秒
378         //-t：持续时间，单位为秒
379         //-r：帧率，每秒抽多少帧
380         var toImage = "-i %s -ss %s -t %s -r %s %s"
!!!         ^ error
381         toImage = java.lang.String.format(
382             Locale.CHINESE,
383             toImage,

```

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:412:9
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt contains `var`iable.
```
```kotlin
409         y: Int,
410         targetFile: String?,
411     ): Array<String?> {
412         var reverseVideo = "-i %s -i %s -filter_complex overlay=%d:%d %s"
!!!         ^ error
413         reverseVideo = String.format(reverseVideo, inputFile1, inputFile2, x, y, targetFile)
414         return reverseVideo.split(" ").toTypedArray()
415     }

```

### style, NewLineAtEndOfFile (1)

Checks whether files end with a line separator.

[Documentation](https://detekt.dev/docs/rules/style#newlineatendoffile)

* model_ffmpeg/src/main/java/com/imcys/bilibilias/ffmpeg/utils/FFmpegUtil.kt:418:2
```
The file E:\Github-learning\bilibilias\model_ffmpeg\src\main\java\com\imcys\bilibilias\ffmpeg\utils\FFmpegUtil.kt is not ending with a new line.
```
```kotlin
415     }
416 
417 
418 }
!!!  ^ error

```

generated with [detekt version 1.23.1](https://detekt.dev/) on 2023-09-10 10:12:09 UTC
