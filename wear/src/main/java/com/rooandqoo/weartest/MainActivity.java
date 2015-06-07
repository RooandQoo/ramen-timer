package com.rooandqoo.weartest;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.WindowInsets;

public class MainActivity extends Activity {

    private GridViewPager mGridViewPager;
    private DotsPageIndicator mDotsPageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mGridViewPager = (GridViewPager) stub.findViewById(R.id.pager);
                mDotsPageIndicator = (DotsPageIndicator) stub.findViewById(R.id.page_indicator);
                setupViewPager();
            }
        });
    }

    private void setupViewPager() {
        final Resources res = getResources();
        mGridViewPager.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                final boolean round = insets.isRound();
                int rowMargin = res.getDimensionPixelOffset(R.dimen.page_row_margin);
                int colMargin = res.getDimensionPixelOffset(round ?
                        R.dimen.page_column_margin_round : R.dimen.page_column_margin);
                mGridViewPager.setPageMargins(rowMargin, colMargin);
                mGridViewPager.onApplyWindowInsets(insets);
                return insets;
            }
        });

        mGridViewPager.setAdapter(new MyGridViewPagerAdapter(this, getFragmentManager()));
        mDotsPageIndicator.setPager(mGridViewPager);
    }

    public void setupTimer(long duration) {
        NotificationManager notificationManager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));

        cancelCountdown(notificationManager);
        notificationManager.notify(Constants.NOTIFICATION_TIMER_COUNTDOWN, buildNotification(duration));
        registerWithAlarmManager(duration);
    }

    private void cancelCountdown(NotificationManager notifyMgr) {
        notifyMgr.cancel(Constants.NOTIFICATION_TIMER_EXPIRED);
    }

    private Notification buildNotification(long duration) {
        // Intent to restart a timer.
        Intent restartIntent = new Intent(Constants.ACTION_RESTART_ALARM, null, this,
                TimerNotificationService.class);
        PendingIntent pendingIntentRestart = PendingIntent
                .getService(this, 0, restartIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Intent to delete a timer.
        Intent deleteIntent = new Intent(Constants.ACTION_DELETE_ALARM, null, this,
                TimerNotificationService.class);
        PendingIntent pendingIntentDelete = PendingIntent
                .getService(this, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create countdown notification using a chronometer style.
        return new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_cc_alarm)
                .setContentTitle("Time Remaining")
                .setContentText(TimerFormat.getTimeString(duration))
                .setUsesChronometer(true)
                .setWhen(System.currentTimeMillis() + duration)
                .setDeleteIntent(pendingIntentDelete)
                .setLocalOnly(true)
                .build();
    }

    private void registerWithAlarmManager(long duration) {
        // Get the alarm manager.
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Create intent that gets fired when timer expires.
        Intent intent = new Intent(Constants.ACTION_SHOW_ALARM, null, this,
                TimerNotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Calculate the time when it expires.
        long wakeupTime = System.currentTimeMillis() + duration;

        // Schedule an alarm.
        alarm.setExact(AlarmManager.RTC_WAKEUP, wakeupTime, pendingIntent);
    }

}
