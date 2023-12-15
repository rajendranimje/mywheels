package in.gov.cgg.vehiclereminderlatest.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import in.gov.cgg.vehiclereminderlatest.BuildConfig;
import in.gov.cgg.vehiclereminderlatest.R;
import in.gov.cgg.vehiclereminderlatest.databinding.ActivityInfoBinding;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class InfoActivity extends AppCompatActivity {

    ImageView home;
    ActivityInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info);

        binding.homeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfoActivity.this, DashboardActivity.class));
                finish();
            }
        });

        binding.appVersion.setText(BuildConfig.VERSION_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.longDes.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            binding.featuresDes.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
    }
}