package cz.maara.strandgui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MotionSelectActivity extends Activity {

    public static final String
            MOTION_VECTOR_EXTRA = "maara.cz:MOTION_VECTOR_EXTRA",
            MOTION_TEXT_EXTRA = "maara.cz:MOTION_TEXT_EXTRA";

    public static String getVectorText(int motion) {
        switch (motion) {
            case -7: return "<<<";
            case -3: return "<<";
            case -1: return "<";
            case 0: return "0";
            case 1: return ">";
            case 3: return ">>";
            case 7: return ">>>";
            default:
                return "0";
        }
    }
    public static int getVectorValue(String text) {
        if (text.equals("0")) return 0;
        if (text.equals(">")) return 1;
        if (text.equals(">>")) return 3;
        if (text.equals(">>>")) return 7;
        if (text.equals("<")) return -1;
        if (text.equals("<<")) return -3;
        if (text.equals("<<<")) return -7;
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_select);

        Intent data = getIntent();
        int motion = data.getIntExtra(MOTION_VECTOR_EXTRA, 0);
        ((SeekBar) findViewById(R.id.seekBarMotion)).setProgress(motion + 3);

        ((SeekBar) findViewById(R.id.seekBarMotion)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int progress;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                commit(progress - 3);
            }
        });

        ((TextView) findViewById(R.id.textFastForward)).setText("<<<");
        ((TextView) findViewById(R.id.textForward)).setText("<<");
        ((TextView) findViewById(R.id.textSlowForward)).setText(" < ");
    }

    public void onIconClicked(View view) {
        String text = ((TextView) view).getText().toString();
        text = text.trim();
        int value = getVectorValue(text);
        commit(value);
    }

    private void commit(int value) {
        System.out.println("Motion selected value: " + value);
        System.out.println("Motion selected text: \"" + getVectorText(value) + "\"");
        Intent data = new Intent();
        data.putExtra(MOTION_VECTOR_EXTRA, value);
        data.putExtra(MOTION_TEXT_EXTRA, getVectorText(value));
        setResult(RESULT_OK, data);
        finish();
    }


}
