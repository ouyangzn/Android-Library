package com.ouyangzn.lib.qr.scan;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.ouyangzn.lib.qr.R;

public class ViewFinderView extends View implements IViewFinder {
  private static final String TAG = "ViewFinderView";
  /** 扫描框最小宽高 */
  private static final int MIN_FRAME_WIDTH = 240;
  private static final int MIN_FRAME_HEIGHT = 240;
  /** 横屏时扫描框占屏幕的比例 */
  private static final float LANDSCAPE_WIDTH_RATIO = 5f / 8;
  private static final float LANDSCAPE_HEIGHT_RATIO = 5f / 8;
  /** 横屏时扫描框最大宽高 */
  private static final int LANDSCAPE_MAX_FRAME_WIDTH = (int) (1920 * LANDSCAPE_WIDTH_RATIO);
      // = 5/8 * 1920
  private static final int LANDSCAPE_MAX_FRAME_HEIGHT = (int) (1080 * LANDSCAPE_HEIGHT_RATIO);
      // = 5/8 * 1080
  /** 竖屏时扫描框占屏幕的比例 */
  private static final float PORTRAIT_WIDTH_RATIO = 7f / 8;
  private static final float PORTRAIT_HEIGHT_RATIO = 3f / 8;
  /** 竖屏时扫描框最大宽高 */
  private static final int PORTRAIT_MAX_FRAME_WIDTH = (int) (1080 * PORTRAIT_WIDTH_RATIO);
      // = 7/8 * 1080
  private static final int PORTRAIT_MAX_FRAME_HEIGHT = (int) (1920 * PORTRAIT_HEIGHT_RATIO);
      // = 3/8 * 1920
  /** 扫描线变化的时间间隔 */
  private static final long ANIMATION_DELAY = 10l;
  /** 画扫描框中间闪烁的线使用 */
  //    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
  //    private int scannerAlpha;
  //    private static final int POINT_SIZE = 10;
  //    private static final long ANIMATION_DELAY = 80l;
  /** 中间那条线每次刷新移动的距离 */
  private static final int MOVE_DISTANCE = 5;
  /** 扫描框中的中间线的宽度 */
  private static final int MIDDLE_LINE_WIDTH = 6;
  /** 提示文字距离扫描框下面的距离 */
  private static final int TIP_TEXT_MARGIN_TOP = 30;
  private final int mDefaultLaserColor = getResources().getColor(R.color.viewfinder_laser);
  private final int mDefaultMaskColor = getResources().getColor(R.color.viewfinder_mask);
  private final int mDefaultBorderColor = getResources().getColor(R.color.viewfinder_border);
  private final int mDefaultBorderStrokeWidth =
      getResources().getInteger(R.integer.viewfinder_border_width);
  private final int mDefaultBorderLineLength =
      getResources().getInteger(R.integer.viewfinder_border_length);
  protected Paint mLaserPaint;
  protected Paint mFinderMaskPaint;
  protected Paint mBorderPaint;
  protected Paint mTipsPaint;
  protected int mBorderLineLength;
  boolean isFirst;
  boolean showScanLine = true;
  /** 扫描框 */
  private Rect mFramingRect;
  /** 中间滑动线的最顶端位置 */
  private int slideTop;
  private int mTipsSize = 15;

  public ViewFinderView(Context context) {
    super(context);
    init();
  }

  public ViewFinderView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private static int findDesiredDimensionInRange(float ratio, int resolution, int hardMin,
      int hardMax) {
    int dim = (int) (ratio * resolution);
    if (dim < hardMin) {
      return hardMin;
    }
    if (dim > hardMax) {
      return hardMax;
    }
    return dim;
  }

  /**
   * sp转px
   *
   * @param context
   * @param spVal sp值
   */
  public static int sp2px(Context context, float spVal) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
        context.getResources().getDisplayMetrics());
  }

  public void showScanLine(boolean showScanLine) {
    this.showScanLine = showScanLine;
  }

  public void setTipsSize(int size) {
    if (size <= 0) return;
    this.mTipsSize = size;
    invalidate();
  }

  private void init() {
    //set up laser paint
    mLaserPaint = new Paint();
    mLaserPaint.setColor(mDefaultLaserColor);
    mLaserPaint.setStyle(Paint.Style.FILL);

    //finder mask paint
    mFinderMaskPaint = new Paint();
    mFinderMaskPaint.setColor(mDefaultMaskColor);

    //border paint
    mBorderPaint = new Paint();
    mBorderPaint.setColor(mDefaultBorderColor);
    mBorderPaint.setStyle(Paint.Style.STROKE);
    mBorderPaint.setStrokeWidth(mDefaultBorderStrokeWidth);

    mTipsPaint = new Paint();
    mTipsPaint.setColor(Color.WHITE);
    mTipsPaint.setTextSize(sp2px(getContext(), mTipsSize));

    mBorderLineLength = mDefaultBorderLineLength;
  }

  public void setLaserColor(int laserColor) {
    mLaserPaint.setColor(laserColor);
  }

  public void setMaskColor(int maskColor) {
    mFinderMaskPaint.setColor(maskColor);
  }

  public void setBorderColor(int borderColor) {
    mBorderPaint.setColor(borderColor);
  }

  public void setBorderStrokeWidth(int borderStrokeWidth) {
    mBorderPaint.setStrokeWidth(borderStrokeWidth);
  }

  public void setBorderLineLength(int borderLineLength) {
    mBorderLineLength = borderLineLength;
  }

  public void setupViewFinder() {
    updateFramingRect();
    invalidate();
  }

  public Rect getFramingRect() {
    return mFramingRect;
  }

  @Override public void onDraw(Canvas canvas) {
    if (mFramingRect == null) {
      return;
    }

    drawViewFinderMask(canvas);
    drawViewFinderBorder(canvas);
    //        drawLaser(canvas);
    if (showScanLine) {
      drawScanLine(canvas);
    }
    drawTipsText(canvas);
  }

  //    /**
  //     * 画中间闪烁的扫描线
  //     * @param canvas
  //     */
  //    public void drawLaser(Canvas canvas) {
  //        // Draw a red "laser scanner" line through the middle to show decoding is active
  //        mLaserPaint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
  //        scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
  //        int middle = mFramingRect.height() / 2 + mFramingRect.top;
  //        canvas.drawRect(mFramingRect.left + 2, middle - 1, mFramingRect.right - 1, middle + 2, mLaserPaint);
  //
  //        postInvalidateDelayed(ANIMATION_DELAY,
  //                mFramingRect.left - POINT_SIZE,
  //                mFramingRect.top - POINT_SIZE,
  //                mFramingRect.right + POINT_SIZE,
  //                mFramingRect.bottom + POINT_SIZE);
  //    }

  /**
   * 画扫描框外面的半透明层
   *
   * @param canvas
   */
  public void drawViewFinderMask(Canvas canvas) {
    int width = canvas.getWidth();
    int height = canvas.getHeight();

    canvas.drawRect(0, 0, width, mFramingRect.top, mFinderMaskPaint);
    canvas.drawRect(0, mFramingRect.top, mFramingRect.left, mFramingRect.bottom + 1,
        mFinderMaskPaint);
    canvas.drawRect(mFramingRect.right + 1, mFramingRect.top, width, mFramingRect.bottom + 1,
        mFinderMaskPaint);
    canvas.drawRect(0, mFramingRect.bottom + 1, width, height, mFinderMaskPaint);
  }

  /**
   * 画扫描框的4个边角
   *
   * @param canvas
   */
  public void drawViewFinderBorder(Canvas canvas) {
    canvas.drawLine(mFramingRect.left - 1, mFramingRect.top - 1, mFramingRect.left - 1,
        mFramingRect.top - 1 + mBorderLineLength, mBorderPaint);
    canvas.drawLine(mFramingRect.left - 1, mFramingRect.top - 1,
        mFramingRect.left - 1 + mBorderLineLength, mFramingRect.top - 1, mBorderPaint);

    canvas.drawLine(mFramingRect.left - 1, mFramingRect.bottom + 1, mFramingRect.left - 1,
        mFramingRect.bottom + 1 - mBorderLineLength, mBorderPaint);
    canvas.drawLine(mFramingRect.left - 1, mFramingRect.bottom + 1,
        mFramingRect.left - 1 + mBorderLineLength, mFramingRect.bottom + 1, mBorderPaint);

    canvas.drawLine(mFramingRect.right + 1, mFramingRect.top - 1, mFramingRect.right + 1,
        mFramingRect.top - 1 + mBorderLineLength, mBorderPaint);
    canvas.drawLine(mFramingRect.right + 1, mFramingRect.top - 1,
        mFramingRect.right + 1 - mBorderLineLength, mFramingRect.top - 1, mBorderPaint);

    canvas.drawLine(mFramingRect.right + 1, mFramingRect.bottom + 1, mFramingRect.right + 1,
        mFramingRect.bottom + 1 - mBorderLineLength, mBorderPaint);
    canvas.drawLine(mFramingRect.right + 1, mFramingRect.bottom + 1,
        mFramingRect.right + 1 - mBorderLineLength, mFramingRect.bottom + 1, mBorderPaint);
  }

  /**
   * 画扫描框中的扫描线
   *
   * @param canvas
   */
  public void drawScanLine(Canvas canvas) {
    //初始化中间线滑动的最上边和最下边
    if (!isFirst) {
      isFirst = true;
      slideTop = mFramingRect.top;
    }
    //绘制中间的线,每次刷新界面，中间的线往下移动MOVE_DISTANCE
    slideTop += MOVE_DISTANCE;
    if (slideTop >= mFramingRect.bottom) {
      slideTop = mFramingRect.top;
    }
    canvas.drawRect(mFramingRect.left, slideTop - MIDDLE_LINE_WIDTH / 2, mFramingRect.right,
        slideTop + MIDDLE_LINE_WIDTH / 2, mLaserPaint);

    //只刷新扫描框的内容，其他地方不刷新
    postInvalidateDelayed(ANIMATION_DELAY, mFramingRect.left, mFramingRect.top, mFramingRect.right,
        mFramingRect.bottom);
  }

  /**
   * 画提示文字
   *
   * @param canvas
   */
  public void drawTipsText(Canvas canvas) {
    String tips = getResources().getString(R.string.qr_scan_tips);
    int screenWidth = canvas.getWidth();
    float tipsWidth = mTipsPaint.measureText(tips);
    //        Log.d(TAG, "------screenWidth = " + screenWidth + " ,tipsWidth = " + tipsWidth + " ,tips = " + tips);
    // 多行文字时的处理，不支持中英文混合
    if (tipsWidth > screenWidth) {
      int lines = (int) (tipsWidth / screenWidth);
      lines += 1;
      for (int i = 0; i < lines; i++) {
        String tempTips;
        if (i == 0) {
          tempTips = tips.substring(0, tips.length() / lines);
        } else if (i == lines - 1) {
          tempTips = tips.substring(tips.length() * i / lines);
        } else {
          tempTips = tips.substring(tips.length() * i / lines, tips.length() * (i + 1) / lines);
        }
        tipsWidth = mTipsPaint.measureText(tempTips);
        canvas.drawText(tempTips, (screenWidth - tipsWidth) / 2,
            (mFramingRect.bottom + (i + 1) * sp2px(getContext(), TIP_TEXT_MARGIN_TOP)), mTipsPaint);
      }
    } else {
      canvas.drawText(tips, (screenWidth - tipsWidth) / 2,
          (mFramingRect.bottom + sp2px(getContext(), TIP_TEXT_MARGIN_TOP)), mTipsPaint);
    }
  }

  @Override protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
    updateFramingRect();
  }

  public synchronized void updateFramingRect() {
    Point viewResolution = new Point(getWidth(), getHeight());
    int width;
    int height;
    int orientation = DisplayUtils.getScreenOrientation(getContext());

    if (orientation != Configuration.ORIENTATION_PORTRAIT) {
      width = findDesiredDimensionInRange(LANDSCAPE_WIDTH_RATIO, viewResolution.x, MIN_FRAME_WIDTH,
          LANDSCAPE_MAX_FRAME_WIDTH);
      height =
          findDesiredDimensionInRange(LANDSCAPE_HEIGHT_RATIO, viewResolution.y, MIN_FRAME_HEIGHT,
              LANDSCAPE_MAX_FRAME_HEIGHT);
    } else {
      width = findDesiredDimensionInRange(PORTRAIT_WIDTH_RATIO, viewResolution.x, MIN_FRAME_WIDTH,
          PORTRAIT_MAX_FRAME_WIDTH);
      height =
          findDesiredDimensionInRange(PORTRAIT_HEIGHT_RATIO, viewResolution.y, MIN_FRAME_HEIGHT,
              PORTRAIT_MAX_FRAME_HEIGHT);
    }

    int leftOffset = (viewResolution.x - width) / 2;
    int topOffset = (viewResolution.y - height) / 2;
    mFramingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
  }
}
