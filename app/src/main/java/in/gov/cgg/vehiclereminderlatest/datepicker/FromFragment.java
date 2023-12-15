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


public class FromFragment extends Fragment {

    DatePicker datePicker;
    Button from_cancel, from_ok;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_from, container, false);

        datePicker = view.findViewById(R.id.datepicker_from);
        from_cancel = view.findViewById(R.id.from_cancel);
        from_ok = view.findViewById(R.id.from_ok);
        getCurrentDate();

        from_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        from_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   ((ReportAnalysisActivity) getActivity()).getTabHost().setCurrentTab(2);
                FromFragment parent;
                parent =  getActivity().getParent();
                parent.switchTab(value);*/
            }
        });

        return view;
    }


    public String getCurrentDate() {

        int day;
        int month;
        int year;
        Calendar calendar = Calendar.getInstance();

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
