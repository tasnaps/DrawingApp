package com.aop.drawingapp;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;

public class PaintMenuFragment extends Fragment {
    LinearLayout paintContainer;
    HorizontalScrollView paintSelector;

    public PaintMenuFragment() {
        super(R.layout.fragment_paint_menu);
    }

    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_paint_menu,
                container, false);
        paintContainer = view.findViewById(R.id.paintContainer);
        paintSelector = view.findViewById(R.id.paintSelector);

        MainActivity main = (MainActivity) getActivity();
        InitializePaintSelection(main.getActiveCanvas());

        return view;
    }

    private void SelectPaint(int paintNumber) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getActiveCanvas().setActivePaintIndex(paintNumber);

        // reset buttons
        for (int i = 0; i < paintContainer.getChildCount(); i++) {
            Button b = (Button) paintContainer.getChildAt(i);
            b.setText(i == paintNumber ? "✓" : "");
        }

    }

    public void InitializePaintSelection(DrawingCanvas canvas) {
        Paint[] paints = canvas.getPaints();
        for (int i = 0; i < paints.length; i++) {
            Paint p = paints[i];
            Button B = new Button(getContext());
            B.setBackgroundColor(p.getColor());
            B.setTag(i);
            B.setTextSize(28f);
            B.setOnClickListener(view -> {
                SelectPaint((int) view.getTag());
            });

            // contrast check, no dark font on dark background etc
            double contrast = ColorUtils.calculateContrast(p.getColor(), Color.BLACK);
            if (contrast < 5f) B.setTextColor(Color.WHITE);

            paintContainer.addView(B);
        }
    }
}