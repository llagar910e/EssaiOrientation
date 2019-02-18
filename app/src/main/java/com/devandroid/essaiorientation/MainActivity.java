package com.devandroid.essaiorientation;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor magnetic, accelerometer;

    float[] acceleromterVector = new float[3];
    float[] magneticVector = new float[3];
    float[] resultMatrix = new float[9];
    float[] values = new float[3];

    float azimuth, pitch, roll;

    TextView texteAzimuth, textePitch, texteRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener((SensorEventListener) this, magnetic, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        texteAzimuth = (TextView) findViewById(R.id.texteAzimuth);
        textePitch = (TextView) findViewById(R.id.textePitch);
        texteRoll = (TextView) findViewById(R.id.texteRoll);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // Mettre à jour la valeur de l'accéléromètre et du champ magnétique
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            acceleromterVector = sensorEvent.values;
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticVector = sensorEvent.values;
        }

        // Demander au sensorManager la matrice de Rotation (resultMatrix)
        SensorManager.getRotationMatrix(resultMatrix, null, acceleromterVector, magneticVector);

        // Demander au SensorManager le vecteur d'orientation associé (values)
        SensorManager.getOrientation(resultMatrix, values);
        // l'azimuth
        azimuth = (float) Math.toDegrees(values[0]);
        // le pitch
        pitch = (float) Math.toDegrees(values[1]);
        // le roll
        roll = (float) Math.toDegrees(values[2]);

        texteAzimuth.setText("Azimuth : " + Float.toString((float) ((int) (azimuth * 1000)) / 1000));
        textePitch.setText("Pitch : " + Float.toString((float) ((int) (pitch * 1000)) / 1000));
        texteRoll.setText("Roll : " + Float.toString((float) ((int) (roll * 1000)) / 1000));
    }


    // Stopper les mesures
    @Override
    protected void onPause() {
        super.onPause();
       sensorManager.unregisterListener(this, magnetic);
        sensorManager.unregisterListener(this, accelerometer);
    }


    // Reprendre les mesures
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener((SensorEventListener) this, magnetic, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}


