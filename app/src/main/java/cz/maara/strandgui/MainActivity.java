package cz.maara.strandgui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cz.maara.androidtest.CommunicatorThread;

public class MainActivity extends Activity {

    public static final String[] RPI_ADDRESSES = new String[]{"192.168.0.22"};
    public static final int RPI_PORT = 12345;

    private final List<View> views = new LinkedList<>();
    private RadioButton[] moduleRadios;
    private int selectedModule = 0;

    private StrandColor
            colorColor = StrandColor.BLACK,
            pulseColor = StrandColor.BLACK,
            waveColor = StrandColor.BLACK;

    private String
            transition = "none",
            filter = "none";
    private int rainbowMotion = 0;

    private final int
            COLOR_COLOR_REQUEST_CODE = 1,
            PULSE_COLOR_REQUEST_CODE = 2,
            WAVE_COLOR_REQUEST_CODE = 3,
            RAINBOW_MOTION_REQUEST_CODE = 4,
            FILTER_SELECT_REQUEST_CODE = 20,
            TRANSITION_SELECT_REQUEST_CODE = 30;

    private CommunicatorThread com;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com = new CommunicatorThread(Arrays.asList(RPI_ADDRESSES), RPI_PORT, new CommunicatorThread.CommunicatorListener() {
            @Override
            public void onConnectionEstablished() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Button u = findViewById(R.id.buttonUpdate);
                        u.setTextSize(16);
                        enableViews();
                    }
                });
            }

            @Override
            public void onConnectionLost() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Button u = findViewById(R.id.buttonUpdate);
                        disableViews("Connecting ...");
                        u.setBackgroundColor(0xffff0000);
                        u.setTextSize(13);
                        u.setText("Connecting ...");
                        u.setEnabled(false);
                    }
                });
            }
        });
        com.start();

        moduleRadios = new RadioButton[]{
                findViewById(R.id.radioColor),
                findViewById(R.id.radioRainbow),
                findViewById(R.id.radioVertigo),
                findViewById(R.id.radioPulse),
                findViewById(R.id.radioWave),
                findViewById(R.id.radioFireplace),
                findViewById(R.id.radioFireworks),
        };

        for (View view : moduleRadios) {
            views.add(view);
        }
        views.add(findViewById(R.id.buttonColorColor));
        views.add(findViewById(R.id.buttonPulseColor));
        views.add(findViewById(R.id.buttonWaveColor));
        views.add(findViewById(R.id.textRainbowMotion));
        views.add(findViewById(R.id.textRainbowCount));
        views.add(findViewById(R.id.buttonUpdate));


        views.remove(findViewById(R.id.radioFireworks));
        views.remove(findViewById(R.id.radioFireplace));

        selectModule(0);
    }

    private void selectModule(int pos) {
        if (pos >= moduleRadios.length || pos < 0) {
            throw new IllegalArgumentException("No module on position " + pos);
        }

        selectedModule = pos;

        for (int i = 0; i < moduleRadios.length; i++) {
            moduleRadios[i].setChecked(i == pos);
        }

        com.sendMessage("m " + getModuleString(), null);
    }

    protected void onRadioColorClick(View view) {
        selectModule(0);
    }

    protected void onRadioRainbowClick(View view) {
        selectModule(1);
    }

    protected void onRadioVertigoClick(View view) {
        selectModule(2);
    }

    protected void onRadioPulseClick(View view) {
        selectModule(3);
    }

    protected void onRadioWaveClick(View view) {
        selectModule(4);
    }

    protected void onRadioFireplaceClick(View view) {
        selectModule(5);
    }

    protected void onRadioFireworksClick(View view) {
        selectModule(6);
    }

    public void onActivityResult(int requestCode, int result_code, Intent data) {
        System.out.println("Result received: code=" + requestCode + ", resultCode=" + result_code);
        if (result_code != RESULT_OK) return;

        switch (requestCode) {
            case COLOR_COLOR_REQUEST_CODE: {
                if (!data.hasExtra(ColorPickActivity.COLOR_KEY)) break;
                StrandColor color = data.getParcelableExtra(ColorPickActivity.COLOR_KEY);
                Button button = findViewById(R.id.buttonColorColor);
                button.setBackgroundColor(color.getGuiValue());
                button.setTextColor(color.getForeground().getGuiValue());
                button.setText(color.name);
                colorColor = color;
                selectModule(0);
                break;
            }
            case PULSE_COLOR_REQUEST_CODE: {
                if (!data.hasExtra(ColorPickActivity.COLOR_KEY)) break;
                StrandColor color = data.getParcelableExtra(ColorPickActivity.COLOR_KEY);
                Button button = findViewById(R.id.buttonPulseColor);
                button.setBackgroundColor(color.getGuiValue());
                button.setTextColor(color.getForeground().getGuiValue());
                button.setText(color.name);
                pulseColor = color;
                selectModule(3);
                break;
            }
            case WAVE_COLOR_REQUEST_CODE: {
                if (!data.hasExtra(ColorPickActivity.COLOR_KEY)) break;
                StrandColor color = data.getParcelableExtra(ColorPickActivity.COLOR_KEY);
                Button button = findViewById(R.id.buttonWaveColor);
                button.setBackgroundColor(color.getGuiValue());
                button.setTextColor(color.getForeground().getGuiValue());
                button.setText(color.name);
                waveColor = color;
                selectModule(4);
                break;
            }
            case RAINBOW_MOTION_REQUEST_CODE: {
                int motion = data.getIntExtra(MotionSelectActivity.MOTION_VECTOR_EXTRA, 0);
                String text = data.getStringExtra(MotionSelectActivity.MOTION_TEXT_EXTRA);
                rainbowMotion = motion;
                TextView view = findViewById(R.id.textRainbowMotion);
                view.setText("Motion: " + text);
                selectModule(1);
                break;
            }
            case TRANSITION_SELECT_REQUEST_CODE: {
                if (!data.hasExtra(TransitionSelectActivity.TRANSITION_SELECT_OPTION_EXTRA)) break;
                String transition = data.getStringExtra(TransitionSelectActivity.TRANSITION_SELECT_OPTION_EXTRA);
                ((TextView) findViewById(R.id.textTransition)).setText(transition);

                this.transition = transition;
                com.sendMessage("t " + transition, null);
                break;
            }
            case FILTER_SELECT_REQUEST_CODE: {
                this.filter = data.getStringExtra(FilterSelectActivity.FILTER_SELECT_OPTION);
                ((TextView) findViewById(R.id.textFilter)).setText(this.filter.trim().split(" ")[0]);
                com.sendMessage("f " + this.filter, null);
                break;
            }

        }
    }

    public void onButtonColorColorClick(View view) {
        Intent intent = new Intent(this, ColorPickActivity.class);
        intent.putExtra(ColorPickActivity.COLOR_KEY, colorColor);
        startActivityForResult(intent, COLOR_COLOR_REQUEST_CODE);
    }

    public void onButtonPulseColorClick(View view) {
        Intent intent = new Intent(this, ColorPickActivity.class);
        intent.putExtra(ColorPickActivity.COLOR_KEY, pulseColor);
        startActivityForResult(intent, PULSE_COLOR_REQUEST_CODE);
    }

    public void onButtonWaveColorClick(View view) {
        Intent intent = new Intent(this, ColorPickActivity.class);
        intent.putExtra(ColorPickActivity.COLOR_KEY, waveColor);
        startActivityForResult(intent, WAVE_COLOR_REQUEST_CODE);
    }

    public void onTransitionRequest(View view) {
        Intent data = new Intent(this, TransitionSelectActivity.class);
        data.putExtra(TransitionSelectActivity.TRANSITION_SELECT_OPTION_EXTRA, transition);
        startActivityForResult(data, TRANSITION_SELECT_REQUEST_CODE);
    }

    public void onFilterRequest(View view) {
        Intent data = new Intent(this, FilterSelectActivity.class);
        data.putExtra(FilterSelectActivity.FILTER_SELECT_OPTION, filter);
        startActivityForResult(data, FILTER_SELECT_REQUEST_CODE);
    }

    public void onRainbowMotionClick(View view) {
        Intent data = new Intent(this, MotionSelectActivity.class);
        data.putExtra(MotionSelectActivity.MOTION_VECTOR_EXTRA, rainbowMotion);
        startActivityForResult(data, RAINBOW_MOTION_REQUEST_CODE);
    }

    private String getModuleString() {
        switch (selectedModule) {
            case 0: return "color " + colorColor.name;
            case 1: return "rainbow " + rainbowMotion;
            case 2: return "vertigo";
            case 3: return "pulse " + pulseColor.name;
            case 4: return "wave " + waveColor.name;
            case 5: return "fireplace";
            case 6: return "fireworks";
        }

        return "";
    }

    public void onUpdate(View view) {
        System.out.println("Module: " + getModuleString());
        System.out.println("Filter: " + this.filter);
        System.out.println("Transition: " + this.transition);

        update();
    }

    private void update() {

        disableViews("Updating");

        com.sendMessage("u", new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enableViews();
                    }
                });
            }
        });
    }

    private void disableViews(String text) {
        for (View view : views) {
            view.setEnabled(false);
        }
        Button u = findViewById(R.id.buttonUpdate);
        u.setBackgroundColor(0xffbbbb00);
        u.setText(text);
    }

    private void enableViews() {
        for (View view : views) {
            view.setEnabled(true);
        }
        Button u = findViewById(R.id.buttonUpdate);
        u.setText("UPDATE");
        u.setBackgroundColor(0xff00ff00);
    }
}