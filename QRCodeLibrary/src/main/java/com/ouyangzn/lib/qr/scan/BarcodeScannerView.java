package com.ouyangzn.lib.qr.scan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public abstract class BarcodeScannerView extends FrameLayout implements Camera.PreviewCallback {
  private Camera mCamera;
  private CameraPreview mPreview;
  private ViewFinderView mViewFinderView;
  private Rect mFramingRectInPreview;
  private CameraHandlerThread mCameraHandlerThread;
  private Boolean mFlashState;
  private boolean mAutofocusState = true;

  public BarcodeScannerView(Context context) {
    super(context);
  }

  public BarcodeScannerView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  public void setTipsSize(int size) {
    if (mViewFinderView != null) {
      mViewFinderView.setTipsSize(size);
    }
  }

  public final void setupLayout(Camera camera) {
    removeAllViews();

    mPreview = new CameraPreview(getContext(), camera, this);
    RelativeLayout relativeLayout = new RelativeLayout(getContext());
    relativeLayout.setGravity(Gravity.CENTER);
    relativeLayout.setBackgroundColor(Color.BLACK);
    relativeLayout.addView(mPreview);
    addView(relativeLayout);

    mViewFinderView = createViewFinderView(getContext());
    if (mViewFinderView instanceof View) {
      addView((View) mViewFinderView);
    } else {
      throw new IllegalArgumentException("IViewFinder object returned by "
          + "'createViewFinderView()' should be instance of android.view.View");
    }
  }

  /**
   * <p>Method that creates view that represents visual appearance of a barcode scanner</p>
   * <p>Override it to provide your own view for visual appearance of a barcode scanner</p>
   *
   * @param context {@link Context}
   * @return {@link View} that implements {@link ViewFinderView}
   */
  protected ViewFinderView createViewFinderView(Context context) {
    return new ViewFinderView(context);
  }

  public void startCamera(int cameraId) {
    if (mCameraHandlerThread == null) {
      mCameraHandlerThread = new CameraHandlerThread(this);
    }
    mCameraHandlerThread.startCamera(cameraId);
  }

  public void setupCameraPreview(Camera camera) {
    mCamera = camera;
    if (mCamera != null) {
      setupLayout(mCamera);
      mViewFinderView.setupViewFinder();
      if (mFlashState != null) {
        setFlash(mFlashState);
      }
      setAutoFocus(mAutofocusState);
    }
  }

  public void startCamera() {
    startCamera(-1);
  }

  public void stopCamera() {
    if (mCamera != null) {
      mPreview.stopCameraPreview();
      mPreview.setCamera(null, null);
      mCamera.release();
      mCamera = null;
    }
    if (mCameraHandlerThread != null) {
      mCameraHandlerThread.quit();
      mCameraHandlerThread = null;
    }
  }

  public void stopCameraPreview() {
    if (mPreview != null) {
      mPreview.stopCameraPreview();
    }
  }

  protected void resumeCameraPreview() {
    if (mPreview != null) {
      mPreview.showCameraPreview();
    }
  }

  public synchronized Rect getFramingRectInPreview(int previewWidth, int previewHeight) {
    if (mFramingRectInPreview == null) {
      Rect framingRect = mViewFinderView.getFramingRect();
      int viewFinderViewWidth = mViewFinderView.getWidth();
      int viewFinderViewHeight = mViewFinderView.getHeight();
      if (framingRect == null || viewFinderViewWidth == 0 || viewFinderViewHeight == 0) {
        return null;
      }

      Rect rect = new Rect(framingRect);
      rect.left = rect.left * previewWidth / viewFinderViewWidth;
      rect.right = rect.right * previewWidth / viewFinderViewWidth;
      rect.top = rect.top * previewHeight / viewFinderViewHeight;
      rect.bottom = rect.bottom * previewHeight / viewFinderViewHeight;

      mFramingRectInPreview = rect;
    }
    return mFramingRectInPreview;
  }

  public boolean getFlash() {
    if (mCamera != null && CameraUtils.isFlashSupported(mCamera)) {
      Camera.Parameters parameters = mCamera.getParameters();
      if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  public void setFlash(boolean flag) {
    mFlashState = flag;
    if (mCamera != null && CameraUtils.isFlashSupported(mCamera)) {

      Camera.Parameters parameters = mCamera.getParameters();
      if (flag) {
        if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
          return;
        }
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
      } else {
        if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)) {
          return;
        }
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
      }
      mCamera.setParameters(parameters);
    }
  }

  public void toggleFlash() {
    if (mCamera != null && CameraUtils.isFlashSupported(mCamera)) {
      Camera.Parameters parameters = mCamera.getParameters();
      if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
      } else {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
      }
      mCamera.setParameters(parameters);
    }
  }

  public void setAutoFocus(boolean state) {
    mAutofocusState = state;
    if (mPreview != null) {
      mPreview.setAutoFocus(state);
    }
  }

  public void hideScanLine() {
    if (mViewFinderView != null) {
      mViewFinderView.showScanLine(false);
    }
  }

  public void showScanLine() {
    if (mViewFinderView != null) {
      mViewFinderView.showScanLine(true);
    }
  }
}
