package com.ouyangzn.lib.utils;

import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Ouyang on 2015/12/15.<br/>
 * Description：IO工具类
 */
public class IOUtils {

  private static final String TAG = IOUtils.class.getSimpleName();

  /**
   * 获取文件的输入流
   *
   * @param file
   * @throws FileNotFoundException
   */
  public static InputStream getInputStream(File file) throws FileNotFoundException {
    return new FileInputStream(file);
  }

  /**
   * 将InputStream转换为byte[]
   *
   * @param is
   * @throws IOException
   */
  public static byte[] getBytes(InputStream is) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] b = new byte[1024];
    int len = 0;
    while ((len = is.read(b, 0, 1024)) != -1) {
      baos.write(b, 0, len);
      baos.flush();
    }
    byte[] bytes = baos.toByteArray();
    return bytes;
  }

  public static byte[] getBytes(File file) {
    ByteArrayOutputStream baos = null;
    InputStream is = null;
    try {
      baos = new ByteArrayOutputStream();
      is = new FileInputStream(file);
      byte[] b = new byte[1024];
      int len = 0;
      while ((len = is.read(b, 0, 1024)) != -1) {
        baos.write(b, 0, len);
        baos.flush();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      closeStream(baos, is);
    }

    byte[] bytes = baos.toByteArray();
    return bytes;
  }

  public static byte[] getBytes(String filePath) {
    return getBytes(new File(filePath));
  }

  /**
   * 把输入流转成BufferedReader
   *
   * @param in
   */
  public static BufferedReader getBufferedReader(InputStream in, String charset) {
    try {
      return new BufferedReader(new InputStreamReader(in, charset));
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "------getBufferedReader出错:UnsupportedEncodingException");
    }
    return null;
  }

  /**
   * 子线程执行写出到文件的操作
   *
   * @param filePath 文件路径
   * @param data 数据
   */
  public static void writeFileAsync(final String filePath, final byte[] data) {
    ThreadUtil.execute(new Runnable() {
      @Override public void run() {
        writeFile(filePath, data);
      }
    });
  }

  /**
   * 写出文件
   *
   * @param filePath 文件路径
   * @param data 数据
   */
  public static boolean writeFile(String filePath, byte[] data) {
    FileOutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(filePath);
      outputStream.write(data);
      return true;
    } catch (FileNotFoundException e) {
      Log.e(TAG, "------writeFile时路径有问题");
      return false;
    } catch (IOException e) {
      Log.e(TAG, "------writeFile时IO异常", e);
      return false;
    } finally {
      closeStream(outputStream);
    }
  }

  /**
   * 写出文件
   *
   * @param filePath 文件路径
   * @param inputStream 带有数据的输入流
   */
  public static boolean writeFile(String filePath, InputStream inputStream) {
    FileOutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(filePath);
      int len = -1;
      byte[] buffer = new byte[10 * 1024];
      while ((len = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, len);
      }
      return true;
    } catch (FileNotFoundException e) {
      Log.e(TAG, "------writeFile时路径有问题", e);
      return false;
    } catch (IOException e) {
      Log.e(TAG, "------writeFile时IO异常", e);
      return false;
    } finally {
      closeStream(outputStream, inputStream);
    }
  }

  /**
   * 复制文件
   *
   * @param srcPath
   * @param targetPath
   */
  public static boolean copyFile(String srcPath, String targetPath) {
    FileOutputStream outputStream = null;
    FileInputStream inputStream = null;
    try {
      outputStream = new FileOutputStream(targetPath);
      inputStream = new FileInputStream(srcPath);
      int len = -1;
      byte[] buffer = new byte[10 * 1024];
      while ((len = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, len);
      }
      return true;
    } catch (FileNotFoundException e) {
      Log.e(TAG, "------writeFile时路径有问题", e);
      return false;
    } catch (IOException e) {
      Log.e(TAG, "------writeFile时IO异常", e);
      return false;
    } finally {
      closeStream(outputStream, inputStream);
    }
  }

  /**
   * 关闭流
   *
   * @param stream
   */
  public static void closeStream(Closeable... stream) {
    if (stream == null) return;
    for (Closeable s : stream) {
      if (s != null) {
        try {
          s.close();
        } catch (IOException e) {
          Log.e(TAG, "关闭流出错", e);
        }
      }
    }
  }
}
