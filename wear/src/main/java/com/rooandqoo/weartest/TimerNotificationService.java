package com.rooandqoo.weartest;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimerNotificationService extends IntentService {

    public static final String TAG = "TimerNotificationSvc";

    public TimerNotificationService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onHandleIntent called with intent: " + intent);
        }
        String action = intent.getAction();
        if (Constants.ACTION_SHOW_ALARM.equals(action)) {
            showTimerDoneNotification();
        } else if (Constants.ACTION_DELETE_ALARM.equals(action)) {
            deleteTimer();
        } else if (Constants.ACTION_RESTART_ALARM.equals(action)) {
            restartAlarm();
        } else {
            throw new IllegalStateException("Undefined constant used: " + action);
        }
    }

    private void restartAlarm() {
        Intent dialogIntent = new Intent(this, MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Timer restarted.");
        }
    }

    private void deleteTimer() {
        cancelCountdownNotification();

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Constants.ACTION_SHOW_ALARM, null, this,
                TimerNotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.cancel(pendingIntent);

        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Timer deleted.");
        }
    }

    private void cancelCountdownNotification() {
        NotificationManager notifyMgr =
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        notifyMgr.cancel(Constants.NOTIFICATION_TIMER_COUNTDOWN);
    }

    private void showTimerDoneNotification() {
        // Cancel the countdown notification to show the "timer done" notification.
        cancelCountdownNotification();

        // Create an intent to restart a timer.
        Intent restartIntent = new Intent(Constants.ACTION_RESTART_ALARM, null, this,
                TimerNotificationService.class);
        PendingIntent pendingIntentRestart = PendingIntent
                .getService(this, 0, restartIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create notification that timer has expired.
        NotificationManager notifyMgr =
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        Notification notif = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_cc_alarm)
                .setContentTitle("Tiimer done")
                .setContentText("Tiimer done")
                .setUsesChronometer(true)
                .setWhen(System.currentTimeMillis())
                .setLocalOnly(true)
                .build();
        notifyMgr.notify(Constants.NOTIFICATION_TIMER_EXPIRED, notif);
    }
}
