# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-libraryjars libs/pushservice-VERSION.jar
-dontwarn com.baidu.**
-keep class com.baidu.**{*; }

-keep class android.support.design.widget.** { *; }
-keep interface android.support.design.widget.** { *; }
-dontwarn android.support.design.**

-keepattributes *Annotation*
-keep public class * extends android.support.design.widget.CoordinatorLayout.Behavior { *; }
-keep public class * extends android.support.design.widget.ViewOffsetBehavior { *; }


#混淆支付宝 -libraryjars libs/alipaySdk-20160825.jar
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}


-keep class com.alipay.android.app.IAliPay{*;}
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep class com.alipay.android.app.IAliPay{*;}
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}


-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory{*;}

#Annotation混淆
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes *JavascriptInterface*

#v4包混淆
-dontwarn android.support.**
-keep class android.support.v4.** { *; }
-keep class android.support.v4.* { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment


-dontwarn com.unionpay.**
-keep class com.unionpay.** { *;}

-keepclassmembers class com.ewgvip.buyer.android.fragment.IndexFragment {
  public *;
}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#-keep   class com.amap.api.location.**{*;}
#-keep   class com.aps.**{*;}
#
#-keep class com.amap.api.maps2d.**{*;}
#-keep class com.amap.api.mapcore2d.**{*;}
#-libraryjars   libs/Android_Location_V1.1.0.jar
#
#-libraryjars   libs/Android_Map_2.2.0.jar

-dontwarn com.amap.api.**
-keep class com.amap.api.**  {*;}


#bugly混淆
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#okhttp 混淆
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**

#okhttp3混淆
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keep interface com.squareup.okhttp3.** { *; }

#retrofit2混淆
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions


#rxjava混淆
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


#微信
-keep class com.tencent.mm.sdk.** {
   *;
}

#新浪微博混淆
-keep class com.sina.weibo.sdk.* { *; }

#腾讯混淆
-keep class com.tencent.* { *; }

-keep class com.baidu.* { *; }
-keep class lombok.ast.ecj.* { *; }
-dontwarn android.support.v4.**
-dontwarn com.tencent.**s
-dontwarn com.baidu.**
-dontwarn com.tencent.**


# Gson specific classes混淆
-keep class sun.misc.Unsafe { *; }

#okio混淆
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }


#butterknife混淆
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


#facebook 混淆
-dontwarn com.facebook.**
-keep class com.facebook.** { *;}
-keep class com.facebook.stetho.** { *; }
-dontwarn org.mozilla.javascript.**
-dontwarn org.mozilla.classfile.**
-keep class org.mozilla.javascript.** { *; }


#Serializable和Parcelable 混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable {*;}
-keepclassmembers class * implements android.os.Parcelable {
 public <fields>;
 private <fields>;
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#webview混淆
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, java.lang.String);
}


#org.apache.http.legacy 混淆
-dontwarn android.net.compatibility.**
-dontwarn android.net.http.**
-dontwarn android.net.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-keep class android.net.compatibility.**{*;}
-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.http.**{*;}
-keep class android.net.SSLCertificateSocketFactory{*;}



#LRecyclerview_library  混淆
-dontwarn com.github.jdsjlzx.**
-keep class com.github.jdsjlzx.**{*;}


#glide+okhttp3
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule

# deal for retrolambda
-dontwarn java.lang.invoke.*


