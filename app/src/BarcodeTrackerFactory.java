package com.akash.booklibrary.utils.scanner.ml.u;
import android.content.Context;


import androidx.annotation.NonNull;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.akash.booklibrary.utils.scanner.ml.ui.GraphicOverlay;

public class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    private Context mContext;

    public BarcodeTrackerFactory(GraphicOverlay<BarcodeGraphic> mGraphicOverlay,
                                 Context mContext) {
        this.mGraphicOverlay = mGraphicOverlay;
        this.mContext = mContext;
    }

   /* @Override
    public Tracker<com.google.mlkit.vision.barcode.Barcode> create(com.google.mlkit.vision.barcode.Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
        return new BarcodeGraphicTracker(mGraphicOverlay, graphic, mContext);
    }*/

    @NonNull
    @Override
    public Tracker<Barcode> create(@NonNull Barcode barcode) {
        /*return null;*/
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
        return new BarcodeGraphicTracker(mGraphicOverlay, graphic, mContext);
    }

    /*@Override
    public Tracker<Barcode> create(Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
        return new BarcodeGraphicTracker(mGraphicOverlay, graphic, mContext);
    }*/



}