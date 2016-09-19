package com.ouyangzn.library;

import android.os.Bundle;
import android.view.View;
import com.ouyangzn.lib.pickview.TimePickerView;
import com.ouyangzn.lib.utils.StringUtil;
import com.ouyangzn.library.base.BaseActivity;
import com.ouyangzn.library.utils.Log;
import java.util.Date;

public class MainActivity extends BaseActivity {

  @Override protected void initData() {

  }

  @Override protected void initView(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main);
  }

  public void selectDate(View view) {
    TimePickerView timePicker =
        new TimePickerView(MainActivity.this, TimePickerView.Type.YEAR_MONTH_DAY);
    timePicker.setCyclic(true);
    timePicker.setRange(2000, 2016);
    timePicker.setTime(new Date());
    timePicker.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        toast("选择的时间为:" + StringUtil.formatDate2String(date.getTime(), "yyyy-MM-dd"));
        Log.d(TAG, "----------selectedTime = " + StringUtil.formatDate2String(date.getTime(),
            "yyyy-MM-dd"));
      }
    });
    timePicker.show();
  }
}
