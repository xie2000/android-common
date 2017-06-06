# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\work\android_sdk_as/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView Build JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-ignorewarnings

-keep public class **.R$* { public static final int *; }
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.view.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep class * implements android.os.Parcelable {public static final android.os.Parcelable$Creator *; }

# 继承实现此接口的不予混淆
-keep public interface xie.com.androidcommon.common.NotObfuscateInterface{public *;}
-keep class * implements xie.com.androidcommon.common.NotObfuscateInterface{
	<methods>;
	<fields>;
}