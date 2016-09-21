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

import android.util.Log;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String帮助类
 *
 * @author ouyangzn
 */
public class StringUtil {

  private static final String TAG = StringUtil.class.getSimpleName();

  private StringUtil() {
    throw new UnsupportedOperationException("cannot be instantiated");
  }

  /**
   * 判断传入的字符串是否为空
   *
   * @param str
   * @return 传入的字符串为空返回true，反之返回flase
   */
  public static boolean isEmpty(String str) {
    if (str == null || "".equals(str)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 判断传入的字符串是否不为空
   *
   * @param str
   * @return 传入的字符串不为空返回true，反之返回false
   */
  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  /**
   * 判断传入的字符串是否是字母
   *
   * @param str
   */
  public static boolean isLetters(String str) {
    if (isNotEmpty(str) && str.matches("^[A-Za-z]+$")) {
      return true;
    }
    return false;
  }

  /**
   * 提取英文的首字母，非英文字母用#代替。
   *
   * @param str
   * @return 返回首字母的大写字母
   */
  public static String getAlpha(String str) {
    if (str == null) {
      return "#";
    }
    str = str.trim();
    if (str.length() == 0) {
      return "#";
    }

    char c = str.substring(0, 1).charAt(0);
    // 正则表达式，判断首字母是否是英文字母
    Pattern pattern = Pattern.compile("^[A-Za-z]+$");
    if (pattern.matcher(c + "").matches()) {
      return (c + "").toUpperCase(); // 大写输出
    } else {
      return "#";
    }
  }

  /**
   * 获取元素在数组的下标
   *
   * @param arr String数据
   * @param item 元素名
   * @return 数组下标，如果数组中找不到则返回-1
   */
  public static int getIndex(String[] arr, String item) {
    int index = -1;
    for (int i = 0; i < arr.length; i++) {
      if (arr[i].equals(item)) {
        index = i;
        break;
      }
    }
    return index;
  }

  /**
   * 验证传入的11位手机号是否正确
   *
   * @param mobile
   */
  public static boolean isMobileNum(String mobile) {
    if (isEmpty(mobile)) return false;
    Pattern p = Pattern.compile("^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])[0-9]{8}$");
    Matcher m = p.matcher(mobile);
    return m.matches();
  }

  /**
   * 验证传入的11位手机号是否正确
   *
   * @param mobile
   */
  public static boolean isMobile(String mobile) {
    if (isEmpty(mobile)) return false;
    Pattern p = Pattern.compile("^1[0-9]{10}$");
    Matcher m = p.matcher(mobile);
    return m.matches();
  }

  /**
   * MD5加密，32位
   *
   * @param str
   */
  public static String MD5(String str) {
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
    char[] charArray = str.toCharArray();
    byte[] byteArray = new byte[charArray.length];
    for (int i = 0; i < charArray.length; i++) {
      byteArray[i] = (byte) charArray[i];
    }
    byte[] md5Bytes = md5.digest(byteArray);
    StringBuffer hexValue = new StringBuffer();
    for (int i = 0; i < md5Bytes.length; i++) {
      int val = ((int) md5Bytes[i]) & 0xff;
      if (val < 16) {
        hexValue.append("0");
      }
      hexValue.append(Integer.toHexString(val));
    }
    return hexValue.toString().toUpperCase();
  }

  /**
   * 判断是否是数字
   *
   * @param str
   */
  public static boolean isNumeric(String str) {
    Pattern pattern = Pattern.compile("[0-9]*");
    return pattern.matcher(str).matches();
  }

  /**
   * 去掉中文字符
   *
   * @param chinese
   */
  public static String replaceCNString(String chinese) {
    return chinese.replace("《", "")
        .replace("》", "")
        .replace("！", "")
        .replace("￥", "")
        .replace("【", "")
        .replace("】", "")
        .replace("（", "")
        .replace("）", "")
        .replace("－", "")
        .replace("；", "")
        .replace("：", "")
        .replace("”", "")
        .replace("“", "")
        .replace("。", "")
        .replace("，", "")
        .replace("、", "")
        .replace("？", "")
        .replace(" ", "")
        .replace("-", "");
  }

  /**
   * 将日期Time转换为日期字符串
   *
   * @param time
   * @param format 格式 eg: MM月dd日 HH:mm
   */
  public static String formatDate2String(long time, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
    return sdf.format(new Date(time));
  }

  /**
   * 获取当前时间
   *
   * @param format 格式
   */
  public static String getNowDate(String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
    sdf.format(new Date());
    return sdf.format(new Date());
  }

  /**
   * 转换时间格式
   *
   * @param str 时间字符串
   * @param format 原时间格式
   * @param newFormat 转换后的格式
   * @return 转换格式后的时间字符串, 如果原字符串不是时间，则返回空字符串
   */
  public static String formatDate(String str, String format, String newFormat) {
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
    String date = "";
    try {
      Date d = sdf.parse(str);
      date = formatDate2String(d.getTime(), newFormat);
    } catch (ParseException e) {
      Log.e(TAG, "转换格式出错:", e);
    }
    return date;
  }

  /**
   * 将日期转换为time
   *
   * @param date 时间字符串
   * @param format 时间格式
   */
  public static long date2time(String date, String format) {
    long dtime = 0L;
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
    try {
      Date d = sdf.parse(date);
      dtime = d.getTime();
    } catch (ParseException e) {
      Log.e(TAG, "将日期转换为time出错:", e);
    }
    return dtime;
  }

  /**
   * 返回byte的数据大小对应的(KB/MB/GB/TB)文本,有小数则保留2位小数  eg:1.11KB
   *
   * @param size
   */
  public static String getDataSize(long size) {
    if (size < 0) {
      size = 0;
    }
    DecimalFormat formater = new DecimalFormat("####0.00");
    if (size < 1024) {
      return size + "B";
    } else if (size < 1024 * 1024) {
      float kbsize = size / 1024f;
      return formater.format(kbsize) + "KB";
    } else if (size < 1024 * 1024 * 1024) {
      float mbsize = size / 1024f / 1024f;
      return formater.format(mbsize) + "MB";
    } else if (size < 1024 * 1024 * 1024 * 1024) {
      float gbsize = size / 1024f / 1024f / 1024f;
      return formater.format(gbsize) + "GB";
    } else {
      float tbsize = size / 1024f / 1024f / 1024f / 1024f;
      return formater.format(tbsize) + "TB";
    }
  }

  /**
   * 判断字符串是否有汉字
   *
   * @param str
   */
  public static boolean hasChinese(String str) {
    return str.length() == str.getBytes().length ? false : true;
  }

  public static class IDCard {

    private static final int[] ERROR_CODE = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    /*********************************** 身份证验证开始 ****************************************/
    /**
     * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
     * 八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * 2、地址码(前六位数）     * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
     * 3、出生日期码（第七位至十四位）     * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
     * 4、顺序码（第十五位至十七位）表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。
     * 5、校验码（第十八位数）
     * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
     * 	   Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * （2）计算模 Y = mod(S, 11)
     * （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
     */

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效, 返回"0" 无效, 1:身份证号码长度应该为15位或18位。2:身份证15位号码都应为数字,18位号码除最后一位外，都应为数字。3:身份证生日无效。
     * 4:身份证生日不在有效范围。5:身份证月份无效。6:身份证日期无效。7:身份证地区编码错误。8:身份证无效，不是合法的身份证号码。
     */
    public static int IDCardValidate(String IDStr) {
      String errorInfo = "";// 记录错误信息
      String[] ValCodeArr = {
          "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"
      };
      String[] Wi = {
          "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"
      };
      String Ai = "";
      // ================ 号码的长度 15位或18位 ================
      if (IDStr.length() != 18 && IDStr.length() != 15) {
        System.out.println("身份证号码长度应该为15位或18位。");
        return ERROR_CODE[1];
      }
      // =======================(end)========================

      // ================ 数字 除最后以为都为数字 ================
      if (IDStr.length() == 18) {
        Ai = IDStr.substring(0, 17);
      } else if (IDStr.length() == 15) {
        Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
      }
      if (!isNumeric(Ai)) {
        System.out.println("身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。");
        return ERROR_CODE[2];
      }
      // =======================(end)========================

      // ================ 出生年月是否有效 ================
      String strYear = Ai.substring(6, 10);// 年份
      String strMonth = Ai.substring(10, 12);// 月份
      String strDay = Ai.substring(12, 14);// 月份
      if (!isDate(strYear + "-" + strMonth + "-" + strDay)) {
        System.out.println("身份证生日无效。");
        return ERROR_CODE[3];
      }
      GregorianCalendar gc = new GregorianCalendar();
      SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
      try {
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
            || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime())
            < 0) {
          System.out.println("身份证生日不在有效范围。");
          return ERROR_CODE[4];
        }
      } catch (NumberFormatException e) {
        e.printStackTrace();
      } catch (ParseException e) {
        e.printStackTrace();
      }
      if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
        System.out.println("身份证月份无效");
        return ERROR_CODE[5];
      }
      if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
        System.out.println("身份证日期无效");
        return ERROR_CODE[6];
      }
      // =====================(end)=====================

      // ================ 地区码时候有效 ================
      Hashtable areaCodeMap = GetAreaCode();
      if (areaCodeMap.get(Ai.substring(0, 2)) == null) {
        System.out.println("身份证地区编码错误。");
        return ERROR_CODE[7];
      }
      // ==============================================

      // ================ 判断最后一位的值 ================
      int TotalmulAiWi = 0;
      for (int i = 0; i < 17; i++) {
        TotalmulAiWi =
            TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
      }
      int modValue = TotalmulAiWi % 11;
      String strVerifyCode = ValCodeArr[modValue];
      Ai = Ai + strVerifyCode;

      if (IDStr.length() == 18) {
        if (Ai.equals(IDStr) == false) {
          System.out.println("身份证无效，不是合法的身份证号码");
          return ERROR_CODE[8];
        }
      } else {
        return ERROR_CODE[0];
      }
      // =====================(end)=====================
      return ERROR_CODE[0];
    }

    /**
     * 功能：获取地区编码
     *
     * @return Hashtable 对象
     */
    private static Hashtable GetAreaCode() {
      Hashtable hashtable = new Hashtable();
      hashtable.put("11", "北京");
      hashtable.put("12", "天津");
      hashtable.put("13", "河北");
      hashtable.put("14", "山西");
      hashtable.put("15", "内蒙古");
      hashtable.put("21", "辽宁");
      hashtable.put("22", "吉林");
      hashtable.put("23", "黑龙江");
      hashtable.put("31", "上海");
      hashtable.put("32", "江苏");
      hashtable.put("33", "浙江");
      hashtable.put("34", "安徽");
      hashtable.put("35", "福建");
      hashtable.put("36", "江西");
      hashtable.put("37", "山东");
      hashtable.put("41", "河南");
      hashtable.put("42", "湖北");
      hashtable.put("43", "湖南");
      hashtable.put("44", "广东");
      hashtable.put("45", "广西");
      hashtable.put("46", "海南");
      hashtable.put("50", "重庆");
      hashtable.put("51", "四川");
      hashtable.put("52", "贵州");
      hashtable.put("53", "云南");
      hashtable.put("54", "西藏");
      hashtable.put("61", "陕西");
      hashtable.put("62", "甘肃");
      hashtable.put("63", "青海");
      hashtable.put("64", "宁夏");
      hashtable.put("65", "新疆");
      hashtable.put("71", "台湾");
      hashtable.put("81", "香港");
      hashtable.put("82", "澳门");
      hashtable.put("91", "国外");
      return hashtable;
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param strDate
     */
    public static boolean isDate(String strDate) {
      Pattern pattern = Pattern.compile(
          "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
      Matcher m = pattern.matcher(strDate);
      if (m.matches()) {
        return true;
      } else {
        return false;
      }
    }
  }
}