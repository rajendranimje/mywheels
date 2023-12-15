package in.gov.cgg.vehiclereminderlatest.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import in.gov.cgg.vehiclereminderlatest.R;


public class CustomDialog {

    Dialog dialog;

    public CustomDialog(Context context1) {
        dialog = new Dialog(context1);
    }

    public void showDialog(String msg) {
        if (!dialog.isShowing()) {
            dialog.setContentView(R.layout.custom_progress_layout);
            TextView text = dialog.findViewById(R.id.textview);
            if (msg != null && !msg.equalsIgnoreCase(""))
                text.setText(msg);
//            if (dialog.getWindow() != null)
//                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    public void hideDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

}
