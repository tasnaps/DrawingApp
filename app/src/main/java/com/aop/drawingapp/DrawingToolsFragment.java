package com.aop.drawingapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;


public class DrawingToolsFragment extends Fragment {
    SeekBar sbStrokeWidth;
    public DrawingToolsFragment() {
        super(R.layout.fragment_drawing_tools);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drawing_tools, container, false);
        sbStrokeWidth = view.findViewById(R.id.sbStrokeWidth);
        MainActivity main = (MainActivity) getActivity();
        setStrokeWidth(main.getActiveCanvas());
        return view;
    }

    private void setStrokeWidth(DrawingCanvas canvas) {
        Paint[] paints = canvas.getPaints();
        sbStrokeWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int width = sbStrokeWidth.getProgress();

                for (Paint p : paints) {
                    p.setStrokeWidth(width);
                }
                //
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
