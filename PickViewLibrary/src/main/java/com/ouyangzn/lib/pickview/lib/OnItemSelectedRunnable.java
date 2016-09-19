package com.ouyangzn.lib.pickview.lib;

final class OnItemSelectedRunnable implements Runnable {
  final WheelView loopView;

  OnItemSelectedRunnable(WheelView loopview) {
    loopView = loopview;
  }

  @Override public final void run() {
    loopView.onItemSelectedListener.onItemSelected(loopView.getCurrentItem());
  }
}