package in.gov.cgg.vehiclereminderlatest.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.gov.cgg.vehiclereminderlatest.R;
import in.gov.cgg.vehiclereminderlatest.bean.LicenceDetails;
import in.gov.cgg.vehiclereminderlatest.database.Database;
import in.gov.cgg.vehiclereminderlatest.utils.GlobalDeclarations;
import in.gov.cgg.vehiclereminderlatest.utils.Utils;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class LicenceActivity extends AppCompatActivity {
    EditText et_licence_no;
    TextView et_licence_valid_upto, tv_licence_no, tv_licence_valid_upto;
    Button btn_save;
    private int year, month, day;
    private String date;
    ImageView date_btn, img_front_capture, img_back_capture;

    private BottomSheetDialog dialog;
    private TextView take_photo, gallery, title;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST = 200;

    int CAMERAOK = 0;
    public Uri fileUri;
    String filePath, frontPath, backPath;
    private String timeStamp, flag;
    ImageView home;
    CardView ll_entry, ll_show;
    Database database;
    Utils utils;
    int pos;
    String licNo;
    public String IMAGE_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence);

        IMAGE_PATH = getExternalFilesDir(null) + "/";

        database = new Database(LicenceActivity.this);
        utils = new Utils(LicenceActivity.this);
        init();
        showDetails();

        if (pos < 0) {
            ll_entry.setVisibility(View.VISIBLE);
            ll_show.setVisibility(View.GONE);
        }
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LicenceActivity.this, DashboardActivity.class));
                finish();
            }
        });
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validuptodatePicker();
            }
        });

        et_licence_valid_upto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validuptodatePicker();
            }
        });
        img_front_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "front";
                chooseImage();

            }
        });

        img_back_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "back";
                chooseImage();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String licence_no = et_licence_no.getText().toString().trim();
                String licence_valid_upto = et_licence_valid_upto.getText().toString().trim();

                if (licence_no.isEmpty()) {
                    et_licence_no.setError("please enter Licence No");
                    et_licence_no.requestFocus();
                } else if (licence_valid_upto.isEmpty()) {
                    Toast.makeText(LicenceActivity.this, "Please enter Licence valid upto ", Toast.LENGTH_SHORT).show();
                } else {

                    LicenceDetails licenceDetails = new LicenceDetails();
                    licenceDetails.setLicNo(licence_no);
                    licenceDetails.setValidUpto(licence_valid_upto);
                    licenceDetails.setFrontImgPath(frontPath);
                    licenceDetails.setBackImgPath(backPath);

                    long rowInserted = database.insertLicenceData(licenceDetails);

                    if (rowInserted != -1) {
                        Toast.makeText(LicenceActivity.this, getResources().getString(R.string.lic_inserted), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(LicenceActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

                    showDetails();
                   /* AlertDialog.Builder alert = new AlertDialog.Builder(LicenceActivity.this);
                    alert.setCancelable(false);
                    alert.setMessage("Licence Details saved successfully");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(LicenceActivity.this, DashboardActivity.class));
                            finish();
                        }
                    });
                    alert.show();
*/

                }
            }
        });
    }

    private void showDetails() {

        ArrayList<LicenceDetails> licenceDetailsArrayList = database.getLicenceDetails();
        pos = licenceDetailsArrayList.size() - 1;

        if (pos >= 0) {
            ll_entry.setVisibility(View.GONE);
            ll_show.setVisibility(View.VISIBLE);

            licNo = licenceDetailsArrayList.get(pos).getLicNo();
            tv_licence_no.setText(licNo);
            tv_licence_valid_upto.setText(licenceDetailsArrayList.get(pos).getValidUpto());
            if (licenceDetailsArrayList.get(pos).getFrontImgPath() != null)
                utils.loadImage(licenceDetailsArrayList.get(pos).getFrontImgPath(), img_front_capture);
            if (licenceDetailsArrayList.get(pos).getBackImgPath() != null)
                utils.loadImage(licenceDetailsArrayList.get(pos).getBackImgPath(), img_back_capture);
            img_front_capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flag = "front";
                    chooseImage();
                }
            });
            img_back_capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flag = "back";
                    chooseImage();
                }
            });
        }

    }

    private void init() {
        et_licence_no = findViewById(R.id.et_lic_no);
        et_licence_valid_upto = findViewById(R.id.et_valid_upto);
        date_btn = findViewById(R.id.date_btn);
        img_front_capture = findViewById(R.id.img_front_capture);
        img_back_capture = findViewById(R.id.img_back_capture);
        btn_save = findViewById(R.id.btn_save);
        home = findViewById(R.id.home_licence);
        ll_entry = findViewById(R.id.ll_entry);
        ll_show = findViewById(R.id.ll_show);
        tv_licence_no = findViewById(R.id.tv_licence_no);
        tv_licence_valid_upto = findViewById(R.id.tv_licence_valid_upto);

    }

    private void validuptodatePicker() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(LicenceActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "/" + (month + 1) + "/" + year;
                et_licence_valid_upto.setText(date);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void chooseImage() {
        View view = getLayoutInflater().inflate(R.layout.dialog_choose_image, null);
        dialog = new BottomSheetDialog(LicenceActivity.this, R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.show();
        take_photo = view.findViewById(R.id.take_photo);
        gallery = view.findViewById(R.id.gallery);
        title = view.findViewById(R.id.title);

        title.setText("Photo");
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                takeCameraImage();

            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectPhotofromGallery();
            }
        });

    }

    //CAMERA OPEN AND TAKE PHOTO CODE
    protected void takeCameraImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    public void selectPhotofromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    public Uri getOutputMediaFileUri(int type) {
        File imageFile = getOutputMediaFile(type);
        Uri imageUri = FileProvider.getUriForFile(
                LicenceActivity.this,
                "in.gov.cgg.vehiclereminderlatest.provider", //(use your app signature + ".provider" )
                imageFile);
        return imageUri;
    }

    //DIRECTORY CREATION CODE
    private File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "Oops! Failed create " + "Android File Upload"
                        + " directory");
                return null;
            }
        }

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpg";

        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {

            mediaFile = new File(mediaStorageDir.getPath() + File.separator + flag + timeStamp);
        } else {
            return null;
        }

        return mediaFile;
    }

    //SET PHOTO AND NAMES ON CLICK OF OK or TICK IN CAMERA
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                CAMERAOK++;

                filePath = IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME;
                String Image_name = flag + timeStamp;
                filePath = filePath + "/" + Image_name;

                loadLicImage();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(LicenceActivity.this,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(LicenceActivity.this,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            filePath = IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME;
            String Image_name = flag + timeStamp;
            filePath = filePath + "/" + Image_name;

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imagePath = cursor.getString(columnIndex);
            try {
                copyFile(new File(imagePath), new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            cursor.close();

            loadLicImage();
        }
//        Toast.makeText(this, "filePath:" + filePath, Toast.LENGTH_SHORT).show();
    }

    private void loadLicImage() {

        if (flag.equals("front")) {
            frontPath = filePath;
            utils.loadImage(frontPath, img_front_capture);

            long rowUpdated = database.updateLicFrontImgPath(frontPath, licNo);
            if (rowUpdated != -1)
                Toast.makeText(LicenceActivity.this, getResources().getString(R.string.img_updated), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(LicenceActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

        } else if (flag.equals("back")) {
            backPath = filePath;
            utils.loadImage(backPath, img_back_capture);
            long rowUpdated = database.updateLicBackImgPath(backPath, licNo);
            if (rowUpdated != -1)
                Toast.makeText(LicenceActivity.this, getResources().getString(R.string.img_updated), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(LicenceActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

        }
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LicenceActivity.this, DashboardActivity.class));
        finish();
    }
}
