package cz.maara.strandgui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class VertigoSpeedActivity extends Activity {
    
    public static String
            VERTIGO_SPEED_OPTION_EXTRA = "maara.cz:VERTIGO_SPEED_EXTRA",
            VERTIGO_SPEED_TEXT_EXTRA = "maara.cz:VERTIGO_TEXT_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertigo_speed);
    }

    public void onVertigoSpeedSelect(View view) {
        double speed = 1;

        if (view.equals(findViewById(R.id.radioHour))) speed = 0.01;
        if (view.equals(findViewById(R.id.radioMinutes))) speed = 0.12;
        if (view.equals(findViewById(R.id.radioDefault))) speed = 1;
        if (view.equals(findViewById(R.id.radioVzzum))) speed = 20;

        Intent data = new Intent();
        data.putExtra(VERTIGO_SPEED_OPTION_EXTRA, speed);
        data.putExtra(VERTIGO_SPEED_TEXT_EXTRA, ((RadioButton) view).getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}
