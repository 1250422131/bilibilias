# 程序设置

## 存储路径

BILIBILIAS 为大家提供了自定义存储路径的功能，你可以修改存储路径，自 2.1.0 开始，默认存储路径被转移到`存储目录/Download/BILIBILIAS`，在这里的好处是 APP 无需权限就可以自由的在 Donwload 文件夹下存放文件了，并且**手机相册可以扫描到下载的视频文件**，因此如果没有特殊需求，请不要修改。当然，如果你对存储路径比较了解，那么你可以自由的修改位置，方便你的文件管理。

<AsImage  width="40%"  src="/images/user/cookbook/settings-about/settings-save-path.png"></AsImage>

### 还原配置

如上图，你可以看到下面有两个选项

- 还原下载路径
- 还原命名规则

如果你发现修改存储路径后缓存视频出现了问题，请直接还原下载路径，这样可以解决一些简单的问题。

## 命名规则

命名规则是方便大家自定义下载后文件命名的功能，BILIBILIAS 提供了若干个变量选项支持这项功能，点击命名规则即可进行自定义，当然如果你不喜欢自己的修改也可以点击下面的还原命名规则。
<AsImage  width="40%"  src="/images/user/cookbook/settings-about/settings-file-name.png"></AsImage>

### 提供变量

点击按钮后你可以看到弹窗已经描述了支持的变量和一个小案例，你可以根据提示修改自己喜欢的命名风格。

<AsImage  width="40%"  src="/images/user/cookbook/settings-about/settings-file-name-dialog.png"></AsImage>

### 案例

::: warning
`{P_TITLE}`偶尔可能会引起视频下载失败，因为视频名称含有特殊字符，视频名称超出长度，也会导致存储失败。
:::

我们以视频 [BV1wFJhzWE2e - 因为上了 ban 位，所以数值乱填](https://www.bilibili.com/video/BV1wFJhzWE2e) 为例，让我们来看 APP 默认的例子：`{BV}/{FILE_TYPE}/{P}_{P_TITLE}_{CID}.{FILE_TYPE}`

在安卓系统中，路径带有`/`，就代表需要分一层文件夹，目前有两个`/`，意味着有 2 个文件夹。

第一个文件夹的名字用`{BV}`代替了，那么意味着这个文件夹的名字就是 BV 号，对于我们本次的案例来说就是`BV1wFJhzWE2e`。

第二个文件夹用`{FILE_TYPE}`代替，而`{FILE_TYPE}`是文件的类型，比如 mp3、mp4、m4a、xml、json 等格式，也就是说如果碰到视频就是`mp4`。

最后使用`{P}_{P_TITLE}_{CID}.{FILE_TYPE}`指代了下载文件的最终文件名，`{P}`意味着是集数，默认是 1，而`{P_TITLE}`是子集标题，注意着可能并非视频标题，而`{CID}`是视频的唯一 ID，使用`{CID}`可以方便我们区分视频。

那最终你的文件会存储到`下载路径/BV1wFJhzWE2e/mp4/1_因为上了ban位，所以数值乱填_xxxx.mp4`，这就是命名规则的作用，我希望命名规则可以方便到大家。

## 合并配置

BILIBILAIS 使用 Dash 模式缓存 B 站视频时，是会下载视频和音频两种文件，最终使用FFmpeg合并到一起。

### 合并命令

::: warning
如果你不清楚什么是FFmpeg,请不要尝试修改。
:::

为了方便大家自己调整，BILIBILIAS 提供了合并命令，你可以按照自己的需要调整，目前的合并命令并非无损合并，只是接近无损合并的命令，因为合并需求越高，时间就会越长。

<div class="sm:flex gap-4">
<AsImage  width="40%"  src="/images/user/cookbook/settings-about/settings-ffmpeg.png"></AsImage>
<AsImage  width="40%"  src="/images/user/cookbook/settings-about/settings-ffmpeg-dialog.png.png"></AsImage>
</div>

同样的，合并命令也提供了变量，用于确定输入的视频路径、音频路径以及最终合并的存储路径。

### 合并后删除原来音视频文件

你可以选择是否保留用于合并的音频和视频，此选项默认开启，如果有需要可以关闭。

