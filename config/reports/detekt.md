# detekt

## Metrics

* 36 number of properties

* 21 number of functions

* 22 number of classes

* 6 number of packages

* 10 number of kt files

## Complexity Report

* 647 lines of code (loc)

* 497 source lines of code (sloc)

* 317 logical lines of code (lloc)

* 64 comment lines of code (cloc)

* 76 cyclomatic complexity (mcc)

* 65 cognitive complexity

* 165 number of total code smells

* 12% comment source ratio

* 239 mcc per 1,000 lloc

* 520 code smells per 1,000 lloc

## Findings (165)

### HbmartinRuleSet, AvoidVarsExceptWithDelegate (15)

Variables shouldn't be used except with delegates

[Documentation](https://detekt.dev/docs/rules/hbmartinruleset#avoidvarsexceptwithdelegate)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:53:5
```
Property bangumiFollowList is a `var`iable, please make it a val.
```
```kotlin
50 
51 class BangumiFollowLogActivity : LogExportBaseActivity() {
52 
53     private lateinit var bangumiFollowList: BangumiFollowList
!!     ^ error
54 
55     lateinit var binding: ActivityBangumiFollowLogBinding
56 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:55:5
```
Property binding is a `var`iable, please make it a val.
```
```kotlin
52 
53     private lateinit var bangumiFollowList: BangumiFollowList
54 
55     lateinit var binding: ActivityBangumiFollowLogBinding
!!     ^ error
56 
57     var selectedLogHeaders = mutableListOf<BangumiFollowLogHeaderBean>()
58 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:57:5
```
Property selectedLogHeaders is a `var`iable, please make it a val.
```
```kotlin
54 
55     lateinit var binding: ActivityBangumiFollowLogBinding
56 
57     var selectedLogHeaders = mutableListOf<BangumiFollowLogHeaderBean>()
!!     ^ error
58 
59     private val defaultLogHeaders by lazy {
60         mutableListOf(

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:220:25
```
Property row is a `var`iable, please make it a val.
```
```kotlin
217             selectedLogHeaders.forEachIndexed { selectIndex, bangumiFollowLogHeaderBean ->
218                 when (bangumiFollowLogHeaderBean.harder) {
219                     Title -> {
220                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
221                         bangumiFollowList.data.list.forEach {
222                             addCell(selectIndex, row++, it?.title ?: "", arial10format)
223                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:227:25
```
Property row is a `var`iable, please make it a val.
```
```kotlin
224                     }
225 
226                     SeasonID -> {
227                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
228                         bangumiFollowList.data.list.forEach {
229                             addCell(
230                                 selectIndex,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:239:25
```
Property row is a `var`iable, please make it a val.
```
```kotlin
236                     }
237 
238                     Evaluate -> {
239                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
240                         bangumiFollowList.data.list.forEach {
241                             addCell(selectIndex, row++, it?.evaluate ?: "", arial10format)
242                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:246:25
```
Property row is a `var`iable, please make it a val.
```
```kotlin
243                     }
244 
245                     Summary -> {
246                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
247                         bangumiFollowList.data.list.forEach {
248                             addCell(selectIndex, row++, it?.summary ?: "", arial10format)
249                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:253:25
```
Property row is a `var`iable, please make it a val.
```
```kotlin
250                     }
251 
252                     TotalCount -> {
253                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
254                         bangumiFollowList.data.list.forEach {
255                             addCell(
256                                 selectIndex,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:265:25
```
Property row is a `var`iable, please make it a val.
```
```kotlin
262                     }
263 
264                     Progress -> {
265                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
266                         bangumiFollowList.data.list.forEach {
267                             addCell(selectIndex, row++, it?.progress ?: "", arial10format)
268                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:272:25
```
Property row is a `var`iable, please make it a val.
```
```kotlin
269                     }
270 
271                     Cover -> {
272                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
273                         bangumiFollowList.data.list.forEach {
274                             addCell(selectIndex, row++, it?.cover ?: "", arial10format)
275                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:279:25
```
Property row is a `var`iable, please make it a val.
```
```kotlin
276                     }
277 
278                     SeasonTitle -> {
279                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
280                         bangumiFollowList.data.list.forEach {
281                             addCell(selectIndex, row++, it?.season_title ?: "", arial10format)
282                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:286:25
```
Property row is a `var`iable, please make it a val.
```
```kotlin
283                     }
284 
285                     Subtitle -> {
286                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
287                         bangumiFollowList.data.list.forEach {
288                             addCell(selectIndex, row++, it?.subtitle ?: "", arial10format)
289                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:293:25
```
Property row is a `var`iable, please make it a val.
```
```kotlin
290                     }
291 
292                     Subtitle14 -> {
293                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
294                         bangumiFollowList.data.list.forEach {
295                             addCell(selectIndex, row++, it?.subtitle_14 ?: "", arial10format)
296                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:300:25
```
Property row is a `var`iable, please make it a val.
```
```kotlin
297                     }
298 
299                     SeasonTypeName -> {
300                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
301                         bangumiFollowList.data.list.forEachIndexed { index, listBean ->
302                             addCell(
303                                 selectIndex,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:25:5
```
Property binding is a `var`iable, please make it a val.
```
```kotlin
22 )
23 class LogExportActivity : LogExportBaseActivity() {
24 
25     lateinit var binding: ActivityLogExportBinding
!!     ^ error
26 
27     override fun onCreate(savedInstanceState: Bundle?) {
28         super.onCreate(savedInstanceState)

```

### HbmartinRuleSet, DontForceCast (1)

Do not use unsafe casts, safely cast with as? instead.

[Documentation](https://detekt.dev/docs/rules/hbmartinruleset#dontforcecast)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:176:19
```
`Label(
                    index,
                    0,
                    bangumiFollowLogHeaderBean.title,
                    arial14format,
                ) as WritableCell?` uses an unsafe cast, safely cast with as? instead.
```
```kotlin
173                     0,
174                     bangumiFollowLogHeaderBean.title,
175                     arial14format,
176                 ) as WritableCell?,
!!!                   ^ error
177             )
178             // 列宽
179             sheet.setColumnView(index, 300)

```

### HbmartinRuleSet, NoVarsInConstructor (5)

Do not use `var`s in a class constructor.

[Documentation](https://detekt.dev/docs/rules/hbmartinruleset#novarsinconstructor)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/ExportItemBean.kt:5:26
```
Disallowed `var` parameter var itemType: ExportItemEnum in constructor for ExportItemBean, please make it a val.
```
```kotlin
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum
4 
5 data class ExportItemBean(
!                          ^ error
6     var itemType: ExportItemEnum,
7     var title: String,
8     var longTitle: String,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/ExportItemBean.kt:5:26
```
Disallowed `var` parameter var title: String in constructor for ExportItemBean, please make it a val.
```
```kotlin
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum
4 
5 data class ExportItemBean(
!                          ^ error
6     var itemType: ExportItemEnum,
7     var title: String,
8     var longTitle: String,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/ExportItemBean.kt:5:26
```
Disallowed `var` parameter var longTitle: String in constructor for ExportItemBean, please make it a val.
```
```kotlin
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum
4 
5 data class ExportItemBean(
!                          ^ error
6     var itemType: ExportItemEnum,
7     var title: String,
8     var longTitle: String,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/ExportItemBean.kt:5:26
```
Disallowed `var` parameter var content: String in constructor for ExportItemBean, please make it a val.
```
```kotlin
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum
4 
5 data class ExportItemBean(
!                          ^ error
6     var itemType: ExportItemEnum,
7     var title: String,
8     var longTitle: String,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/ExportItemBean.kt:5:26
```
Disallowed `var` parameter var icoUrl: String in constructor for ExportItemBean, please make it a val.
```
```kotlin
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum
4 
5 data class ExportItemBean(
!                          ^ error
6     var itemType: ExportItemEnum,
7     var title: String,
8     var longTitle: String,

```

### complexity, CyclomaticComplexMethod (1)

Prefer splitting up complex methods into smaller, easier to test methods.

[Documentation](https://detekt.dev/docs/rules/complexity#cyclomaticcomplexmethod)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:211:17
```
The function editExcel appears to be too complex based on Cyclomatic Complexity (complexity: 45). Defined complexity threshold for methods is set to '15'
```
```kotlin
208     /**
209      * 编辑excel
210      */
211     private fun editExcel(bangumiFollowList: BangumiFollowList, sheet: WritableSheet, i: Int) {
!!!                 ^ error
212         sheet.apply {
213             val arial14font = WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD)
214             val arial10format = WritableCellFormat(arial14font)

```

### complexity, LongMethod (1)

One method should have one responsibility. Long methods tend to handle many things at once. Prefer smaller methods to make them easier to understand.

[Documentation](https://detekt.dev/docs/rules/complexity#longmethod)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:211:17
```
The function editExcel is too long (92). The maximum length is 60.
```
```kotlin
208     /**
209      * 编辑excel
210      */
211     private fun editExcel(bangumiFollowList: BangumiFollowList, sheet: WritableSheet, i: Int) {
!!!                 ^ error
212         sheet.apply {
213             val arial14font = WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD)
214             val arial10format = WritableCellFormat(arial14font)

```

### formatting, CommentSpacing (28)

Checks if comments have the right spacing

[Documentation](https://detekt.dev/docs/rules/formatting#commentspacing)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:4:11
```
Missing space before //
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.mEnum
2 
3 enum class BangumiFollowLogHeader {
4     Title,//标题
!           ^ error
5     Evaluate,//评价
6     SeasonID,//SSID
7     Summary,//简介

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:4:11
```
Missing space after //
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.mEnum
2 
3 enum class BangumiFollowLogHeader {
4     Title,//标题
!           ^ error
5     Evaluate,//评价
6     SeasonID,//SSID
7     Summary,//简介

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:5:14
```
Missing space before //
```
```kotlin
2 
3 enum class BangumiFollowLogHeader {
4     Title,//标题
5     Evaluate,//评价
!              ^ error
6     SeasonID,//SSID
7     Summary,//简介
8     Subtitle,//短简介

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:5:14
```
Missing space after //
```
```kotlin
2 
3 enum class BangumiFollowLogHeader {
4     Title,//标题
5     Evaluate,//评价
!              ^ error
6     SeasonID,//SSID
7     Summary,//简介
8     Subtitle,//短简介

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:6:14
```
Missing space before //
```
```kotlin
3  enum class BangumiFollowLogHeader {
4      Title,//标题
5      Evaluate,//评价
6      SeasonID,//SSID
!               ^ error
7      Summary,//简介
8      Subtitle,//短简介
9      Subtitle14,//14字短简介

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:6:14
```
Missing space after //
```
```kotlin
3  enum class BangumiFollowLogHeader {
4      Title,//标题
5      Evaluate,//评价
6      SeasonID,//SSID
!               ^ error
7      Summary,//简介
8      Subtitle,//短简介
9      Subtitle14,//14字短简介

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:7:13
```
Missing space before //
```
```kotlin
4      Title,//标题
5      Evaluate,//评价
6      SeasonID,//SSID
7      Summary,//简介
!              ^ error
8      Subtitle,//短简介
9      Subtitle14,//14字短简介
10     TotalCount,//总集数

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:7:13
```
Missing space after //
```
```kotlin
4      Title,//标题
5      Evaluate,//评价
6      SeasonID,//SSID
7      Summary,//简介
!              ^ error
8      Subtitle,//短简介
9      Subtitle14,//14字短简介
10     TotalCount,//总集数

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:8:14
```
Missing space before //
```
```kotlin
5      Evaluate,//评价
6      SeasonID,//SSID
7      Summary,//简介
8      Subtitle,//短简介
!               ^ error
9      Subtitle14,//14字短简介
10     TotalCount,//总集数
11     Progress,//观看进度

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:8:14
```
Missing space after //
```
```kotlin
5      Evaluate,//评价
6      SeasonID,//SSID
7      Summary,//简介
8      Subtitle,//短简介
!               ^ error
9      Subtitle14,//14字短简介
10     TotalCount,//总集数
11     Progress,//观看进度

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:9:16
```
Missing space before //
```
```kotlin
6      SeasonID,//SSID
7      Summary,//简介
8      Subtitle,//短简介
9      Subtitle14,//14字短简介
!                 ^ error
10     TotalCount,//总集数
11     Progress,//观看进度
12     Cover,//封面海报

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:9:16
```
Missing space after //
```
```kotlin
6      SeasonID,//SSID
7      Summary,//简介
8      Subtitle,//短简介
9      Subtitle14,//14字短简介
!                 ^ error
10     TotalCount,//总集数
11     Progress,//观看进度
12     Cover,//封面海报

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:10:16
```
Missing space before //
```
```kotlin
7      Summary,//简介
8      Subtitle,//短简介
9      Subtitle14,//14字短简介
10     TotalCount,//总集数
!!                ^ error
11     Progress,//观看进度
12     Cover,//封面海报
13     SeasonTitle,//季度信息

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:10:16
```
Missing space after //
```
```kotlin
7      Summary,//简介
8      Subtitle,//短简介
9      Subtitle14,//14字短简介
10     TotalCount,//总集数
!!                ^ error
11     Progress,//观看进度
12     Cover,//封面海报
13     SeasonTitle,//季度信息

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:11:14
```
Missing space before //
```
```kotlin
8      Subtitle,//短简介
9      Subtitle14,//14字短简介
10     TotalCount,//总集数
11     Progress,//观看进度
!!              ^ error
12     Cover,//封面海报
13     SeasonTitle,//季度信息
14     SeasonTypeName,//创作类型,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:11:14
```
Missing space after //
```
```kotlin
8      Subtitle,//短简介
9      Subtitle14,//14字短简介
10     TotalCount,//总集数
11     Progress,//观看进度
!!              ^ error
12     Cover,//封面海报
13     SeasonTitle,//季度信息
14     SeasonTypeName,//创作类型,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:12:11
```
Missing space before //
```
```kotlin
9      Subtitle14,//14字短简介
10     TotalCount,//总集数
11     Progress,//观看进度
12     Cover,//封面海报
!!           ^ error
13     SeasonTitle,//季度信息
14     SeasonTypeName,//创作类型,
15 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:12:11
```
Missing space after //
```
```kotlin
9      Subtitle14,//14字短简介
10     TotalCount,//总集数
11     Progress,//观看进度
12     Cover,//封面海报
!!           ^ error
13     SeasonTitle,//季度信息
14     SeasonTypeName,//创作类型,
15 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:13:17
```
Missing space before //
```
```kotlin
10     TotalCount,//总集数
11     Progress,//观看进度
12     Cover,//封面海报
13     SeasonTitle,//季度信息
!!                 ^ error
14     SeasonTypeName,//创作类型,
15 
16 }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:13:17
```
Missing space after //
```
```kotlin
10     TotalCount,//总集数
11     Progress,//观看进度
12     Cover,//封面海报
13     SeasonTitle,//季度信息
!!                 ^ error
14     SeasonTypeName,//创作类型,
15 
16 }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:14:20
```
Missing space before //
```
```kotlin
11     Progress,//观看进度
12     Cover,//封面海报
13     SeasonTitle,//季度信息
14     SeasonTypeName,//创作类型,
!!                    ^ error
15 
16 }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:14:20
```
Missing space after //
```
```kotlin
11     Progress,//观看进度
12     Cover,//封面海报
13     SeasonTitle,//季度信息
14     SeasonTypeName,//创作类型,
!!                    ^ error
15 
16 }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:60:13
```
Missing space after //
```
```kotlin
57 
58 
59         binding.logExportHomeRv.linear().setup {
60             //防抖动
!!             ^ error
61             clickThrottle = 1000 // 单位毫秒
62             setAnimation(AnimationType.SCALE)
63             addType<ExportItemBean>(R.layout.log_export_item_export_tool)

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:25:13
```
Missing space after //
```
```kotlin
22          */
23         @SuppressLint("InflateParams", "MissingInflatedId")
24         fun loadDialog(context: Context, mutableState: String): BottomSheetDialog {
25             //先获取View实例
!!             ^ error
26             val view: View = LayoutInflater.from(context)
27                 .inflate(R.layout.log_export_dialog_load_bottomsheet, null, false)
28             val loadText = view.findViewById<TextView>(R.id.log_export_dl_load_title)

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:30:13
```
Missing space after //
```
```kotlin
27                 .inflate(R.layout.log_export_dialog_load_bottomsheet, null, false)
28             val loadText = view.findViewById<TextView>(R.id.log_export_dl_load_title)
29             loadText.text = mutableState
30             //设置布局背景
!!             ^ error
31             //自定义方案
32             //mDialogBehavior.peekHeight = 600
33 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:31:13
```
Missing space after //
```
```kotlin
28             val loadText = view.findViewById<TextView>(R.id.log_export_dl_load_title)
29             loadText.text = mutableState
30             //设置布局背景
31             //自定义方案
!!             ^ error
32             //mDialogBehavior.peekHeight = 600
33 
34             return initBottomSheetDialog(context, view)

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:32:13
```
Missing space after //
```
```kotlin
29             loadText.text = mutableState
30             //设置布局背景
31             //自定义方案
32             //mDialogBehavior.peekHeight = 600
!!             ^ error
33 
34             return initBottomSheetDialog(context, view)
35         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:44:13
```
Missing space after //
```
```kotlin
41                 context,
42                 com.imcys.asbottomdialog.bottomdialog.R.style.asdialog_BottomSheetDialog
43             )
44             //设置布局
!!             ^ error
45             bottomSheetDialog.setContentView(view)
46 
47             return bottomSheetDialog

```

### formatting, FinalNewline (7)

Detects missing final newlines

[Documentation](https://detekt.dev/docs/rules/formatting#finalnewline)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:1:1
```
File must end with a newline (\n)
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.mEnum
! ^ error
2 
3 enum class BangumiFollowLogHeader {
4     Title,//标题

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/ExportItemEnum.kt:1:1
```
File must end with a newline (\n)
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.mEnum
! ^ error
2 
3 enum class ExportItemEnum {
4     BangumiFollowLog,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/ExportItemBean.kt:1:1
```
File must end with a newline (\n)
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.model
! ^ error
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum
4 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:1:1
```
File must end with a newline (\n)
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.ui.activity
! ^ error
2 
3 import android.content.Context
4 import android.os.Bundle

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExcelUtils.kt:1:1
```
File must end with a newline (\n)
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.utils
! ^ error
2 
3 import jxl.Workbook
4 import jxl.format.CellFormat

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:1:1
```
File must end with a newline (\n)
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.utils
! ^ error
2 
3 import android.annotation.SuppressLint
4 import android.content.Context

```

* E:/Github-learning/bilibilias/tool_log_export/src/test/java/com/imcys/bilibilias/tool_log_export/ExampleUnitTest.kt:1:1
```
File must end with a newline (\n)
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export
! ^ error
2 
3 import org.junit.Assert.assertEquals
4 import org.junit.Test

```

### formatting, NoBlankLineBeforeRbrace (8)

Detects blank lines before rbraces

[Documentation](https://detekt.dev/docs/rules/formatting#noblanklinebeforerbrace)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:14:27
```
Unexpected blank line(s) before "}"
```
```kotlin
11     Progress,//观看进度
12     Cover,//封面海报
13     SeasonTitle,//季度信息
14     SeasonTypeName,//创作类型,
!!                           ^ error
15 
16 }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:38:19
```
Unexpected blank line(s) before "}"
```
```kotlin
35             logExportHomeTopLy.addStatusBarTopPadding()
36         }
37 
38         initView()
!!                   ^ error
39 
40     }
41 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:67:14
```
Unexpected blank line(s) before "}"
```
```kotlin
64 
65             onClick(R.id.log_export_tool_item_ly) {
66                 activateClickEvent(getModel())
67             }
!!              ^ error
68 
69         }.models = exportItemBeans
70 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:69:35
```
Unexpected blank line(s) before "}"
```
```kotlin
66                 activateClickEvent(getModel())
67             }
68 
69         }.models = exportItemBeans
!!                                   ^ error
70 
71 
72     }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:80:41
```
Unexpected blank line(s) before "}"
```
```kotlin
77                 BangumiFollowLogActivity.actionStart(this)
78             }
79 
80             ExportItemEnum.VideoLog -> {
!!                                         ^ error
81 
82             }
83         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:92:10
```
Unexpected blank line(s) before "}"
```
```kotlin
89                 .with(context)
90                 .hostAndPath(ARouterAddress.LogExportActivity)
91                 .forward { }
92         }
!!          ^ error
93 
94     }
95 }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExcelUtils.kt:32:6
```
Unexpected blank line(s) before "}"
```
```kotlin
29     ): WritableSheet {
30         this.addCell(Label(column, row, content, st))
31         return this
32     }
!!      ^ error
33 
34 
35 }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:49:6
```
Unexpected blank line(s) before "}"
```
```kotlin
46 
47             return bottomSheetDialog
48         }
49     }
!!      ^ error
50 
51 }

```

### formatting, NoConsecutiveBlankLines (7)

Reports consecutive blank lines

[Documentation](https://detekt.dev/docs/rules/formatting#noconsecutiveblanklines)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:56:10
```
Needless blank line(s)
```
```kotlin
53                 "",
54                 "https://s1.ax1x.com/2023/02/06/pS6OIfg.png"
55             )
56         )
!!          ^ error
57 
58 
59         binding.logExportHomeRv.linear().setup {

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:69:35
```
Needless blank line(s)
```
```kotlin
66                 activateClickEvent(getModel())
67             }
68 
69         }.models = exportItemBeans
!!                                   ^ error
70 
71 
72     }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExcelUtils.kt:8:20
```
Needless blank line(s)
```
```kotlin
5  import jxl.write.Label
6  import jxl.write.WritableSheet
7  import jxl.write.WritableWorkbook
8  import java.io.File
!                     ^ error
9  
10 
11 object ExcelUtils {

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExcelUtils.kt:21:6
```
Needless blank line(s)
```
```kotlin
18         }
19 
20         return Workbook.createWorkbook(file)
21     }
!!      ^ error
22 
23 
24     fun WritableSheet.addCell(

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExcelUtils.kt:32:6
```
Needless blank line(s)
```
```kotlin
29     ): WritableSheet {
30         this.addCell(Label(column, row, content, st))
31         return this
32     }
!!      ^ error
33 
34 
35 }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:9:46
```
Needless blank line(s)
```
```kotlin
6  import android.view.View
7  import android.widget.TextView
8  import com.google.android.material.bottomsheet.BottomSheetDialog
9  import com.imcys.bilibilias.tool_log_export.R
!                                               ^ error
10 
11 
12 /**

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:35:10
```
Needless blank line(s)
```
```kotlin
32             //mDialogBehavior.peekHeight = 600
33 
34             return initBottomSheetDialog(context, view)
35         }
!!          ^ error
36 
37 
38         private fun initBottomSheetDialog(context: Context, view: View): BottomSheetDialog {

```

### formatting, NoEmptyFirstLineInMethodBlock (4)

Reports methods that have an empty first line.

[Documentation](https://detekt.dev/docs/rules/formatting#noemptyfirstlineinmethodblock)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:42:29
```
First line in a method block should not be empty
```
```kotlin
39 
40     }
41 
42     private fun initView() {
!!                             ^ error
43 
44         loadRvData()
45     }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:80:41
```
First line in a method block should not be empty
```
```kotlin
77                 BangumiFollowLogActivity.actionStart(this)
78             }
79 
80             ExportItemEnum.VideoLog -> {
!!                                         ^ error
81 
82             }
83         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExcelUtils.kt:13:52
```
First line in a method block should not be empty
```
```kotlin
10 
11 object ExcelUtils {
12 
13     fun initExcel(path: String): WritableWorkbook {
!!                                                    ^ error
14 
15         val file = File(path)
16         if (!file.exists()) {

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:38:93
```
First line in a method block should not be empty
```
```kotlin
35         }
36 
37 
38         private fun initBottomSheetDialog(context: Context, view: View): BottomSheetDialog {
!!                                                                                             ^ error
39 
40             val bottomSheetDialog = BottomSheetDialog(
41                 context,

```

### formatting, PackageName (10)

Checks package name is formatted correctly

[Documentation](https://detekt.dev/docs/rules/formatting#packagename)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/base/activity/LogExportBaseActivity.kt:1:1
```
Package name must not contain underscore
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.base.activity
! ^ error
2 
3 import android.os.Bundle
4 import com.imcys.bilibilias.common.base.AbsActivity

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:1:1
```
Package name must not contain underscore
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.mEnum
! ^ error
2 
3 enum class BangumiFollowLogHeader {
4     Title,//标题

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/ExportItemEnum.kt:1:1
```
Package name must not contain underscore
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.mEnum
! ^ error
2 
3 enum class ExportItemEnum {
4     BangumiFollowLog,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/BangumiFollowLogHeaderBean.kt:1:1
```
Package name must not contain underscore
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.model
! ^ error
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.BangumiFollowLogHeader
4 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/ExportItemBean.kt:1:1
```
Package name must not contain underscore
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.model
! ^ error
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum
4 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:1:1
```
Package name must not contain underscore
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.ui.activity
! ^ error
2 
3 import android.content.Context
4 import android.content.Intent

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:1:1
```
Package name must not contain underscore
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.ui.activity
! ^ error
2 
3 import android.content.Context
4 import android.os.Bundle

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExcelUtils.kt:1:1
```
Package name must not contain underscore
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.utils
! ^ error
2 
3 import jxl.Workbook
4 import jxl.format.CellFormat

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:1:1
```
Package name must not contain underscore
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.utils
! ^ error
2 
3 import android.annotation.SuppressLint
4 import android.content.Context

```

* E:/Github-learning/bilibilias/tool_log_export/src/test/java/com/imcys/bilibilias/tool_log_export/ExampleUnitTest.kt:1:1
```
Package name must not contain underscore
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export
! ^ error
2 
3 import org.junit.Assert.assertEquals
4 import org.junit.Test

```

### formatting, SpacingAroundComma (11)

Reports spaces around commas

[Documentation](https://detekt.dev/docs/rules/formatting#spacingaroundcomma)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:4:10
```
Missing spacing after ","
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.mEnum
2 
3 enum class BangumiFollowLogHeader {
4     Title,//标题
!          ^ error
5     Evaluate,//评价
6     SeasonID,//SSID
7     Summary,//简介

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:5:13
```
Missing spacing after ","
```
```kotlin
2 
3 enum class BangumiFollowLogHeader {
4     Title,//标题
5     Evaluate,//评价
!             ^ error
6     SeasonID,//SSID
7     Summary,//简介
8     Subtitle,//短简介

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:6:13
```
Missing spacing after ","
```
```kotlin
3  enum class BangumiFollowLogHeader {
4      Title,//标题
5      Evaluate,//评价
6      SeasonID,//SSID
!              ^ error
7      Summary,//简介
8      Subtitle,//短简介
9      Subtitle14,//14字短简介

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:7:12
```
Missing spacing after ","
```
```kotlin
4      Title,//标题
5      Evaluate,//评价
6      SeasonID,//SSID
7      Summary,//简介
!             ^ error
8      Subtitle,//短简介
9      Subtitle14,//14字短简介
10     TotalCount,//总集数

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:8:13
```
Missing spacing after ","
```
```kotlin
5      Evaluate,//评价
6      SeasonID,//SSID
7      Summary,//简介
8      Subtitle,//短简介
!              ^ error
9      Subtitle14,//14字短简介
10     TotalCount,//总集数
11     Progress,//观看进度

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:9:15
```
Missing spacing after ","
```
```kotlin
6      SeasonID,//SSID
7      Summary,//简介
8      Subtitle,//短简介
9      Subtitle14,//14字短简介
!                ^ error
10     TotalCount,//总集数
11     Progress,//观看进度
12     Cover,//封面海报

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:10:15
```
Missing spacing after ","
```
```kotlin
7      Summary,//简介
8      Subtitle,//短简介
9      Subtitle14,//14字短简介
10     TotalCount,//总集数
!!               ^ error
11     Progress,//观看进度
12     Cover,//封面海报
13     SeasonTitle,//季度信息

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:11:13
```
Missing spacing after ","
```
```kotlin
8      Subtitle,//短简介
9      Subtitle14,//14字短简介
10     TotalCount,//总集数
11     Progress,//观看进度
!!             ^ error
12     Cover,//封面海报
13     SeasonTitle,//季度信息
14     SeasonTypeName,//创作类型,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:12:10
```
Missing spacing after ","
```
```kotlin
9      Subtitle14,//14字短简介
10     TotalCount,//总集数
11     Progress,//观看进度
12     Cover,//封面海报
!!          ^ error
13     SeasonTitle,//季度信息
14     SeasonTypeName,//创作类型,
15 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:13:16
```
Missing spacing after ","
```
```kotlin
10     TotalCount,//总集数
11     Progress,//观看进度
12     Cover,//封面海报
13     SeasonTitle,//季度信息
!!                ^ error
14     SeasonTypeName,//创作类型,
15 
16 }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:14:19
```
Missing spacing after ","
```
```kotlin
11     Progress,//观看进度
12     Cover,//封面海报
13     SeasonTitle,//季度信息
14     SeasonTypeName,//创作类型,
!!                   ^ error
15 
16 }

```

### impure, LoopDefinition (1)

Shouldn't use loops in functional code

[Documentation](https://detekt.dev/docs/rules/impure#loopdefinition)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:187:13
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains loop.
```
```kotlin
184             val loadDialog =
185                 ExportDialogUtils.loadDialog(this@BangumiFollowLogActivity, loadTitle.value)
186                     .apply { show() }
187             for (i in 1..totalPage) {
!!!             ^ error
188                 loadTitle.value = "正在获取第${i}页"
189                 launchIO { val bangumiFollowList = getBangumiFollowList(i) }
190                 loadTitle.value = "正在存储第${i}页"

```

### impure, ReturnStatement (5)

Pure function shouldn't have return statement

[Documentation](https://detekt.dev/docs/rules/impure#returnstatement)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:316:9
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `return` statement.
```
```kotlin
313     }
314 
315     private suspend fun getBangumiFollowList(pn: Int): BangumiFollowList {
316         return KtHttpUtils.addHeader(COOKIE, asUser.cookie)
!!!         ^ error
317             .asyncGet("${BilibiliApi.bangumiFollowPath}?vmid=${asUser.mid}&type=1&pn=$pn&ps=30")
318     }
319 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExcelUtils.kt:20:9
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\utils\ExcelUtils.kt contains `return` statement.
```
```kotlin
17             file.createNewFile()
18         }
19 
20         return Workbook.createWorkbook(file)
!!         ^ error
21     }
22 
23 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExcelUtils.kt:31:9
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\utils\ExcelUtils.kt contains `return` statement.
```
```kotlin
28         st: CellFormat
29     ): WritableSheet {
30         this.addCell(Label(column, row, content, st))
31         return this
!!         ^ error
32     }
33 
34 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:34:13
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\utils\ExportDialogUtils.kt contains `return` statement.
```
```kotlin
31             //自定义方案
32             //mDialogBehavior.peekHeight = 600
33 
34             return initBottomSheetDialog(context, view)
!!             ^ error
35         }
36 
37 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:47:13
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\utils\ExportDialogUtils.kt contains `return` statement.
```
```kotlin
44             //设置布局
45             bottomSheetDialog.setContentView(view)
46 
47             return bottomSheetDialog
!!             ^ error
48         }
49     }
50 

```

### impure, VariableDefinition (15)

Variables shouldn't be used in pure code

[Documentation](https://detekt.dev/docs/rules/impure#variabledefinition)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:53:5
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
50 
51 class BangumiFollowLogActivity : LogExportBaseActivity() {
52 
53     private lateinit var bangumiFollowList: BangumiFollowList
!!     ^ error
54 
55     lateinit var binding: ActivityBangumiFollowLogBinding
56 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:55:5
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
52 
53     private lateinit var bangumiFollowList: BangumiFollowList
54 
55     lateinit var binding: ActivityBangumiFollowLogBinding
!!     ^ error
56 
57     var selectedLogHeaders = mutableListOf<BangumiFollowLogHeaderBean>()
58 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:57:5
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
54 
55     lateinit var binding: ActivityBangumiFollowLogBinding
56 
57     var selectedLogHeaders = mutableListOf<BangumiFollowLogHeaderBean>()
!!     ^ error
58 
59     private val defaultLogHeaders by lazy {
60         mutableListOf(

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:220:25
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
217             selectedLogHeaders.forEachIndexed { selectIndex, bangumiFollowLogHeaderBean ->
218                 when (bangumiFollowLogHeaderBean.harder) {
219                     Title -> {
220                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
221                         bangumiFollowList.data.list.forEach {
222                             addCell(selectIndex, row++, it?.title ?: "", arial10format)
223                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:227:25
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
224                     }
225 
226                     SeasonID -> {
227                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
228                         bangumiFollowList.data.list.forEach {
229                             addCell(
230                                 selectIndex,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:239:25
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
236                     }
237 
238                     Evaluate -> {
239                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
240                         bangumiFollowList.data.list.forEach {
241                             addCell(selectIndex, row++, it?.evaluate ?: "", arial10format)
242                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:246:25
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
243                     }
244 
245                     Summary -> {
246                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
247                         bangumiFollowList.data.list.forEach {
248                             addCell(selectIndex, row++, it?.summary ?: "", arial10format)
249                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:253:25
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
250                     }
251 
252                     TotalCount -> {
253                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
254                         bangumiFollowList.data.list.forEach {
255                             addCell(
256                                 selectIndex,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:265:25
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
262                     }
263 
264                     Progress -> {
265                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
266                         bangumiFollowList.data.list.forEach {
267                             addCell(selectIndex, row++, it?.progress ?: "", arial10format)
268                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:272:25
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
269                     }
270 
271                     Cover -> {
272                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
273                         bangumiFollowList.data.list.forEach {
274                             addCell(selectIndex, row++, it?.cover ?: "", arial10format)
275                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:279:25
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
276                     }
277 
278                     SeasonTitle -> {
279                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
280                         bangumiFollowList.data.list.forEach {
281                             addCell(selectIndex, row++, it?.season_title ?: "", arial10format)
282                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:286:25
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
283                     }
284 
285                     Subtitle -> {
286                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
287                         bangumiFollowList.data.list.forEach {
288                             addCell(selectIndex, row++, it?.subtitle ?: "", arial10format)
289                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:293:25
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
290                     }
291 
292                     Subtitle14 -> {
293                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
294                         bangumiFollowList.data.list.forEach {
295                             addCell(selectIndex, row++, it?.subtitle_14 ?: "", arial10format)
296                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:300:25
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\BangumiFollowLogActivity.kt contains `var`iable.
```
```kotlin
297                     }
298 
299                     SeasonTypeName -> {
300                         var row = if (i == 0) i + 1 else 30 * i
!!!                         ^ error
301                         bangumiFollowList.data.list.forEachIndexed { index, listBean ->
302                             addCell(
303                                 selectIndex,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:25:5
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\LogExportActivity.kt contains `var`iable.
```
```kotlin
22 )
23 class LogExportActivity : LogExportBaseActivity() {
24 
25     lateinit var binding: ActivityLogExportBinding
!!     ^ error
26 
27     override fun onCreate(savedInstanceState: Bundle?) {
28         super.onCreate(savedInstanceState)

```

### libraries, ForbiddenPublicDataClass (2)

The data classes are bad for the binary compatibility in public APIs. Avoid to use it.

[Documentation](https://detekt.dev/docs/rules/libraries#forbiddenpublicdataclass)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/BangumiFollowLogHeaderBean.kt:5:12

```kotlin
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.BangumiFollowLogHeader
4 
5 data class BangumiFollowLogHeaderBean(
!            ^ error
6     val title: String,
7     val harder: BangumiFollowLogHeader,
8 )

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/ExportItemBean.kt:5:12

```kotlin
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum
4 
5 data class ExportItemBean(
!            ^ error
6     var itemType: ExportItemEnum,
7     var title: String,
8     var longTitle: String,

```

### libraries, LibraryEntitiesShouldNotBePublic (9)

Library classes should not be public.

[Documentation](https://detekt.dev/docs/rules/libraries#libraryentitiesshouldnotbepublic)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/base/activity/LogExportBaseActivity.kt:6:1
```
Class LogExportBaseActivity should not be public
```
```kotlin
3  import android.os.Bundle
4  import com.imcys.bilibilias.common.base.AbsActivity
5  
6  open class LogExportBaseActivity : AbsActivity() {
!  ^ error
7      override fun onCreate(savedInstanceState: Bundle?) {
8          super.onCreate(savedInstanceState)
9          // 沉浸式

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:3:1
```
Class BangumiFollowLogHeader should not be public
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.mEnum
2 
3 enum class BangumiFollowLogHeader {
! ^ error
4     Title,//标题
5     Evaluate,//评价
6     SeasonID,//SSID

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/ExportItemEnum.kt:3:1
```
Class ExportItemEnum should not be public
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.mEnum
2 
3 enum class ExportItemEnum {
! ^ error
4     BangumiFollowLog,
5     VideoLog,
6 }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/BangumiFollowLogHeaderBean.kt:5:1
```
Class BangumiFollowLogHeaderBean should not be public
```
```kotlin
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.BangumiFollowLogHeader
4 
5 data class BangumiFollowLogHeaderBean(
! ^ error
6     val title: String,
7     val harder: BangumiFollowLogHeader,
8 )

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/ExportItemBean.kt:5:1
```
Class ExportItemBean should not be public
```
```kotlin
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum
4 
5 data class ExportItemBean(
! ^ error
6     var itemType: ExportItemEnum,
7     var title: String,
8     var longTitle: String,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:51:1
```
Class BangumiFollowLogActivity should not be public
```
```kotlin
48 import jxl.write.WritableSheet
49 import kotlin.math.ceil
50 
51 class BangumiFollowLogActivity : LogExportBaseActivity() {
!! ^ error
52 
53     private lateinit var bangumiFollowList: BangumiFollowList
54 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:20:1
```
Class LogExportActivity should not be public
```
```kotlin
17 import com.xiaojinzi.component.impl.Router
18 import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
19 
20 @RouterAnno(
!! ^ error
21     hostAndPath = ARouterAddress.LogExportActivity,
22 )
23 class LogExportActivity : LogExportBaseActivity() {

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:12:1
```
Class ExportDialogUtils should not be public
```
```kotlin
9  import com.imcys.bilibilias.tool_log_export.R
10 
11 
12 /**
!! ^ error
13  * 导出
14  */
15 class ExportDialogUtils {

```

* E:/Github-learning/bilibilias/tool_log_export/src/test/java/com/imcys/bilibilias/tool_log_export/ExampleUnitTest.kt:6:1
```
Class ExampleUnitTest should not be public
```
```kotlin
3  import org.junit.Assert.assertEquals
4  import org.junit.Test
5  
6  /**
!  ^ error
7   * Example local unit test, which will execute on the development machine (host).
8   *
9   * See [testing documentation](http://d.android.com/tools/testing).

```

### naming, PackageNaming (10)

Package names should match the naming convention set in the configuration.

[Documentation](https://detekt.dev/docs/rules/naming#packagenaming)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/base/activity/LogExportBaseActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.base.activity
! ^ error
2 
3 import android.os.Bundle
4 import com.imcys.bilibilias.common.base.AbsActivity

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.mEnum
! ^ error
2 
3 enum class BangumiFollowLogHeader {
4     Title,//标题

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/ExportItemEnum.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.mEnum
! ^ error
2 
3 enum class ExportItemEnum {
4     BangumiFollowLog,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/BangumiFollowLogHeaderBean.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.model
! ^ error
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.BangumiFollowLogHeader
4 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/ExportItemBean.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.data.model
! ^ error
2 
3 import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum
4 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.ui.activity
! ^ error
2 
3 import android.content.Context
4 import android.content.Intent

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.ui.activity
! ^ error
2 
3 import android.content.Context
4 import android.os.Bundle

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExcelUtils.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.utils
! ^ error
2 
3 import jxl.Workbook
4 import jxl.format.CellFormat

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export.utils
! ^ error
2 
3 import android.annotation.SuppressLint
4 import android.content.Context

```

* E:/Github-learning/bilibilias/tool_log_export/src/test/java/com/imcys/bilibilias/tool_log_export/ExampleUnitTest.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.imcys.bilibilias.tool_log_export
! ^ error
2 
3 import org.junit.Assert.assertEquals
4 import org.junit.Test

```

### style, MagicNumber (17)

Report magic numbers. Magic number is a numeric literal that is not defined as a constant and hence it's unclear what the purpose of this number is. It's better to declare such numbers as constants and give them a proper name. By default, -1, 0, 1, and 2 are not considered to be magic numbers.

[Documentation](https://detekt.dev/docs/rules/style#magicnumber)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:147:38
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
144     private fun createExcel() {
145         // 获取追番总数和分页数（目前每页30条记录）
146         val total = bangumiFollowList.data.total
147         val totalPage = ceil(total / 30.0).toInt()
!!!                                      ^ error
148 
149         val path = "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/追番.xls"
150 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:152:60
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
149         val path = "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/追番.xls"
150 
151         // 设置表头的字体大小和背景颜色
152         val arial14font = WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD)
!!!                                                            ^ error
153         arial14font.colour = jxl.format.Colour.LIGHT_BLUE
154         val arial14format = WritableCellFormat(arial14font)
155         // 居中方式

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:166:29
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
163         // 设置工作簿
164         val sheet = workbook.createSheet("追番", 0)
165         // 行高
166         sheet.setRowView(0, 340)
!!!                             ^ error
167 
168         // 创建标题栏
169         selectedLogHeaders.forEachIndexed { index, bangumiFollowLogHeaderBean ->

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:179:40
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
176                 ) as WritableCell?,
177             )
178             // 列宽
179             sheet.setColumnView(index, 300)
!!!                                        ^ error
180         }
181 
182         launchUI {

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:213:64
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
210      */
211     private fun editExcel(bangumiFollowList: BangumiFollowList, sheet: WritableSheet, i: Int) {
212         sheet.apply {
213             val arial14font = WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD)
!!!                                                                ^ error
214             val arial10format = WritableCellFormat(arial14font)
215             arial10format.alignment = Alignment.CENTRE
216 

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:220:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
217             selectedLogHeaders.forEachIndexed { selectIndex, bangumiFollowLogHeaderBean ->
218                 when (bangumiFollowLogHeaderBean.harder) {
219                     Title -> {
220                         var row = if (i == 0) i + 1 else 30 * i
!!!                                                          ^ error
221                         bangumiFollowList.data.list.forEach {
222                             addCell(selectIndex, row++, it?.title ?: "", arial10format)
223                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:227:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
224                     }
225 
226                     SeasonID -> {
227                         var row = if (i == 0) i + 1 else 30 * i
!!!                                                          ^ error
228                         bangumiFollowList.data.list.forEach {
229                             addCell(
230                                 selectIndex,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:239:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
236                     }
237 
238                     Evaluate -> {
239                         var row = if (i == 0) i + 1 else 30 * i
!!!                                                          ^ error
240                         bangumiFollowList.data.list.forEach {
241                             addCell(selectIndex, row++, it?.evaluate ?: "", arial10format)
242                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:246:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
243                     }
244 
245                     Summary -> {
246                         var row = if (i == 0) i + 1 else 30 * i
!!!                                                          ^ error
247                         bangumiFollowList.data.list.forEach {
248                             addCell(selectIndex, row++, it?.summary ?: "", arial10format)
249                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:253:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
250                     }
251 
252                     TotalCount -> {
253                         var row = if (i == 0) i + 1 else 30 * i
!!!                                                          ^ error
254                         bangumiFollowList.data.list.forEach {
255                             addCell(
256                                 selectIndex,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:265:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
262                     }
263 
264                     Progress -> {
265                         var row = if (i == 0) i + 1 else 30 * i
!!!                                                          ^ error
266                         bangumiFollowList.data.list.forEach {
267                             addCell(selectIndex, row++, it?.progress ?: "", arial10format)
268                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:272:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
269                     }
270 
271                     Cover -> {
272                         var row = if (i == 0) i + 1 else 30 * i
!!!                                                          ^ error
273                         bangumiFollowList.data.list.forEach {
274                             addCell(selectIndex, row++, it?.cover ?: "", arial10format)
275                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:279:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
276                     }
277 
278                     SeasonTitle -> {
279                         var row = if (i == 0) i + 1 else 30 * i
!!!                                                          ^ error
280                         bangumiFollowList.data.list.forEach {
281                             addCell(selectIndex, row++, it?.season_title ?: "", arial10format)
282                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:286:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
283                     }
284 
285                     Subtitle -> {
286                         var row = if (i == 0) i + 1 else 30 * i
!!!                                                          ^ error
287                         bangumiFollowList.data.list.forEach {
288                             addCell(selectIndex, row++, it?.subtitle ?: "", arial10format)
289                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:293:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
290                     }
291 
292                     Subtitle14 -> {
293                         var row = if (i == 0) i + 1 else 30 * i
!!!                                                          ^ error
294                         bangumiFollowList.data.list.forEach {
295                             addCell(selectIndex, row++, it?.subtitle_14 ?: "", arial10format)
296                         }

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/BangumiFollowLogActivity.kt:300:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
297                     }
298 
299                     SeasonTypeName -> {
300                         var row = if (i == 0) i + 1 else 30 * i
!!!                                                          ^ error
301                         bangumiFollowList.data.list.forEachIndexed { index, listBean ->
302                             addCell(
303                                 selectIndex,

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:61:29
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
58 
59         binding.logExportHomeRv.linear().setup {
60             //防抖动
61             clickThrottle = 1000 // 单位毫秒
!!                             ^ error
62             setAnimation(AnimationType.SCALE)
63             addType<ExportItemBean>(R.layout.log_export_item_export_tool)
64 

```

### style, NewLineAtEndOfFile (7)

Checks whether files end with a line separator.

[Documentation](https://detekt.dev/docs/rules/style#newlineatendoffile)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/BangumiFollowLogHeader.kt:16:2
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\data\mEnum\BangumiFollowLogHeader.kt is not ending with a new line.
```
```kotlin
13     SeasonTitle,//季度信息
14     SeasonTypeName,//创作类型,
15 
16 }
!!  ^ error

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/mEnum/ExportItemEnum.kt:6:2
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\data\mEnum\ExportItemEnum.kt is not ending with a new line.
```
```kotlin
3  enum class ExportItemEnum {
4      BangumiFollowLog,
5      VideoLog,
6  }
!   ^ error

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/data/model/ExportItemBean.kt:11:2
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\data\model\ExportItemBean.kt is not ending with a new line.
```
```kotlin
8      var longTitle: String,
9      var content: String,
10     var icoUrl: String,
11 )
!!  ^ error

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/ui/activity/LogExportActivity.kt:95:2
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\ui\activity\LogExportActivity.kt is not ending with a new line.
```
```kotlin
92         }
93 
94     }
95 }
!!  ^ error

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExcelUtils.kt:35:2
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\utils\ExcelUtils.kt is not ending with a new line.
```
```kotlin
32     }
33 
34 
35 }
!!  ^ error

```

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:51:2
```
The file E:\Github-learning\bilibilias\tool_log_export\src\main\java\com\imcys\bilibilias\tool_log_export\utils\ExportDialogUtils.kt is not ending with a new line.
```
```kotlin
48         }
49     }
50 
51 }
!!  ^ error

```

* E:/Github-learning/bilibilias/tool_log_export/src/test/java/com/imcys/bilibilias/tool_log_export/ExampleUnitTest.kt:16:2
```
The file E:\Github-learning\bilibilias\tool_log_export\src\test\java\com\imcys\bilibilias\tool_log_export\ExampleUnitTest.kt is not ending with a new line.
```
```kotlin
13     fun addition_isCorrect() {
14         assertEquals(4, 2 + 2)
15     }
16 }
!!  ^ error

```

### style, UtilityClassWithPublicConstructor (1)

The class declaration is unnecessary because it only contains utility functions. An object declaration should be used instead.

[Documentation](https://detekt.dev/docs/rules/style#utilityclasswithpublicconstructor)

* E:/Github-learning/bilibilias/tool_log_export/src/main/java/com/imcys/bilibilias/tool_log_export/utils/ExportDialogUtils.kt:12:1
```
The class ExportDialogUtils only contains utility functions. Consider defining it as an object.
```
```kotlin
9  import com.imcys.bilibilias.tool_log_export.R
10 
11 
12 /**
!! ^ error
13  * 导出
14  */
15 class ExportDialogUtils {

```

generated with [detekt version 1.23.1](https://detekt.dev/) on 2023-09-13 15:30:07 UTC
