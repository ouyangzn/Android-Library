package com.ouyangzn.lib.qr.scan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ouyang on 2016/4/22.<br/>
 * Description：二维码解析
 */
public class QRScanner {

  /**
   * 解析图片（只解析二维码图片，以utf-8编码结果）
   *
   * @param imgPath 图片地址
   * @return 二维码携带的信息
   */
  public static String decode(String imgPath) {
    return decode(imgPath, null, null);
  }

  /**
   * 解析图片（只解析二维码图片，以utf-8编码结果）
   *
   * @param bitmap 图片
   * @return 二维码携带的信息
   */
  public static String decode(Bitmap bitmap) {
    return decode(bitmap, null, null);
  }

  /**
   * 解析图片
   *
   * @param imgPath 图片地址
   * @param charset 二维码信息的编码
   * @param formats 支持的识别类型（eg:二维码、条形码...） {@link BarcodeFormat}
   * @return 二维码携带的信息
   */
  public static String decode(String imgPath, String charset, List<BarcodeFormat> formats) {
    if (TextUtils.isEmpty(imgPath)) {
      return null;
    }
    Bitmap image = BitmapFactory.decodeFile(imgPath);
    if (image == null) {
      return null;
    }
    return decode(image, charset, formats);
  }

  /**
   * 解析图片
   *
   * @param image 图片
   * @param charset 二维码信息的编码
   * @param formats 支持的识别类型（eg:二维码、条形码...）
   * @return 二维码携带的信息
   */
  public static String decode(Bitmap image, String charset, List<BarcodeFormat> formats) {
    if (image == null) {
      return null;
    }
    if (TextUtils.isEmpty(charset)) {
      charset = "utf-8";
    }
    if (formats == null || formats.size() == 0) {
      formats = new ArrayList<>();
      formats.add(BarcodeFormat.QR_CODE);
    }
    MultiFormatReader mMultiFormatReader = new MultiFormatReader();
    Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
    hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
    hints.put(DecodeHintType.CHARACTER_SET, charset);
    mMultiFormatReader.setHints(hints);
    int width = image.getWidth(), height = image.getHeight();
    int[] pixels = new int[width * height];
    image.getPixels(pixels, 0, width, 0, 0, width, height);
    RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    try {
      Result rawResult = mMultiFormatReader.decodeWithState(bitmap);
      return rawResult.getText();
    } catch (NotFoundException re) {
      Log.e("QRScanner", "解析二维码图片出错", re);
      return null;
    } finally {
      mMultiFormatReader.reset();
    }
  }
}
