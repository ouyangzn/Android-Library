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

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import java.io.File;

/**
 * sdcard工具类
 *
 * @author ouyangzn
 */
public class SDCardUtils {
  //    /** app的根目录文件夹名-根据不同项目或公司自行设置 */
  //    private final static String ROOT_PATH_NAME = "";
  //
  //    /** 更新时下载的安装包 */
  //    public static final String APK = "apk";
  //    /** 异常日志保存的目录 */
  //    public static final String CRASH = "crash";
  //    /** 图片 */
  //    public static final String IMAGE = "image";
  //    /** 企业（医院）logo */
  //    public static final String ENT_LOGO = "logo";
  //    /** 语音 */
  //    public static final String AUDIO = "audio";
  //    /** 头像 */
  //    public static final String HEAD = "head";
  //    /** 实名认证图片 */
  //    public static final String AUTH = "auth";
  //    /** 二维码图片 */
  //    public static final String QR_CODE = "qrCode";

  private SDCardUtils() {
    throw new UnsupportedOperationException("cannot be instantiated");
  }

  /**
   * 判断sd卡是否已挂载
   */
  public static boolean hasSdcard() {
    String status = Environment.getExternalStorageState();
    if (status.equals(Environment.MEDIA_MOUNTED)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 获取sd卡的路径
   */
  public static String getSDCardPath() {
    if (hasSdcard()) {
      return Environment.getExternalStorageDirectory().getAbsolutePath();
    } else {
      return null;
    }
  }

  /**
   * 获取内存可用空间
   */
  public static long getAvailableLength(Context context) {
    StatFs stat = null;
    if (hasSdcard()) {
      stat = new StatFs(getSDCardPath());
    } else {
      stat = new StatFs(context.getFilesDir().getPath());
    }
    // 获取空闲的数据块的数量
    long availableBlocks = (long) stat.getAvailableBlocks() - 4;
    // 获取单个数据块的大小（byte）
    long blockLength = stat.getBlockSize();
    return blockLength * availableBlocks;
  }

  /**
   * 获取系统存储路径
   */
  public static String getRootDirectoryPath() {
    return Environment.getRootDirectory().getAbsolutePath();
  }

  /**
   * 创建文件夹
   *
   * @param path 路径
   */
  public static void mkDirs(String path) {
    File file = new File(path);
    if (!file.exists()) {
      file.mkdirs();
    }
  }

  //    /**
  //     * 获取本应用的根目录路径
  //     * @return 有sd卡则为sdcard/xxx/,没有则返回/data/data/应用包名/xxx/
  //     */
  //    public static String getAppRootPath(Context context) {
  //        if (hasSdcard()) {
  //            return getSDCardPath() + File.separator + ROOT_PATH_NAME
  //                    + File.separator;
  //        }
  //        else {
  //            return context.getFilesDir().getPath() + File.separator
  //                    + ROOT_PATH_NAME + File.separator;
  //        }
  //    }
  //
  //    /**
  //     * 创建本应用的根目录
  //     * @param context
  //     */
  //    public static void createAppRootPath(Context context) {
  //        String rootPath = "";
  //        if (hasSdcard()) {
  //            rootPath = getSDCardPath() + File.separator + ROOT_PATH_NAME
  //                    + File.separator;
  //        }
  //        else {
  //            rootPath = context.getFilesDir().getPath() + File.separator
  //                    + ROOT_PATH_NAME + File.separator;
  //        }
  //        File file = new File(rootPath);
  //        if (!file.exists()) {
  //            file.mkdirs();
  //        }
  //    }
  //
  //
  //    /**
  //     * 获取APP更新时下载的安装包保存目录
  //     * @param context
  //     * @return sdcard/{@link #ROOT_PATH_NAME}/apk
  //     */
  //    public static String getUpdateAPKPath(Context context) {
  //        String path = getAppRootPath(context) + APK;
  //        mkDirs(path);
  //        return path;
  //    }
  //
  //    /**
  //     * 获取保存错误日志的目录
  //     * @param context
  //     * @return
  //     */
  //    public static String getCrashPath(Context context) {
  //        String path = getAppRootPath(context) + File.separator + CRASH;
  //        mkDirs(path);
  //        return path;
  //    }
  //
  //    /**
  //     * 获取企业logo保存的路径
  //     * @param context
  //     * @return sdcard/{@link #ROOT_PATH_NAME}/image/logo
  //     */
  //    public static String getEntLogoPath(Context context) {
  //        String path = getImagePath(context) + File.separator + ENT_LOGO;
  //        mkDirs(path);
  //        return path;
  //    }
  //
  //    /**
  //     * 获取头像保存的路径
  //     * @param context
  //     * @return sdcard/{@link #ROOT_PATH_NAME}/image/head
  //     */
  //    public static String getHeadPath(Context context) {
  //        String path = getImagePath(context) + File.separator + HEAD;
  //        mkDirs(path);
  //        return path;
  //    }
  //
  //    /**
  //     * 获取图片保存的路径
  //     * @param context
  //     * @return sdcard/{@link #ROOT_PATH_NAME}/image
  //     */
  //    public static String getImagePath(Context context) {
  //        String path = getAppRootPath(context) + IMAGE;
  //        mkDirs(path);
  //        return path;
  //    }
  //
  //    /**
  //     * 获取聊天图片、声音等保存的路径
  //     * @param context
  //     * @return sdcard/{@link #ROOT_PATH_NAME}/用户id
  //     */
  //    public static String getChatsFilePath(Context context, long userId) {
  //        String path = getAppRootPath(context) + userId;
  //        mkDirs(path);
  //        return path;
  //    }
  //
  //    /**
  //     * 获取聊天图片保存的路径
  //     * @param context
  //     * @return sdcard/{@link #ROOT_PATH_NAME}/用户id/image
  //     */
  //    public static String getChatsImagePath(Context context, long userId) {
  //        String path = getChatsFilePath(context, userId) + File.separator + IMAGE;
  //        mkDirs(path);
  //        return path;
  //    }
  //
  //    /**
  //     * 获取聊天语音保存的路径
  //     * @param context
  //     * @return sdcard/{@link #ROOT_PATH_NAME}/用户id/audio
  //     */
  //    public static String getChatsAudioPath(Context context, long userId) {
  //        String path = getChatsFilePath(context, userId) + File.separator + AUDIO;
  //        mkDirs(path);
  //        return path;
  //    }
  //
  //    /**
  //     * 获取实名认证图片保存的路径
  //     * @param context
  //     * @return sdcard/{@link #ROOT_PATH_NAME}/image/auth
  //     */
  //    public static String getAuthImagePath(Context context) {
  //        String path = getImagePath(context) + File.separator +  AUTH;
  //        mkDirs(path);
  //        return path;
  //    }
  //
  //    /**
  //     * 获取二维码图片保存的路径
  //     * @param context
  //     * @return sdcard/{@link #ROOT_PATH_NAME}/image/qrCode
  //     */
  //    public static String getQRCodeImagePath(Context context) {
  //        String path = getImagePath(context) + File.separator +  QR_CODE;
  //        mkDirs(path);
  //        return path;
  //    }
}
