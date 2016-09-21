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