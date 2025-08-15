package edu.unikom.lupaminum.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import edu.unikom.lupaminum.R
import edu.unikom.lupaminum.utils.NeedWaterLevel.needWaterLevel

class WaterReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        // 1. Hit API weather -- masih dummy
        val temp = getTemperatureFromApi()
        val humidity = getHumidityFromApi()

        // 2. Tentukan level minum
        val level = needWaterLevel(temp, humidity)

        // 3. Tampilkan notifikasi
        showNotification(level)

        return Result.success()
    }

    private fun showNotification(level: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "water_reminder_channel"

        // buat channel jika Android >= Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pengingat Minum Air",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Waktunya Minum Air 💧")
            .setContentText("$level")
            .setSmallIcon(R.drawable.img_water)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(level)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }

    // Contoh dummy fungsi API
    private fun getTemperatureFromApi(): Double = 32.5
    private fun getHumidityFromApi(): Int = 60
}