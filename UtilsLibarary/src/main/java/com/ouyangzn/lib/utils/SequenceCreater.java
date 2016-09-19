/*
 * 版权所有 (C) 2001-2012 深圳市艾派应用系统有限公司。保留所有权利。
 * 版本：
 * 修改记录：
 *      1、Aug 28, 2015，ouyangzn创建。 
 */
package com.ouyangzn.lib.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 序列号生成器
 *
 * @author ouyangzn
 */
public class SequenceCreater {
  private static AtomicInteger seq = new AtomicInteger(1);

  private SequenceCreater() {
    throw new UnsupportedOperationException("cannot be instantiated");
  }

  public synchronized static int getSeq() {
    return seq.getAndIncrement();
  }
}