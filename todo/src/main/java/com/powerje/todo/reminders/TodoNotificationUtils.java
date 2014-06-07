package com.powerje.todo.reminders;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;

import com.powerje.todo.R;
import com.powerje.todo.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jep on 6/7/14.
 */
public class TodoNotificationUtils {

    public interface TodoNotification {
        public String getText();
        public boolean isChecked();
    }

    final static String GROUP_KEY_TODOS = "group_key_todos";

    public static void setupNotifications(List<TodoNotification> todos, Context context) {

        if (todos.size() == 0) { return; }
        int id = 0;

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                 // Open app intent

        Intent resultIntent = new Intent(context, MainActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        //Create builder for the main notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Todo")
                        .setContentText(todos.get(0).getText())
                        .setContentIntent(resultPendingIntent);

        List<Notification> notifications = new ArrayList<>();
        for (int i = 1; i < todos.size(); i++) {
            TodoNotification n = todos.get(i);

            // Create second page notification
            NotificationCompat.Builder newTodoNotification =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("Todo")
                            .setContentText(n.getText());

            Notification grouped = new WearableNotifications.Builder(newTodoNotification)
                    .setGroup(GROUP_KEY_TODOS)
                    .build();
            notifications.add(grouped);


            /*
        Notification twoPageNotification =
                new WearableNotifications.Builder(notificationBuilder)
                        .setGroup(GROUP_KEY_TODOS)
                        .build();
                        */
            mNotifyMgr.notify(id++, grouped);
        }

        // Create main notification and add the second page
        /*
        Notification twoPageNotification =
                new WearableNotifications.Builder(notificationBuilder)
                        .setGroup(GROUP_KEY_TODOS)
//                        .addPages(notifications)
                        .build();
                        */


//        mNotifyMgr.notify(1, twoPageNotification);
    }
}
