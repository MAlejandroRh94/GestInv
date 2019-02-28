package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.BroadcastReceiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.AjustarInventarioActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificationChannelName";
            String description = "NotificationChannelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("NotificationChannelId", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent1 = new Intent(context, AjustarInventarioActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "NotificationChannelId")
                .setSmallIcon(R.drawable.ic_action_ajustar_inventario)
                .setContentTitle("Ajustar Inventario")
                .setContentText("Iniciar ajuste de inventario")
                .setContentIntent(pendingIntent1)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, mBuilder.build());
    }
}
