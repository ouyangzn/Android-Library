/*
 * Copyright (c) 2016.  ouyangzn   <ouyangzn@163.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ouyangzn.lib.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import java.util.List;

/**
 * 跟App相关的辅助类,<pre>eg:获取app名、获取app版本号等</pre>
 *
 * @author ouyangzn
 */
public class AppUtils {

  private static final String TAG = AppUtils.class.getSimpleName();

  private AppUtils() {
    throw new UnsupportedOperationException("cannot be instantiated");
  }

  /**
   * 获取app名
   *
   * @param context
   * @return 当前app名
   */
  public static String getAppName(Context context) {
    PackageInfo packageInfo = getPackageManager(context);
    if (packageInfo != null) {
      int labelRes = packageInfo.applicationInfo.labelRes;
      return context.getResources().getString(labelRes);
    }
    return null;
  }

  /**
   * 获取应用versionName信息
   *
   * @param context
   * @return 当前应用的版本名称
   */
  public static String getVersionName(Context context) {
    PackageInfo packageInfo = getPackageManager(context);
    if (packageInfo != null) {
      return packageInfo.versionName;
    }
    return null;
  }

  /**
   * 获取应用versionCode信息
   *
   * @param context
   * @return 当前应用的版本名称
   */
  public static int getVersionCode(Context context) {
    PackageInfo packageInfo = getPackageManager(context);
    if (packageInfo != null) {
      return packageInfo.versionCode;
    }
    return 0;
  }

  /**
   * 获取包信息
   *
   * @param context
   * @return 如果找不到此包名对应的信息则返回null, 否则返回PackageInfo
   */
  public static PackageInfo getPackageManager(Context context) {
    try {
      return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    } catch (NameNotFoundException e) {
      Log.e(TAG, "获取包信息出错", e);
      return null;
    }
  }

  /**
   * app是否已到后台
   *
   * @param context
   * @return 在后台返回true
   */
  public static boolean isBackground(Context context) {
    ActivityManager activityManager =
        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> appProcesses =
        activityManager.getRunningAppProcesses();
    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
      if (appProcess.processName.equals(context.getPackageName())) {
        if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
          Log.d(TAG, "前台" + appProcess.processName);
          return false;
        } else {
          Log.d(TAG, "后台" + appProcess.processName);
          return true;
        }
      }
    }
    return true;
  }
}
