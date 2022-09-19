package com.aop.drawingapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

public class MainActivity extends AppCompatActivity {

    DrawingCanvas drawingCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingCanvas = findViewById(R.id.canvas);
    }

    public DrawingCanvas getActiveCanvas() {
        return drawingCanvas;
    }


    public void newFile(View view) {
        MaterialAlertDialogBuilder builder =
                new MaterialAlertDialogBuilder(this);

        // text
        builder.setMessage(R.string.dialog_new_info);

        // confirm
        builder.setPositiveButton(R.string.dialog_confirm,
                (dialogInterface, i) -> drawingCanvas.clearCanvas());

        // cancel doesn't need to do anything
        builder.setNegativeButton(R.string.dialog_cancel,
                (dialogInterface, i) -> {
                });

        builder.show();
    }

    public void saveFile(View view) {
        drawingCanvas.saveBitmap();
    }

    public void printFile(View view) {
        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = drawingCanvas.getBitmapFromCanvas();
        photoPrinter.printBitmap("DrawingApp - print", bitmap);

    }
}