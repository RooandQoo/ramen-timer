package com.rooandqoo.weartest;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomFragment extends Fragment {

    private static final String PARAM_MINUTES = "minutes";

    public static CustomFragment createInstance(int minutes) {
        CustomFragment fragment = new CustomFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_MINUTES, minutes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int minutes = getArguments().getInt(PARAM_MINUTES);

        TextView min = (TextView) view.findViewById(R.id.text_minutes);
        min.setText(String.valueOf(minutes));

        View timerContainer = view.findViewById(R.id.container_start_timer);
        timerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupTimer(minutes * 1000 * 60);
                getActivity().finish();
            }
        });
    }

    public void setupTimer(long duration) {
        NotificationManager notificationManager = ((NotificationManager) getActivity().getSystemService(Activity.NOTIFICATION_SERVICE));

        cancelCountdown(notificationManager);
        notificationManager.notify(Constants.NOTIFICATION_TIMER_COUNTDOWN, buildNotification(duration));
        registerWithAlarmManager(duration);
    }

    private void cancelCountdown(NotificationManager notifyMgr) {
        notifyMgr.cancel(Constants.NOTIFICATION_TIMER_EXPIRED);
    }

    private Notification buildNotification(long duration) {
        Intent deleteIntent = new Intent(Constants.ACTION_DELETE_ALARM, null, getActivity(),
                TimerNotificationService.class);
        PendingIntent pendingIntentDelete = PendingIntent
                .getService(getActivity(), 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new Notification.Builder(getActivity())
                .setSmallIcon(R.drawable.ic_cc_alarm)
                .setContentTitle("Time Remaining")
                .setContentText(TimerFormat.getTimeString(duration))
                .setUsesChronometer(true)
                .setWhen(System.currentTimeMillis() + duration)
                .addAction(R.drawable.ic_cc_alarm, "Delete",
                        pendingIntentDelete)
                .setDeleteIntent(pendingIntentDelete)
                .setLocalOnly(true)
                .build();
    }

    private void registerWithAlarmManager(long duration) {
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Constants.ACTION_SHOW_ALARM, null, getActivity(),
                TimerNotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        long wakeupTime = System.currentTimeMillis() + duration;
        alarm.setExact(AlarmManager.RTC_WAKEUP, wakeupTime, pendingIntent);
    }

}
