package cz.maara.strandgui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.HashMap;
import java.util.Map;

public class ColorPickActivity extends Activity {

    public static final String COLOR_KEY = "maara.cz:COLOR_KEY";

    Map<View, StrandColor> viewToColorMap = new HashMap<>();

    private StrandColor color = StrandColor.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_pick);

        viewToColorMap.put(findViewById(R.id.buttonBlack), StrandColor.BLACK);
        viewToColorMap.put(findViewById(R.id.buttonRed), StrandColor.RED);
        viewToColorMap.put(findViewById(R.id.buttonGreen), StrandColor.GREEN);
        viewToColorMap.put(findViewById(R.id.buttonBlue), StrandColor.BLUE);
        viewToColorMap.put(findViewById(R.id.buttonWhite), new StrandColor(63, 63, 63, "white"));
        viewToColorMap.put(findViewById(R.id.buttonYellow), new StrandColor(63, 63, 0, "yellow"));
        viewToColorMap.put(findViewById(R.id.buttonCyan), new StrandColor(0, 63, 63, "cyan"));
        viewToColorMap.put(findViewById(R.id.buttonMagenta), new StrandColor(63, 0, 63, "magenta"));

        setupSeekBarListeners();

        Intent data = getIntent();
        StrandColor color = data.getParcelableExtra(COLOR_KEY);

        ((SeekBar) findViewById(R.id.seekBarRed)).setProgress(color.r == 255 ? 64 : color.r >> 2);
        ((SeekBar) findViewById(R.id.seekBarGreen)).setProgress(color.g == 255 ? 64 : color.g >> 2);
        ((SeekBar) findViewById(R.id.seekBarBlue)).setProgress(color.b == 255 ? 64 : color.b >> 2);
        setColor(color);
    }

    private void setColor(StrandColor color) {
        Button preview = findViewById(R.id.previewButton);
        preview.setBackgroundColor(color.getGuiValue());
        preview.setTextColor(color.getForeground().getGuiValue());
        this.color = color;
    }

    public void onColorButtonClick(View view) {
        StrandColor color = viewToColorMap.get(view);
        if (color == null) color = StrandColor.BLACK;
        ((SeekBar) findViewById(R.id.seekBarRed)).setProgress(color.r >> 2);
        ((SeekBar) findViewById(R.id.seekBarGreen)).setProgress(color.g >> 2);
        ((SeekBar) findViewById(R.id.seekBarBlue)).setProgress(color.b >> 2);
        setColor(color);
    }

    public void selectClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra(COLOR_KEY, this.color);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setupSeekBarListeners() {
        ((SeekBar) findViewById(R.id.seekBarRed)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setColor(ColorPickActivity.this.color.cloneRed(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        ((SeekBar) findViewById(R.id.seekBarGreen)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setColor(ColorPickActivity.this.color.cloneGreen(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        ((SeekBar) findViewById(R.id.seekBarBlue)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setColor(ColorPickActivity.this.color.cloneBlue(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

}
