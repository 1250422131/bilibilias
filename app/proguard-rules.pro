# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

 #中文混淆
-classobfuscationdictionary ./proguard-class.txt
-packageobfuscationdictionary ./proguard-package.txt
-obfuscationdictionary ./proguard-package.txt

############################################
#
# 复制的混淆
# 对于一些基本指令的添加
#
#############################################
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*


-keep class com.zackratos.ultimatebarx.ultimatebarx.** { *; }

# 保留R下面的资源
-keep class **.R$* {*;}


-keep class com.youth.banner.** {
    *;
 }

-keepclassmembers class * {
    void *(*Event);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

#百度统计
-keep class com.baidu.mobstat.** { *; }
-keep class com.bun.miitmdid.core.** {*;}
-keep class com.baidu.bottom.** { *; }


#我们认为这些类混淆后可能将导致播放器出现问题
-keep class cn.jzvd.Jzvd{*; }
-keep class cn.jzvd.JzvdStd{*; }
-keep class com.imcys.bilibilias.common.base.view.AsJzvdStd{*; }
-keep class com.imcys.bilibilias.base.view.AppAsJzvdStd{*; }

-keep class cn.jzvd.JZUtils{*; }

-keep class cn.jzvd.**{*;}
-keep public class * extends cn.jzvd.JZMediaInterface
-keep public class * extends cn.jzvd.JzvdStd

-keep class tv.danmaku.ijk.media.player.** {*; }
-dontwarn tv.danmaku.ijk.media.player.*
-keep interface tv.danmaku.ijk.media.player.** { *; }



-keepclassmembers class * implements android.os.Parcelable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class com.imcys.bilibilias.home.ui.model.**{*;} # 自定义数据模型的bean目录
-keep class com.imcys.bilibilias.base.model.**{*;} # 自定义数据模型的bean目录
-keep class com.imcys.bilibilias.common.base.model.**{*;} # 自定义数据模型的bean目录
-keep class com.imcys.bilibilias.tool_log_export.data.**{*;} # 自定义数据模型的bean目录

-dontwarn io.microshow.rxffmpeg.**
-keep class io.microshow.rxffmpeg.**{*;}


-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}

-keep class com.baidu.mobstat.** { *; }
-keep class com.bun.miitmdid.core.** {*;}


#-keep class com.baidu.helios.OnGetIdResultCallback
#-keep class com.squareup.picasso.Picasso
#-keep class com.squareup.picasso.RequestCreator
#-keep class edu.umd.cs.findbugs.annotations.SuppressFBWarnings
#-keep class org.bouncycastle.jsse.BCSSLParameters
#-keep class org.bouncycastle.jsse.BCSSLSocket
#-keep class org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
#-keep class org.conscrypt.Conscrypt$Version
#-keep class org.conscrypt.Conscrypt
#-keep class org.conscrypt.ConscryptHostnameVerifier
#-keep class org.openjsse.javax.net.ssl.SSLParameters
#-keep class org.openjsse.javax.net.ssl.SSLSocket
#-keep class org.openjsse.net.ssl.OpenJSSE
#-keep class org.slf4j.impl.StaticLoggerBinder


# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider

-dontwarn com.baidu.helios.OnGetIdResultCallback
-dontwarn com.squareup.picasso.Picasso
-dontwarn com.squareup.picasso.RequestCreator
-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn com.imcys.bilibilias.home.ui.model.ArchiveCoinsBean$DataBean
-dontwarn com.imcys.bilibilias.home.ui.model.ArchiveFavouredBean$DataBean