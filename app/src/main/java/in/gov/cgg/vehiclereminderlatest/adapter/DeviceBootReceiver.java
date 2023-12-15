package in.gov.cgg.vehiclereminderlatest.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;

import in.gov.cgg.vehiclereminderlatest.bean.BatteryDetails;
import in.gov.cgg.vehiclereminderlatest.bean.InsuranceDetails;
import in.gov.cgg.vehiclereminderlatest.bean.PollutionDetails;
import in.gov.cgg.vehiclereminderlatest.bean.RCDetails;
import in.gov.cgg.vehiclereminderlatest.bean.ServicingDetails;
import in.gov.cgg.vehiclereminderlatest.bean.VehicleDetails;
import in.gov.cgg.vehiclereminderlatest.database.Database;
import in.gov.cgg.vehiclereminderlatest.utils.GlobalDeclarations;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // on device boot compelete, reset the alarm
//            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
//
//            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            calendar.set(Calendar.HOUR_OF_DAY, 7);
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.SECOND, 1);
//
//            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                    AlarmManager.INTERVAL_DAY, pendingIntent);
            Database database = new Database(context);
            ArrayList<VehicleDetails> detailslist = database.getVehicleDetails();

            int ins_pos, rc_pos, poll_pos, serv_pos, battery_pos;

            if (detailslist != null && detailslist.size() > 0) {
                for (int i = 0; i < detailslist.size(); i++) {

                    ArrayList<RCDetails> rcDetailsArrayList = database.getRCDetails(detailslist.get(i).getVehicleNo());
                    if (rcDetailsArrayList != null && rcDetailsArrayList.size() > 0) {
                        rc_pos = rcDetailsArrayList.size() - 1;
                        setAlarm(rcDetailsArrayList.get(rc_pos).getId(), rcDetailsArrayList.get(rc_pos).getValidUpto(), context);
                    }

                    ArrayList<InsuranceDetails> insurancelist = database.getInsuranceDetailsList(detailslist.get(i).getVehicleNo());
                    if (insurancelist != null && insurancelist.size() > 0) {
                        ins_pos = insurancelist.size() - 1;
                        setAlarm(insurancelist.get(ins_pos).getId(), insurancelist.get(ins_pos).getValidUpto(), context);
                    }

                    ArrayList<PollutionDetails> pollutionlist = database.getPollutionDetailsList(detailslist.get(i).getVehicleNo());
                    if (pollutionlist != null && pollutionlist.size() > 0) {
                        poll_pos = pollutionlist.size() - 1;
                        setAlarm(pollutionlist.get(poll_pos).getId(), pollutionlist.get(poll_pos).getValidUpto(), context);
                    }

                    ArrayList<ServicingDetails> servicinglist = database.getServicingDetailsList(detailslist.get(i).getVehicleNo());
                    if (servicinglist != null && servicinglist.size() > 0) {
                        serv_pos = servicinglist.size() - 1;
                        setAlarm(servicinglist.get(serv_pos).getId(), servicinglist.get(serv_pos).getNext_date(), context);
                    }

                    ArrayList<BatteryDetails> batterylist = database.getBatteryDetails(detailslist.get(i).getVehicleNo());
                    if (batterylist != null && batterylist.size() > 0) {
                        battery_pos = batterylist.size() - 1;
                        setAlarm(batterylist.get(battery_pos).getId(), batterylist.get(battery_pos).getWarrantyEndDate(), context);
                    }
                }
            }
        }
    }

    private void setAlarm(long timeinmillis, String date, Context context) {

        String[] date1 = date.split("/");
        int day = Integer.parseInt(date1[0]);
        int mnth = Integer.parseInt(date1[1]);
        int yr = Integer.parseInt(date1[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(yr, mnth - 1, day, GlobalDeclarations.HOUR, GlobalDeclarations.MINUTE, GlobalDeclarations.SECONDS);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) timeinmillis, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);

    }
}


