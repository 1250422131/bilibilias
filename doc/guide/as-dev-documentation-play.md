
# 内置播放器

BILIBILIAS内部播放器采用了[饺子播放器](https://github.com/Jzvd/JZVideo),显然这一个播放器并不能满足我们的需求，因为我们有弹幕需要播放，因此又在此基础上添加了[烈焰弹幕使](https://github.com/bilibili/DanmakuFlameMaster)，
下面将阐述下现在BILIBILIAS的播放器[AsJzvdStd](https://github.com/1250422131/bilibilias/blob/45a18de2d405a04e62952957e489a39852e4272c/common/src/main/java/com/imcys/bilibilias/common/base/view/AsJzvdStd.kt)

需要注意的是[AsJzvdStd](https://github.com/1250422131/bilibilias/blob/45a18de2d405a04e62952957e489a39852e4272c/common/src/main/java/com/imcys/bilibilias/common/base/view/AsJzvdStd.kt)中提供了一些基础性的功能，各个模块应当制定属于自己的的播放器，比如[AppAsJzvdStd](https://github.com/1250422131/bilibilias/blob/45a18de2d405a04e62952957e489a39852e4272c/app/src/main/java/com/imcys/bilibilias/base/view/AppAsJzvdStd.kt)，这个AppAsJzvdStd我们随后会讲到。  



## 功能概览

### 拓展功能

我们拓展了一些简单的功能在饺子播放器的基础之上。

- 支持弹幕开启和关闭
- 视频滚动弹幕也跟随跳转
- 自定义视频加载海报
- 更新视频封面
- 下载视频封面
- 横竖屏视频不同全屏模式

## 拓展方法

### 扩展接口

```kotlin
interface JzbdStdInfo {
    //视频开始播放（视频已经在播放了，暂停播放也会换起）
    fun statePlaying(state: Int)
    //视频暂停播放
    fun stopPlay(state: Int)
    //视频播放结束
    fun endPlay(state: Int)
    //视频进度被拖动
    fun seekBarStopTracking(state: Int)
}
```

这些接口是扩展自饺子播放器的，因为饺子播放器有部分接口并未暴露，需要我们继承后进行处理，因此BILIBILIAS新增了一组接口，用来填补这部分空缺。

利用这些接口，你可以做许多事情。

### 更新视频封面

```kotlin
    fun updatePoster(url: String) {
        posterImageUrl = url
        Glide.with(this.context)
            .load(url)
            .into(this.posterImageView)
    }
```
通过调用该方法可以更新当前视频未播放时的海报



### 改变竖屏视频全屏方案
我们覆写了饺子播放器中的`gotoFullscreen()`方法，在该方法中加了一则判断。

```kotlin
    override fun gotoFullscreen() {
        //...............
        //通过判断宽高来确定是不是横向视频
        if (isHorizontalAsVideo()) {
            JZUtils.setRequestedOrientation(jzvdContext, FULLSCREEN_ORIENTATION)
        } else {
            JZUtils.setRequestedOrientation(jzvdContext, NORMAL_ORIENTATION)

        }
      //...............
    }

    private fun isHorizontalAsVideo(): Boolean {
        return width > height
    }
```
通过该方法判断横竖屏之后改变全屏方案，这样就能让竖屏视频也有很好的全屏模式了。

### 下载视频封面

我们封装了一个简单下载封面图片的方法`clickPicDownload()`

代码如下，假如你想在这里做动作，那么就可以考虑在这块修改
```kotlin
    private fun clickPicDownload() {
        if (posterImageUrl != "") {
            downloadPic()
        }
    }

```

在页面`onClick`方法中我们会判断是否点击了`as_jzvdstd_pic_dl_bt`按钮，假如被点击了那么就执行`clickPicDownload()`，当然在下载后我们会通知相册更新这张图片，以便系统发现它。


## DataBinding支持

尽管我们没有对AsJzvdStd进行databinding迁移，但是我们仍然提供了一个adapter，帮我们简化一些数据绑定流程。


```xml
    <com.imcys.bilibilias.base.view.AppAsJzvdStd
        android:transitionName="videoPic"
        android:id="@+id/as_video_asJzvdStd"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:imageUrl="@{VideoBaseBean.data.pic}"
        app:layout_collapseMode="parallax"
        app:showPlayButton="false" />
```


如你所见，接受一个imageUrl属性，这并不是普通的自定义属性，而这个属性是被databinding处理的。

如果你使用过databinding就明白，使用**BindingAdapter**这个注解，可以为组件添加一个自定义属性，[AsJzvdStdAdapter](https://github.com/1250422131/bilibilias/blob/45a18de2d405a04e62952957e489a39852e4272c/common/src/main/java/com/imcys/bilibilias/common/base/adapter/AsJzvdStdAdapter.java)就是这样诞生的。

```java
public class AsJzvdStdAdapter {

    @BindingAdapter({"android:imageUrl"})
    public static void loadImage(AsJzvdStd asJzvdStd, String url) {
        asJzvdStd.setPosterImageUrl(url);
        Glide.with(asJzvdStd.getContext())
                .load(url)
                .into(asJzvdStd.posterImageView);
    }
}
```
是一段比较常规的代码，我们利用setPosterImageUrl的方法设置了posterImageUrl这个属性，并且为`posterImageView`设置了图片。




## AppAsJzvdStd

[AppAsJzvdStd](https://github.com/1250422131/bilibilias/blob/45a18de2d405a04e62952957e489a39852e4272c/app/src/main/java/com/imcys/bilibilias/base/view/AppAsJzvdStd.kt)它可以说是进一步定制的饺子播放器，它继承自AsJzvdStd，App代表它服务于APP模块。

### 拓展能力

- 支持显示/关闭弹幕按钮
- 新增视频加载等待动画
- 修改原本的视频播放器图标样式


### 布局改动

**AppAsJzvdStd**重点强调了一些用户体验，因此我们正在布局上也有一些调整，改变后的布局为`app_as_jz_layout_std`。

```kotlin
    override fun getLayoutId(): Int {
        return R.layout.app_as_jz_layout_std
    }
```

当然这是有代价的，我们改变了组件id，这会导致原本的饺子播放器有一部分ID无法找到，无法正确响应事件，因此我们需要重新对事件进行绑定。

```kotlin

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (!isInEditMode) {
            //弹幕按钮事件绑定
            bingAppAsJzStdDanmakuButtonEvent()
            //绑定播放按钮事件
            bindingAppAsJzStdPlayButtonEvent()
        }
    }

```

### 弹幕显示/隐藏切换

这里的话我们不仅仅需要改变弹幕状态，而且需要记住这个状态。并且，假如上一次是已经关闭了弹幕，那么这一次就不再展示弹幕，除非用户手动开启。


``` kotlin
    private fun bingAppAsJzStdDanmakuButtonEvent() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val danmakuSwitch =
            sharedPreferences.getBoolean("user_video_danmaku_switch", true)

        if (!danmakuSwitch) {
            //隐藏弹幕
            asDanmaku.hide()
            appAsJzStdDanmakuButton.setImageResource(com.imcys.bilibilias.common.R.drawable.ic_asplay_barrage_off)
        }

        appAsJzStdDanmakuButton.setOnClickListener {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            val danmakuSwitch =
                sharedPreferences.getBoolean("user_video_danmaku_switch", true)

            sharedPreferences.edit {
                putBoolean("user_video_danmaku_switch", !danmakuSwitch)
            }
            //注意，这里是变动前的标志
            if (danmakuSwitch) {
                appAsJzStdDanmakuButton.setImageResource(com.imcys.bilibilias.common.R.drawable.ic_asplay_barrage_off)
                asDanmaku.hide()
            } else {
                appAsJzStdDanmakuButton.setImageResource(com.imcys.bilibilias.common.R.drawable.ic_asplay_barrage_on)
                asDanmaku.show()
            }
        }
    }
```

::: warning SharedPreferences即将被迁移
BILIBILIAS正在逐步过渡至使用[MMKV]()，因此这里很可能在后面发送变动。
:::


### 视频播放前加载动画

当视频加载时会出现两个卡通人物，它们收集自网络，当加载时这个函数会被调用，我们随机一个数字，并且根据结果展示不同的gif。

如果你还有其他好看的gif或者想要调整这一内容，可以考虑在这里入手。

```kotlin
    @SuppressLint("CheckResult")
    override fun onStatePreparing() {
        super.onStatePreparing()
        posterImageView.visibility = View.GONE
        asJzvdstdPosterFL.setBackgroundColor(resources.getColor(R.color.white))
        appAsJzStdLoadImage.visibility = View.VISIBLE
        Glide.with(context).asGif()
            .apply {
                when ((0..1).random()) {
                    0 -> load(com.imcys.bilibilias.common.R.drawable.ic_public_load_play_iloli_1)
                    1 -> load(com.imcys.bilibilias.common.R.drawable.ic_public_load_play_iloli_2)
                }
            }
            .into(appAsJzStdLoadImage)

    }
```