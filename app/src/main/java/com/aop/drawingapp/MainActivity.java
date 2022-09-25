package com.aop.drawingapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;
import com.google.android.material.slider.Slider;
import com.google.android.material.slider.Slider.OnSliderTouchListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

public class MainActivity extends AppCompatActivity {
    DrawingCanvas drawingCanvas;
    private Switch swCircle;
    private Slider slider;
    float temp;
    int alpha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpCircle();
        drawingCanvas = findViewById(R.id.canvas);
        setUpListener();
        slider = findViewById(R.id.slTransparency);
        slider.addOnSliderTouchListener(touchListener);

    }
    private final OnSliderTouchListener touchListener =
            new OnSliderTouchListener(){
        @Override
                public void onStartTrackingTouch(Slider slider) {
            temp = slider.getValue();
            alpha = ((int) temp);
            drawingCanvas.setAlpha(alpha);

        }
        @Override
                public void onStopTrackingTouch(Slider slider){
            alpha = (int)slider.getValue();
            System.out.println(alpha);
            drawingCanvas.setAlpha(alpha);
        }
            };



    public DrawingCanvas getActiveCanvas() {
        return drawingCanvas;
    }

    private void setUpCircle(){
        this.swCircle = findViewById(R.id.swCircle);
    }
    private void setUpListener(){

        this.swCircle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(drawingCanvas.type.equals("circle")){
                    drawingCanvas.type = "line";
                }else{
                    drawingCanvas.type = "circle";
                }

            }
        });
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