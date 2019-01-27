package com.example.acid.crimson;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.SurfaceView;
import android.view.View;
import android.widget.*;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;
import com.skydoves.colorpickerpreference.ColorPickerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";
    private ColorPickerView colors;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PaintView surfView = findViewById(R.id.surfView);

        View.OnClickListener buttonsListener = v -> {
            switch (v.getId()) {
                case R.id.spline:
                    surfView.setMode(PaintView.DrawingMode.Spline);
                    break;
                case R.id.line:
                    surfView.setMode(PaintView.DrawingMode.Line);
                    break;
                case R.id.rectangle:
                    surfView.setMode(PaintView.DrawingMode.Rect);
                    break;
                case R.id.circle:
                    surfView.setMode(PaintView.DrawingMode.Circle);
                    break;
                case R.id.redo:
                    surfView.redo();
                    break;
                case R.id.col:
                    ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                    builder.setTitle("ColorPicker Dialog");
                    builder.setPreferenceName("MyColorPickerDialog");
                    builder.setFlagView(new CustomFlag(this, R.layout.flag_color_layout));
                    builder.setPositiveButton(getString(R.string.confirm), colorEnvelope -> surfView.setColor(colorEnvelope.getColor()));
                    builder.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
                    builder.show();
                    surfView.update();
                    break;
                case R.id.size:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Paint size dialog");
                    //EditText editText = new EditText(dialog.getContext());
                    LinearLayout layout = new LinearLayout(dialog.getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    SeekBar bar = new SeekBar(layout.getContext());
                    //bar.setMax(50);
                    TextView text = new TextView(layout.getContext());
                    text.setTextSize(50);
                    text.setText("0");
                    bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            text.setText(String.valueOf(seekBar.getProgress()));
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                            text.setText(String.valueOf(seekBar.getProgress()));
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            text.setText(String.valueOf(seekBar.getProgress()));
                        }
                    });
                    layout.addView(text);
                    layout.addView(bar);
                    dialog.setView(layout);
                    dialog.setPositiveButton("OK", (dialogInterface, i) -> {
                        surfView.setSize(bar.getProgress());
                        surfView.update();
                    });
                    dialog.show();
                    break;
                case R.id.clear:
                    surfView.clear();
                    break;
                case R.id.fill:
                    ColorPickerDialog.Builder backColor = new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                    backColor.setTitle("ColorPicker Dialog");
                    backColor.setPreferenceName("Background color picker");
                    backColor.setFlagView(new CustomFlag(this, R.layout.flag_color_layout));
                    backColor.setPositiveButton(getString(R.string.confirm), colorEnvelope -> {
                        surfView.setBackColor(colorEnvelope.getColor());
                        surfView.update();
                    });
                    backColor.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
                    backColor.show();
                    break;
            }
        };

        findViewById(R.id.spline).setOnClickListener(buttonsListener);
        findViewById(R.id.line).setOnClickListener(buttonsListener);
        findViewById(R.id.rectangle).setOnClickListener(buttonsListener);
        findViewById(R.id.circle).setOnClickListener(buttonsListener);
        findViewById(R.id.redo).setOnClickListener(buttonsListener);
        findViewById(R.id.col).setOnClickListener(buttonsListener);
        findViewById(R.id.size).setOnClickListener(buttonsListener);
        findViewById(R.id.fill).setOnClickListener(buttonsListener);
        findViewById(R.id.clear).setOnClickListener(buttonsListener);
    }
}