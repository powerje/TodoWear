package com.powerje.todo.reminders;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;

import com.powerje.todo.R;
import com.powerje.todo.views.MainActivity;

import java.util.List;

/**
 * Created by jep on 6/7/14.
 */
public class TodoNotificationUtils {

    public interface TodoNotification {
        public String getText();
        public boolean isChecked();
    }

    public static void setupNotifications(List<TodoNotification> todos, Context context) {

        // Nuke all previous notifications and generate unique ids
//        NotificationManagerCompat.from(context).cancelAll();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int notificationId = 0;

        // String to represent the group all the notifications will be a part of
        final String GROUP_KEY_TODO = "group_key_todo";


        // Group notification that will be visible on the phone
        NotificationCompat.Builder builderG = new NotificationCompat.Builder(context)
                .setContentTitle(todos.size() + " todos")
                .setContentText(todos.get(0).getText())
                .setSmallIcon(R.drawable.ic_launcher);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle(builderG)
                .setBigContentTitle(todos.size() + " todo items");
        for (TodoNotification n : todos) {
            style.addLine(n.getText());
        }

        Notification summaryNotification = new WearableNotifications.Builder(builderG)
                .setGroup(GROUP_KEY_TODO, WearableNotifications.GROUP_ORDER_SUMMARY)
                .build();
        notificationManager.notify(notificationId + 0, summaryNotification);

        for (TodoNotification n : todos) {
            // Separate notifications that will be visible on the watch
            Intent viewIntent1 = new Intent(context, MainActivity.class);
            PendingIntent viewPendingIntent1 =
                    PendingIntent.getActivity(context, notificationId + 1, viewIntent1, 0);
            NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context)
                    .addAction(R.drawable.ic_launcher, "Done", viewPendingIntent1)
                    .setContentTitle("Todo")
                    .setContentText(n.getText())
                    .setSmallIcon(R.drawable.ic_launcher);
            Notification notification1 = new WearableNotifications.Builder(builder1)
                    .setGroup(GROUP_KEY_TODO)
                    .build();
            notificationManager.notify(notificationId++, notification1);
        }

        /*
        Intent viewIntent2 = new Intent(context, MainActivity.class);
        PendingIntent viewPendingIntent2 =
                PendingIntent.getActivity(context, notificationId + 2, viewIntent2, 0);
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context)
                .addAction(R.drawable.ic_launcher, "Water Filled", viewPendingIntent2)
                .setContentTitle("Message from Dylan")
                .setContentText("Can you refill our water bowl?")
                .setSmallIcon(R.drawable.ic_launcher);
        Notification notification2 = new WearableNotifications.Builder(builder2)
                .setGroup(GROUP_KEY_TODO)
                .build();
        */
// Issue the group notification


// Issue the separate wear notifications
        /*
        notificationManager.notify(notificationId + 2, notification2);
        notificationManager.notify(notificationId + 1, notification1);
        */


//        if (todos.size() == 0) { return; }
//        int id = 0;
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.cancelAll();
//
//        /*
//         // Open app intent
//        Intent resultIntent = new Intent(context, MainActivity.class);
//        // Because clicking the notification opens a new ("special") activity, there's
//        // no need to create an artificial back stack.
//        PendingIntent resultPendingIntent =
//                PendingIntent.getActivity(
//                        context,
//                        0,
//                        resultIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        */
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.ic_launcher);
//
//        WearableNotifications.Builder wearableBuilder = new WearableNotifications
//                .Builder(builder)
//                .setGroup(GROUP_KEY_TODOS);
//
//        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle(
//                wearableBuilder.getCompatBuilder())
//                .setBigContentTitle(todos.size() + " todo items");
//
//        for (TodoNotification n : todos) {
//            style.addLine(n.getText());
//        }
//
//        // Group notification that will be visible on the phone
//        NotificationCompat.Builder builderG = new NotificationCompat.Builder(context)
//                .setStyle(style)
//                .setContentTitle(todos.size() + " todo items")
//                .setContentText("Mila and Dylan both sent messages")
//                .setSmallIcon(R.drawable.ic_launcher);
//
//        Notification summaryNotification = new WearableNotifications.Builder(builderG)
//                .setGroup(GROUP_KEY_TODOS)
//                .build();
//        notificationManager.notify(id++, summaryNotification);
//
//
//        for (int i = 0; i < todos.size(); i++) {
//            TodoNotification n = todos.get(i);
//
//            Intent viewIntent = new Intent(context, MainActivity.class);
//            PendingIntent viewPendingIntent1 =
//                    PendingIntent.getActivity(context, id, viewIntent, 0);
//
//            NotificationCompat.Builder newTodoNotification =
//                    new NotificationCompat.Builder(context)
//                            .addAction(R.drawable.ic_launcher, "Completed", viewPendingIntent1)
//                            .setSmallIcon(R.drawable.ic_launcher)
//                            .setContentTitle("Todo")
//                            .setContentText(n.getText());
//
//            WearableNotifications.Builder grouped = new WearableNotifications.Builder(newTodoNotification)
//                    .setGroup(GROUP_KEY_TODOS, WearableNotifications.GROUP_ORDER_SUMMARY);
//
//
//            notificationManager.notify(id++, grouped.build());
//        }
    }
}
