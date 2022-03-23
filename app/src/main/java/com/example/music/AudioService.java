package com.example.music;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class AudioService extends Service {
    public static MediaPlayer mediaplayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        SensorManager mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mediaplayer=MediaPlayer.create(this,R.raw.music);
        mediaplayer.setVolume(50,50);
        if (lightSensor != null) {
            mySensorManager.registerListener(
                    lightSensorListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);


        }

    }


    private final SensorEventListener lightSensorListener
            = new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            sendMessage(Float.toString(event.values[0]));
            if(event.values[0]<200){
                mediaplayer.start();
            }
            else{
                if(mediaplayer.isPlaying()){
                    mediaplayer.pause();
                }

            }

        }

    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        //сообщение об остановке службы
        mediaplayer.stop();
        mediaplayer.release();
    }
    private void sendMessage(String value) {
        Intent intent = new Intent("custom-event-name");
        intent.putExtra("message", value);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}



