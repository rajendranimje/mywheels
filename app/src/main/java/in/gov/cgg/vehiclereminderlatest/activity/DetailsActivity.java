package in.gov.cgg.vehiclereminderlatest.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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
import in.gov.cgg.vehiclereminderlatest.adapter.AlarmReceiver;
import in.gov.cgg.vehiclereminderlatest.adapter.DetailsListAdapter;
import in.gov.cgg.vehiclereminderlatest.bean.ChallansPaidDetails;
import in.gov.cgg.vehiclereminderlatest.bean.DetailsList;
import in.gov.cgg.vehiclereminderlatest.bean.FuelRefillsDetails;
import in.gov.cgg.vehiclereminderlatest.bean.InsuranceClaimsDetails;
import in.gov.cgg.vehiclereminderlatest.bean.InsuranceDetails;
import in.gov.cgg.vehiclereminderlatest.bean.PollutionDetails;
import in.gov.cgg.vehiclereminderlatest.bean.ServicingDetails;
import in.gov.cgg.vehiclereminderlatest.database.Database;
import in.gov.cgg.vehiclereminderlatest.utils.Dates;
import in.gov.cgg.vehiclereminderlatest.utils.GlobalDeclarations;
import in.gov.cgg.vehiclereminderlatest.utils.Utils;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class DetailsActivity extends AppCompatActivity {

    private FloatingActionMenu fab;
    private FloatingActionButton fab_insurance, fab_pollution, fab_servicing;
    private TextView insurance_et_validfrom, insurance_et_validupto, pollution_et_validfrom, pollution_et_validupto;

    private int from_year, from_month, from_day, upto_year, upto_month, upto_day;

    private BottomSheetDialog dialog;
    private TextView take_photo, gallery, title;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST = 200;
    private ImageView insurance_imageView, pollution_imageView, challansPaid_imageView;

    int CAMERAOK = 0;
    public Uri fileUri;
    String filePath, insurancePath, pollutionPath;
    private String timeStamp, flag;
    Database database;
    InsuranceDetails insuranceDetails;
    PollutionDetails pollutionDetails;
    ChallansPaidDetails challansPaidDetails;
    InsuranceClaimsDetails insuranceClaimsDetails;
    FuelRefillsDetails fuelRefillsDetails;
    ServicingDetails servicingDetails;

    ArrayList<InsuranceDetails> insurance_detailslist;
    ArrayList<PollutionDetails> pollution_detailslist;
    ArrayList<ChallansPaidDetails> challansPaid_detailslist;
    ArrayList<InsuranceClaimsDetails> insuranceClaims_detailslist;
    ArrayList<FuelRefillsDetails> fuelRefills_detailslist;
    ArrayList<ServicingDetails> servicing_detailslist;

    private TextView challansPaid_et_date, insuranceClaims_et_date, fuelRefills_et_date, servicing_et_date, servicing_et_next_date;
    private AutoCompleteTextView inputSearch;

    private static final String[] DETAILS = new String[]{
            "Insurance", "Pollution", "Servicing", "Challans Paid", "Insurance Claims", "Fuel Refills"
    };
    private RecyclerView recyclerview_list;
    private ArrayList<DetailsList> detailsListArrayList, list_temp;
    ;
    private DetailsListAdapter listAdapter;
    private TextView tv_no_results;
    Calendar calendar;
    ImageView home;
    ProgressBar pb_insurance_popup, pb_pollution_popup, pb_challanspaid_popup;
    Spinner sp_details;
    Utils utils;
    public String IMAGE_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_temp);

        IMAGE_PATH = getExternalFilesDir(null) + "/";
        init();
        cursorVisibilityOnTouch();
        utils = new Utils(DetailsActivity.this);
        database = new Database(DetailsActivity.this);
        detailsListArrayList = new ArrayList<>();

        getInsuranceDetails();
        getPollutionDetails();
        getServicingDetails();
        getListDetails();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsActivity.this, DashboardActivity.class));
                finish();
            }
        });

        fab_insurance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fab.close(true);
                inputSearch.setText("");
                insurancePopup();

            }
        });

        fab_pollution.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fab.close(true);
                inputSearch.setText("");
                pollutionPopup();

            }
        });

        fab_servicing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fab.close(true);
                inputSearch.setText("");
                servicingPopup();

            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                recyclerview_list.getLayoutManager().scrollToPosition(0);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());

                if (text.length() <= 0) {
                    recyclerview_list.getLayoutManager().scrollToPosition(0);
                }
            }
        });
        inputSearch.setText("");
        sp_details.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (sp_details.getSelectedItemPosition() == 0)
                    recyclerview_list.getLayoutManager().scrollToPosition(0);
                else
                    filter(sp_details.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void showListDetails() {
        list_temp = new ArrayList<>(detailsListArrayList);
        recyclerview_list.setLayoutManager(new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.VERTICAL, false));
        listAdapter = new DetailsListAdapter(DetailsActivity.this, detailsListArrayList, insurance_detailslist.size(), pollution_detailslist.size(), servicing_detailslist.size());
        recyclerview_list.setAdapter(listAdapter);

    }

    private void getListDetails() {
        detailsListArrayList.clear();
        for (int i = insurance_detailslist.size() - 1; i >= 0; i--)
            detailsListArrayList.add(new DetailsList(DetailsList.INSURANCE_TYPE, insurance_detailslist.get(i), null, null, null, null, null));
        for (int i = pollution_detailslist.size() - 1; i >= 0; i--)
            detailsListArrayList.add(new DetailsList(DetailsList.POLLUTION_TYPE, null, pollution_detailslist.get(i), null, null, null, null));
        for (int i = servicing_detailslist.size() - 1; i >= 0; i--)
            detailsListArrayList.add(new DetailsList(DetailsList.SERVICING_TYPE, null, null, null, null, null, servicing_detailslist.get(i)));

        showListDetails();
    }

    private void cursorVisibilityOnTouch() {
        inputSearch = findViewById(R.id.inputSearch);
        View.OnTouchListener otl = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true; // the listener has consumed the event
            }
        };
        inputSearch.setOnTouchListener(otl);
        inputSearch.setInputType(InputType.TYPE_NULL);// disable soft input
        inputSearch.setCursorVisible(true);

        inputSearch.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fab.close(true);
                inputSearch.setInputType(InputType.TYPE_CLASS_TEXT); // restore input type
                inputSearch.onTouchEvent(event); // call native handler
                return true; // consume touch even
            }
        });

    }

  /*  public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        detailsListArrayList.clear();
        if (charText.length() == 0) {
            detailsListArrayList.addAll(list_temp);
        } else {
            //for (DetailsList list : list_temp) {
                if (charText.equals("Insurance")) {
                    for (DetailsList list : list_temp) {
                        this.detailsListArrayList.add();
                    }
                } else if (charText.equals("Pollution")) {
                    this.detailsListArrayList.add(list);
                }
           // }
        }
        listAdapter.notifyDataSetChanged();
    }
*/

    public void filter(String text) {

        if (text.length() > 0) {
            tv_no_results.setVisibility(View.GONE);
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailsActivity.this,
//                    android.R.layout.simple_dropdown_item_1line, DETAILS);
//            inputSearch.setAdapter(adapter);
            if (text.equals("Insurance") && insurance_detailslist.size() > 0) {
                //scrollView.scrollTo(0, insurance_title.getTop());
                recyclerview_list.getLayoutManager().scrollToPosition(0);
            } else if (text.equals("Pollution") && pollution_detailslist.size() > 0) {
                recyclerview_list.getLayoutManager().scrollToPosition(insurance_detailslist.size());
//                recyclerview_list.smoothScrollToPosition(insurance_detailslist.size());
            } else if (text.equals("Servicing") && servicing_detailslist.size() > 0) {
                recyclerview_list.getLayoutManager().scrollToPosition(insurance_detailslist.size() + pollution_detailslist.size());
            } else {
                recyclerview_list.getLayoutManager().scrollToPosition(0);
            }
        } else {
            recyclerview_list.getLayoutManager().scrollToPosition(0);
            tv_no_results.setVisibility(View.GONE);
        }
        // adapter.notifyDataSetChanged();
    }

    private void getServicingDetails() {
        servicing_detailslist = new ArrayList<>();
        servicing_detailslist = database.getServicingDetailsList(GlobalDeclarations.VEHICLENO);
    }

    private void getPollutionDetails() {
        pollution_detailslist = new ArrayList<>();
        pollution_detailslist = database.getPollutionDetailsList(GlobalDeclarations.VEHICLENO);
    }

    public void getInsuranceDetails() {
        insurance_detailslist = new ArrayList<>();
        insurance_detailslist = database.getInsuranceDetailsList(GlobalDeclarations.VEHICLENO);
    }

    private void insurancePopup() {
        flag = "Insurance";
        LayoutInflater factory = LayoutInflater.from(DetailsActivity.this);
        final View insuranve_view = factory.inflate(R.layout.popup_insurance_details, null);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailsActivity.this);
        builder1.setView(insuranve_view);
        final AlertDialog alert = builder1.create();
        alert.show();

        insurance_et_validfrom = insuranve_view.findViewById(R.id.et_validfrom);
        insurance_et_validupto = insuranve_view.findViewById(R.id.et_validupto);
        final EditText insurance_et_agency = insuranve_view.findViewById(R.id.et_agency);

        ImageView insurance_btn_valid_from = insuranve_view.findViewById(R.id.btn_valid_from);
        ImageView insurance_btn_valid_upto = insuranve_view.findViewById(R.id.btn_valid_upto);
        Button btn_insurance_submit = insuranve_view.findViewById(R.id.btn_insurance_submit);
        insurance_imageView = insuranve_view.findViewById(R.id.insurance_image);
        pb_insurance_popup = insuranve_view.findViewById(R.id.pb_insurance_popup);

        insurance_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        insurance_et_validfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validfromdatePicker();
            }
        });
        insurance_btn_valid_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validfromdatePicker();
            }
        });
        insurance_et_validupto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validuptodatePicker();
            }
        });
        insurance_btn_valid_upto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validuptodatePicker();
            }
        });

        btn_insurance_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (insurancePath == null) {
                    Toast.makeText(DetailsActivity.this, "Please Capture Insurance Certificate Image", Toast.LENGTH_SHORT).show();
                }
//                else if (insurance_et_validfrom.getText().toString().trim().isEmpty()) {
//                    Toast.makeText(DetailsActivity.this, "Please Select valid from", Toast.LENGTH_SHORT).show();
//                }
                else if (insurance_et_validupto.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DetailsActivity.this, "Please Select valid upto", Toast.LENGTH_SHORT).show();
                } else if (insurance_et_agency.getText().toString().trim().isEmpty()) {
                    insurance_et_agency.requestFocus();
                    Toast.makeText(DetailsActivity.this, "Please enter valid Agency", Toast.LENGTH_SHORT).show();
                } else {

                    String imagePath = filePath.toString().trim();
                    String validfrom = insurance_et_validfrom.getText().toString().trim();
                    String validupto = insurance_et_validupto.getText().toString().trim();

                    String agency = insurance_et_agency.getText().toString().trim();
                    long timeinmillis = calendar.getTimeInMillis();

                    insuranceDetails = new InsuranceDetails();
                    insuranceDetails.setImage(imagePath);
                    insuranceDetails.setValidFrom(validfrom);
                    insuranceDetails.setValidUpto(validupto);
                    insuranceDetails.setAgency(agency);
                    insuranceDetails.setCurrentDate(Dates.getCurrentDate());
                    insuranceDetails.setId(timeinmillis);
                    insurance_detailslist.add(insuranceDetails);

                    database.insertInsuranceData(insuranceDetails);
                    alert.dismiss();
                    setAlarm(timeinmillis, validupto);
                    getListDetails();
                    listAdapter.notifyDataSetChanged();

                }
            }

        });

    }

    private void pollutionPopup() {
        flag = "Pollution";
        LayoutInflater factory = LayoutInflater.from(DetailsActivity.this);
        final View view = factory.inflate(R.layout.popup_pollution_details, null);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailsActivity.this);
        builder1.setView(view);
        final AlertDialog alert = builder1.create();
        alert.show();

        pollution_et_validfrom = view.findViewById(R.id.et_validfrom);
        pollution_et_validupto = view.findViewById(R.id.et_validupto);

        ImageView btn_valid_from = view.findViewById(R.id.btn_valid_from);
        ImageView btn_valid_upto = view.findViewById(R.id.btn_valid_upto);
        Button btn_insurance_submit = view.findViewById(R.id.btn_insurance_submit);
        pollution_imageView = view.findViewById(R.id.insurance_image);
        pb_pollution_popup = view.findViewById(R.id.pb_pollution_popup);


        pollution_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        pollution_et_validfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validfromdatePicker();
            }
        });
        btn_valid_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validfromdatePicker();
            }
        });
        pollution_et_validupto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validuptodatePicker();
            }
        });
        btn_valid_upto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validuptodatePicker();
            }
        });

        btn_insurance_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pollutionPath == null) {
                    Toast.makeText(DetailsActivity.this, "Please Capture Pollution Certificate Image", Toast.LENGTH_SHORT).show();
                }
//                else if (pollution_et_validfrom.getText().toString().trim().isEmpty()) {
//                    Toast.makeText(DetailsActivity.this, "Please Select valid from", Toast.LENGTH_SHORT).show();
//                }
                else if (pollution_et_validupto.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DetailsActivity.this, "Please Select valid upto", Toast.LENGTH_SHORT).show();
                } else {

                    String imagePath = filePath.toString().trim();
                    String validfrom = pollution_et_validfrom.getText().toString().trim();
                    String validupto = pollution_et_validupto.getText().toString().trim();
                    long timeinmillis = calendar.getTimeInMillis();

                    pollutionDetails = new PollutionDetails();
                    pollutionDetails.setImage(imagePath);
                    pollutionDetails.setValidFrom(validfrom);
                    pollutionDetails.setValidUpto(validupto);
                    pollutionDetails.setCurrentDate(Dates.getCurrentDate());
                    pollutionDetails.setId(timeinmillis);
                    pollution_detailslist.add(pollutionDetails);

                    database.insertPollutionData(pollutionDetails);
                    alert.dismiss();
                    setAlarm(timeinmillis, validupto);
                    getListDetails();
                    listAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    private void servicingPopup() {
        flag = "Servicing";
        LayoutInflater factory = LayoutInflater.from(DetailsActivity.this);
        final View view = factory.inflate(R.layout.popup_servicing_details, null);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailsActivity.this);
        builder1.setView(view);
        final AlertDialog alert = builder1.create();
        alert.show();

        servicing_et_date = view.findViewById(R.id.et_validfrom);
        servicing_et_next_date = view.findViewById(R.id.et_validupto);
        final EditText et_services = view.findViewById(R.id.et_quantity);
        final EditText et_km = view.findViewById(R.id.et_amount_claimed);
        final EditText et_next_km = view.findViewById(R.id.et_amount_settled);
        final CheckBox cb_general_service = view.findViewById(R.id.cb_general_service);
        final CheckBox cb_others = view.findViewById(R.id.cb_others);
        final LinearLayout ll_services = view.findViewById(R.id.ll_services);

        ImageView btn_valid_from = view.findViewById(R.id.btn_valid_from);
        ImageView btn_valid_upto = view.findViewById(R.id.btn_valid_upto);
        Button btn_insurance_submit = view.findViewById(R.id.btn_insurance_submit);

        servicing_et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validfromdatePicker();
            }
        });

        btn_valid_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validfromdatePicker();
            }
        });
        servicing_et_next_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validuptodatePicker();
            }
        });
        btn_valid_upto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validuptodatePicker();
            }
        });

        cb_general_service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        cb_others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ll_services.setVisibility(View.VISIBLE);
                else
                    ll_services.setVisibility(View.GONE);
            }
        });

        btn_insurance_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (servicing_et_date.getText().toString().trim().isEmpty()) {
//                    Toast.makeText(DetailsActivity.this, "Please Select Servicing Date", Toast.LENGTH_SHORT).show();
//                }else
                if (servicing_et_next_date.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DetailsActivity.this, "Please Select Next Servicing Date", Toast.LENGTH_SHORT).show();
                } else if (cb_others.isChecked() && et_services.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DetailsActivity.this, "Please enter Services", Toast.LENGTH_SHORT).show();
                    et_services.requestFocus();
                }
//                else if (et_km.getText().toString().trim().isEmpty()) {
//                    et_km.requestFocus();
//                    Toast.makeText(DetailsActivity.this, "Please enter Servicing at km", Toast.LENGTH_SHORT).show();
//                }
                else if (et_next_km.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DetailsActivity.this, "Please enter Next Servicing at km", Toast.LENGTH_SHORT).show();
                    et_next_km.requestFocus();
                } else {
                    String services;
                    String date = servicing_et_date.getText().toString().trim();
                    String nextdate = servicing_et_next_date.getText().toString().trim();
                    String km = et_km.getText().toString().trim();
                    String next_km = et_next_km.getText().toString().trim();
                    if (cb_general_service.isChecked())
                        services = "General Service";
                    else
                        services = "";
                    if (cb_others.isChecked())
                        services = services + "," + et_services.getText().toString().trim();

                    long timeinmillis = calendar.getTimeInMillis();

                    servicingDetails = new ServicingDetails();
                    servicingDetails.setDate(date);
                    servicingDetails.setNext_date(nextdate);
                    servicingDetails.setServices(services);
                    servicingDetails.setKm(km);
                    servicingDetails.setNext_km(next_km);
                    servicingDetails.setCurrentDate(Dates.getCurrentDate());
                    servicingDetails.setId(timeinmillis);
                    servicing_detailslist.add(servicingDetails);

                    database.insertServicingData(servicingDetails);
                    alert.dismiss();
                    setAlarm(timeinmillis, nextdate);
                    getListDetails();
                    listAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    private void setAlarm(long timeinmillis, String date) {

        String[] date1 = date.split("/");
        int day = Integer.parseInt(date1[0]);
        int mnth = Integer.parseInt(date1[1]);
        int yr = Integer.parseInt(date1[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(yr, mnth - 1, day, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) + 4, 00);

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), (int) timeinmillis, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);

    }

    public int getAlarmId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
        int alarmId = preferences.getInt("ALARM", 1);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ALARM", alarmId + 1).apply();

        return alarmId;
    }

    private void chooseImage() {
        View view = getLayoutInflater().inflate(R.layout.dialog_choose_image, null);
        dialog = new BottomSheetDialog(DetailsActivity.this, R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.show();
        take_photo = view.findViewById(R.id.take_photo);
        gallery = view.findViewById(R.id.gallery);
        title = view.findViewById(R.id.title);

        if (flag.equals("Insurance"))
            title.setText("Insurance Photo");
        else if (flag.equals("Pollution"))
            title.setText("Pollution Photo");
        else if (flag.equals("ChallansPaid"))
            title.setText("ChallansPaid Photo");
        else
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
                DetailsActivity.this,
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

                loadImages();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(DetailsActivity.this,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(DetailsActivity.this,
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

            loadImages();

        }
//        Toast.makeText(this, "filePath:" + filePath, Toast.LENGTH_SHORT).show();
    }

    private void loadImages() {
        if (flag.equals("Insurance")) {
            insurancePath = filePath;
            pb_insurance_popup.setVisibility(View.VISIBLE);
            utils.loadImage(insurancePath, insurance_imageView);
                  /*  Picasso.with(DetailsActivity.this)
                            .load(new File(insurancePath))
                            .centerCrop()
                            .resize(80, 80)
                            .error(R.drawable.camera_primary)
                            .into(insurance_imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    pb_insurance_popup.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    pb_insurance_popup.setVisibility(View.GONE);
                                }
                            });*/

        } else if (flag.equals("Pollution")) {
            pollutionPath = filePath;
            pb_pollution_popup.setVisibility(View.VISIBLE);
            utils.loadImage(pollutionPath, pollution_imageView);
                   /* Picasso.with(DetailsActivity.this)
                            .load(new File(pollutionPath))
                            .centerCrop()
                            .resize(80, 80)
                            .error(R.drawable.camera_primary)
                            .into(pollution_imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    pb_pollution_popup.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    pb_pollution_popup.setVisibility(View.GONE);
                                }
                            });*/
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

    private void validuptodatePicker() {
        calendar = Calendar.getInstance();
        upto_year = calendar.get(Calendar.YEAR);
        upto_month = calendar.get(Calendar.MONTH);
        upto_day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(DetailsActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                String upto_date = dayOfMonth + "/" + (month + 1) + "/" + year;
//                calendar.set(yr, mnth, day, 10, 30, 0);
//                calendar.set(yr, mnth, day, calendar.get(Calendar.HOUR_OF_DAY), (calendar.get(Calendar.MINUTE)) + 2, 0);

                Log.d("date", String.valueOf(dayOfMonth + " " + calendar.get(Calendar.HOUR_OF_DAY) + " " + ((calendar.get(Calendar.MINUTE)) + 3)));

                if (flag.equals("Insurance"))
                    insurance_et_validupto.setText(upto_date);
                else if (flag.equals("Pollution"))
                    pollution_et_validupto.setText(upto_date);
                else if (flag.equals("Servicing"))
                    servicing_et_next_date.setText(upto_date);

            }
        }, upto_year, upto_month, upto_day);
        //datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void validfromdatePicker() {
        Calendar calendar1 = Calendar.getInstance();
        from_year = calendar1.get(Calendar.YEAR);
        from_month = calendar1.get(Calendar.MONTH);
        from_day = calendar1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(DetailsActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String from_date = dayOfMonth + "/" + (month + 1) + "/" + year;
                if (flag.equals("Insurance"))
                    insurance_et_validfrom.setText(from_date);
                else if (flag.equals("Pollution"))
                    pollution_et_validfrom.setText(from_date);
                else if (flag.equals("Servicing"))
                    servicing_et_date.setText(from_date);

            }
        }, from_year, from_month, from_day);
        // datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void init() {
        fab = findViewById(R.id.fab);

        home = findViewById(R.id.home_details);
        fab_insurance = findViewById(R.id.fab_insurance);
        fab_pollution = findViewById(R.id.fab_pollution);
        fab_servicing = findViewById(R.id.fab_servicing);

        inputSearch = findViewById(R.id.inputSearch);
        recyclerview_list = findViewById(R.id.recyclerview_list);
        tv_no_results = findViewById(R.id.tv_no_results);
        sp_details = findViewById(R.id.spinner_details);

    }

    public void openImage(String path) {

//        Dialog dialog = new Dialog(DetailsActivity.this);
//        dialog.setContentView(R.layout.popup_image);
//        dialog.show();


        LayoutInflater factory = LayoutInflater.from(DetailsActivity.this);
        final View view = factory.inflate(R.layout.popup_image, null);

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailsActivity.this);
        builder1.setView(view);
        final AlertDialog alert = builder1.create();
        alert.show();

        ImageView imageView = view.findViewById(R.id.imageView);
        ImageView iv_close = view.findViewById(R.id.close);
        final ProgressBar progressbar = view.findViewById(R.id.progressbar);


        utils.loadImage(path, imageView);
       /* Picasso.with(DetailsActivity.this)
                .load(new File(path))
                .error(R.drawable.camera_primary)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressbar.setVisibility(View.GONE);
                    }
                });*/


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }

}
