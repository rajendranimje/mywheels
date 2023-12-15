package in.gov.cgg.vehiclereminderlatest.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ajts.androidmads.library.ExcelToSQLite;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import in.gov.cgg.vehiclereminderlatest.Interface.VehicleInterface;
import in.gov.cgg.vehiclereminderlatest.Interface.VehicleimgUpload;
import in.gov.cgg.vehiclereminderlatest.R;
import in.gov.cgg.vehiclereminderlatest.adapter.AlarmReceiver;
import in.gov.cgg.vehiclereminderlatest.adapter.VehicleListAdapter;
import in.gov.cgg.vehiclereminderlatest.bean.BatteryDetails;
import in.gov.cgg.vehiclereminderlatest.bean.InsuranceDetails;
import in.gov.cgg.vehiclereminderlatest.bean.PollutionDetails;
import in.gov.cgg.vehiclereminderlatest.bean.RCDetails;
import in.gov.cgg.vehiclereminderlatest.bean.ServicingDetails;
import in.gov.cgg.vehiclereminderlatest.bean.VehicleDetails;
import in.gov.cgg.vehiclereminderlatest.database.Database;
import in.gov.cgg.vehiclereminderlatest.database.TempDatabase;
import in.gov.cgg.vehiclereminderlatest.databinding.ActivityDashboardBinding;
import in.gov.cgg.vehiclereminderlatest.databinding.PopupVehicleDetailsBinding;
import in.gov.cgg.vehiclereminderlatest.utils.CustomDialog;
import in.gov.cgg.vehiclereminderlatest.utils.Dates;
import in.gov.cgg.vehiclereminderlatest.utils.GlobalDeclarations;
import in.gov.cgg.vehiclereminderlatest.utils.Utils;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, VehicleimgUpload, VehicleInterface {

    VehicleListAdapter adapter;
    ArrayList<VehicleDetails> detailslist, list_temp;
    VehicleDetails vehicleDetails;
    TextView et_rcvalidupto, et_warranty_end_date, et_purchased_on;
    ImageView btn_date;
    private int year, month, day;
    private String date;
    Database database;
    private String item;
    private ImageView rc_front_img, rc_back_img, bill_img, battery_img;

    private BottomSheetDialog dialog;
    private TextView take_photo, gallery, title;
    public static final int CAMERA_REQUEST = 100;
    private static final int GALLERY_REQUEST = 200;
    private static final int EXCEL_REQUEST = 300;

    int CAMERAOK = 0;
    public Uri fileUri;
    String filePath, vehicleImg1Path, RCFrontImgPath, RCBackImgPath, vehicleImg2Path,
            vehicleImg3Path, insurancePath, pollutionPath, billImgPath, batteryImgPath;
    private String timeStamp, flag;
    Calendar calendar;
    private SQLiteToExcel sqliteToExcel;
    public String TAG = "msg";
    private static final int BUFFER = 2048;
    Uri uri;
    Utils utils;
    private TextView ins_tv_validfrom, ins_tv_validupto,
            poll_tv_validfrom, pollution_et_validupto,
            servicing_et_date, servicing_et_next_date;
    private ImageView insurance_imageView, pollution_imageView, imgview;
    private ImageView edit_ins, edit_rc, edit_poll, edit_serv, edit_battery;
    LinearLayout ll_ins_entry, ll_ins_show, ll_rc_entry, ll_rc_show,
            ll_poll_entry, ll_poll_show, ll_battery_entry, ll_battery_show;
    CardView ll_serv_entry, ll_serv_show;
    private int ins_pos, rc_pos, poll_pos, serv_pos, battery_pos;
    ActivityDashboardBinding binding;
    PopupVehicleDetailsBinding vehicleDetailsBinding;
    TempDatabase tempDatabase;
    boolean export_flag;
    CustomDialog customDialog;
    public String IMAGE_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        setSupportActionBar(binding.appBarDashboard.toolbar);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        IMAGE_PATH = getExternalFilesDir(null) + "/";

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, binding.appBarDashboard.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cursorVisibilityOnTouch();

        customDialog = new CustomDialog(DashboardActivity.this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        utils = new Utils(DashboardActivity.this);
//        calendar = Calendar.getInstance();
        database = new Database(DashboardActivity.this);
        tempDatabase = new TempDatabase(DashboardActivity.this);
        vehicleDetails = new VehicleDetails();
        showVehicleDetails();

        binding.appBarDashboard.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicleDetails.setFlag(false);
                showVehiclePopup(vehicleDetails);
            }
        });

        binding.appBarDashboard.contentDashboard.inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = binding.appBarDashboard.contentDashboard.inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }
        });
        binding.appBarDashboard.contentDashboard.inputSearch.setText("");

        binding.appBarDashboard.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(IMAGE_PATH + GlobalDeclarations.ZIP_NAME);
                if (file.exists()) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("application/zip");
                    i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));
                    startActivity(Intent.createChooser(i, "Share via"));
                } else
                    Toast.makeText(DashboardActivity.this, "Please Export vehicle", Toast.LENGTH_SHORT).show();
            }
        });
        binding.appBarDashboard.contentDashboard.selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showVehicleDetails();
                String value;
                if (isChecked)
                    value = "Yes";
                else
                    value = "No";
                for (int i = 0; i < detailslist.size(); i++)
                    database.updateFlag(value, detailslist.get(i).getVehicleNo());
                showVehicleDetails();
            }
        });

        binding.appBarDashboard.contentDashboard.tvSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVehicleDetails();
                binding.appBarDashboard.contentDashboard.tvSelectAll.setVisibility(View.GONE);
                binding.appBarDashboard.contentDashboard.tvDeselect.setVisibility(View.VISIBLE);
                for (int i = 0; i < detailslist.size(); i++) {
                    detailslist.get(i).setChecked("Yes");
                }
                adapter.notifyDataSetChanged();
            }
        });
        binding.appBarDashboard.contentDashboard.tvDeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVehicleDetails();
                binding.appBarDashboard.contentDashboard.tvSelectAll.setVisibility(View.VISIBLE);
                binding.appBarDashboard.contentDashboard.tvDeselect.setVisibility(View.GONE);
                for (int i = 0; i < detailslist.size(); i++) {
                    detailslist.get(i).setChecked("No");
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Thread thread;
        switch (id) {

            case R.id.action_import:
                importZip();
                return true;
            case R.id.action_export:
                selectVehicles();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    public void selectVehicles() {

        File mediaStorageDir = new File(IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "Oops! Failed create " + "Android File Upload"
                        + " directory");
            }
        }

        detailslist = new ArrayList<>();
        detailslist = database.getVehicleDetails();

        ArrayList<RCDetails> rclist;
        ArrayList<InsuranceDetails> insurancelist;
        ArrayList<PollutionDetails> pollutionlist;
        ArrayList<ServicingDetails> servicinglist;
        ArrayList<BatteryDetails> batterylist;
        export_flag = false;
        if (detailslist != null && detailslist.size() > 0) {
            for (int i = 0; i < detailslist.size(); i++) {
                if (detailslist.get(i).getChecked().equalsIgnoreCase("Yes")) {
                    export_flag = true;
                    VehicleDetails vehicleDetails = new VehicleDetails();

                    vehicleDetails.setId(detailslist.get(i).getId());
                    vehicleDetails.setVehicleNo(detailslist.get(i).getVehicleNo());
                    vehicleDetails.setMake(detailslist.get(i).getMake());
                    vehicleDetails.setModel(detailslist.get(i).getModel());
                    vehicleDetails.setYear(detailslist.get(i).getYear());
                    vehicleDetails.setFueltype(detailslist.get(i).getFueltype());
                    vehicleDetails.setVehicleImg1Path(detailslist.get(i).getVehicleImg1Path());
                    vehicleDetails.setVehicleImg2Path(detailslist.get(i).getVehicleImg2Path());
                    vehicleDetails.setVehicleImg3Path(detailslist.get(i).getVehicleImg3Path());
                    vehicleDetails.setRcDate(detailslist.get(i).getRcDate());
                    vehicleDetails.setInsDate(detailslist.get(i).getInsDate());
                    vehicleDetails.setPollDate(detailslist.get(i).getPollDate());
                    vehicleDetails.setServDate(detailslist.get(i).getServDate());
                    vehicleDetails.setBatteryDate(detailslist.get(i).getBatteryDate());
                    vehicleDetails.setChecked("No");

                    tempDatabase.insertVehicleData(vehicleDetails);

                    rclist = database.getRCDetails(detailslist.get(i).getVehicleNo());
                    if (rclist != null && rclist.size() > 0) {
                        rc_pos = rclist.size() - 1;

                        RCDetails rcDetails = new RCDetails();
                        rcDetails.setRcFrontImgPath(rclist.get(rc_pos).getRcFrontImgPath());
                        rcDetails.setRcBackImgPath(rclist.get(rc_pos).getRcBackImgPath());
                        rcDetails.setVehicleNo(rclist.get(rc_pos).getVehicleNo());
                        rcDetails.setValidUpto(rclist.get(rc_pos).getValidUpto());
                        rcDetails.setCurrentDate(rclist.get(rc_pos).getCurrentDate());
                        rcDetails.setId(rclist.get(rc_pos).getId());

                        tempDatabase.insertRCData(rcDetails);
                    }

                    insurancelist = database.getInsuranceDetailsList(detailslist.get(i).getVehicleNo());
                    if (insurancelist != null && insurancelist.size() > 0) {

                        ins_pos = insurancelist.size() - 1;

                        InsuranceDetails insuranceDetails = new InsuranceDetails();
                        insuranceDetails.setImage(insurancelist.get(ins_pos).getImage());
                        insuranceDetails.setVehicleNo(insurancelist.get(ins_pos).getVehicleNo());
                        insuranceDetails.setValidFrom(insurancelist.get(ins_pos).getValidFrom());
                        insuranceDetails.setValidUpto(insurancelist.get(ins_pos).getValidUpto());
                        insuranceDetails.setAgency(insurancelist.get(ins_pos).getAgency());
                        insuranceDetails.setAmount(insurancelist.get(ins_pos).getAmount());
                        insuranceDetails.setCurrentDate(insurancelist.get(ins_pos).getCurrentDate());
                        insuranceDetails.setId(insurancelist.get(ins_pos).getId());

                        tempDatabase.insertInsuranceData(insuranceDetails);
                    }

                    pollutionlist = database.getPollutionDetailsList(detailslist.get(i).getVehicleNo());

                    if (pollutionlist != null && pollutionlist.size() > 0) {
                        poll_pos = pollutionlist.size() - 1;

                        PollutionDetails pollutionDetails = new PollutionDetails();
                        pollutionDetails.setImage(pollutionlist.get(poll_pos).getImage());
                        pollutionDetails.setVehicleNo(pollutionlist.get(poll_pos).getVehicleNo());
                        pollutionDetails.setValidFrom(pollutionlist.get(poll_pos).getValidFrom());
                        pollutionDetails.setValidUpto(pollutionlist.get(poll_pos).getValidUpto());
                        pollutionDetails.setCurrentDate(pollutionlist.get(poll_pos).getCurrentDate());
                        pollutionDetails.setId(pollutionlist.get(poll_pos).getId());

                        tempDatabase.insertPollutionData(pollutionDetails);
                    }

                    servicinglist = database.getServicingDetailsList(detailslist.get(i).getVehicleNo());
                    if (servicinglist != null && servicinglist.size() > 0) {
                        serv_pos = servicinglist.size() - 1;

                        ServicingDetails servicingDetails = new ServicingDetails();
                        servicingDetails.setDate(servicinglist.get(serv_pos).getDate());
                        servicingDetails.setVehicleNo(servicinglist.get(serv_pos).getVehicleNo());
                        servicingDetails.setNext_date(servicinglist.get(serv_pos).getNext_date());
                        servicingDetails.setGeneralService(servicinglist.get(serv_pos).getGeneralService());
                        servicingDetails.setGsAmount(servicinglist.get(serv_pos).getGsAmount());
                        servicingDetails.setServices(servicinglist.get(serv_pos).getServices());
                        servicingDetails.setOsAmount(servicinglist.get(serv_pos).getOsAmount());
                        servicingDetails.setKm(servicinglist.get(serv_pos).getKm());
                        servicingDetails.setNext_km(servicinglist.get(serv_pos).getNext_km());
                        servicingDetails.setCurrentDate(servicinglist.get(serv_pos).getCurrentDate());
                        servicingDetails.setId(servicinglist.get(serv_pos).getId());

                        tempDatabase.insertServicingData(servicingDetails);
                    }
                    batterylist = database.getBatteryDetails(detailslist.get(i).getVehicleNo());
                    if (batterylist != null && batterylist.size() > 0) {
                        battery_pos = batterylist.size() - 1;

                        BatteryDetails batteryDetails = new BatteryDetails();
                        batteryDetails.setBillImgPath(batterylist.get(battery_pos).getBillImgPath());
                        batteryDetails.setBatteryImgPath(batterylist.get(battery_pos).getBatteryImgPath());
                        batteryDetails.setVehicleNo(batterylist.get(battery_pos).getVehicleNo());
                        batteryDetails.setMake(batterylist.get(battery_pos).getMake());
                        batteryDetails.setWarrantyEndDate(batterylist.get(battery_pos).getWarrantyEndDate());
                        batteryDetails.setPurchasedOn(batterylist.get(battery_pos).getPurchasedOn());
                        batteryDetails.setCurrentDate(batterylist.get(battery_pos).getCurrentDate());
                        batteryDetails.setId(batterylist.get(battery_pos).getId());

                        tempDatabase.insertBatteryData(batteryDetails);
                    }
//                Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (!export_flag)
            Toast.makeText(this, "Please select vehicle", Toast.LENGTH_SHORT).show();
        else
            new exportAsync().execute();

    }
    //Open Content provider for ZIP files

    private void importZip() {
        File filePath = new File(IMAGE_PATH + GlobalDeclarations.ZIP_NAME);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/zip");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

//        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
        startActivityForResult(intent, EXCEL_REQUEST);
    }

    //For Extracting ZIP File

    private boolean unpackZip(String path) {
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            File mediaStorageDir = new File(IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("TAG", "Oops! Failed create " + "Android File Upload"
                            + " directory");
                }
            }


            while ((ze = zis.getNextEntry()) != null) {

                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME + "/");
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME + "/" + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }
            zis.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    //Adding Files or Images Path to create ZIP File

    private void packzip() {
        String rootPath = IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME;
        File destFolder = new File(rootPath);
        File[] destFiles = destFolder.listFiles();


        ArrayList<String> jsonALFiles = new ArrayList<>();

        if (destFiles != null) {
            for (int i = 0; i < destFiles.length; i++) {
                if (destFiles[i].isFile()) {
                    jsonALFiles.add(i, destFolder + "/" + destFiles[i].getName());
                }
            }

            if (jsonALFiles.size() > 0) {

                String[] jsonFiles = new String[jsonALFiles.size()];
                jsonFiles = jsonALFiles.toArray(jsonFiles);
                ZipOutputStream outputStream = zip(jsonFiles, IMAGE_PATH + GlobalDeclarations.ZIP_NAME);

                if (outputStream != null) {
//                    Toast.makeText(DashboardActivity.this, "Successufully zipfile created", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    //For Creating and Store ZIP File

    public ZipOutputStream zip(String[] jsonFiles, String zipFN) {
        ZipOutputStream out = null;
        try {
            FileOutputStream dest = new FileOutputStream(zipFN);
            out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            for (int i = 0; i < jsonFiles.length; i++) {
                FileInputStream fi = new FileInputStream(jsonFiles[i]);
                BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(jsonFiles[i].substring(jsonFiles[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    private void showVehicleDetails() {

        detailslist = new ArrayList<>();
        detailslist = database.getVehicleDetails();
        list_temp = new ArrayList<>(detailslist);

        binding.appBarDashboard.contentDashboard.recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        adapter = new VehicleListAdapter(this, this, detailslist, this);
        binding.appBarDashboard.contentDashboard.recyclerview.setAdapter(adapter);
        binding.appBarDashboard.contentDashboard.recyclerview.scrollToPosition(detailslist.size() - 1);

        if (detailslist != null && detailslist.size() > 0) {
            binding.appBarDashboard.contentDashboard.recyclerview.setVisibility(View.VISIBLE);
            binding.appBarDashboard.contentDashboard.tvEmpty.setVisibility(View.GONE);

            binding.appBarDashboard.contentDashboard.inputSearch.setVisibility(View.VISIBLE);
//            binding.appBarDashboard.contentDashboard.selectAll.setVisibility(View.VISIBLE);
            binding.appBarDashboard.contentDashboard.tvSelectAll.setVisibility(View.VISIBLE);
            binding.appBarDashboard.contentDashboard.tvDeselect.setVisibility(View.GONE);
        } else {
            binding.appBarDashboard.contentDashboard.recyclerview.setVisibility(View.GONE);
            binding.appBarDashboard.contentDashboard.tvEmpty.setVisibility(View.VISIBLE);

            binding.appBarDashboard.contentDashboard.inputSearch.setVisibility(View.GONE);
//            binding.appBarDashboard.contentDashboard.selectAll.setVisibility(View.GONE);
            binding.appBarDashboard.contentDashboard.tvSelectAll.setVisibility(View.GONE);
            binding.appBarDashboard.contentDashboard.tvDeselect.setVisibility(View.GONE);
        }
    }

    public void showVehiclePopup(final VehicleDetails vehicleDetails) {

        this.vehicleDetails = vehicleDetails;
        String vehicleNo = vehicleDetails.getVehicleNo();
        final int position = vehicleDetails.getPosition();

        vehicleImg1Path = vehicleDetails.getVehicleImg1Path();
        vehicleImg2Path = vehicleDetails.getVehicleImg2Path();
        vehicleImg3Path = vehicleDetails.getVehicleImg3Path();

        vehicleDetailsBinding = PopupVehicleDetailsBinding.inflate(LayoutInflater.from(this));

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);
        builder1.setView(vehicleDetailsBinding.getRoot());
        final AlertDialog alert = builder1.create();
        alert.show();

        if (vehicleNo != null && vehicleDetails.isFlag()) {
            vehicleDetailsBinding.etVehicleno1.setText(vehicleNo.substring(0, 2));
            vehicleDetailsBinding.etVehicleno2.setText(vehicleNo.substring(3, 5));
            vehicleDetailsBinding.etVehicleno3.setText(vehicleNo.substring(6, 8));
            vehicleDetailsBinding.etVehicleno4.setText(vehicleNo.substring(9, 13));
        }

        if (vehicleDetails.getMake() != null && vehicleDetails.isFlag())
            vehicleDetailsBinding.etMake.setText(vehicleDetails.getMake());
        if (vehicleDetails.getModel() != null && vehicleDetails.isFlag())
            vehicleDetailsBinding.etModel.setText(vehicleDetails.getModel());
        if (vehicleDetails.getYear() != null && vehicleDetails.isFlag())
            vehicleDetailsBinding.etYear.setText(vehicleDetails.getYear());
        if (vehicleDetails.getVehicleImg1Path() != null && vehicleDetails.isFlag())
            utils.loadImage(vehicleDetails.getVehicleImg1Path(), vehicleDetailsBinding.vehicleImg);
        if (vehicleDetails.getVehicleImg2Path() != null && vehicleDetails.isFlag())
            utils.loadImage(vehicleDetails.getVehicleImg2Path(), vehicleDetailsBinding.vehicleImgTwo);
        if (vehicleDetails.getVehicleImg3Path() != null && vehicleDetails.isFlag())
            utils.loadImage(vehicleDetails.getVehicleImg3Path(), vehicleDetailsBinding.vehicleImgThree);

        if (vehicleDetails.getFueltype() != null && vehicleDetails.isFlag()) {
            String[] fuel_array = getResources().getStringArray(R.array.fuel_array);
            for (int i = 1; i < fuel_array.length; i++) {
                if (vehicleDetails.getFueltype().equalsIgnoreCase(fuel_array[i])) {
                    vehicleDetailsBinding.etFueltype.setSelection(i);
                    break;
                }
            }
        }

        vehicleDetailsBinding.vehicleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(GlobalDeclarations.vehicle_img1_flag, vehicleDetailsBinding.vehicleImg);
            }
        });

        vehicleDetailsBinding.vehicleImgTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "VehicleTwo";
                chooseImage(GlobalDeclarations.vehicle_img2_flag, vehicleDetailsBinding.vehicleImgTwo);
            }
        });
        vehicleDetailsBinding.vehicleImgThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "VehicleThree";
                chooseImage(GlobalDeclarations.vehicle_img3_flag, vehicleDetailsBinding.vehicleImgThree);
            }
        });

        vehicleDetailsBinding.etFueltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        autoPopulate();
        //edit onClick
        vehicleDetailsBinding.editRcImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setup the alert builder
                LayoutInflater factory = LayoutInflater.from(DashboardActivity.this);
                final View v = factory.inflate(R.layout.popup_vehicle_details, null);
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);
                builder1.setView(v);
                final AlertDialog alert = builder1.create();
                alert.show();
            }
        });


        vehicleDetailsBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateVehicleDetails()) {
                    String vehicleNo = vehicleDetailsBinding.etVehicleno1.getText().toString().trim() + " " +
                            vehicleDetailsBinding.etVehicleno2.getText().toString().trim() + " " +
                            vehicleDetailsBinding.etVehicleno3.getText().toString().trim() + " " +
                            vehicleDetailsBinding.etVehicleno4.getText().toString().trim();

                    long timeinmillis = System.currentTimeMillis();
                    if (!vehicleDetails.isFlag()) {

                        vehicleDetails.setVehicleNo(vehicleNo);
                        vehicleDetails.setMake(vehicleDetailsBinding.etMake.getText().toString().trim());
                        vehicleDetails.setModel(vehicleDetailsBinding.etModel.getText().toString().trim());
                        vehicleDetails.setFueltype(item);
                        vehicleDetails.setYear(vehicleDetailsBinding.etYear.getText().toString().trim());
                        vehicleDetails.setVehicleImg1Path(vehicleImg1Path);
                        vehicleDetails.setVehicleImg2Path(vehicleImg2Path);
                        vehicleDetails.setVehicleImg3Path(vehicleImg3Path);
                        vehicleDetails.setRcDate(null);
                        vehicleDetails.setInsDate(null);
                        vehicleDetails.setPollDate(null);
                        vehicleDetails.setServDate(null);
                        vehicleDetails.setBatteryDate(null);
                        vehicleDetails.setChecked("No");
                        vehicleDetails.setId(timeinmillis);

                        detailslist.add(vehicleDetails);

                        long rowInserted = database.insertVehicleData(vehicleDetails);
                        if (rowInserted != -1) {
                            Toast.makeText(DashboardActivity.this, getResources().getString(R.string.vehicle_inserted), Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                        } else
                            Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();


                    } else {


                        detailslist.get(position).setVehicleNo(vehicleNo);
                        detailslist.get(position).setMake(vehicleDetailsBinding.etMake.getText().toString().trim());
                        detailslist.get(position).setModel(vehicleDetailsBinding.etModel.getText().toString().trim());
                        detailslist.get(position).setYear(vehicleDetailsBinding.etYear.getText().toString().trim());
                        detailslist.get(position).setFueltype(item);
                        detailslist.get(position).setVehicleImg1Path(vehicleImg1Path);
                        detailslist.get(position).setVehicleImg2Path(vehicleImg2Path);
                        detailslist.get(position).setVehicleImg3Path(vehicleImg3Path);
                        detailslist.get(position).setRcDate(vehicleDetails.getRcDate());
                        detailslist.get(position).setInsDate(vehicleDetails.getInsDate());
                        detailslist.get(position).setPollDate(vehicleDetails.getPollDate());
                        detailslist.get(position).setServDate(vehicleDetails.getServDate());
                        detailslist.get(position).setBatteryDate(vehicleDetails.getBatteryDate());
                        detailslist.get(position).setChecked(vehicleDetails.getChecked());

                        alert.dismiss();
                        long rowUpdated = database.updateVehicleData(detailslist.get(position));
                        if (rowUpdated != -1) {
                            Toast.makeText(DashboardActivity.this, getResources().getString(R.string.vehicle_updated), Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                        } else
                            Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                    }
//                    adapter.notifyDataSetChanged();
                    showVehicleDetails();
                }
            }
        });
    }

    private boolean validateVehicleDetails() {
        String vehicleno1 = vehicleDetailsBinding.etVehicleno1.getText().toString().trim();
        String vehicleno2 = vehicleDetailsBinding.etVehicleno2.getText().toString().trim();
        String vehicleno3 = vehicleDetailsBinding.etVehicleno3.getText().toString().trim();
        String vehicleno4 = vehicleDetailsBinding.etVehicleno4.getText().toString().trim();
        if (vehicleno1.isEmpty() || vehicleno1.length() < 2) {
            Toast.makeText(DashboardActivity.this, "Please enter valid VehicleNo", Toast.LENGTH_SHORT).show();
            vehicleDetailsBinding.etVehicleno1.requestFocus();
            return false;
        } else if (vehicleno2.isEmpty() || vehicleno2.length() < 2) {
            Toast.makeText(DashboardActivity.this, "Please enter valid VehicleNo", Toast.LENGTH_SHORT).show();
            vehicleDetailsBinding.etVehicleno2.requestFocus();
            return false;
        } else if (vehicleno3.isEmpty() || vehicleno3.length() < 2) {
            Toast.makeText(DashboardActivity.this, "Please enter valid VehicleNo", Toast.LENGTH_SHORT).show();
            vehicleDetailsBinding.etVehicleno3.requestFocus();
            return false;
        } else if (vehicleno4.isEmpty() || vehicleno4.length() < 4) {
            Toast.makeText(DashboardActivity.this, "Please enter valid VehicleNo", Toast.LENGTH_SHORT).show();
            vehicleDetailsBinding.etVehicleno4.requestFocus();
            return false;
        } else if (vehicleDetailsBinding.etMake.getText().toString().trim().isEmpty()) {
            vehicleDetailsBinding.etMake.setError("Please enter valid Vehicle Make");
            vehicleDetailsBinding.etMake.requestFocus();
            return false;
        } else if (vehicleDetailsBinding.etModel.getText().toString().trim().isEmpty()) {
            vehicleDetailsBinding.etModel.setError("Please enter valid Vehicle Model");
            vehicleDetailsBinding.etModel.requestFocus();
            return false;
        } else if (vehicleDetailsBinding.etYear.getText().toString().trim().isEmpty()
                || vehicleDetailsBinding.etYear.getText().toString().trim().length() != 4) {
            vehicleDetailsBinding.etYear.setError("Please enter valid Year");
            vehicleDetailsBinding.etYear.requestFocus();
            return false;
        } else if (vehicleDetailsBinding.etFueltype.getSelectedItemPosition() == 0) {
            Toast.makeText(DashboardActivity.this, "Please Select FuelType", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void showRCPopup(final String vehicleNo) {

        flag = "RC";
        GlobalDeclarations.VEHICLENO = vehicleNo;
        LayoutInflater factory = LayoutInflater.from(DashboardActivity.this);
        final View v = factory.inflate(R.layout.popup_rc_details, null);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);
        builder1.setView(v);
        final AlertDialog alert = builder1.create();
        alert.show();

        rc_front_img = v.findViewById(R.id.rc_front_img);
        rc_back_img = v.findViewById(R.id.rc_back_img);
        ll_rc_entry = v.findViewById(R.id.ll_rc_entry);
        ll_rc_show = v.findViewById(R.id.ll_rc_show);
        edit_rc = v.findViewById(R.id.edit_rc);
        ImageView close_rc = v.findViewById(R.id.close_rc);

        showRcDetails(v, vehicleNo);

        if (rc_pos < 0) {
            ll_rc_entry.setVisibility(View.VISIBLE);
            ll_rc_show.setVisibility(View.GONE);
            edit_rc.setVisibility(View.GONE);
        }

        et_rcvalidupto = v.findViewById(R.id.et_rcvalidupto);
        btn_date = v.findViewById(R.id.date_btn);
        Button btn_submit = v.findViewById(R.id.btn_submit);

        rc_front_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = GlobalDeclarations.rc_front_img_flag;
                chooseImage(GlobalDeclarations.rc_front_img_flag, rc_front_img);
            }
        });
        rc_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = GlobalDeclarations.rc_back_img_flag;
                chooseImage(GlobalDeclarations.rc_back_img_flag, rc_back_img);
            }
        });


        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validuptodatePicker();
            }
        });

        et_rcvalidupto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validuptodatePicker();
            }
        });

        edit_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rc_pos = -1;
                ll_rc_entry.setVisibility(View.VISIBLE);
                ll_rc_show.setVisibility(View.GONE);
                edit_rc.setVisibility(View.GONE);
                RCFrontImgPath = null;
                RCBackImgPath = null;
            }
        });

        close_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (RCFrontImgPath == null) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.select_rc_front_img), Toast.LENGTH_SHORT).show();
                } else if (RCBackImgPath == null) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.select_rc_back_img), Toast.LENGTH_SHORT).show();
                } else if (et_rcvalidupto.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.select_valid_upto), Toast.LENGTH_SHORT).show();
                } else {

                    String validupto = et_rcvalidupto.getText().toString().trim();
                    long timeinmillis = calendar.getTimeInMillis();

                    RCDetails rcDetails = new RCDetails();
                    rcDetails.setRcFrontImgPath(RCFrontImgPath);
                    rcDetails.setRcBackImgPath(RCBackImgPath);
                    rcDetails.setVehicleNo(vehicleNo);
                    rcDetails.setValidUpto(validupto);
                    rcDetails.setCurrentDate(Dates.getCurrentDate());
                    rcDetails.setId(timeinmillis);

                    long rowInserted = database.insertRCData(rcDetails);
                    if (rowInserted != -1)
                        setAlarm(timeinmillis, validupto, GlobalDeclarations.rc_flag);

                    long rowUpdated = database.updateRcDate(validupto, vehicleNo);

                    if (rowUpdated != -1)
                        Toast.makeText(DashboardActivity.this, getResources().getString(R.string.rc_updated), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                    alert.dismiss();
                    showVehicleDetails();

//                    getListDetails();
//                    listAdapter.notifyDataSetChanged();

                }
            }

        });
    }

    private void showRcDetails(View view, String vehicleNo) {

        TextView tv_validupto_show = view.findViewById(R.id.tv_rcvalidupto);
        ImageView rc_front_img_show = view.findViewById(R.id.rc_front_img_show);
        ImageView rc_back_img_show = view.findViewById(R.id.rc_back_img_show);

        final ArrayList<RCDetails> rcDetailsArrayList = database.getRCDetails(vehicleNo);
        rc_pos = rcDetailsArrayList.size() - 1;

        if (rc_pos >= 0) {
            ll_rc_entry.setVisibility(View.GONE);
            ll_rc_show.setVisibility(View.VISIBLE);
            edit_rc.setVisibility(View.VISIBLE);

            tv_validupto_show.setText(rcDetailsArrayList.get(rc_pos).getValidUpto());

            if (rcDetailsArrayList.get(rc_pos).getRcFrontImgPath() != null)
                utils.loadImage(rcDetailsArrayList.get(rc_pos).getRcFrontImgPath(), rc_front_img_show);

            rc_front_img_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    chooseImage(GlobalDeclarations.rc_front_img_flag, rc_front_img);
                    openImage(rcDetailsArrayList.get(rc_pos).getRcFrontImgPath());
                }
            });
            if (rcDetailsArrayList.get(rc_pos).getRcBackImgPath() != null)
                utils.loadImage(rcDetailsArrayList.get(rc_pos).getRcBackImgPath(), rc_back_img_show);

            rc_back_img_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    chooseImage(GlobalDeclarations.rc_back_img_flag, rc_back_img);
                    openImage(rcDetailsArrayList.get(rc_pos).getRcBackImgPath());
                }
            });
        }
    }

    @Override
    public void insurancePopup(final String vehicleNo) {
        flag = "Insurance";
        GlobalDeclarations.VEHICLENO = vehicleNo;
        LayoutInflater factory = LayoutInflater.from(DashboardActivity.this);
        final View insuranve_view = factory.inflate(R.layout.popup_insurance_details, null);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);
        builder1.setView(insuranve_view);
        final AlertDialog alert = builder1.create();
        alert.show();

        insurance_imageView = insuranve_view.findViewById(R.id.insurance_image);

        edit_ins = insuranve_view.findViewById(R.id.edit_ins);
        ImageView close_ins = insuranve_view.findViewById(R.id.close_ins);
        ll_ins_entry = insuranve_view.findViewById(R.id.ll_entry);
        ll_ins_show = insuranve_view.findViewById(R.id.ll_show);

        showInsDetails(insuranve_view, vehicleNo);

        if (ins_pos < 0) {
            ll_ins_entry.setVisibility(View.VISIBLE);
            edit_ins.setVisibility(View.GONE);
            ll_ins_show.setVisibility(View.GONE);
        }

        ins_tv_validfrom = insuranve_view.findViewById(R.id.et_validfrom);
        ins_tv_validupto = insuranve_view.findViewById(R.id.et_validupto);
        final EditText insurance_et_agency = insuranve_view.findViewById(R.id.et_agency);
        final EditText insurance_et_amount = insuranve_view.findViewById(R.id.et_amount_paid);

        ImageView insurance_btn_valid_from = insuranve_view.findViewById(R.id.btn_valid_from);
        ImageView insurance_btn_valid_upto = insuranve_view.findViewById(R.id.btn_valid_upto);
        Button btn_insurance_submit = insuranve_view.findViewById(R.id.btn_insurance_submit);


        insurance_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(GlobalDeclarations.ins_flag, insurance_imageView);
            }
        });

        edit_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ins_pos = -1;
                ll_ins_entry.setVisibility(View.VISIBLE);
                edit_ins.setVisibility(View.GONE);
                ll_ins_show.setVisibility(View.GONE);
                insurancePath = null;
            }
        });
        close_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        ins_tv_validfrom.setOnClickListener(new View.OnClickListener() {
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
        ins_tv_validupto.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.select_ins_img), Toast.LENGTH_SHORT).show();
                } else if (ins_tv_validupto.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.select_valid_upto), Toast.LENGTH_SHORT).show();
                } else if (insurance_et_agency.getText().toString().trim().isEmpty()) {
                    insurance_et_agency.requestFocus();
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.select_agency), Toast.LENGTH_SHORT).show();
                } else {

                    String validfrom = ins_tv_validfrom.getText().toString().trim();
                    String validupto = ins_tv_validupto.getText().toString().trim();
                    String agency = insurance_et_agency.getText().toString().trim();
                    String amount = insurance_et_amount.getText().toString().trim();
                    long timeinmillis = calendar.getTimeInMillis();

                    InsuranceDetails insuranceDetails = new InsuranceDetails();
                    insuranceDetails.setImage(insurancePath);
                    insuranceDetails.setVehicleNo(vehicleNo);
                    insuranceDetails.setValidFrom(validfrom);
                    insuranceDetails.setValidUpto(validupto);
                    insuranceDetails.setAgency(agency);
                    insuranceDetails.setAmount(amount);
                    insuranceDetails.setCurrentDate(Dates.getCurrentDate());
                    insuranceDetails.setId(timeinmillis);
//                    insurance_detailslist.add(insuranceDetails);

                    long rowInserted = database.insertInsuranceData(insuranceDetails);
                    if (rowInserted != -1)
                        setAlarm(timeinmillis, validupto, GlobalDeclarations.ins_flag);

                    long rowUpdated = database.updateInsDate(validupto, vehicleNo);

                    if (rowUpdated != -1)
                        Toast.makeText(DashboardActivity.this, getResources().getString(R.string.ins_updated), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                    alert.dismiss();
                    showVehicleDetails();

//                    getListDetails();
//                    listAdapter.notifyDataSetChanged();

                }
            }

        });
    }

    private void showInsDetails(View insuranve_view, String vehicleNo) {

        TextView tv_validfrom_show = insuranve_view.findViewById(R.id.tv_validfrom_show);
        TextView tv_validupto_show = insuranve_view.findViewById(R.id.tv_validupto_show);
        TextView tv_agency_show = insuranve_view.findViewById(R.id.tv_agency_show);
        TextView tv_amount_show = insuranve_view.findViewById(R.id.tv_amount_show);
        ImageView insurance_imageView_show = insuranve_view.findViewById(R.id.insurance_image_show);

        final ArrayList<InsuranceDetails> insuranceDetailsArrayList = database.getInsuranceDetailsList(vehicleNo);
        ins_pos = insuranceDetailsArrayList.size() - 1;

        if (ins_pos >= 0) {
            ll_ins_entry.setVisibility(View.GONE);
            ll_ins_show.setVisibility(View.VISIBLE);
            edit_ins.setVisibility(View.VISIBLE);

            tv_validfrom_show.setText(insuranceDetailsArrayList.get(ins_pos).getValidFrom());
            tv_validupto_show.setText(insuranceDetailsArrayList.get(ins_pos).getValidUpto());
            tv_agency_show.setText(insuranceDetailsArrayList.get(ins_pos).getAgency());
            tv_amount_show.setText(insuranceDetailsArrayList.get(ins_pos).getAmount());

            if (insuranceDetailsArrayList.get(ins_pos).getImage() != null)
                utils.loadImage(insuranceDetailsArrayList.get(ins_pos).getImage(), insurance_imageView_show);

            insurance_imageView_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    chooseImage(GlobalDeclarations.ins_flag, insurance_imageView);
                    openImage(insuranceDetailsArrayList.get(ins_pos).getImage());
                }
            });
        }
    }

    @Override
    public void pollutionPopup(final String vehicleNo) {
        flag = "Pollution";
        GlobalDeclarations.VEHICLENO = vehicleNo;
        LayoutInflater factory = LayoutInflater.from(DashboardActivity.this);
        final View view = factory.inflate(R.layout.popup_pollution_details, null);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);
        builder1.setView(view);
        final AlertDialog alert = builder1.create();
        alert.show();

        ll_poll_entry = view.findViewById(R.id.ll_entry);
        ll_poll_show = view.findViewById(R.id.ll_show);
        edit_poll = view.findViewById(R.id.edit_poll);
        ImageView close_poll = view.findViewById(R.id.close_poll);
        pollution_imageView = view.findViewById(R.id.insurance_image);

        showPollDetails(view, vehicleNo);
        if (poll_pos < 0) {
            ll_poll_entry.setVisibility(View.VISIBLE);
            ll_poll_show.setVisibility(View.GONE);
            edit_poll.setVisibility(View.GONE);
        }

        poll_tv_validfrom = view.findViewById(R.id.et_validfrom);
        pollution_et_validupto = view.findViewById(R.id.et_validupto);

        ImageView btn_valid_from = view.findViewById(R.id.btn_valid_from);
        ImageView btn_valid_upto = view.findViewById(R.id.btn_valid_upto);
        Button btn_insurance_submit = view.findViewById(R.id.btn_insurance_submit);

        pollution_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(GlobalDeclarations.poll_flag, pollution_imageView);
            }
        });
        poll_tv_validfrom.setOnClickListener(new View.OnClickListener() {
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
        edit_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poll_pos = -1;
                ll_poll_entry.setVisibility(View.VISIBLE);
                ll_poll_show.setVisibility(View.GONE);
                edit_poll.setVisibility(View.GONE);
                pollutionPath = null;
            }
        });
        close_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        btn_insurance_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pollutionPath == null) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.select_poll_img), Toast.LENGTH_SHORT).show();
                } else if (pollution_et_validupto.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.select_valid_upto), Toast.LENGTH_SHORT).show();
                } else {

                    String validfrom = poll_tv_validfrom.getText().toString().trim();
                    String validupto = pollution_et_validupto.getText().toString().trim();

                    long timeinmillis = calendar.getTimeInMillis();

                    PollutionDetails pollutionDetails = new PollutionDetails();
                    pollutionDetails.setImage(pollutionPath);
                    pollutionDetails.setVehicleNo(vehicleNo);
                    pollutionDetails.setValidFrom(validfrom);
                    pollutionDetails.setValidUpto(validupto);
                    pollutionDetails.setCurrentDate(Dates.getCurrentDate());
                    pollutionDetails.setId(timeinmillis);
//                    pollution_detailslist.add(pollutionDetails);

                    long rowInserted = database.insertPollutionData(pollutionDetails);
                    if (rowInserted != -1)
                        setAlarm(timeinmillis, validupto, GlobalDeclarations.poll_flag);

                    long rowUpdated = database.updatePollDate(validupto, vehicleNo);

                    if (rowUpdated != -1)
                        Toast.makeText(DashboardActivity.this, getResources().getString(R.string.poll_updated), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

                    alert.dismiss();
                    showVehicleDetails();
//                    getListDetails();
//                    listAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    private void showPollDetails(View view, String vehicleNo) {

        TextView tv_validfrom_show = view.findViewById(R.id.tv_validfrom_show);
        TextView tv_validupto_show = view.findViewById(R.id.tv_validupto_show);
        ImageView pollution_imageView_show = view.findViewById(R.id.insurance_image_show);

        final ArrayList<PollutionDetails> pollutionDetailsList = database.getPollutionDetailsList(vehicleNo);
        poll_pos = pollutionDetailsList.size() - 1;

        if (poll_pos >= 0) {
            ll_poll_entry.setVisibility(View.GONE);
            ll_poll_show.setVisibility(View.VISIBLE);

            tv_validfrom_show.setText(pollutionDetailsList.get(poll_pos).getValidFrom());
            tv_validupto_show.setText(pollutionDetailsList.get(poll_pos).getValidUpto());

            if (pollutionDetailsList.get(poll_pos).getImage() != null)
                utils.loadImage(pollutionDetailsList.get(poll_pos).getImage(), pollution_imageView_show);

            pollution_imageView_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    chooseImage(GlobalDeclarations.ins_flag, insurance_imageView);
                    openImage(pollutionDetailsList.get(poll_pos).getImage());
                }
            });

        }
    }

    @Override
    public void servicingPopup(final String vehicleNo) {
        flag = "Servicing";
        GlobalDeclarations.VEHICLENO = vehicleNo;
        LayoutInflater factory = LayoutInflater.from(DashboardActivity.this);
        final View view = factory.inflate(R.layout.popup_servicing_details, null);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);
        builder1.setView(view);
        final AlertDialog alert = builder1.create();
        alert.show();


        ll_serv_entry = view.findViewById(R.id.ll_entry);
        ll_serv_show = view.findViewById(R.id.ll_show);
        edit_serv = view.findViewById(R.id.edit_serv);
        ImageView close_serv = view.findViewById(R.id.close_serv);

        showServDetails(view, vehicleNo);
        if (serv_pos < 0) {
            ll_serv_entry.setVisibility(View.VISIBLE);
            ll_serv_show.setVisibility(View.GONE);
            edit_serv.setVisibility(View.GONE);
        }

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
        final LinearLayout ll_gs_amount = view.findViewById(R.id.ll_gs_amount);
        final LinearLayout ll_os_amount = view.findViewById(R.id.ll_os_amount);
        final EditText et_gs_amount = view.findViewById(R.id.et_gs_amount);
        final EditText et_os_amount = view.findViewById(R.id.et_os_amount);
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
        edit_serv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serv_pos = -1;
                ll_serv_entry.setVisibility(View.VISIBLE);
                ll_serv_show.setVisibility(View.GONE);
                edit_serv.setVisibility(View.GONE);
            }
        });
        close_serv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        cb_general_service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ll_gs_amount.setVisibility(View.VISIBLE);
                else
                    ll_gs_amount.setVisibility(View.GONE);

            }
        });
        cb_others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_services.setVisibility(View.VISIBLE);
                    ll_os_amount.setVisibility(View.VISIBLE);
                } else {
                    ll_services.setVisibility(View.GONE);
                    ll_os_amount.setVisibility(View.GONE);
                }
            }
        });

        btn_insurance_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (servicing_et_next_date.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.sel_next_date), Toast.LENGTH_SHORT).show();
                } else if (cb_general_service.isChecked() && et_gs_amount.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.sel_gs_amount), Toast.LENGTH_SHORT).show();
                    et_gs_amount.requestFocus();
                } else if (cb_others.isChecked() && et_os_amount.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.sel_os_amount), Toast.LENGTH_SHORT).show();
                    et_os_amount.requestFocus();
                } else if (cb_others.isChecked() && et_services.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.sel_services), Toast.LENGTH_SHORT).show();
                    et_services.requestFocus();
                }
                /*else if (et_next_km.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.sel_next_servicing_date), Toast.LENGTH_SHORT).show();
                    et_next_km.requestFocus();
                }*/
                else {
                    String services;
                    String date = servicing_et_date.getText().toString().trim();
                    String nextdate = servicing_et_next_date.getText().toString().trim();

                    String km = et_km.getText().toString().trim();
                    String next_km = et_next_km.getText().toString().trim();
                    if (cb_general_service.isChecked())
                        services = "Yes";
                    else
                        services = "No";
//                    if (cb_others.isChecked())
//                        services = services + "," + ;

                    long timeinmillis = calendar.getTimeInMillis();

                    ServicingDetails servicingDetails = new ServicingDetails();
                    servicingDetails.setDate(date);
                    servicingDetails.setVehicleNo(vehicleNo);
                    servicingDetails.setNext_date(nextdate);
                    servicingDetails.setGeneralService(services);
                    servicingDetails.setGsAmount(et_gs_amount.getText().toString().trim());
                    servicingDetails.setServices(et_services.getText().toString().trim());
                    servicingDetails.setOsAmount(et_os_amount.getText().toString().trim());
                    servicingDetails.setKm(km);
                    servicingDetails.setNext_km(next_km);
                    servicingDetails.setCurrentDate(Dates.getCurrentDate());
                    servicingDetails.setId(timeinmillis);
//                    servicing_detailslist.add(servicingDetails);

                    long rowInserted = database.insertServicingData(servicingDetails);
                    if (rowInserted != -1)
                        setAlarm(timeinmillis, nextdate, GlobalDeclarations.ser_flag);

                    long rowUpdated = database.updateServDate(nextdate, vehicleNo);

                    if (rowUpdated != -1)
                        Toast.makeText(DashboardActivity.this, getResources().getString(R.string.serv_updated), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

                    alert.dismiss();
                    showVehicleDetails();
//                    getListDetails();
//                    listAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    private void showServDetails(View view, String vehicleNo) {

        TextView tv_validfrom_show = view.findViewById(R.id.tv_validfrom_show);
        TextView tv_validupto_show = view.findViewById(R.id.tv_validupto_show);
        TextView tv_km_show = view.findViewById(R.id.tv_km_show);
        TextView tv_next_km_show = view.findViewById(R.id.tv_next_km_show);
        CheckBox cb_general_service_show = view.findViewById(R.id.cb_general_service_show);
        CheckBox cb_others_show = view.findViewById(R.id.cb_others_show);
        TextView tv_services_show = view.findViewById(R.id.tv_quantity_show);
        LinearLayout ll_services_show = view.findViewById(R.id.ll_services_show);
        LinearLayout ll_gs_amount_show = view.findViewById(R.id.ll_gs_amount_show);
        LinearLayout ll_os_amount_show = view.findViewById(R.id.ll_os_amount_show);
        TextView tv_gs_amount_show = view.findViewById(R.id.tv_gs_amount_show);
        TextView tv_os_amount_show = view.findViewById(R.id.tv_os_amount_show);

        ArrayList<ServicingDetails> servicingDetailsList = database.getServicingDetailsList(vehicleNo);
        serv_pos = servicingDetailsList.size() - 1;

        if (serv_pos >= 0) {
            ll_serv_entry.setVisibility(View.GONE);
            ll_serv_show.setVisibility(View.VISIBLE);

            tv_validfrom_show.setText(servicingDetailsList.get(serv_pos).getDate());
            tv_validupto_show.setText(servicingDetailsList.get(serv_pos).getNext_date());
            tv_km_show.setText(servicingDetailsList.get(serv_pos).getKm());
            tv_next_km_show.setText(servicingDetailsList.get(serv_pos).getNext_km());

            if (servicingDetailsList.get(serv_pos).getGeneralService().equalsIgnoreCase("Yes")) {
                cb_general_service_show.setChecked(true);
            } else {
                cb_general_service_show.setChecked(false);
            }
            if (servicingDetailsList.get(serv_pos).getServices() == null || servicingDetailsList.get(serv_pos).getServices().equalsIgnoreCase("")) {
                cb_others_show.setChecked(false);
                ll_services_show.setVisibility(View.GONE);
            } else {
                cb_others_show.setChecked(true);
                tv_services_show.setText(servicingDetailsList.get(serv_pos).getServices());
                ll_services_show.setVisibility(View.VISIBLE);
            }
            if (servicingDetailsList.get(serv_pos).getGsAmount() == null || servicingDetailsList.get(serv_pos).getGsAmount().equalsIgnoreCase("")) {
                ll_gs_amount_show.setVisibility(View.GONE);
            } else {
                tv_gs_amount_show.setText(servicingDetailsList.get(serv_pos).getGsAmount());
                ll_gs_amount_show.setVisibility(View.VISIBLE);
            }
            if (servicingDetailsList.get(serv_pos).getOsAmount() == null || servicingDetailsList.get(serv_pos).getOsAmount().equalsIgnoreCase("")) {
                cb_others_show.setChecked(false);
                ll_os_amount_show.setVisibility(View.GONE);
            } else {
                cb_others_show.setChecked(true);
                tv_os_amount_show.setText(servicingDetailsList.get(serv_pos).getOsAmount());
                ll_os_amount_show.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void batteryInfoPopup(final String vehicleNo) {
        flag = "Battery";
        GlobalDeclarations.VEHICLENO = vehicleNo;
        LayoutInflater factory = LayoutInflater.from(DashboardActivity.this);
        final View v = factory.inflate(R.layout.popup_battery_info_details, null);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);
        builder1.setView(v);
        final AlertDialog alert = builder1.create();
        alert.show();

        ll_battery_entry = v.findViewById(R.id.ll_battery_entry);
        ll_battery_show = v.findViewById(R.id.ll_battery_show);
        edit_battery = v.findViewById(R.id.edit_battery);
        ImageView close_battery = v.findViewById(R.id.close_battery);

        showBatteryDetails(v, vehicleNo);

        if (battery_pos < 0) {
            ll_battery_entry.setVisibility(View.VISIBLE);
            ll_battery_show.setVisibility(View.GONE);
            edit_battery.setVisibility(View.GONE);
        }

        bill_img = v.findViewById(R.id.bill_img);
        battery_img = v.findViewById(R.id.battery_img);
        et_warranty_end_date = v.findViewById(R.id.et_warranty_end_date);
        et_purchased_on = v.findViewById(R.id.et_purchased_on);
        final EditText et_make = v.findViewById(R.id.et_make_battery);
        ImageView date_btn_warranty = v.findViewById(R.id.date_btn_warranty);
        ImageView date_btn_purshaed_on = v.findViewById(R.id.date_btn_purshaed_on);
        Button btn_submit = v.findViewById(R.id.btn_submit);

        bill_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = GlobalDeclarations.bill_flag;
                chooseImage(GlobalDeclarations.bill_flag, bill_img);
            }
        });
        battery_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = GlobalDeclarations.battery_flag;
                chooseImage(GlobalDeclarations.battery_flag, battery_img);
            }
        });


        date_btn_warranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = GlobalDeclarations.bill_flag;
                validuptodatePicker();
            }
        });


        date_btn_purshaed_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = GlobalDeclarations.battery_flag;
                validfromdatePicker();
            }
        });

        et_warranty_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = GlobalDeclarations.bill_flag;
                validuptodatePicker();
            }
        });

        et_purchased_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = GlobalDeclarations.battery_flag;
                validfromdatePicker();
            }
        });

        edit_battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                battery_pos = -1;
                ll_battery_entry.setVisibility(View.VISIBLE);
                ll_battery_show.setVisibility(View.GONE);
                edit_battery.setVisibility(View.GONE);
                billImgPath = null;
                batteryImgPath = null;
            }
        });

        close_battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (billImgPath == null) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.select_bill_img), Toast.LENGTH_SHORT).show();
                } else if (batteryImgPath == null) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.select_battery_img), Toast.LENGTH_SHORT).show();
                } else if (et_warranty_end_date.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DashboardActivity.this, getResources().getString(R.string.sel_warranty), Toast.LENGTH_SHORT).show();
                } else {
                    String make = et_make.getText().toString().trim();
                    String warranty_end_date = et_warranty_end_date.getText().toString().trim();
                    String purchased_on = et_purchased_on.getText().toString().trim();
                    long timeinmillis = calendar.getTimeInMillis();

                    BatteryDetails batteryDetails = new BatteryDetails();
                    batteryDetails.setBillImgPath(billImgPath);
                    batteryDetails.setBatteryImgPath(batteryImgPath);
                    batteryDetails.setVehicleNo(vehicleNo);
                    batteryDetails.setMake(make);
                    batteryDetails.setWarrantyEndDate(warranty_end_date);
                    batteryDetails.setPurchasedOn(purchased_on);
                    batteryDetails.setCurrentDate(Dates.getCurrentDate());
                    batteryDetails.setId(timeinmillis);

                    long rowInserted = database.insertBatteryData(batteryDetails);

                    if (rowInserted != -1)
                        setAlarm(timeinmillis, warranty_end_date, GlobalDeclarations.battery_flag);

                    long rowUpdated = database.updateBatteryDate(warranty_end_date, vehicleNo);

                    if (rowUpdated != -1)
                        Toast.makeText(DashboardActivity.this, getResources().getString(R.string.battery_updated), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

                    alert.dismiss();
                    showVehicleDetails();

//                    getListDetails();
//                    listAdapter.notifyDataSetChanged();

                }
            }

        });

    }

    private void showBatteryDetails(View view, String vehicleNo) {

        TextView tv_make_battery = view.findViewById(R.id.tv_make_battery);
        TextView tv_purchased_on_show = view.findViewById(R.id.tv_purchased_on_show);
        TextView tv_warranty_end_date_show = view.findViewById(R.id.tv_warranty_end_date_show);
        ImageView bill_img_show = view.findViewById(R.id.bill_img_show);
        ImageView battery_img_show = view.findViewById(R.id.battery_img_show);

        final ArrayList<BatteryDetails> batteryDetails = database.getBatteryDetails(vehicleNo);
        battery_pos = batteryDetails.size() - 1;

        if (battery_pos >= 0) {

            ll_battery_entry.setVisibility(View.GONE);
            ll_battery_show.setVisibility(View.VISIBLE);
            edit_battery.setVisibility(View.VISIBLE);

            tv_make_battery.setText(batteryDetails.get(battery_pos).getMake());
            tv_purchased_on_show.setText(batteryDetails.get(battery_pos).getPurchasedOn());
            tv_warranty_end_date_show.setText(batteryDetails.get(battery_pos).getWarrantyEndDate());

            if (batteryDetails.get(battery_pos).getBillImgPath() != null)
                utils.loadImage(batteryDetails.get(battery_pos).getBillImgPath(), bill_img_show);

            bill_img_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    chooseImage(GlobalDeclarations.rc_front_img_flag, rc_front_img);
                    openImage(batteryDetails.get(battery_pos).getBillImgPath());
                }
            });
            if (batteryDetails.get(battery_pos).getBatteryImgPath() != null)
                utils.loadImage(batteryDetails.get(battery_pos).getBatteryImgPath(), battery_img_show);

            battery_img_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    chooseImage(GlobalDeclarations.rc_back_img_flag, rc_back_img);
                    openImage(batteryDetails.get(battery_pos).getBatteryImgPath());
                }
            });
        }
    }

    private void autoPopulate() {
        vehicleDetailsBinding.etVehicleno1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 2)
                    vehicleDetailsBinding.etVehicleno2.requestFocus();
            }
        });
        vehicleDetailsBinding.etVehicleno2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 2)
                    vehicleDetailsBinding.etVehicleno3.requestFocus();
            }
        });


        vehicleDetailsBinding.etVehicleno3.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 2) {
                    vehicleDetailsBinding.etVehicleno4.requestFocus();
                }


            }

        });
        vehicleDetailsBinding.etVehicleno4.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 4) {
                    vehicleDetailsBinding.etMake.requestFocus();
                }


            }

        });
    }

    private void cursorVisibilityOnTouch() {
        View.OnTouchListener otl = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true; // the listener has consumed the event
            }
        };
        binding.appBarDashboard.contentDashboard.inputSearch.setOnTouchListener(otl);
        binding.appBarDashboard.contentDashboard.inputSearch.setInputType(InputType.TYPE_NULL);// disable soft input
        binding.appBarDashboard.contentDashboard.inputSearch.setCursorVisible(true);

        binding.appBarDashboard.contentDashboard.inputSearch.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.appBarDashboard.contentDashboard.inputSearch.setInputType(InputType.TYPE_CLASS_TEXT); // restore input type
                binding.appBarDashboard.contentDashboard.inputSearch.onTouchEvent(event); // call native handler
                return true; // consume touch even
            }
        });

    }

    private void validfromdatePicker() {
//        Calendar calendar1 = Calendar.getInstance();
//        from_year = calendar1.get(Calendar.YEAR);
//        from_month = calendar1.get(Calendar.MONTH);
//        from_day = calendar1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(DashboardActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String from_date = dayOfMonth + "/" + (month + 1) + "/" + year;
                if (flag.equalsIgnoreCase("Insurance"))
                    ins_tv_validfrom.setText(from_date);
                else if (flag.equalsIgnoreCase("Pollution"))
                    poll_tv_validfrom.setText(from_date);
                else if (flag.equalsIgnoreCase("Servicing"))
                    servicing_et_date.setText(from_date);
                else if (flag.equalsIgnoreCase(GlobalDeclarations.battery_flag))
                    et_purchased_on.setText(from_date);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void validuptodatePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(DashboardActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String upto_date = dayOfMonth + "/" + (month + 1) + "/" + year;
                if (flag.equalsIgnoreCase("Insurance"))
                    ins_tv_validupto.setText(upto_date);
                else if (flag.equalsIgnoreCase("Pollution"))
                    pollution_et_validupto.setText(upto_date);
                else if (flag.equalsIgnoreCase("Servicing"))
                    servicing_et_next_date.setText(upto_date);
                else if (flag.equalsIgnoreCase(GlobalDeclarations.rc_front_img_flag) || flag.equalsIgnoreCase(GlobalDeclarations.rc_back_img_flag))
                    et_rcvalidupto.setText(upto_date);
                else if (flag.equalsIgnoreCase(GlobalDeclarations.bill_flag))
                    et_warranty_end_date.setText(upto_date);

            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        detailslist.clear();
        if (charText.length() == 0) {
            detailslist.addAll(list_temp);
        } else {
            for (VehicleDetails list : list_temp) {
                if (list.getVehicleNo().toLowerCase(Locale.getDefault()).contains(charText)/* || list.getModel().toLowerCase(Locale.getDefault()).contains(charText) || list.getMake().toLowerCase(Locale.getDefault()).contains(charText)*/) {
                    this.detailslist.add(list);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void chooseImage(String flag_, ImageView imageView) {
        flag = flag_;
        imgview = imageView;
        View view = getLayoutInflater().inflate(R.layout.dialog_choose_image, null);
        dialog = new BottomSheetDialog(DashboardActivity.this, R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.show();
        take_photo = view.findViewById(R.id.take_photo);
        gallery = view.findViewById(R.id.gallery);
        title = view.findViewById(R.id.title);

        if (flag.equalsIgnoreCase(GlobalDeclarations.vehicle_img1_flag))
            title.setText("Vehicle Photo");
        else if (flag.equalsIgnoreCase(GlobalDeclarations.rc_front_img_flag))
            title.setText("RC Photo");
        else if (flag.equalsIgnoreCase(GlobalDeclarations.rc_back_img_flag))
            title.setText("RC Photo");
        else if (flag.equalsIgnoreCase(GlobalDeclarations.vehicle_img2_flag))
            title.setText("Vehicle Photo");
        else if (flag.equalsIgnoreCase(GlobalDeclarations.vehicle_img3_flag))
            title.setText("Vehicle Photo");
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

    @Override
    public void selectAllVehicles() {

        detailslist = new ArrayList<>();
        detailslist = database.getVehicleDetails();

        boolean yes_flag = true;
        for (int i = 0; i < detailslist.size(); i++) {
            if (detailslist.get(i).getChecked().equalsIgnoreCase("No")) {
                yes_flag = false;
                break;
            }
        }
        if (yes_flag) {
            binding.appBarDashboard.contentDashboard.tvSelectAll.setVisibility(View.GONE);
            binding.appBarDashboard.contentDashboard.tvDeselect.setVisibility(View.VISIBLE);
        } else {
            binding.appBarDashboard.contentDashboard.tvSelectAll.setVisibility(View.VISIBLE);
            binding.appBarDashboard.contentDashboard.tvDeselect.setVisibility(View.GONE);
        }
    }

    /*public void deSelectAllVehicles() {
        showVehicleDetails();
        boolean no_flag = true;
        for (int i = 0; i < detailslist.size(); i++) {
            if (detailslist.get(i).getChecked().equalsIgnoreCase("Yes")) {
                no_flag = false;
                break;
            }
        }
        if (no_flag)
            binding.appBarDashboard.contentDashboard.selectAll.setChecked(false);
        else
            binding.appBarDashboard.contentDashboard.selectAll.setChecked(true);
    }*/

    //CAMERA OPEN AND TAKE PHOTO CODE
    protected void takeCameraImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_REQUEST);

    }

    public Uri getOutputMediaFileUri(int type) {
        File imageFile = getOutputMediaFile(type);
        Uri imageUri = FileProvider.getUriForFile(
                DashboardActivity.this,
                "in.gov.cgg.vehiclereminderlatest.provider", //(use your app signature + ".provider" )
                imageFile);
        return imageUri;
    }

    //DIRECTORY CREATION CODE
    private File getOutputMediaFile(int type) {


        File mediaStorageDir = new File(IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME_TEMP);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "Oops! Failed create " + "Android File Upload"
                        + " directory");
                return null;
            }
        }

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {

            mediaFile = new File(mediaStorageDir.getPath() + File.separator + flag + timeStamp + "_temp" + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }

    //SET PHOTO AND NAMES ON CLICK OF OK or TICK IN CAMERA
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                CAMERAOK++;
                filePath = IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME_TEMP;
                String Image_name = flag + timeStamp + "_temp" + ".png";
                filePath = filePath + "/" + Image_name;
                filePath = compressImage(filePath);
                loadVehicleImages();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(DashboardActivity.this,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(DashboardActivity.this,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            filePath = IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME_TEMP;
            String Image_name = flag + timeStamp + "_temp" + ".png";
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

            filePath = compressImage(filePath);

            loadVehicleImages();

        } else if (requestCode == EXCEL_REQUEST && resultCode == RESULT_OK) {

            filePath = IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME;
            filePath = filePath + "/" + GlobalDeclarations.EXCEL_NAME;

            uri = data.getData();

            new unpackzipAsync().execute();

            /*String path = null;
            path = getPath(DashboardActivity.this, uri);
            Log.d(TAG, path);
            if (path != null)
                unpackZip(path);
            else
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();*/

        }
    }

    private void loadVehicleImages() {

        if (flag.equalsIgnoreCase(GlobalDeclarations.vehicle_img1_flag)) {
            vehicleImg1Path = filePath;
            utils.loadImage(vehicleImg1Path, imgview);
            long rowUpdated = database.updateVehicleImgPath(vehicleImg1Path, GlobalDeclarations.VEHICLENO);

            if (rowUpdated != -1)
                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.img_updated), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

        } else if (flag.equalsIgnoreCase(GlobalDeclarations.vehicle_img2_flag)) {
            vehicleImg2Path = filePath;
            utils.loadImage(vehicleImg2Path, vehicleDetailsBinding.vehicleImgTwo);

        } else if (flag.equalsIgnoreCase(GlobalDeclarations.vehicle_img3_flag)) {
            vehicleImg3Path = filePath;
            utils.loadImage(vehicleImg3Path, vehicleDetailsBinding.vehicleImgThree);
        } else if (flag.equalsIgnoreCase(GlobalDeclarations.rc_front_img_flag)) {
            RCFrontImgPath = filePath;
            utils.loadImage(RCFrontImgPath, rc_front_img);

//            long rowUpdated = database.updateRcFrontImgPath(RCFrontImgPath, GlobalDeclarations.VEHICLENO);
//            if (rowUpdated != -1)
//                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.img_updated), Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

        } else if (flag.equalsIgnoreCase(GlobalDeclarations.rc_back_img_flag)) {
            RCBackImgPath = filePath;
            utils.loadImage(RCBackImgPath, rc_back_img);

//            long rowUpdated = database.updateRcBackImgPath(RCBackImgPath, GlobalDeclarations.VEHICLENO);
//            if (rowUpdated != -1)
//                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.img_updated), Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

        } else if (flag.equalsIgnoreCase(GlobalDeclarations.ins_flag)) {
            insurancePath = filePath;
            utils.loadImage(insurancePath, insurance_imageView);

//            long rowUpdated = database.updateInsImgPath(insurancePath, GlobalDeclarations.VEHICLENO);
//            if (rowUpdated != -1)
//                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.img_updated), Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

        } else if (flag.equalsIgnoreCase(GlobalDeclarations.poll_flag)) {
            pollutionPath = filePath;
            utils.loadImage(pollutionPath, pollution_imageView);

//            long rowUpdated = database.updatePollImgPath(pollutionPath, GlobalDeclarations.VEHICLENO);
//            if (rowUpdated != -1)
//                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.img_updated), Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

        } else if (flag.equalsIgnoreCase(GlobalDeclarations.bill_flag)) {
            billImgPath = filePath;
            utils.loadImage(billImgPath, bill_img);

//            long rowUpdated = database.updatePollImgPath(billImgPath, GlobalDeclarations.VEHICLENO);
//            if (rowUpdated != -1)
//                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.img_updated), Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

        } else if (flag.equalsIgnoreCase(GlobalDeclarations.battery_flag)) {
            batteryImgPath = filePath;
            utils.loadImage(batteryImgPath, battery_img);

//            long rowUpdated = database.updatePollImgPath(billImgPath, GlobalDeclarations.VEHICLENO);
//            if (rowUpdated != -1)
//                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.img_updated), Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(DashboardActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();

        }

        File mediaStorageDir = new File(IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME_TEMP);

        if (mediaStorageDir.isDirectory()) {
            String[] children = mediaStorageDir.list();
            for (int i = 0; i < children.length; i++)
                new File(mediaStorageDir, children[i]).delete();
            mediaStorageDir.delete();
        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {

        File mediaStorageDir = new File(IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "Oops! Failed create " + "Android File Upload"
                        + " directory");
                return null;
            }
        }

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        String mediaFile = mediaStorageDir.getPath() + File.separator + flag + timeStamp + ".png";

        return mediaFile;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    //Get Path from Content provider for all mobiles
    public static String getPathforXiaomi(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        Log.i("URI", uri + "");
        String result = uri + "";
        // DocumentProvider
        //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isKitKat && (result.contains("media.documents"))) {
            String[] ary = result.split("/");
            int length = ary.length;
            String imgary = ary[length - 1];
            final String[] dat = imgary.split("%3A");
            final String docId = dat[1];
            final String type = dat[0];
            Uri contentUri = null;
            if ("image".equalsIgnoreCase(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equalsIgnoreCase(type)) {
                Log.e("Error", "Video Type");
            } else if ("audio".equalsIgnoreCase(type)) {
                Log.e("Error", "Audio Type");

            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{
                    dat[1]
            };
            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    //Get Path from Content provider for all mobiles
    public static String getPath(Context context, Uri uri) {

        Log.d("model", android.os.Build.MANUFACTURER);
        Log.d("Version", Build.MODEL);
        Log.d("Version", Build.VERSION.RELEASE);

        String xiaomi = "Xiaomi";
        String deviceManufacturer = android.os.Build.MANUFACTURER;

        if (deviceManufacturer.equalsIgnoreCase(xiaomi)) {

            String path = getPathforXiaomi(context, uri);
            return path;

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            // DocumentProvider
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equalsIgnoreCase(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equalsIgnoreCase(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equalsIgnoreCase(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            String path = "";
            try {
                path = uri.getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return path;
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[]
            selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equalsIgnoreCase(uri.getAuthority());
    }

    public void selectPhotofromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, GALLERY_REQUEST);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        int id = binding.navView.getId();

        if (id == R.id.nav_reports) {
            Intent intent = new Intent(DashboardActivity.this, MultiLineChartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_licenece) {
            Intent intent = new Intent(DashboardActivity.this, LicenceActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_import) {
            importZip();
        } else if (id == R.id.nav_export) {
            selectVehicles();
        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(DashboardActivity.this, InfoActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openImage(String path) {

//        final Dialog dialog = new Dialog(DashboardActivity.this);
//        dialog.setContentView(R.layout.popup_image);
//        dialog.show();

        LayoutInflater factory = LayoutInflater.from(DashboardActivity.this);
        final View view = factory.inflate(R.layout.popup_image, null);

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);
        builder1.setView(view);
        final AlertDialog alert = builder1.create();
        alert.show();

        ImageView imageView = view.findViewById(R.id.imageView);
        ImageView iv_close = view.findViewById(R.id.close);

        utils.loadImage(path, imageView);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.appBarDashboard.contentDashboard.recyclerview.scrollToPosition(detailslist.size() - 1);
        deleteData();

    }

    private void deleteData() {
//        binding.appBarDashboard.contentDashboard.selectAll.setChecked(false);
        binding.appBarDashboard.contentDashboard.tvSelectAll.setVisibility(View.VISIBLE);
        binding.appBarDashboard.contentDashboard.tvDeselect.setVisibility(View.GONE);
        export_flag = false;

        detailslist = new ArrayList<>();
        detailslist = database.getVehicleDetails();
        for (int i = 0; i < detailslist.size(); i++)
            database.updateFlag("No", detailslist.get(i).getVehicleNo());

        tempDatabase.deleteVehicleData();
        tempDatabase.deleteBatteryData();
        tempDatabase.deleteInsuranceData();
        tempDatabase.deletePollutionData();
        tempDatabase.deleteRCData();
        tempDatabase.deleteServicingData();
        showVehicleDetails();
    }

    private void setAlarm(long timeinmillis, String date, String flag) {

       /* if (timeinmillis == 0 && date.equalsIgnoreCase("")) {
            for(int i=0;i<detailslist.size();i++){
                detailslist.get(i).getId();
            }
        }*/
        Toast.makeText(DashboardActivity.this, "" + flag, Toast.LENGTH_SHORT).show();

        String[] date1 = date.split("/");
        int day = Integer.parseInt(date1[0]);
        int mnth = Integer.parseInt(date1[1]);
        int yr = Integer.parseInt(date1[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(yr, mnth - 1, day, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE) + 2, GlobalDeclarations.SECONDS);
//        calendar.set(yr, mnth - 1, day, GlobalDeclarations.HOUR, GlobalDeclarations.MINUTE, GlobalDeclarations.SECONDS);

        Intent intent = new Intent(DashboardActivity.this, AlarmReceiver.class);
        intent.putExtra("FLAG", flag);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                DashboardActivity.this, (int) timeinmillis, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);

    }

    // Export SQLite DB as EXCEL FILE

    private void exportExcel() {
        sqliteToExcel = new SQLiteToExcel(DashboardActivity.this, GlobalDeclarations.TEMP_DATABASE_NAME, IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME + "/");
        sqliteToExcel.exportAllTables(GlobalDeclarations.EXCEL_NAME, new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String filePath) {
                Toast.makeText(DashboardActivity.this, "Excel exported successfully", Toast.LENGTH_SHORT).show();
                Log.d("exportExcel", "success");
                new packzipAsync().execute();
                deleteData();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(DashboardActivity.this, "" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Import EXCEL FILE to SQLite

    private void importExcel() {

        String directory_path = IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME + "/" + GlobalDeclarations.EXCEL_NAME;
        Log.d("directory_path", directory_path);

        File file = new File(directory_path);
        if (!file.exists()) {
            Log.i(TAG, "importExcel: " + "File doesn't exist");
            return;
        }

        // Is used to import data from excel without dropping table
        // ExcelToSQLite excelToSQLite = new ExcelToSQLite(getApplicationContext(), DBHelper.DB_NAME);

        // if you want to add column in excel and import into DB, you must drop the table
        ExcelToSQLite excelToSQLite = new ExcelToSQLite(DashboardActivity.this, GlobalDeclarations.DATABASE_NAME, true);
        // Import EXCEL FILE to SQLite
        excelToSQLite.importFromFile(directory_path, new ExcelToSQLite.ImportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String dbName) {
                showVehicleDetails();
                Toast.makeText(DashboardActivity.this, "Excel imported into " + dbName, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(DashboardActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public String chooseImageInterfaceCall(Activity activity, Context context) {
        //     chooseImageFromAdaptor(activity, context);
        return vehicleImg1Path;
    }

    private class exportAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            binding.appBarDashboard.progressbar.setVisibility(View.VISIBLE);
//            binding.appBarDashboard.loader.setText("Exporting...");
//            binding.appBarDashboard.loader.setVisibility(View.VISIBLE);

            customDialog.showDialog("Exporting...");
        }

        @Override
        protected String doInBackground(String... strings) {

            exportExcel();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            binding.appBarDashboard.progressbar.setVisibility(View.GONE);
//            binding.appBarDashboard.loader.setVisibility(View.GONE);
//            customDialog.hideDialog();
        }
    }

    private class packzipAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            binding.appBarDashboard.progressbar.setVisibility(View.VISIBLE);
//            binding.appBarDashboard.loader.setText("Exporting...");
//            binding.appBarDashboard.loader.setVisibility(View.VISIBLE);

//            customDialog.showDialog("Exporting...");
        }

        @Override
        protected String doInBackground(String... strings) {

            packzip();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            binding.appBarDashboard.progressbar.setVisibility(View.GONE);
//            binding.appBarDashboard.loader.setVisibility(View.GONE);
            customDialog.hideDialog();

            Toast.makeText(DashboardActivity.this, "Successufully ZIP file created in " + IMAGE_PATH + GlobalDeclarations.IMAGE_DIRECTORY_NAME, Toast.LENGTH_SHORT).show();

        }
    }

    private class unpackzipAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            binding.appBarDashboard.progressbar.setVisibility(View.VISIBLE);
//            binding.appBarDashboard.loader.setText("Importing...");
//            binding.appBarDashboard.loader.setVisibility(View.VISIBLE);
            customDialog.showDialog("Importing...");
        }

        @Override
        protected String doInBackground(String... strings) {

            String path = null;
            path = getPath(DashboardActivity.this, uri);
            Log.d(TAG, path);
            if (path != null)
                unpackZip(path);
            else
                Toast.makeText(DashboardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            binding.appBarDashboard.progressbar.setVisibility(View.GONE);
//            binding.appBarDashboard.loader.setVisibility(View.GONE);
//            customDialog.hideDialog();
            Toast.makeText(DashboardActivity.this, "ZIP file successfully imported", Toast.LENGTH_SHORT).show();
            new importAsync().execute();
        }
    }

    private class importAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            binding.appBarDashboard.progressbar.setVisibility(View.VISIBLE);
//            binding.appBarDashboard.loader.setText("Importing...");
//            binding.appBarDashboard.loader.setVisibility(View.VISIBLE);
//            customDialog.showDialog("Importing...");
        }

        @Override
        protected String doInBackground(String... strings) {

            importExcel();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            binding.appBarDashboard.progressbar.setVisibility(View.GONE);
//            binding.appBarDashboard.loader.setVisibility(View.GONE);
            customDialog.hideDialog();
        }
    }

}
