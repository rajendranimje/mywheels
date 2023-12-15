package in.gov.cgg.vehiclereminderlatest.datepicker;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import in.gov.cgg.vehiclereminderlatest.R;


public class ToFragment extends Fragment {

    DatePicker datePicker;
    Button display;
    public static int day;
    public static int month;
    public static int year;
    Calendar calendar = Calendar.getInstance();
    Button to_cancel, to_ok;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to, container, false);
        datePicker = view.findViewById(R.id.datepicker_to);
        to_cancel = view.findViewById(R.id.to_cancel);
        to_ok = view.findViewById(R.id.to_ok);
        getCurrentDate();

        to_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        to_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    public String getCurrentDate() {
        StringBuilder builder = new StringBuilder();
        builder.append(datePicker.getDayOfMonth() + "/");
        builder.append((datePicker.getMonth() + 1) + "/");
        builder.append(datePicker.getYear());

        day = datePicker.getDayOfMonth();
        month = datePicker.getMonth();
        year = datePicker.getYear();
        datePicker.setMaxDate(calendar.getTimeInMillis());

        return builder.toString();
    }

}
