package in.gov.cgg.vehiclereminderlatest.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.gov.cgg.vehiclereminderlatest.Interface.VehicleInterface;
import in.gov.cgg.vehiclereminderlatest.R;
import in.gov.cgg.vehiclereminderlatest.activity.DashboardActivity;
import in.gov.cgg.vehiclereminderlatest.activity.DetailsActivity;
import in.gov.cgg.vehiclereminderlatest.bean.VehicleDetails;
import in.gov.cgg.vehiclereminderlatest.database.Database;
import in.gov.cgg.vehiclereminderlatest.utils.Dates;
import in.gov.cgg.vehiclereminderlatest.utils.GlobalDeclarations;
import in.gov.cgg.vehiclereminderlatest.utils.Utils;


/**
 * Created by niharika.p on 29-06-2019.
 */

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.MyViewHolder> {

    Context context;
    Activity activity;
    ArrayList<VehicleDetails> detailslist;
    Utils utils;
    VehicleInterface vehicleInterface;
    Dates dates;


    public VehicleListAdapter(Context context, Activity activity, ArrayList<VehicleDetails> detailslist, VehicleInterface vehicleInterface) {
        this.context = context;
        this.detailslist = detailslist;
        this.activity = activity;
        utils = new Utils(context);
        this.vehicleInterface = vehicleInterface;
        dates = new Dates();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_vehicle_details, viewGroup, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        GlobalDeclarations.VEHICLENO = detailslist.get(position).getVehicleNo();

        myViewHolder.tv_vehicleno.setText(detailslist.get(position).getVehicleNo());
        myViewHolder.tv_make.setText(detailslist.get(position).getMake());
        myViewHolder.tv_model.setText(detailslist.get(position).getModel());
        myViewHolder.tv_fuel_type.setText(detailslist.get(position).getFueltype());
        myViewHolder.tv_year.setText(detailslist.get(position).getYear());
        myViewHolder.vehicle_image.setImageBitmap(stringToBitmap(detailslist.get(position).getVehicleImg1Path()));

        if (detailslist.get(position).getInsDate() != null && !detailslist.get(position).getInsDate().equalsIgnoreCase("")) {
            int days = dates.getCountOfDays(detailslist.get(position).getInsDate());
            if (days >= 0)
                myViewHolder.insurance_days.setText(setColor(myViewHolder.insurance_days, days));
            else
                myViewHolder.insurance_days.setText("+" + "\n" + "Add");
        } else
            myViewHolder.insurance_days.setText("+" + "\n" + "Add");

        if (detailslist.get(position).getPollDate() != null && !detailslist.get(position).getPollDate().equalsIgnoreCase("")) {
            int days = dates.getCountOfDays(detailslist.get(position).getPollDate());
            if (days >= 0)
                myViewHolder.pollution_days.setText(setColor(myViewHolder.pollution_days, days));
            else
                myViewHolder.pollution_days.setText("+" + "\n" + "Add");
        } else
            myViewHolder.pollution_days.setText("+" + "\n" + "Add");

        if (detailslist.get(position).getServDate() != null && !detailslist.get(position).getServDate().equalsIgnoreCase("")) {
            int days = dates.getCountOfDays(detailslist.get(position).getServDate());
            if (days >= 0)
                myViewHolder.servicing_days.setText(setColor(myViewHolder.servicing_days, days));
            else
                myViewHolder.servicing_days.setText("+" + "\n" + "Add");
        } else
            myViewHolder.servicing_days.setText("+" + "\n" + "Add");

        if (detailslist.get(position).getRcDate() != null && !detailslist.get(position).getRcDate().equalsIgnoreCase("")) {
            int days = dates.getCountOfDays(detailslist.get(position).getRcDate());
            if (days >= 0)
                myViewHolder.rc_days.setText(setColor(myViewHolder.rc_days, days));
            else
                myViewHolder.rc_days.setText("+" + "\n" + "Add");
        } else
            myViewHolder.rc_days.setText("+" + "\n" + "Add");

        if (detailslist.get(position).getBatteryDate() != null &&
                !detailslist.get(position).getBatteryDate().equalsIgnoreCase("")) {
            int days = dates.getCountOfDays(detailslist.get(position).getBatteryDate());
            if (days >= 0)
                setImageColor(myViewHolder.battery_info, days);
            else
                myViewHolder.battery_info.setColorFilter(ContextCompat.getColor(context, R.color.black));
        } else
            myViewHolder.battery_info.setColorFilter(ContextCompat.getColor(context, R.color.black));


        myViewHolder.edit_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 showVehiclePopup(position);
                VehicleDetails vehicleDetails = new VehicleDetails();
                vehicleDetails.setVehicleNo(detailslist.get(position).getVehicleNo());
                vehicleDetails.setMake(detailslist.get(position).getMake());
                vehicleDetails.setModel(detailslist.get(position).getModel());
                vehicleDetails.setYear(detailslist.get(position).getYear());
                vehicleDetails.setFueltype(detailslist.get(position).getFueltype());
                vehicleDetails.setVehicleImg1Path(detailslist.get(position).getVehicleImg1Path());
                vehicleDetails.setVehicleImg2Path(detailslist.get(position).getVehicleImg2Path());
                vehicleDetails.setVehicleImg3Path(detailslist.get(position).getVehicleImg3Path());
                vehicleDetails.setRcDate(detailslist.get(position).getRcDate());
                vehicleDetails.setInsDate(detailslist.get(position).getInsDate());
                vehicleDetails.setPollDate(detailslist.get(position).getPollDate());
                vehicleDetails.setServDate(detailslist.get(position).getServDate());
                vehicleDetails.setFlag(true);
                vehicleDetails.setPosition(position);

                ((DashboardActivity) context).showVehiclePopup(vehicleDetails);
            }
        });
        myViewHolder.ll_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicleInterface.showRCPopup(detailslist.get(position).getVehicleNo());
            }
        });

        myViewHolder.ll_insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vehicleInterface.insurancePopup(detailslist.get(position).getVehicleNo());
            }
        });

        myViewHolder.ll_pollution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vehicleInterface.pollutionPopup(detailslist.get(position).getVehicleNo());
            }
        });

        myViewHolder.ll_servicing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vehicleInterface.servicingPopup(detailslist.get(position).getVehicleNo());
            }
        });

        myViewHolder.battery_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicleInterface.batteryInfoPopup(detailslist.get(position).getVehicleNo());
            }
        });

        String path = detailslist.get(position).getVehicleImg1Path();
        utils.loadImage(path, myViewHolder.vehicle_image);

        myViewHolder.vehicle_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalDeclarations.VEHICLENO = detailslist.get(position).getVehicleNo();
                vehicleInterface.chooseImage(GlobalDeclarations.vehicle_img1_flag, myViewHolder.vehicle_image);
            }
        });

        myViewHolder.view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalDeclarations.VEHICLENO = detailslist.get(position).getVehicleNo();
                Intent intent = new Intent(context, DetailsActivity.class);
                context.startActivity(intent);
            }
        });
        myViewHolder.root_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myViewHolder.checkBox.setVisibility(View.VISIBLE);
                return false;
            }
        });

        if (detailslist.get(position).getChecked().equalsIgnoreCase("Yes"))
            myViewHolder.checkBox.setChecked(true);
        else
            myViewHolder.checkBox.setChecked(false);

        myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String value;
                if (isChecked)
                    value = "Yes";
                else
                    value = "No";

                Database db = new Database(context);
                db.updateFlag(value, detailslist.get(position).getVehicleNo());
                vehicleInterface.selectAllVehicles();
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailslist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        View root_view;
        TextView tv_vehicleno, tv_rc_valid_upto, tv_make, tv_model, tv_fuel_type, tv_year;
        ImageView edit_vehicle, view_details, battery_info;
        TextView insurance_days, pollution_days, servicing_days, rc_days;
        LinearLayout ll_rc, ll_insurance, ll_pollution, ll_servicing;
        ImageView vehicle_image;
        ProgressBar pb_vehicle;
        CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);

            root_view = view.findViewById(R.id.root_view);
            tv_vehicleno = view.findViewById(R.id.tv_vehicleno);
            tv_rc_valid_upto = view.findViewById(R.id.tv_rc_valid_upto);
            tv_make = view.findViewById(R.id.tv_make);
            tv_model = view.findViewById(R.id.tv_model);
            tv_fuel_type = view.findViewById(R.id.tv_fuel_type);
            tv_year = view.findViewById(R.id.tv_year);
            vehicle_image = view.findViewById(R.id.vehicle_image);

            pb_vehicle = view.findViewById(R.id.pb_vehicle);
            edit_vehicle = view.findViewById(R.id.edit_vehicle);
            view_details = view.findViewById(R.id.view_details);
            battery_info = view.findViewById(R.id.battery_info);

            ll_rc = view.findViewById(R.id.ll_rc);
            ll_insurance = view.findViewById(R.id.ll_insurance);
            ll_pollution = view.findViewById(R.id.ll_pollution);
            ll_servicing = view.findViewById(R.id.ll_servicing);

            rc_days = view.findViewById(R.id.rc_days);
            insurance_days = view.findViewById(R.id.insurance_days);
            pollution_days = view.findViewById(R.id.pollution_days);
            servicing_days = view.findViewById(R.id.servicing_days);
            checkBox = view.findViewById(R.id.checkbox);

        }
    }

    public Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private String setColor(TextView textView, int daycount) {

        textView.setTextColor(ContextCompat.getColor(context, R.color.black));
        String days;
        if (daycount <= 1)
            days = daycount + " day";
        else
            days = daycount + " days";

        if (daycount <= 7)
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        else if (daycount <= 15)
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
        else
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        return days;

    }

    private String setImageColor(ImageView imageView, int daycount) {

        imageView.setColorFilter(ContextCompat.getColor(context, R.color.black));
        String days;
        if (daycount <= 1)
            days = daycount + " day";
        else
            days = daycount + " days";

        if (daycount <= 7)
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.red));
        else if (daycount <= 15)
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.orange));
        else
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.green));
        return days;

    }


}
