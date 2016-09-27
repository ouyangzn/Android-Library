package com.ouyangzn.lib.pickview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.ouyangzn.lib.pickview.view.BasePickerView;
import com.ouyangzn.lib.pickview.view.WheelTime;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sai on 15/11/22.
 * update by ouyangzn on 2016/9/27
 */
public class TimePickerView extends BasePickerView implements View.OnClickListener {
  private WheelTime wheelTime;
  private TextView tvTitle;
  private OnTimeSelectListener timeSelectListener;
  private OnCancelListener mOnCancelListener;

  public TimePickerView(Activity context, Type type) {
    super(context);

    LayoutInflater.from(context).inflate(R.layout.pickerview_time, contentContainer);
    // -----确定和取消按钮
    findViewById(R.id.btnSubmit).setOnClickListener(this);
    findViewById(R.id.btnCancel).setOnClickListener(this);
    //顶部标题
    tvTitle = (TextView) findViewById(R.id.tvTitle);
    // ----时间转轮
    final View timepickerview = findViewById(R.id.timepicker);
    wheelTime = new WheelTime(timepickerview, type);

    //默认选中当前时间
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hours = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    wheelTime.setPicker(year, month, day, hours, minute);
  }

  /**
   * 设置可以选择的时间范围
   *
   * @param startYear
   * @param endYear
   */
  public void setRange(int startYear, int endYear) {
    wheelTime.setStartYear(startYear);
    wheelTime.setEndYear(endYear);
  }

  /**
   * 设置选中时间
   *
   * @param date
   */
  public void setTime(Date date) {
    Calendar calendar = Calendar.getInstance();
    if (date == null) {
      calendar.setTimeInMillis(System.currentTimeMillis());
    } else {
      calendar.setTime(date);
    }
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hours = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    wheelTime.setPicker(year, month, day, hours, minute);
  }

  /**
   * 设置是否循环滚动
   *
   * @param cyclic
   */
  public void setCyclic(boolean cyclic) {
    wheelTime.setCyclic(cyclic);
  }

  //    /**
  //     * 指定选中的时间，显示选择器
  //     *
  //     * @param date
  //     */
  //    public void show(Date date) {
  //        Calendar calendar = Calendar.getInstance();
  //        if (date == null)
  //            calendar.setTimeInMillis(System.currentTimeMillis());
  //        else
  //            calendar.setTime(date);
  //        int year = calendar.get(Calendar.YEAR);
  //        int month = calendar.get(Calendar.MONTH);
  //        int day = calendar.get(Calendar.DAY_OF_MONTH);
  //        int hours = calendar.get(Calendar.HOUR_OF_DAY);
  //        int minute = calendar.get(Calendar.MINUTE);
  //        wheelTime.setPicker(year, month, day, hours, minute);
  //        show();
  //    }

  @Override public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.btnSubmit) {
      if (timeSelectListener != null) {
        try {
          Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
          timeSelectListener.onTimeSelect(date);
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
      dismiss();
    } else if (id == R.id.btnCancel) {
      dismiss();
      if (mOnCancelListener != null) {
        mOnCancelListener.onCancel();
      }
    }
  }

  public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
    this.timeSelectListener = timeSelectListener;
  }

  public void setOnCancelListener(OnCancelListener onCancelListener) {
    mOnCancelListener = onCancelListener;
  }

  public void setTitle(String title) {
    tvTitle.setText(title);
  }

  public enum Type {
    ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN, YEAR_MONTH
  }// 四种选择模式，年月日时分，年月日，时分，月日时分

  public interface OnTimeSelectListener {
    void onTimeSelect(Date date);
  }

  public interface OnCancelListener {
    void onCancel();
  }
}
