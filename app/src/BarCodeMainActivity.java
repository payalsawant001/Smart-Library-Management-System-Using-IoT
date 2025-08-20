package com.akash.booklibrary.barcodem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.akash.booklibrary.BookDetailsActivity;
import com.akash.booklibrary.R;
import com.akash.booklibrary.databinding.ActivityBarCodeMainBinding;
import com.akash.booklibrary.utils.Tools;
import com.akash.booklibrary.utils.scanner.ml.u.BarcodeGraphic;
import com.akash.booklibrary.utils.scanner.ml.u.BarcodeGraphicTracker;
import com.akash.booklibrary.utils.scanner.ml.u.BarcodeTrackerFactory;
import com.akash.booklibrary.utils.scanner.ml.ui.CameraSource;
import com.akash.booklibrary.utils.scanner.ml.ui.CameraSourcePreview;
import com.akash.booklibrary.utils.scanner.ml.ui.GraphicOverlay;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BarCodeMainActivity extends AppCompatActivity implements BarcodeGraphicTracker.BarcodeUpdateListener{
    private ActivityBarCodeMainBinding binding;

    private static final String TAG = "Barcode-reader";

    // intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    // constants used to pass extra data in the intent
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    public static final String BarcodeObject = "Barcode";

    boolean autoFocus = true;
    boolean useFlash = false;

    //private ImageView img_scanner_flash,img_scanner_auto_focus,img_cancel_scan;
    //private TextView txt_image_selection,txtScanQRHeader;
    //private LinearLayout control2;

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    // helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    String type,aadhar_number;

    Tools tools = new Tools();
    /* AadharCard aadharData;
     String aadhar_name,aadhar_gender="",aadhar_address,aadhar_pin_code,email_id,mobile_number,aadhar_dob="";*/
    int rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_bar_code_main);
        binding = ActivityBarCodeMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        TextView title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.name);
        title.setText("BARCODE SCANNER");

        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>) findViewById(R.id.graphicOverlay);

        // read parameters from the intent used to launch the activity.
        autoFocus = getIntent().getBooleanExtra(AutoFocus, false);
        useFlash = getIntent().getBooleanExtra(UseFlash, false);

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash);
        } else {
            requestCameraPermission();
        }

        gestureDetector = new GestureDetector(this, new CaptureGestureListener());
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        /*Snackbar.make(mGraphicOverlay, "Tap to capture. Pinch/Stretch to zoom",
                        Snackbar.LENGTH_LONG)
                .show();*/

        initComponents();
    }

    private void initComponents() {

        /*binding.txtImageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, 111);
            }
        });*/

        binding.imgScannerFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rc == PackageManager.PERMISSION_GRANTED) {
                    if (!useFlash){
                        useFlash = true;
                        binding.imgScannerFlash.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_scanner_flash_on_white));
                        mCameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        // createCameraSource(autoFocus,useFlash);
                    }else {
                        mCameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        binding.imgScannerFlash.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_scanner_flash_off_white));
                        useFlash = false;
                        // createCameraSource(autoFocus,useFlash);
                    }
                }else {
                    requestCameraPermission();
                }
            }
        });
        binding.imgScannerAutoFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rc == PackageManager.PERMISSION_GRANTED) {
                    if (!autoFocus){
                        autoFocus = true;
                        binding.imgScannerAutoFocus.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_scanner_auto_focus_on_white));
                        mCameraSource.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                        //createCameraSource(autoFocus,useFlash);
                    }else {
                        binding.imgScannerAutoFocus.setImageDrawable(getResources().getDrawable(R.drawable.ic_code_scanner_auto_focus_off_white));
                        autoFocus = false;
                        mCameraSource.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    }
                }else {
                    requestCameraPermission();
                }
            }
        });
        binding.imgCancelScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarCodeMainActivity.this.finish();
            }
        });
    }

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        /*findViewById(R.id.topLayout).setOnClickListener(listener);*/
        findViewById(R.id.root).setOnClickListener(listener);
        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                        Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean b = scaleGestureDetector.onTouchEvent(e);

        boolean c = gestureDetector.onTouchEvent(e);

        return b || c || super.onTouchEvent(e);
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        autoFocus = true;
        //-//Log.e("autoFocusV:...",""+autoFocus);
        //-//Log.e("useFlashV:...",""+useFlash);

        Context context = getApplicationContext();

        // A barcode detector is created to track barcodes.  An associated multi-processor instance
        // is set to receive the barcode detection results, track the barcodes, and maintain
        // graphics for each barcode on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each barcode.
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, BarCodeMainActivity.this);
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());

        if (!barcodeDetector.isOperational()) {
            // Note: The first time that an app using the barcode or face API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any barcodes
            // and/or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the barcode detector to detect small barcodes
        // at long distances.
        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                /*.setRequestedPreviewSize(1600, 1024)*/
                .setRequestedPreviewSize(1280, 720)
                .setRequestedFps(15.0f);


        // make sure that auto focus is an available option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(
                    autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : Camera.Parameters.FOCUS_MODE_AUTO);//null
        }

        mCameraSource = builder
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .build();
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mPreview.isActivated()){
            mPreview.release();
        }
        /*if (mPreview != null){
            mPreview.release();
        }*/
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            autoFocus = getIntent().getBooleanExtra(AutoFocus,false);
            useFlash = getIntent().getBooleanExtra(UseFlash, false);
            createCameraSource(autoFocus, useFlash);
            return;
        }

        //-//Log.e(TAG, "Permission not granted: results len = " + grantResults.length +" Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // this code will be executed after 2 seconds
                        float scaleValue = 1.0f;
                        float increment = 1.0f;
                        scaleValue += increment;
                        mCameraSource.doZoom(scaleValue);
                    }
                }, 2000);
            } catch (IOException e) {
                //-//Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    /**
     * onTap returns the tapped barcode result to the calling Activity.
     *
     * @param rawX - the raw position of the tap
     * @param rawY - the raw position of the tap.
     * @return true if the activity is ending.
     */
    private boolean onTap(float rawX, float rawY) {
        // Find tap point in preview frame coordinates.
        int[] location = new int[2];
        mGraphicOverlay.getLocationOnScreen(location);
        float x = (rawX - location[0]) / mGraphicOverlay.getWidthScaleFactor();
        float y = (rawY - location[1]) / mGraphicOverlay.getHeightScaleFactor();

        // Find the barcode whose center is closest to the tapped point.
        Barcode best = null;
        float bestDistance = Float.MAX_VALUE;
        for (BarcodeGraphic graphic : mGraphicOverlay.getGraphics()) {
            Barcode barcode = graphic.getBarcode();
            if (barcode.getBoundingBox().contains((int) x, (int) y)) {
                // Exact hit, no need to keep looking.
                best = barcode;
                break;
            }
            float dx = x - barcode.getBoundingBox().centerX();
            float dy = y - barcode.getBoundingBox().centerY();
            float distance = (dx * dx) + (dy * dy);  // actually squared distance
            if (distance < bestDistance) {
                best = barcode;
                bestDistance = distance;
            }
        }

        if (best != null) {
            Intent data = new Intent();
            data.putExtra(BarcodeObject, best);
            setResult(CommonStatusCodes.SUCCESS, data);
            finish();
            return true;
        }
        return false;
    }

    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         * <p/>
         * Once a scale has ended, {@link ScaleGestureDetector#getFocusX()}
         * and {@link ScaleGestureDetector#getFocusY()} will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         */
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mCameraSource.doZoom(detector.getScaleFactor());
        }
    }

    @Override
    public void onBarcodeDetected(Barcode barcode) {
        // do something with barcode data returned
        /*Intent data = new Intent();
        data.putExtra(BarcodeFormat, barcode.format);
        data.putExtra(BarcodeType, barcode.valueFormat);
        data.putExtra(BarcodeValue, barcode.rawValue);
        setResult(RESULT_OK, data);
        finish();*/

       // Log.e("qrResult:...",""+barcode.rawValue);
        MediaPlayer mp = MediaPlayer.create(BarCodeMainActivity.this, R.raw.scanner_tone_n);
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(BarCodeMainActivity.this, R.raw.scanner_tone_n);
            } mp.start();
        } catch(Exception e) { e.printStackTrace(); }


        /*if (type!=null && !type.isEmpty()){
            processAadharData(barcode.rawValue);
        }else {
            getBillDetails(barcode.rawValue);
        }*/

        //Toast.makeText(BarCodeMainActivity.this,""+barcode.rawValue,Toast.LENGTH_LONG).show();
        Log.e("BArCodeResult:...",""+barcode.rawValue);

        //BarCodeMainActivity.this.finish();
        gotoResult(barcode.rawValue);
    }

    void gotoResult(String barCodeResult){
        Intent intent = new Intent(BarCodeMainActivity.this, BookDetailsActivity.class);
        intent.putExtra("barCodeResult", ""+barCodeResult);
        startActivity(intent);
        finish();
    }
}