package com.aop.drawingapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import androidx.annotation.Nullable;


public class DrawingCanvas extends View {
    private LinkedList<PaintedPath> Paths = new LinkedList<>();
    private PaintedPath activePath;

    private int selectedPaint = 0;
    private Paint[] paints;

    public DrawingCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializePaints();
    }

    private void initializePaints() {
        int[] colorCodes = new int[]{Color.BLACK, Color.WHITE,
                Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                Color.DKGRAY, Color.CYAN, Color.MAGENTA, Color.LTGRAY};

        paints = new Paint[colorCodes.length];

        for (int i = 0; i < colorCodes.length; i++) {
            int colorCode = colorCodes[i];

            Paint p = new Paint();
            p.setColor(colorCode);
            p.setAntiAlias(true);
            p.setStrokeWidth(15f);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeJoin(Paint.Join.MITER);

            paints[i] = p;
        }
    }


    public Paint[] getPaints() {
        return paints;
    }

    public void setActivePaintIndex(int paintNumber) {
        selectedPaint = paintNumber;
    }

    @Override
    protected void onDraw(Canvas targetCanvas) {
        super.onDraw(targetCanvas);

        for (PaintedPath p : Paths) {
            targetCanvas.drawPath(p.path, p.paint);
        }
    }

    public void clearCanvas() {
        Paths = new LinkedList<PaintedPath>();
        // invalidate for redraw
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // grab touch coordinates
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activePath = new PaintedPath(paints[selectedPaint]);
                activePath.path.moveTo(x, y);
                Paths.add(activePath);
                break;
            case MotionEvent.ACTION_MOVE:
                activePath.path.lineTo(x, y);
                break;
        }

        // invalidate canvas for redraw
        invalidate();
        return true;
    }

    public Bitmap getBitmapFromCanvas() {
        Canvas canvas = new Canvas();
        // setup bitmap
        Bitmap bitmap = createBitmap();
        canvas.setBitmap(bitmap);
        // draw white background
        canvas.drawColor(Color.WHITE);

        // remember to call draw on the canvas to save paths to bitmap
        draw(canvas);
        return bitmap;
    }

    private Bitmap createBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(
                getWidth(),
                getHeight(),
                Bitmap.Config.ARGB_8888);
        return bitmap;
    }

    public void saveBitmap() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String datetime = dateFormat.format(System.currentTimeMillis());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "Drawing_" + datetime + ".png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        Bitmap bitmap = getBitmapFromCanvas();

        Uri url = null;

        // reference to content resolver
        ContentResolver cr = getContext().getContentResolver();

        try {

            // insert values into external content-contentresolver table,
            // get Uri to the row in return,
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            OutputStream imageOut = cr.openOutputStream(url);

            // bitmap compress compresses the bitmap into given outputstream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOut);

            // close the stream
            imageOut.flush();
            imageOut.close();

            Toast.makeText(
                    getContext(),
                    "Saved image as: " + values.get(MediaStore.Audio.Media.DISPLAY_NAME),
                    Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            // clear the content resolver row, since we didn't actually insert anything
            if (url != null) cr.delete(url, null, null);
            url = null;

            Toast.makeText(
                    getContext(),
                    "Saving image failed!",
                    Toast.LENGTH_SHORT).show();

        }
    }


    /**
     * Helper class for Painting,
     * grouping Path + Paint for different colored paths
     */
    private class PaintedPath {
        public PaintedPath(Paint paint) {
            this.path = new Path();
            this.paint = paint;
        }

        public Path path;
        public Paint paint;
    }

}
