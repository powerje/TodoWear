package com.powerje.todo.reminders;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;

import com.powerje.todo.R;
import com.powerje.todo.receivers.TodoReceiver;

import java.util.List;

/**
 * Created by jep on 6/7/14.
 */
public class TodoNotificationUtils {

    public interface TodoNotification extends Parcelable {
        public int getId();
        public String getText();
        public boolean isChecked();
    }

    public static void setupNotifications(List<TodoNotification> todos, Context context) {

        // Nuke all previous notifications and generate unique ids
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancelAll();

        int notificationId = 0;

        // String to represent the group all the notifications will be a part of
        final String GROUP_KEY_TODO = "group_key_todo";

        // Group notification that will be visible on the phone
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle()
                .setBigContentTitle(todos.size() + " todo items");
        for (TodoNotification n : todos) {
            style.addLine(n.getText());
        }

        NotificationCompat.Builder builderG = new NotificationCompat.Builder(context)
                .setStyle(style)
                .setContentTitle(todos.size() + " todos")
                .setContentText(todos.get(0).getText())
                .setSmallIcon(R.drawable.ic_launcher);

        Notification summaryNotification = new WearableNotifications.Builder(builderG)
                .setGroup(GROUP_KEY_TODO, WearableNotifications.GROUP_ORDER_SUMMARY)
                .build();
        notificationManager.notify(notificationId, summaryNotification);

        for (TodoNotification n : todos) {
            // Separate notifications that will be visible on the watch
            notificationId++;
            PendingIntent viewPendingIntent = TodoNotificationUtils.toggleTodoIntent(n, context, notificationId);
            NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context)
                    .addAction(R.drawable.ic_launcher, "Done", viewPendingIntent)
                    .setContentTitle("Todo")
                    .setContentText(n.getText())
                    .setSmallIcon(R.drawable.ic_launcher);
            Notification notification1 = new WearableNotifications.Builder(builder1)
                    .setGroup(GROUP_KEY_TODO)
                    .build();
            notificationManager.notify(notificationId, notification1);
        }
    }

    public static PendingIntent toggleTodoIntent(TodoNotification todo, Context context, int notificationId) {
        Intent intent = new Intent(TodoReceiver.ACTION_TODO_TOGGLED);
        intent.putExtra("todo", todo);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        return pendingIntent;
    }
}
