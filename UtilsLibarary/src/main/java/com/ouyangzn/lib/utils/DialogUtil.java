package com.ouyangzn.lib.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * dialog工具类
 *
 * @author ouyangzn
 */
public class DialogUtil {

  private DialogUtil() {
    throw new UnsupportedOperationException("cannot be instantiated");
  }

  /**
   * 显示一个进度圈
   *
   * @param context
   * @param message 对话框中的信息
   * @param cancelable 对话框能否取消
   * @return ProgressDialog
   */
  public static synchronized ProgressDialog showProgressDialog(Context context, String message,
      boolean cancelable) {
    ProgressDialog dialog = new ProgressDialog(context);
    // show需要在setContentView之前，否则会报requestFeature() must be called before adding content
    dialog.show();
    //        dialog.setContentView(R.layout.progress_dialog);
    //        ((TextView)dialog.findViewById(R.id.tv_progressbar_tips)).setText(Message);
    dialog.setMessage(message);
    dialog.setCancelable(cancelable);
    return dialog;
  }

  /**
   * 隐藏进度对话框
   */
  public static synchronized void dismissProgressDialog(ProgressDialog dialog) {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }
  }

  /**
   * 取消进度对话框,cancel时会回调OnCancelListener中的onCancel()
   */
  public static synchronized void cancelProgressDialog(ProgressDialog dialog) {
    if (dialog != null && dialog.isShowing()) {
      dialog.cancel();
    }
  }

  /**
   * 获取一个对话框AlertDialog
   *
   * @param context
   */
  public static synchronized AlertDialog.Builder getAlertDialog(Context context) {
    return new AlertDialog.Builder(context/*, R.style.BaseAlertDialog*/);
  }

  /**
   * 获取一个PopupWindow
   *
   * @param contentView 内容view
   * @return PopupWindow
   */
  public static synchronized PopupWindow getPopupWindow(View contentView) {
    PopupWindow mPopupWindow = new PopupWindow(contentView);
    mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
    mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
    mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    mPopupWindow.setOutsideTouchable(true);
    mPopupWindow.setFocusable(true);
    mPopupWindow.setAnimationStyle(R.style.PopupWindowLoadAnimation);
    return mPopupWindow;
  }
}
