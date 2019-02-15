package com.devandroid.essaigyroscope;

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
    float x, y, z;
    float[] acceleromterVector = new float[3];
    float[] magneticVector = new float[3];
    float[] resultMatrix = new float[9];
    float[] values = new float[3];
    TextView texteX, texteY, texteZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener((SensorEventListener) this, magnetic, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        texteX = (TextView) findViewById(R.id.texteX);
        texteY = (TextView) findViewById(R.id.texteY);
        texteZ = (TextView) findViewById(R.id.texteZ);
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
        x = (float) Math.toDegrees(values[0]);
        // le pitch
        y = (float) Math.toDegrees(values[1]);
        // le roll
        z = (float) Math.toDegrees(values[2]);

        texteX.setText("X : " + Float.toString((float) ((int) (x * 1000)) / 1000));
        texteY.setText("Y : " + Float.toString((float) ((int) (y * 1000)) / 1000));
        texteZ.setText("Z : " + Float.toString((float) ((int) (z * 1000)) / 1000));
    }

    @Override
    protected void onPause() {
        super.onPause();
       sensorManager.unregisterListener(this, magnetic);
        sensorManager.unregisterListener(this, accelerometer);
    }

}


