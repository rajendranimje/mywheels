package in.gov.cgg.vehiclereminderlatest.adapter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import in.gov.cgg.vehiclereminderlatest.R;
import in.gov.cgg.vehiclereminderlatest.utils.GlobalDeclarations;


public class MyForeGroundService extends Service {
    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_START_RC_FOREGROUND_SERVICE = "ACTION_START_RC_FOREGROUND_SERVICE";

    public static final String ACTION_START_INS_FOREGROUND_SERVICE = "ACTION_START_INS_FOREGROUND_SERVICE";

    public static final String ACTION_START_POLL_FOREGROUND_SERVICE = "ACTION_START_POLL_FOREGROUND_SERVICE";

    public static final String ACTION_START_SERV_FOREGROUND_SERVICE = "ACTION_START_SERV_FOREGROUND_SERVICE";

    public static final String ACTION_START_BATTERY_FOREGROUND_SERVICE = "ACTION_START_BATTERY_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    public static final String ACTION_PAUSE = "ACTION_PAUSE";

    public static final String ACTION_PLAY = "ACTION_PLAY";

    private final String MY_CHANNEL = "my_channel";

    private NotificationManager mNotificationManager;
    private final long[] vibrationScheme = new long[]{200, 400};
    Context context;

    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    MediaPlayer mp;


    public MyForeGroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG_FOREGROUND_SERVICE, "My foreground service onCreate().");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String action = intent.getAction();


            switch (action) {
                case ACTION_START_RC_FOREGROUND_SERVICE:
                    startMyForegroundService(GlobalDeclarations.rc_flag);
                    break;
                case ACTION_START_INS_FOREGROUND_SERVICE:
                    startMyForegroundService(GlobalDeclarations.ins_flag);
                    break;
                case ACTION_START_POLL_FOREGROUND_SERVICE:
                    startMyForegroundService(GlobalDeclarations.poll_flag);
                    break;
                case ACTION_START_SERV_FOREGROUND_SERVICE:
                    startMyForegroundService(GlobalDeclarations.ser_flag);
                    break;
                case ACTION_START_BATTERY_FOREGROUND_SERVICE:
                    startMyForegroundService(GlobalDeclarations.battery_flag);
                    break;
                case ACTION_START_FOREGROUND_SERVICE:
                    startMyForegroundService(GlobalDeclarations.vehicle_flag);
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopMyForegroundService();
                    break;

                case ACTION_PAUSE:
                    stopMyForegroundService();
                    break;
            }
        }
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void stopMyForegroundService() {

        Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.");
        mp.stop();
        // Stop foreground service and remove the notification.
        stopForeground(true);
        // Stop the foreground service.
        stopSelf();

    }

    private void startMyForegroundService(String flag) {

        createNotificationChannel();
        mp = MediaPlayer.create(this, notification);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mp.setLooping(true);
                mp.start();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopMyForegroundService();
            }
        });
        thread.start();

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, MY_CHANNEL)
                .setContentTitle("Reminder")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(largeIconBitmap);
        if (flag.equalsIgnoreCase(GlobalDeclarations.rc_flag))
            notification.setContentText("Last date for RC");
        else if (flag.equalsIgnoreCase(GlobalDeclarations.ins_flag))
            notification.setContentText("Last date for Insurance");
        else if (flag.equalsIgnoreCase(GlobalDeclarations.poll_flag))
            notification.setContentText("Last date for Pollution");
        else if (flag.equalsIgnoreCase(GlobalDeclarations.ser_flag))
            notification.setContentText("Last date for Servicing");
        else if (flag.equalsIgnoreCase(GlobalDeclarations.battery_flag))
            notification.setContentText("Last date for Warranty");
        else
            notification.setContentText("Last date for Vehicle");

        // Make the notification max priority.
        notification.setPriority(Notification.PRIORITY_MAX);
        // Make head-up notification.
        notification.setFullScreenIntent(pendingIntent, true);

        Intent pauseIntent = new Intent(this, MyForeGroundService.class);
        pauseIntent.setAction(ACTION_PAUSE);
        PendingIntent pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Stop", pendingPauseIntent);
        notification.addAction(prevAction);

        startForeground(1, notification.build());


       /* Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.alarm);

        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MY_CHANNEL)
                .setContentTitle("Alarm")
                .setContentText("By Android Team")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(largeIconBitmap);

        // Make the notification max priority.
        builder.setPriority(Notification.PRIORITY_MAX);
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true);

        Intent pauseIntent = new Intent(this, MyForeGroundService.class);
        pauseIntent.setAction(ACTION_PAUSE);
        PendingIntent pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Stop", pendingPauseIntent);
        builder.addAction(prevAction);

        Notification notification = builder.build();
        startForeground(1, notification);*/


    }

    private void createNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create the channel object with the unique ID MY_CHANNEL
            NotificationChannel myChannel = new NotificationChannel(MY_CHANNEL, getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            // Configure the channel's initial settings
            myChannel.setLightColor(Color.GREEN);
            myChannel.setVibrationPattern(vibrationScheme);

            if (mNotificationManager == null) {
                mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }
            // Submit the notification channel object to the notification manager
            mNotificationManager.createNotificationChannel(myChannel);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop foreground service and remove the notification.
        stopForeground(false);
        // Stop the foreground service.
//        stopSelf();
    }
}
