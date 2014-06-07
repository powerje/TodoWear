package com.powerje.todo.reminders;

import java.util.List;

/**
 * Created by jep on 6/7/14.
 */
public class TodoNotificationUtils {

    public interface TodoNotification {
        public String getText();

        public boolean isChecked();
    }

    public void setupNotifications(List<TodoNotification> todos) {
        /*
        //Create builder for the main notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.new_message)
                        .setContentTitle("Page 1")
                        .setContentText("Short message")
                        .setContentIntent(viewPendingIntent);

// Create a big text style for the second page
        BigTextStyle secondPageStyle = new NotificationCompat.BigTextStyle();
        secondPageStyle.setBigContentTitle("Page 2")
                .bigText("A lot of text...");

// Create second page notification
        Notification secondPageNotification =
                new NotificationCompat.Builder(this)
                        .setStyle(secondPageStyle)
                        .build();

// Create main notification and add the second page
        Notification twoPageNotification =
                new WearableNotifications.Builder(notificationBuilder)
                        .addPage(secondPageNotification)
                        .build();
                        */
    }
}
