package in.gov.cgg.vehiclereminderlatest.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import in.gov.cgg.vehiclereminderlatest.utils.GlobalDeclarations;

/**
 * Created by niharika.p on 19-07-2019.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.d("Alarm", "Started");
//        Toast.makeText(context, "Alarm started", Toast.LENGTH_SHORT).show();
//        String flag = intent.getStringExtra("FLAG");
        String flag = intent.getExtras().getString("FLAG");

        Toast.makeText(context, ""+flag, Toast.LENGTH_SHORT).show();

        Intent intentService = new Intent(context, MyForeGroundService.class);
        if (flag.equalsIgnoreCase(GlobalDeclarations.rc_flag))
            intentService.setAction(MyForeGroundService.ACTION_START_RC_FOREGROUND_SERVICE);
        else if (flag.equalsIgnoreCase(GlobalDeclarations.ins_flag))
            intentService.setAction(MyForeGroundService.ACTION_START_INS_FOREGROUND_SERVICE);
        else if (flag.equalsIgnoreCase(GlobalDeclarations.poll_flag))
            intentService.setAction(MyForeGroundService.ACTION_START_POLL_FOREGROUND_SERVICE);
        else if (flag.equalsIgnoreCase(GlobalDeclarations.ser_flag))
            intentService.setAction(MyForeGroundService.ACTION_START_SERV_FOREGROUND_SERVICE);
        else if (flag.equalsIgnoreCase(GlobalDeclarations.battery_flag))
            intentService.setAction(MyForeGroundService.ACTION_START_BATTERY_FOREGROUND_SERVICE);
        else
            intentService.setAction(MyForeGroundService.ACTION_START_FOREGROUND_SERVICE);
        context.startService(intentService);

    }
}

