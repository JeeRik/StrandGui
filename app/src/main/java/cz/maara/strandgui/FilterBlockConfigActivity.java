package cz.maara.strandgui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import java.util.HashMap;
import java.util.Map;

public class FilterBlockConfigActivity extends Activity {

    private static final Map<String, String> nameToConfigMap = new HashMap<String, String>() {
        {
            put("dots", "-1 1 50");
            put("bugs", "3 3 306");
            put("hypno", "1 4 10");
            put("race", "6 24 36");
        }

        public String get(String key) {
            String ret = super.get(key);
            if (ret == null) {
                ret = super.get("hypno");
            }
            return ret;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_block_config);
    }

    public void onClick(View view) {
        String option = ((RadioButton) view).getText().toString();

        String config = nameToConfigMap.get(option);

        Intent data = new Intent();
        data.putExtra(FilterSelectActivity.FILTER_APPENDIX_EXTRA, config);
        setResult(RESULT_OK, data);
        finish();
    }
}
