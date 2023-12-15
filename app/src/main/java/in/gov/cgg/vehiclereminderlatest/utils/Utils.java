package in.gov.cgg.vehiclereminderlatest.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import in.gov.cgg.vehiclereminderlatest.R;

public class Utils {
    CustomProgressDialog customProgressDialog;
    Context context;

    public Utils(Context context) {
        try {
            this.context = context;
//            customProgressDialog = new CustomProgressDialog(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImage(String path, ImageView imageView) {
        try {

            Glide.with(context)
                    .load(path)
                    .placeholder(R.drawable.image)
                    .error(R.drawable.camera_primary)
                    .into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
