package in.gov.cgg.vehiclereminderlatest.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by niharika.p on 15-07-2019.
 */

public class CustomDateListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
