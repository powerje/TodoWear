package com.powerje.todo.reminders;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;

import com.powerje.todo.R;
import com.powerje.todo.data.Todo;
import com.powerje.todo.receivers.TodoReceiver;
import com.powerje.todo.views.MainActivity;

import java.util.List;

/**
 * Created by jep on 6/7/14.
 */
public class TodoNotificationUtils {

    public static void setupNotifications(final List<Todo> todos, final Context context) {
        if (todos.size() == 0) { return; }

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Nuke all previous notifications and generate unique ids
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.cancelAll();
                SystemClock.sleep(1000); // Sometimes the new notificartions would get canceled by the cancelAll, wut

                int notificationId = 0;

                // String to represent the group all the notifications will be a part of
                final String GROUP_KEY_TODO = "group_key_todo";

                // Group notification that will be visible on the phone
                NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle()
                        .setBigContentTitle(todos.size() + " todo items");

                int toBeDone = 0;

                for (Todo n : todos) {
                    style.addLine(n.getText());
                    if(!n.isChecked()){
                        toBeDone++;
                    }
                }

                NotificationCompat.Builder builderG = new NotificationCompat.Builder(context)
                        .setStyle(style)
                        .setContentTitle(toBeDone + " of "+todos.size()+" todos to complete.")
                        .setContentIntent(TodoNotificationUtils.launchAppIntent(context))
                        .setContentText(todos.get(0).getText())
                        .setSmallIcon(R.drawable.ic_launcher);

                Notification summaryNotification = new WearableNotifications.Builder(builderG)
                        .setGroup(GROUP_KEY_TODO, WearableNotifications.GROUP_ORDER_SUMMARY)
                        .build();
                notificationManager.notify(notificationId, summaryNotification);


                for (Todo n : todos) {
                    if (n.isChecked()) { continue; }
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
        }).start();
    }

    private static PendingIntent toggleTodoIntent(Todo todo, Context context, int notificationId) {
        Intent intent = new Intent(TodoReceiver.ACTION_TODO_TOGGLED);
        intent.putExtra("todoId", todo.getId());

        return PendingIntent.getBroadcast(context,
                notificationId,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private static PendingIntent launchAppIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
