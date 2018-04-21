package cz.maara.strandgui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class FilterBlockConfigActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_block_config);
    }

    public void onClick(View view) {
        String option = ((RadioButton) view).getText().toString();
        Intent data = new Intent();
        data.putExtra(FilterSelectActivity.FILTER_APPENDIX_EXTRA, option);
        setResult(RESULT_OK, data);
        finish();
    }
}
