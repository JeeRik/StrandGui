package cz.maara.strandgui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class RainbowMultipleActivity extends Activity {

    public static final String RAINBOW_MULTIPLE_OPTION_EXTRA = "maara.cz:RAINBOW_MULTIPLE_OPTION_EXTRA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainbow_multiple);
    }

    public void onClick(View view) {
        Intent data = new Intent();
        data.putExtra(RAINBOW_MULTIPLE_OPTION_EXTRA, ((RadioButton) view).getText().toString().toLowerCase());
        setResult(RESULT_OK, data);
        finish();
    }
}
