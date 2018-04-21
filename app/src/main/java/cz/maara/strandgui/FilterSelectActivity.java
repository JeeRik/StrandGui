package cz.maara.strandgui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import java.util.HashMap;
import java.util.Map;

public class FilterSelectActivity extends Activity {

    public static final String
            FILTER_SELECT_OPTION = "maara.cz:FILTER_SELECT_OPTION",
            FILTER_APPENDIX_EXTRA = "maara.cz:FILTER_APPENDIX_EXTRA";


    private static final int BLOCK_CONFIG_CODE = 1;

    private final Map<String, RadioButton> map = new HashMap<>();

    private String filter = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_select);

        map.put("none", (RadioButton) findViewById(R.id.radioNone));
        map.put("door", (RadioButton) findViewById(R.id.radioDoor));
        map.put("window", (RadioButton) findViewById(R.id.radioWindow));
        map.put("chase", (RadioButton) findViewById(R.id.radioChase));
        map.put("arrow", (RadioButton) findViewById(R.id.radioArrow));
        map.put("gaze", (RadioButton) findViewById(R.id.radioGaze));
        map.put("sonar", (RadioButton) findViewById(R.id.radioSonar));

        Intent data = getIntent();

        String option = data.getStringExtra(FILTER_SELECT_OPTION);

        option = option.trim().split(" ")[0];

        selectOption("none");
    }

    private void selectOption(String option) {
        RadioButton radio = map.get(option);
        if (radio == null) radio = map.get("none");

        radio.setSelected(true);
    }

    public void onSelect(View view) {
        String option = ((RadioButton) view).getText().toString();

        if (option.equals("none") || option.equals("door") || option.equals("window") || option.equals("sonar")) {
            commit(option);
        } else if (option.equals("chase") || option.equals("arrow") || option.equals("gaze")) {
            // TODO: Fire config selector
            filter = option;
            startActivityForResult(new Intent(this, FilterBlockConfigActivity.class), BLOCK_CONFIG_CODE);
        }
    }

    public void onActivityResult(int requestCode, int result_code, Intent data) {
        if (result_code == RESULT_OK) {
            commit(filter + " " + data.getStringExtra(FILTER_APPENDIX_EXTRA));
        }
    }

    public void commit(String option) {
        Intent data = new Intent();
        data.putExtra(FILTER_SELECT_OPTION, option);
        setResult(RESULT_OK, data);
        finish();
    }
}
