package cz.maara.strandgui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class TransitionSelectActivity extends Activity {

    public static final String TRANSITION_SELECT_OPTION_EXTRA = "maara.cz:TRANSITION_OPTION";
    private String defaultOption = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_select);

        Intent data = getIntent();
        String option = data.hasExtra(TRANSITION_SELECT_OPTION_EXTRA) ?
                data.getStringExtra(TRANSITION_SELECT_OPTION_EXTRA) :
                "none";

        if (option.equals("pop")) {
            ((RadioButton) findViewById(R.id.radioPop)).setChecked(true);
        } else if (option.equals("blend")) {
            ((RadioButton) findViewById(R.id.radioBlend)).setChecked(true);
        } else if (option.equals("wrap")) {
            ((RadioButton) findViewById(R.id.radioWrap)).setChecked(true);
        } else {
            ((RadioButton) findViewById(R.id.radioNone)).setChecked(true);
        }

        ((RadioButton) findViewById(R.id.radioPop)).setText("fade & rise");
        this.defaultOption = option;
    }

    public void onRadioSelect(View view) {
        String option = "none";
        if (view.equals(findViewById(R.id.radioPop))) option = "pop";
        if (view.equals(findViewById(R.id.radioBlend))) option = "blend";
        if (view.equals(findViewById(R.id.radioWrap))) option = "wrap";

        Intent data = new Intent();
        data.putExtra(TRANSITION_SELECT_OPTION_EXTRA, option);
        setResult(RESULT_OK, data);
        finish();
    }
}
