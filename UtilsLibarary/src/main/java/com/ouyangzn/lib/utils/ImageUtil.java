package com.ouyangzn.lib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片处理工具类
 *
 * @author ouyangzn
 */
public class ImageUtil {

  private static final int LEFT = 0;
  private static final int RIGHT = 1;
  private static final int TOP = 3;
  private static final int BOTTOM = 4;

  private ImageUtil() {
    throw new UnsupportedOperationException("cannot be instantiated");
  }

  /**
   * 加载本地图片为bitmap(不进行压缩等处理)
   *
   * @param path 图片路径
   */
  public static Bitmap getBitmap(String path) {
    return BitmapFactory.decodeFile(path);
  }

  /**
   * Bitmap转换为byte[]
   *
   * @param bitmap
   */
  public static byte[] bitmap2Bytes(Bitmap bitmap) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    /**
     * 将图片压缩为PNG格式，100表示压缩为最高质量，即为无损压缩
     */
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
    return baos.toByteArray();
  }

  /**
   * byte[]转换为Bitmap
   *
   * @param b
   */
  public static Bitmap bytes2Bitmap(byte[] b) {
    if (b.length != 0) {
      return BitmapFactory.decodeByteArray(b, 0, b.length);
    } else {
      return null;
    }
  }

  /**
   * 将bitmap进行缩放
   *
   * @param bitmap
   * @param width 缩放后宽度
   * @param height 缩放后高度
   */
  public static Bitmap scaleBitmap(Bitmap bitmap, int width, int height) {
    return Bitmap.createScaledBitmap(bitmap, width, height, true);
  }

  /**
   * 将Drawable转化为Bitmap
   *
   * @param drawable
   */
  public static Bitmap drawableToBitmap(Drawable drawable) {
    // 取 drawable 的长宽
    int w = drawable.getIntrinsicWidth();
    int h = drawable.getIntrinsicHeight();

    // 取 drawable 的颜色格式
    Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
    // 建立对应 bitmap
    Bitmap bitmap = Bitmap.createBitmap(w, h, config);
    // 建立对应 bitmap 的画布
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, w, h);
    // 把 drawable 内容画到画布中
    drawable.draw(canvas);
    return bitmap;
  }

  /**
   * 获得带倒影的图片
   *
   * @param bitmap
   */
  public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
    final int reflectionGap = 4;
    int w = bitmap.getWidth();
    int h = bitmap.getHeight();

    Matrix matrix = new Matrix();
    matrix.preScale(1, -1);

    Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w, h / 2, matrix, false);

    Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2), Config.ARGB_8888);

    Canvas canvas = new Canvas(bitmapWithReflection);
    canvas.drawBitmap(bitmap, 0, 0, null);
    Paint defaultPaint = new Paint();
    canvas.drawRect(0, h, w, h + reflectionGap, defaultPaint);

    canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

    Paint paint = new Paint();
    LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
        bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
    paint.setShader(shader);
    // Set the Transfer mode to be porter duff and destination in
    paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
    // Draw a rectangle using the paint with our linear gradient
    canvas.drawRect(0, h, w, bitmapWithReflection.getHeight() + reflectionGap, paint);

    return bitmapWithReflection;
  }

  /**
   * Drawable缩放
   *
   * @param drawable
   * @param w 缩放后的宽
   * @param h 缩放后的高
   */
  public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
    return new BitmapDrawable(scaleBitmap(drawableToBitmap(drawable), w, h));
  }

  /**
   * 图片去色,返回灰度图片
   *
   * @param bmpOriginal 传入的图片
   * @return 去色后的图片
   */
  public static Bitmap toGrayscale(Bitmap bmpOriginal) {
    int width, height;
    height = bmpOriginal.getHeight();
    width = bmpOriginal.getWidth();
    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.RGB_565);
    Canvas c = new Canvas(bmpGrayscale);
    Paint paint = new Paint();
    ColorMatrix cm = new ColorMatrix();
    cm.setSaturation(0);
    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
    paint.setColorFilter(f);
    c.drawBitmap(bmpOriginal, 0, 0, paint);
    return bmpGrayscale;
  }

  /**
   * 去色同时加圆角
   *
   * @param bmpOriginal 原图
   * @param pixels 圆角弧度
   * @return 修改后的图片
   */
  public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
    return getRoundedCornerBitmap(toGrayscale(bmpOriginal), pixels);
  }

  /**
   * 获得圆角图片
   *
   * @param bitmap
   * @param roundPx 圆角弧度
   */
  public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
    int w = bitmap.getWidth();
    int h = bitmap.getHeight();
    Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
    Canvas canvas = new Canvas(output);
    final int color = 0xff424242;
    final Paint paint = new Paint();
    final Rect rect = new Rect(0, 0, w, h);
    final RectF rectF = new RectF(rect);
    paint.setAntiAlias(true);
    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(color);
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);

    return output;
  }

  /**
   * 使圆角功能支持BitampDrawable
   *
   * @param bitmapDrawable
   * @param pixels
   */
  public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels) {
    Bitmap bitmap = bitmapDrawable.getBitmap();
    bitmapDrawable = new BitmapDrawable(getRoundedCornerBitmap(bitmap, pixels));
    return bitmapDrawable;
  }

  /**
   * 保存图片到本地
   *
   * @param bitmap
   * @param path 保存的全路径
   * @return 保存是否成功
   */
  public static boolean saveImage2Local(Bitmap bitmap, String path) {
    File file = new File(path);
    try {
      FileOutputStream out = new FileOutputStream(file);
      if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
        out.flush();
        out.close();
      }
      return true;
    } catch (FileNotFoundException e) {
      Log.e("saveImage2Local", "保存图片到本地出错,FileNotFoundException:", e);
      return false;
    } catch (IOException e) {
      Log.e("saveImage2Local", "保存图片到本地出错,IOException:", e);
      return false;
    } catch (Exception e) {
      Log.e("saveImage2Local", "保存图片到本地出错,Exception:", e);
      return false;
    }
  }

  /**
   * 保存到本地
   *
   * @param data 数据
   * @param path 保存的全路径
   * @return 保存是否成功
   */
  public static boolean saveImage2Local(byte[] data, String path) {
    return IOUtils.writeFile(path, data);
  }

  /**
   * 水印
   *
   * @param src 原图
   * @param watermark 水印图
   */
  public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
    if (src == null) {
      return null;
    }
    int w = src.getWidth();
    int h = src.getHeight();
    int ww = watermark.getWidth();
    int wh = watermark.getHeight();
    // create the new blank bitmap
    Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
    Canvas cv = new Canvas(newb);
    // draw src into
    cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
    // draw watermark into
    cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
    // save all clip
    cv.save(Canvas.ALL_SAVE_FLAG);// 保存
    // store
    cv.restore();// 存储
    return newb;
  }

  /**
   * 图片合成
   */
  public static Bitmap potoMix(int direction, Bitmap... bitmaps) {
    if (bitmaps.length <= 0) {
      return null;
    }
    if (bitmaps.length == 1) {
      return bitmaps[0];
    }
    Bitmap newBitmap = bitmaps[0];
    // newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
    for (int i = 1; i < bitmaps.length; i++) {
      newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
    }
    return newBitmap;
  }

  /**
   * 图片合成
   *
   * @param first
   * @param second
   * @param direction
   */
  private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second, int direction) {
    if (first == null) {
      return null;
    }
    if (second == null) {
      return first;
    }
    int fw = first.getWidth();
    int fh = first.getHeight();
    int sw = second.getWidth();
    int sh = second.getHeight();
    Bitmap newBitmap = null;
    if (direction == LEFT) {
      newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Config.ARGB_8888);
      Canvas canvas = new Canvas(newBitmap);
      canvas.drawBitmap(first, sw, 0, null);
      canvas.drawBitmap(second, 0, 0, null);
    } else if (direction == RIGHT) {
      newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Config.ARGB_8888);
      Canvas canvas = new Canvas(newBitmap);
      canvas.drawBitmap(first, 0, 0, null);
      canvas.drawBitmap(second, fw, 0, null);
    } else if (direction == TOP) {
      newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Config.ARGB_8888);
      Canvas canvas = new Canvas(newBitmap);
      canvas.drawBitmap(first, 0, sh, null);
      canvas.drawBitmap(second, 0, 0, null);
    } else if (direction == BOTTOM) {
      newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Config.ARGB_8888);
      Canvas canvas = new Canvas(newBitmap);
      canvas.drawBitmap(first, 0, 0, null);
      canvas.drawBitmap(second, 0, fh, null);
    }
    return newBitmap;
  }

  /**
   * 以最省内存的方式读取本地资源的图片
   *
   * @param context
   * @paramres resId 图片的资源id
   */
  public static Bitmap getBitmapFromRes(Context context, int resId) {
    BitmapFactory.Options opt = new BitmapFactory.Options();
    opt.inPreferredConfig = Config.RGB_565;
    opt.inPurgeable = true;
    opt.inInputShareable = true;
    // 获取资源图片
    InputStream is = context.getResources().openRawResource(resId);
    return BitmapFactory.decodeStream(is, null, opt);
  }

  /**
   * 压缩图片的尺寸为指定尺寸
   *
   * @param path
   * @param reqWidth
   * @param reqHeight
   */
  public static Bitmap compressSize(String path, int reqWidth, int reqHeight) {

    BitmapFactory.Options newOpts = new BitmapFactory.Options();
    newOpts.inJustDecodeBounds = true;
    // 开始读入图片参数
        /*Bitmap bitmap = */
    BitmapFactory.decodeFile(path, newOpts);
    int realWidth = newOpts.outWidth;
    int realHeight = newOpts.outHeight;
    // 缩放比,be=1表示不缩放
    int inSampleSize = 1;
    // 如果宽度大的话根据宽度固定大小缩放
    if (realWidth > realHeight && realWidth > reqWidth) {
      inSampleSize = (int) (realWidth / reqWidth);
    }
    // 如果高度高的话根据宽度固定大小缩放
    else if (realWidth < realHeight && realHeight > reqHeight) {
      inSampleSize = (int) (realHeight / reqHeight);
    }
    if (inSampleSize <= 0) {

      inSampleSize = 1;
    }
    // 设置缩放比例
    newOpts.inSampleSize = inSampleSize;
    // 重新读入图片资源
    newOpts.inJustDecodeBounds = false;
    Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);

    return bitmap;
  }

  /**
   * 压缩图片质量使图片大小缩小到指定大小
   *
   * @param image
   * @param size 最终大小，单位kb
   */
  public static byte[] compressQuality(Bitmap image, int size) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    int options = 100;
    // 循环判断如果压缩后图片是否大于size,大于继续压缩
    while (baos.toByteArray().length / 1024 > size) {
      // 重置baos即清空baos
      options -= 10;
      baos.reset();
      // 这里压缩options%，把压缩后的数据存放到baos中
      image.compress(Bitmap.CompressFormat.JPEG, options, baos);
      // 每次都减少10
      if (options == 0) break;
    }
    byte[] photo = baos.toByteArray();
    try {
      if (baos != null) {
        baos.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return photo;
  }
}